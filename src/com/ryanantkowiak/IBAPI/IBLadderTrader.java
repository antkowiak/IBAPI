/**
 * Ladder Trader is akin to the IB Book Trader.  A single click places an order at a
 * price level on either the bid or ask side.
 */
package com.ryanantkowiak.IBAPI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.ib.client.Contract;
import com.ib.client.TagValue;

/**
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 *
 */
public class IBLadderTrader implements ActionListener, MouseListener, IBTickListener
{
	/*
	 * Class to store price value in both cents (integer) and floating point
	 */
	private class Price
	{
		/*
		 * Cents value in moneyu
		 */
		private int centsValue;
		
		/*
		 * Money value in floating point
		 */
		private double moneyValue;
		
		/*
		 *	@brief	Constructor that takes the money floating point parameter
		 *	@param	d - the money amount as floating point
		 */
		public Price (double d)
		{
			moneyValue = d;
			centsValue = (int) Math.round(d * 100);
		}
		
		/*
		 * 	@brief	Return the money value as floating point
		 * 	@return	double - the money value
		 */
		public double getMoneyValue()
		{
			return moneyValue;
		}
		
		/*
		 * 	@brief	Return the cents value
		 * 	@return	int - the cents value integer
		 */
		public int getCentsValue()
		{
			return centsValue;
		}
		
		/*
		 * 	@brief	Return a string representation of the money
		 * 	@return	String - the string representation of the money value
		 */
		public String getStringValue()
		{
			if (centsValue == 0)
			{
				return "0.00";
			}
			else
			{
				int dollars = Math.abs(centsValue / 100);
				int cents = Math.abs(centsValue % 100);
				
				StringBuilder sb = new StringBuilder();
				
				if (centsValue < 0)
				{
					sb.append("-");
				}
				
				if (dollars == 0)
				{
					sb.append("0.");
				}
				else
				{
					sb.append(dollars);
					sb.append(".");
				}
				
				if (cents < 10)
				{
					sb.append("0");
				}
				
				sb.append(cents);
				
				return sb.toString();
			}
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString()
		{
			return getStringValue();
		}
		
		/*
		 * 	@brief	Returns if the passed in object is equivalent
		 * 	@param	rhs - the object to compare
		 * 	@return	bool - true if the passed in object is equivalent
		 */
		public boolean equals(Price rhs)
		{
			if (null == rhs)
			{
				return false;
			}
			
			return centsValue == rhs.centsValue;
		}
	}
	
	/*
	 * The number of rows of the ladder trader prices to display at once
	 */
	private static final int NUM_ROWS = 25;
	
	/*
	 * References to the IB message senders and receivers
	 */
	private IBMessageSender m_ibms;
	private IBMessageReceiver m_ibmr;
	
	/*
	 * Reference to the configuration of the IB Ladder Trader
	 */
	private IBLadderTraderConfig m_config;
	
	private final Object priceLock = new Object();
	private boolean m_firstTick = true;
	
    private JFrame m_frame;
    private JPanel m_panel;
    private GridLayout m_gridLayout;
    
    private JTextField m_qtyTextField;

    private JButton [] m_buyButtons;
    private JLabel [] m_priceLabels;
    private JButton [] m_sellButtons;
    private Price [] m_prices;
    
    private Price m_lastPrice = new Price(0.0);
    private Price m_bidPrice = new Price(0.0);
    private Price m_askPrice = new Price(0.0);
    private int m_tickerId;
	
