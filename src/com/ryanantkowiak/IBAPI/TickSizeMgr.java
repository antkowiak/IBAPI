/**
 * TickSizeMgr - Manager class to cache tick sizes
 */
package com.ryanantkowiak.IBAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ryan Antkowiak 
 *
 */
public class TickSizeMgr
{
	private static Map<Integer, TickSizeData> m_tickSizes = new HashMap<Integer, TickSizeData>();
	
	public static synchronized void tickSize(int tickerId, int field, int size)
	{
		TickSizeData tsd = m_tickSizes.get(tickerId);
			
		if (null == tsd)
		{
			tsd = new TickSizeData();
		}
		
		tsd.tickSize(tickerId, field, size);
	}
	
	public static synchronized TickSizeData getTickSizeData(int tickerId)
	{
		return m_tickSizes.get(tickerId).clone();
	}
}
