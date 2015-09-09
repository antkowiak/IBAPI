/**
 * 
 */

package com.ryanantkowiak.IBAPI;

import java.util.ArrayList;
import java.util.List;

import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.UnderComp;

/**
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 *
 */
public class IBMessageReceiver implements EWrapper
{
	public List<IBCommunicationListener> m_commmunicationListeners;
	public List<IBErrorMessageListener> m_errorMessageListeners;
	public List<IBTickListener> m_tickListeners;
	
	public IBMessageReceiver()
	{
		m_commmunicationListeners = new ArrayList<IBCommunicationListener>();
		m_errorMessageListeners = new ArrayList<IBErrorMessageListener>();
		m_tickListeners = new ArrayList<IBTickListener>();
	}
	
	public void addIBCommmunicationListener(IBCommunicationListener listener)
	{
		synchronized (m_commmunicationListeners)
		{
			if (null != listener)
			{
				if (!m_commmunicationListeners.contains(listener))
				{
					m_commmunicationListeners.add(listener);
				}
			}
		}
	}
	
	public void removeIBCommunicationListener(IBCommunicationListener listener)
	{
		synchronized (m_commmunicationListeners)
		{
			if (null != listener)
			{
				m_commmunicationListeners.remove(listener);	
			}
		}
	}
	
	public void addIBErrorMessageListener(IBErrorMessageListener listener)
	{
		synchronized (m_errorMessageListeners)
		{
			if (null != listener)
			{
				if (!m_errorMessageListeners.contains(listener))
				{
					m_errorMessageListeners.add(listener);
				}
			}
		}
	}
	
	public void removeIBErrorMessageListener(IBErrorMessageListener listener)
	{
		synchronized (m_errorMessageListeners)
		{
			if (null != listener)
			{
				m_errorMessageListeners.remove(listener);	
			}
		}
	}
	
	public void addIBTickListener(IBTickListener listener)
	{
		synchronized (m_tickListeners)
		{
			if (null != listener)
			{
				if (!m_tickListeners.contains(listener))
				{
					m_tickListeners.add(listener);
				}
			}
		}
	}
	
	public void removeIBTickListener(IBTickListener listener)
	{
		synchronized (m_tickListeners)
		{
			if (null != listener)
			{
				m_tickListeners.remove(listener);
			}
		}
	}
	
	private void logFunction(String text)
	{
		synchronized (m_commmunicationListeners)
		{
			for (IBCommunicationListener cl : m_commmunicationListeners)
			{
				cl.handleCommmunicationText("____________________________________________________________________________");
				cl.handleCommmunicationText("IBMessageReceiver." + text);
			}
		}
	}
	
	private void logPurpose(String text)
	{
		synchronized (m_commmunicationListeners)
		{
			for (IBCommunicationListener cl : m_commmunicationListeners)
			{
				cl.handleCommmunicationText("PURPOSE: " + text);
			}
		}
	}
	
	
	private void logParams(Object ... objects)
	{
		if (null != objects)
		{
			StringBuilder sb = new StringBuilder();
			
			sb.append("Parameters:\n");
			
			for (int i = 0 ; i < objects.length ; ++i)
			{
				sb.append("PARAM");
				sb.append(i);
				sb.append("=\"");
				if (objects[i] == null)
				{
					sb.append("(null)\n");
				}
				else
				{
					sb.append(objects[i].toString());
					sb.append("\"\n");
				}

			}
			
			synchronized (m_commmunicationListeners)
			{
				for (IBCommunicationListener cl : m_commmunicationListeners)
				{
					cl.handleCommmunicationText(sb.toString());
				}
			}
		}
	}
	

	public void error(Exception e)
	{
		logFunction("error(Exception e)");
        logPurpose("This method is called when an exception occurs while handling a request.");
		logParams(e);
		
		synchronized (m_errorMessageListeners)
		{
			for (IBErrorMessageListener eml : m_errorMessageListeners)
			{
				eml.handleErrorMessage(e.getMessage());
			}
		}
	}