    private JButton m_upButton;
    private JButton m_downButton;
	private JButton m_cancelAllButton;
	private JButton m_revButton;
	private JButton m_flatButton;
	private JButton m_centerButton;
	
  
	/*
	 * 	@brief	Constructor
	 * 
	 * 	@param	ibms - The IB Message Sender
	 * 	@param	ibmr - The IB Message Receiver
	 * 	@param	config - The configuration to use for ladder trader
	 */
	public IBLadderTrader(IBMessageSender ibms, IBMessageReceiver ibmr, IBLadderTraderConfig config)
	{
		m_config = new IBLadderTraderConfig(config);
		m_ibms = ibms;
		m_ibmr = ibmr;
		
		m_ibmr.addIBTickListener(this);;
		
		m_tickerId = TickPriceMgr.getTickerIdForSymbol(m_config.symbol);
		// find out the params
		Contract contract = new Contract();
		contract.m_secType = "STK";
		contract.m_symbol = m_config.symbol;
		contract.m_localSymbol = m_config.symbol;
		contract.m_exchange = "SMART";
		contract.m_currency = "USD";
		m_ibms.reqMktData(m_tickerId, contract, "", false, Collections.<TagValue>emptyList());
		
		m_buyButtons = new JButton[NUM_ROWS];
		m_priceLabels = new JLabel[NUM_ROWS];
		m_sellButtons = new JButton[NUM_ROWS];
		m_prices = new Price[NUM_ROWS];
		
		createScreen();
	}

	
	/*
	 * 	@brief	Re-center the display of the price ladder around the given center price
	 * 	@param	centerPrice - the price to display in the middle when re-centering the ladder
	 */
	public void recenter(Price centerPrice)
	{
		synchronized (priceLock)
		{
			int start = centerPrice.getCentsValue() + (NUM_ROWS / 2);

			for (int i = 0 ; i < NUM_ROWS ; ++i)
			{
				m_prices[i] = new Price((start - i) / 100.0);
				m_priceLabels[i].setText(m_prices[i].getStringValue());
				m_priceLabels[i].setOpaque(true);
				
				boolean updated = false;
				
				if (m_bidPrice.equals(m_prices[i]))
				{
					m_priceLabels[i].setBackground(Color.BLUE);
					updated = true;
				}
				if (m_askPrice.equals(m_prices[i]))
				{
					m_priceLabels[i].setBackground(Color.RED);
					updated = true;
				}
				
				if (m_lastPrice.equals(m_prices[i]))
				{
					m_priceLabels[i].setBackground(Color.ORANGE);
					updated = true;
				}
				
				if (false == updated)
				{
					m_priceLabels[i].setBackground(Color.WHITE);
				}

			}
		}
	}
	
	
	/*
	 * 	@brief	Creates all screen elements for ladder trader
	 */
	private void createScreen()
	{
		
        m_frame = new JFrame(m_config.symbol + " - 0.00");
        m_frame.setVisible(false);
		m_frame.setSize(300, 720);
		m_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		m_frame.getContentPane().setLayout(new BorderLayout());

		m_gridLayout = new GridLayout((NUM_ROWS + 5),3);
		
		m_panel = new JPanel();
		m_panel.setLayout(m_gridLayout);

		// Add everything to the layout
		
		m_panel.add(new JLabel());
		m_upButton = new JButton("/\\");
		m_upButton.setEnabled(false);
		m_upButton.addActionListener(this);
		m_panel.add(m_upButton);
		m_panel.add(new JLabel());
		
		for (int i = 0 ; i < NUM_ROWS ; ++i)
		{
			m_buyButtons[i] = new JButton("Buy");
			m_buyButtons[i].setBackground(Color.BLUE);
			//m_buyButtons[i].setForeground(Color.WHITE);
			m_buyButtons[i].setOpaque(true);
			m_buyButtons[i].addActionListener(this);
			m_panel.add(m_buyButtons[i]);
			m_priceLabels[i] = new JLabel("", SwingConstants.CENTER);
			m_priceLabels[i].addMouseListener(this);
			m_panel.add(m_priceLabels[i]);
			m_sellButtons[i] = new JButton("Sell");
			m_sellButtons[i].setBackground(Color.RED);
			//m_sellButtons[i].setForeground(Color.WHITE);
			m_sellButtons[i].setOpaque(true);
			m_sellButtons[i].addActionListener(this);
			m_panel.add(m_sellButtons[i]);
		}
		
		
		m_panel.add(new JLabel());
		m_downButton = new JButton("\\/");
		m_downButton.setEnabled(false);;
		m_downButton.addActionListener(this);
		m_panel.add(m_downButton);
		m_panel.add(new JLabel());
		
		m_panel.add(new JLabel("Qty:"));
		m_qtyTextField = new JTextField("" + m_config.defaultQuantity);
		m_panel.add(m_qtyTextField);
		m_panel.add(new JLabel(m_config.symbol));
		
		
		m_cancelAllButton = new JButton("CXL ALL");
		m_revButton = new JButton("REV");
		m_flatButton = new JButton("FLAT");
		m_centerButton = new JButton("CENTER");
		
		m_cancelAllButton.setEnabled(false);
		m_revButton.setEnabled(false);
		m_flatButton.setEnabled(false);
		m_centerButton.setEnabled(false);
		
		m_cancelAllButton.addActionListener(this);
		m_revButton.addActionListener(this);
		m_flatButton.addActionListener(this);
		m_centerButton.addActionListener(this);
		
		m_panel.add(m_cancelAllButton);
		m_panel.add(m_revButton);
		m_panel.add(m_flatButton);
		m_panel.add(m_centerButton);

		
		disableAllButtons();
		enableAllButtons();
		
		m_frame.getContentPane().add(m_panel, BorderLayout.CENTER);
		m_frame.setVisible(true);
		
		recenter(new Price((NUM_ROWS / 200.0)));
	}
	
