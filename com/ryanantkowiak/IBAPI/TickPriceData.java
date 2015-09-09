/**
 * 
 */
package com.ryanantkowiak.IBAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 *
 */

public class TickPriceData
{
	public static final int TICK_PRICE_FIELD_BID = 1;
	public static final int TICK_PRICE_FIELD_ASK = 2;
	public static final int TICK_PRICE_FIELD_LAST = 4;
	public static final int TICK_PRICE_FIELD_HIGH = 6;
	public static final int TICK_PRICE_FIELD_LOW = 7;
	public static final int TICK_PRICE_FIELD_CLOSE = 9;
	
	public int tickerId;
	
	// Map of fields to the field data
	public Map<Integer, TickPriceFieldData> fieldData;
	
	public TickPriceData()
	{
		fieldData = new HashMap<Integer, TickPriceFieldData>();
	}
	
	public TickPriceData clone()
	{
		TickPriceData tpd = new TickPriceData();
		tpd.tickerId = tickerId;
		
		for (Map.Entry<Integer, TickPriceFieldData> entry : fieldData.entrySet())
		{
			tpd.fieldData.put(new Integer(entry.getKey()), entry.getValue().clone());
		}
		
		return tpd;
	}
	
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute)
	{
		this.tickerId = tickerId;
		TickPriceFieldData tpfd = new TickPriceFieldData();
		tpfd.tickPrice(tickerId, field, price, canAutoExecute);
		fieldData.put(field, tpfd);
	}
}
