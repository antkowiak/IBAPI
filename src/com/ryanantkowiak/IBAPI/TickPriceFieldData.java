/**
 * 
 */
package com.ryanantkowiak.IBAPI;

/**
 * @author Ryan Antkowiak 
 *
 */
public class TickPriceFieldData
{
	public static final int TICK_PRICE_FIELD_BID = 1;
	public static final int TICK_PRICE_FIELD_ASK = 2;
	public static final int TICK_PRICE_FIELD_LAST = 4;
	public static final int TICK_PRICE_FIELD_HIGH = 6;
	public static final int TICK_PRICE_FIELD_LOW = 7;
	public static final int TICK_PRICE_FIELD_CLOSE = 9;
	
	public int tickerId;
	public int field;
	public double price;
	public int canAutoExecute;
	
	public TickPriceFieldData()
	{
	}
	
	public TickPriceFieldData clone()
	{
		TickPriceFieldData tpfd = new TickPriceFieldData();
		tpfd.tickerId = tickerId;
		tpfd.field = field;
		tpfd.price = price;
		tpfd.canAutoExecute = canAutoExecute;
		return tpfd;
	}
	
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute)
	{
		this.tickerId = tickerId;
		this.field = field;
		this.price = price;
		this.canAutoExecute = canAutoExecute;
	}
	
}