	public void error(String str)
	{
		logFunction("error(String str)");
        logPurpose("This method is called when TWS wants to send an error message to the client. (V1).");
		logParams(str);
		
		synchronized (m_errorMessageListeners)
		{
			for (IBErrorMessageListener eml : m_errorMessageListeners)
			{
				eml.handleErrorMessage(str);
			}
		}
	}

	public void error(int id, int errorCode, String errorMsg)
	{
		logFunction("error(int orderId, int errorCode, String errorMsg)");
        logPurpose("This method is called when there is an error with the communication or when TWS wants to send a message to the client.");
		logParams(id, errorCode, errorMsg);
		
		StringBuilder sb = new StringBuilder();
		sb.append("id=");
		sb.append(id);
		sb.append(" errorCode=");
		sb.append(errorCode);
		sb.append(" errorMsg=");
		sb.append(errorMsg);
		
		synchronized (m_errorMessageListeners)
		{
			for (IBErrorMessageListener eml : m_errorMessageListeners)
			{
				eml.handleErrorMessage(sb.toString());
			}
		}
	}

	public void connectionClosed()
	{
		logFunction("connectionClosed()");
        logPurpose("This method is called when TWS closes the sockets connection, or when TWS is shut down.");
		logParams();

	}

	public void tickPrice(int tickerId, int field, double price, int canAutoExecute)
	{
		logFunction("tickPrice(int tickerId, int field, double price, int canAutoExecute)");
        logPurpose("This method is called when the market data changes. Prices are updated immediately with no delay.");
		logParams(tickerId, field, price, canAutoExecute);
		
		TickPriceMgr.tickPrice(tickerId, field, price, canAutoExecute);
		
		synchronized (m_tickListeners)
		{
			for (IBTickListener tl : m_tickListeners)
			{
				tl.tickPrice(tickerId, field, price, canAutoExecute);
			}
		}

	}

	public void tickSize(int tickerId, int field, int size)
	{
		logFunction("tickSize(int tickerId, int field, int size)");
        logPurpose("This method is called when the market data changes. Sizes are updated immediately with no delay.");
		logParams(tickerId, field, size);
		
		TickSizeMgr.tickSize(tickerId, field, size);
		
		synchronized (m_tickListeners)
		{
			for (IBTickListener tl : m_tickListeners)
			{
				tl.tickSize(tickerId, field, size);
			}
		}
	}

	public void tickOptionComputation(	int tickerId,
										int field,
										double impliedVol,
										double delta,
										double optPrice,
										double pvDividend,
										double gamma,
										double vega,
										double theta,
										double undPrice)
	{
		logFunction("tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice)");
        logPurpose("This method is called when the market in an option or its underlier moves. TWS's option model volatilities, prices, and deltas, along with the present value of dividends expected on that options underlier are received.");
		logParams(tickerId, field, impliedVol, delta, optPrice, pvDividend, gamma, vega, theta, undPrice);

		synchronized (m_tickListeners)
		{
			for (IBTickListener tl : m_tickListeners)
			{
				tl.tickOptionComputation(tickerId, field, impliedVol, delta, optPrice, pvDividend, gamma, vega, theta, undPrice);
			}
		}
	}

	public void tickGeneric(int tickerId, int tickType, double value)
	{
		logFunction("tickGeneric(int tickerId, int tickType, double value)");
        logPurpose("This method is called when the market data changes. Values are updated immediately with no delay.");
		logParams(tickerId, tickType, value);

		synchronized (m_tickListeners)
		{
			for (IBTickListener tl : m_tickListeners)
			{
				tl.tickGeneric(tickerId, tickType, value);
			}
		}
	}

	public void tickString(int tickerId, int tickType, String value)
	{
		logFunction("tickString(int tickerId, int tickType, String value)");
        logPurpose("This method is called when the market data changes. Values are updated immediately with no delay.");
		logParams(tickerId, tickType, value);
		
		synchronized (m_tickListeners)
		{
			for (IBTickListener tl : m_tickListeners)
			{
				tl.tickString(tickerId, tickType, value);
			}
		}

	}

