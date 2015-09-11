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
public class TickSizeData
{
	public static final int TICK_SIZE_FIELD_BID = 0;
	public static final int TICK_SIZE_FIELD_ASK = 3;
	public static final int TICK_SIZE_FIELD_LAST = 5;
	public static final int TICK_SIZE_FIELD_VOLUME = 8;

	
	public int tickerId;
	
	// Map of fields to the field data
	public Map<Integer, TickSizeFieldData> fieldData;
	
	public TickSizeData()
	{
		fieldData = new HashMap<Integer, TickSizeFieldData>();
	}
	
	public TickSizeData clone()
	{
		TickSizeData tsd = new TickSizeData();
		tsd.tickerId = tickerId;
		
		for (Map.Entry<Integer, TickSizeFieldData> entry : fieldData.entrySet())
		{
			tsd.fieldData.put(new Integer(entry.getKey()), entry.getValue().clone());
		}
		
		return tsd;
	}
	
	public void tickSize(int tickerId, int field, int size)
	{
		this.tickerId = tickerId;
		TickSizeFieldData tsfd = new TickSizeFieldData();
		tsfd.tickSize(tickerId, field, size);
		fieldData.put(field, tsfd);
	}
}