	/*
	 * 	@brief	Disable all the buttons on the ladder trader display
	 */
	private void disableAllButtons()
	{
		for (int i = 0 ; i < NUM_ROWS ; ++i)
		{
			m_buyButtons[i].setEnabled(false);
			m_sellButtons[i].setEnabled(false);
		}
		
		m_upButton.setEnabled(false);
		m_downButton.setEnabled(false);
		m_cancelAllButton.setEnabled(false);
		m_revButton.setEnabled(false);
		m_flatButton.setEnabled(false);
		m_centerButton.setEnabled(false);
	}
	
	/*
	 * 	@brief	Enable all the buttons on the ladder trader display
	 */
	private void enableAllButtons()
	{
		for (int i = 0 ; i < NUM_ROWS ; ++i)
		{
			m_buyButtons[i].setEnabled(true);
			m_sellButtons[i].setEnabled(true);
		}

		m_upButton.setEnabled(true);
		m_downButton.setEnabled(true);
		m_cancelAllButton.setEnabled(true);
		m_revButton.setEnabled(true);
		m_flatButton.setEnabled(true);
		m_centerButton.setEnabled(true);
	}

	/*
	 * 	@brief	Handle a press on a button to send a Buy Order at an index
	 * 	@param	index - the button number index for the buy order
	 */
	private void handleBuyButton(int index)
	{
		synchronized (priceLock)
		{
			System.out.println("BUY AT: " + m_prices[index].getMoneyValue());
			placeStockLimitOrder("BUY", Integer.parseInt(m_qtyTextField.getText()), m_prices[index]);
		}
	}
	
	/*
	 * 	@brief	Handle a press on a button to send a Sell Order at an index
	 * 	@param	index - the button number index for the sell order
	 */
	private void handleSellButton(int index)
	{
		synchronized (priceLock)
		{
			System.out.println("SELL AT: " + m_prices[index].getMoneyValue());
			placeStockLimitOrder("SELL", Integer.parseInt(m_qtyTextField.getText()), m_prices[index]);
		}
	}
	
	/*
	 * 	@brief	handle a press on the button to scroll the price ladder up
	 */
	private void handleUpButton()
	{
		synchronized (priceLock)
		{
			recenter(m_prices[0]);
		}
	}
	