	public void tickEFP(	int tickerId,
							int tickType,
							double basisPoints,
							String formattedBasisPoints,
							double impliedFuture,
							int holdDays,
							String futureExpiry,
							double dividendImpact,
							double dividendsToExpiry)
	{
		logFunction("tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureExpiry, double dividendImpact, double dividendsToExpiry)");
        logPurpose("This method is called when the market data changes. Values are updated immediately with no delay.");
		logParams(tickerId, tickType, basisPoints, formattedBasisPoints, impliedFuture, holdDays, futureExpiry, dividendImpact, dividendsToExpiry);

		synchronized (m_tickListeners)
		{
			for (IBTickListener tl : m_tickListeners)
			{
				tl.tickEFP(tickerId, tickType, basisPoints, formattedBasisPoints, impliedFuture, holdDays, futureExpiry, dividendImpact, dividendsToExpiry);
			}
		}
	}

	public void orderStatus(	int orderId,
								String status,
								int filled,
								int remaining,
								double avgFillPrice,
								int permId,
								int parentId,
								double lastFillPrice,
								int clientId,
								String whyHeld)
	{
		logFunction("orderStatus(int orderId, String status, int filled, int remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld)");
        logPurpose("This method is called whenever the status of an order changes. It is also fired after reconnecting to TWS if the client has any open orders.");
		logParams(orderId, status, filled, remaining, avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld);

	}

	public void openOrder(int orderId, Contract contract, Order order, OrderState orderState)
	{
		logFunction("openOrder(int orderId, Contract contract, Order order, OrderState orderState)");
        logPurpose("This method is called to feed in open orders.");
		logParams(orderId, contract, order, orderState);

	}

	public void openOrderEnd()
	{
		logFunction("openOrderEnd()");
        logPurpose("This is called at the end of a given request for open orders.");
		logParams();

	}

	public void updateAccountValue(String key, String value, String currency, String accountName)
	{
		logFunction("updateAccountValue(String key, String value, String currency, String accountName)");
        logPurpose("This method is called only when reqAccountUpdates() method on the EClientSocket object has been called.");
		logParams(key, value, currency, accountName);

	}

	public void updatePortfolio(	Contract contract,
									int position,
									double marketPrice,
									double marketValue,
									double averageCost,
									double unrealizedPNL,
									double realizedPNL,
									String accountName)
	{
		logFunction("updatePortfolio(Contract contract, int position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName)");
        logPurpose("This method is called only when reqAccountUpdates() method on the EClientSocket object has been called.");
		logParams(contract, position, marketPrice, marketValue, averageCost, unrealizedPNL, realizedPNL, accountName);

	}

	public void updateAccountTime(String timeStamp)
	{
		logFunction("updateAccountTime(String timeStamp)");
        logPurpose("This method is called only when reqAccountUpdates() method on the EClientSocket object has been called.");
		logParams(timeStamp);

	}

	public void accountDownloadEnd(String accountName)
	{
		logFunction("accountDownloadEnd(String accountName)");
        logPurpose("This event is called after a batch updateAccountValue() and updatePortfolio() is sent.");
		logParams(accountName);

	}

	public void nextValidId(int orderId)
	{
		logFunction("nextValidId(int orderId)");
        logPurpose("This method is called after a successful connection to TWS.");
		logParams(orderId);

		NextValidOrderIdMgr.setNextValidId(orderId);
	}

	public void contractDetails(int reqId, ContractDetails contractDetails)
	{
		logFunction("contractDetails(int reqId, ContractDetails contractDetails)");
        logPurpose("This method is called only when reqContractDetails method on the EClientSocket object has been called.");
		logParams(reqId, contractDetails);

	}