	/*
	 * 	@brief	handle a press on the button to scroll the price ladder down
	 */
	private void handleDownButton()
	{
		synchronized (priceLock)
		{
			recenter(m_prices[NUM_ROWS-1]);
		}
	}
	
	private void handleCancelAllButton()
	{
		m_ibms.reqGlobalCancel();
	}
	
	private void handleRevButton()
	{
		int currentPosition = 0;
		
		m_ibms.reqGlobalCancel();
		
		Map<Contract, Integer> positionMap = null;
		PositionData posData = PositionMgr.getPositions(m_config.account);
		if (null != posData)
		{
			positionMap = posData.positionMap;
		}
		
		if (null != positionMap)
		{
			for (Map.Entry<Contract, Integer> entry : positionMap.entrySet())
			{
				Contract c = entry.getKey();
				
				if (c.m_symbol.equals(m_config.symbol) && c.m_secIdType == "STK")
				{
					currentPosition = currentPosition + entry.getValue().intValue();
				}
			}
		}
		
		if (currentPosition == 0)
		{
			System.out.println("Reverse Error - No current position.");
		}
		else if (currentPosition > 0)
		{
			placeStockLimitOrder("SELL",  Math.abs(2 * currentPosition), m_bidPrice);
		}
		else if (currentPosition < 0)
		{
			placeStockLimitOrder("BUY", Math.abs(2 * currentPosition), m_askPrice);
		}
	}
	
	private void handleFlatButton()
	{
		int currentPosition = 0;
		
		m_ibms.reqGlobalCancel();
		
		Map<Contract, Integer> positionMap = null;
		PositionData posData = PositionMgr.getPositions(m_config.account);
		if (null != posData)
		{
			positionMap = posData.positionMap;
		}
		
		if (null != positionMap)
		{
			for (Map.Entry<Contract, Integer> entry : positionMap.entrySet())
			{
				Contract c = entry.getKey();
				
				if (c.m_symbol.equals(m_config.symbol) && c.m_secIdType == "STK")
				{
					currentPosition = currentPosition + entry.getValue().intValue();
				}
			}
		}
		
		if (currentPosition == 0)
		{
			System.out.println("Flatten Error - No current position.");
		}
		else if (currentPosition > 0)
		{
			placeStockLimitOrder("SELL",  Math.abs(currentPosition), m_bidPrice);
		}
		else if (currentPosition < 0)
		{
			placeStockLimitOrder("BUY", Math.abs(currentPosition), m_askPrice);
		}
	}
	
	private void handleCenterButton()
	{
		synchronized (priceLock)
		{
			if (null != m_lastPrice)
			{
				recenter(m_lastPrice);
			}
		}
	}
	
	
	private int placeStockLimitOrder(String side, int quantity, Price limitPrice)
	{
		if (side != "BUY" && side != "SELL")
		{
			System.err.println("Invalid order side: " + side);
			return 0;
		}
		
		if (quantity < 1 || quantity > 3000)
		{
			System.err.println("Invalid order quantity: " + quantity);
			return 0;
		}
		
		if (limitPrice.getMoneyValue() < 1.0 || limitPrice.getMoneyValue() > 500.0)
		{
			System.err.println("Invalid order price: " + limitPrice.getMoneyValue());
			return 0;
		}
		
		int orderId = NextValidOrderIdMgr.getNextValidId();

		Contract contract = new Contract();
		contract.m_secType = "STK";
		contract.m_symbol = m_config.symbol;
		contract.m_localSymbol = m_config.symbol;
		contract.m_exchange = "SMART";
		contract.m_currency = "USD";
		
		com.ib.client.Order order = new com.ib.client.Order();
		order.m_clientId = m_config.clientId;
		order.m_orderId = orderId;
		order.m_action = side;
		order.m_lmtPrice = limitPrice.getMoneyValue();
		order.m_orderType = "LMT";
		order.m_totalQuantity = quantity;
		order.m_tif = "DAY";
		order.m_transmit = true;
		
		m_ibms.placeOrder(orderId, contract, order);
		
		return orderId;	
	
	}
	

	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		
		for (int i = 0 ; i < NUM_ROWS ; ++i)
		{
			if (source == m_buyButtons[i])
			{
				handleBuyButton(i);
			}
			else if (source == m_sellButtons[i])
			{
				handleSellButton(i);
			}
		}
		
		
		if (source == m_upButton)
		{
			handleUpButton();
		}
		else if (source == m_downButton)
		{
			handleDownButton();
		}
		else if (source == m_cancelAllButton)
		{
			handleCancelAllButton();
		}
		else if (source == m_revButton)
		{
			handleRevButton();
		}
		else if (source == m_flatButton)
		{
			handleFlatButton();
		}
		else if (source == m_centerButton)
		{
			handleCenterButton();
		}
				
	}