	public void bondContractDetails(int reqId, ContractDetails contractDetails)
	{
		logFunction("bondContractDetails(int reqId, ContractDetails contractDetails)");
        logPurpose("This method is called only when reqContractDetails method on the EClientSocket object has been called for bonds.");
		logParams(reqId, contractDetails);

	}

	public void contractDetailsEnd(int reqId)
	{
		logFunction("contractDetailsEnd(int reqId)");
        logPurpose("This method is called once all contract details for a given request are received. This helps to define the end of an option chain.");
		logParams(reqId);

	}

	public void execDetails(int reqId, Contract contract, Execution execution)
	{
		logFunction("execDetails(int reqId, Contract contract, Execution execution)");
        logPurpose("This method is called when the reqExecutions() method is invoked, or when an order is filled.");
		logParams(reqId, contract, execution);
		
	}

	public void execDetailsEnd(int reqId)
	{
		logFunction("execDetailsEnd(int reqId)");
        logPurpose("This method is called once all executions have been sent to a client in response to reqExecutions().");
		logParams(reqId);

	}

	public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size)
	{
		logFunction("updateMktDepth(int tickerId, int position, int operation, int side, double price, int size)");
        logPurpose("This method is called when the market depth changes.");
		logParams(tickerId, position, operation, side, price, size);

	}

	public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, int size)
	{
		logFunction("updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, int size)");
        logPurpose("This method is called when the Level II market depth changes.");
		logParams(tickerId, position, marketMaker, operation, side, price, size);

	}

	public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange)
	{
		logFunction("updateNewsBulletin(int msgId, int msgType, String message, String origExchange)");
        logPurpose("This method is triggered for each new bulletin if the client has subscribed (i.e. by calling the reqNewsBulletins() method.");
		logParams(msgId, msgType, message, origExchange);

	}

	public void managedAccounts(String accountsList)
	{
		logFunction("managedAccounts(String accountsList)");
        logPurpose("This method is called when a successful connection is made to an account. It is also called when the reqManagedAccts() method is invoked.");
		logParams(accountsList);
		
		ManagedAccountsMgr.managedAccounts(accountsList);

	}

	public void receiveFA(int faDataType, String xml)
	{
		logFunction("receiveFA(int faDataType, String xml)");
        logPurpose("This method receives previously requested FA configuration information from TWS.");
		logParams(faDataType, xml);

	}

	public void historicalData(	int reqId,
								String date,
								double open,
								double high,
								double low,
								double close,
								int volume,
								int count,
								double WAP,
								boolean hasGaps)
	{
		logFunction("historicalData(int reqId, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps)");
        logPurpose("This method receives the requested historical data results.");
		logParams(reqId, date, open, high, low, close, volume, count, WAP, hasGaps);

	}

	public void scannerParameters(String xml)
	{
		logFunction("scannerParameters(String xml)");
        logPurpose("This method receives an XML document that describes the valid parameters that a scanner subscription can have.");
		logParams(xml);

	}

	public void scannerData(	int reqId,
								int rank,
								ContractDetails contractDetails,
								String distance,
								String benchmark,
								String projection,
								String legsStr)
	{
		logFunction("scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr)");
        logPurpose("This method receives the requested market scanner data results.");
		logParams(reqId, rank, contractDetails, distance, benchmark, projection, legsStr);

	}

	public void scannerDataEnd(int reqId)
	{
		logFunction("scannerDataEnd(int reqId)");
        logPurpose("This method is called when the snapshot is received and marks the end of one scan.");
		logParams(reqId);

	}

	public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count)
	{
		logFunction("realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count)");
        logPurpose("This method receives the real-time bars data results.");
		logParams(reqId, time, open, high, low, close, volume, wap, count);

	}

	public void currentTime(long time)
	{
		logFunction("currentTime(long time)");
        logPurpose("This method receives the current system time on the server side.");
		logParams(time);

	}

	public void fundamentalData(int reqId, String data)
	{
		logFunction("fundamentalData(int reqId, String data)");
        logPurpose("This method is called to receive Reuters global fundamental market data. There must be a subscription to Reuters Fundamental set up in Account Management before you can receive this data.");
		logParams(reqId, data);

	}
	
	public void deltaNeutralValidation(int reqId, UnderComp underComp)
	{
		logFunction("deltaNeutralValidation(int reqId, UnderComp underComp)");
        logPurpose("Upon accepting a Delta-Neutral RFQ(request for quote), the server sends a deltaNeutralValidation() message with the UnderComp structure. If the delta and price fields are empty in the original request, the confirmation will contain the current values from the server. These values are locked when the RFQ is processed and remain locked until the RFQ is canceled.");
		logParams(reqId, underComp);

	}

	public void tickSnapshotEnd(int reqId)
	{
		logFunction("tickSnapshotEnd(int reqId)");
        logPurpose("This is called when a snapshot market data subscription has been fully handled and there is nothing more to wait for. This also covers the timeout case.");
		logParams(reqId);

	}

	public void marketDataType(int reqId, int marketDataType)
	{
		logFunction("marketDataType(int reqId, int marketDataType)");
        logPurpose("TWS sends a marketDataType(type) callback to the API, where type is set to Frozen or RealTime, to announce that market data has been switched between frozen and real-time. This notification occurs only when market data switches between real-time and frozen. The marketDataType( ) callback accepts a reqId parameter and is sent per every subscription because different contracts can generally trade on a different schedule.");
		logParams(reqId, marketDataType);

	}

	public void commissionReport(CommissionReport commissionReport)
	{
		logFunction("commissionReport(CommissionReport commissionReport)");
        logPurpose("The commissionReport() callback is triggered as follows: Immediately after a trade execution, or By calling reqExecutions().");
		logParams(commissionReport);

	}

	public void position(String account, Contract contract, int pos, double avgCost)
	{
		logFunction("position(String account, Contract contract, int pos, double avgCost)");
        logPurpose("This event returns real-time positions for all accounts in response to the reqPositions() method.");
		logParams(account, contract, pos, avgCost);
		
		PositionMgr.position(account, contract, pos, avgCost);
		
	}

	public void positionEnd()
	{
		logFunction("positionEnd()");
        logPurpose("This is called once all position data for a given request are received and functions as an end marker for the position() data.");
		logParams();
		
		PositionMgr.positionEnd();
	}

	public void accountSummary(int reqId, String account, String tag, String value, String currency)
	{
		logFunction("accountSummary(int reqId, String account, String tag, String value, String currency)");
        logPurpose("Returns the data from the TWS Account Window Summary tab in response to reqAccountSummary().");
		logParams(reqId, account, tag, value, currency);

	}

	public void accountSummaryEnd(int reqId)
	{
		logFunction("accountSummaryEnd(int reqId)");
        logPurpose("This method is called once all account summary data for a given request are received.");
		logParams(reqId);

	}

	public void verifyMessageAPI(String apiData)
	{
		logFunction("verifyMessageAPI(String apiData)");
        logPurpose("Undocumented!");
		logParams(apiData);

	}

	public void verifyCompleted(boolean isSuccessful, String errorText)
	{
		logFunction("verifyCompleted(boolean isSuccessful, String errorText)");
        logPurpose("Undocumented!");
		logParams(isSuccessful, errorText);

	}

	public void displayGroupList(int reqId, String groups)
	{
		logFunction("displayGroupList(int reqId, String groups)");
        logPurpose("This callback is a one-time response to queryDisplayGroups().");
		logParams(reqId, groups);

	}

	public void displayGroupUpdated(int reqId, String contractInfo)
	{
		logFunction("displayGroupUpdated(int reqId, String contractInfo)");
        logPurpose("This is sent by TWS to the API client once after receiving the subscription request subscribeToGroupEvents(), and will be sent again if the selected contract in the subscribed display group has changed.");
		logParams(reqId, contractInfo);

	}

}