	public void mouseClicked(MouseEvent event)
	{
		Object source = event.getSource();
		
		for (int i = 0 ; i < NUM_ROWS ; ++i)
		{
			if (source == m_priceLabels[i])
			{
				synchronized (priceLock)
				{
					if (null != m_lastPrice)
					{
						recenter(m_lastPrice);
					}
				}

				break;
			}
		}
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
	}

	public void tickPrice(int tickerId, int field, double price, int canAutoExecute)
	{
		if (m_tickerId == tickerId)
		{
			if (field == TickPriceData.TICK_PRICE_FIELD_LAST ||
				field == TickPriceData.TICK_PRICE_FIELD_BID ||
				field == TickPriceData.TICK_PRICE_FIELD_ASK)
			{
				synchronized (priceLock)
				{
					if (field == TickPriceData.TICK_PRICE_FIELD_LAST)
					{
						m_lastPrice = new Price(price);
					}
					else if (field == TickPriceData.TICK_PRICE_FIELD_BID)
					{
						m_bidPrice = new Price(price);
					}
					else if (field == TickPriceData.TICK_PRICE_FIELD_ASK)
					{
						m_askPrice = new Price(price);
					}
					
					m_frame.setTitle(m_config.symbol + " - " + m_lastPrice.getStringValue());
					
					for (int i = 0 ; i < NUM_ROWS ; ++i)
					{
						if (m_priceLabels[i] != null)
						{
							m_priceLabels[i].setOpaque(true);
		
							boolean updated = false;
							
							if (m_bidPrice.equals(m_prices[i]))
							{
								m_priceLabels[i].setBackground(Color.BLUE);
								updated = true;
							}
							if (m_askPrice.equals(m_prices[i]))
							{
								m_priceLabels[i].setBackground(Color.RED);
								updated = true;
							}
							
							if (m_lastPrice.equals(m_prices[i]))
							{
								m_priceLabels[i].setBackground(Color.ORANGE);
								updated = true;
							}
							
							if (false == updated)
							{
								m_priceLabels[i].setBackground(Color.WHITE);
							}
						}
					}
					
					if (m_firstTick && field == TickPriceData.TICK_PRICE_FIELD_LAST)
					{
						m_firstTick = false;
						
						recenter(m_lastPrice);
					}
				}
			}
		}
	}

	public void tickSize(int tickerId, int field, int size)
	{
	}

	public void tickOptionComputation(int tickerId, int field,
			double impliedVol, double delta, double optPrice,
			double pvDividend, double gamma, double vega, double theta,
			double undPrice)
	{
	}

	public void tickGeneric(int tickerId, int tickType, double value)
	{
	}

	public void tickString(int tickerId, int tickType, String value)
	{
	}

	public void tickEFP(int tickerId, int tickType, double basisPoints,
			String formattedBasisPoints, double impliedFuture, int holdDays,
			String futureExpiry, double dividendImpact, double dividendsToExpiry)
	{
	}
	
}
