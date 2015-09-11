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
public class TickPriceMgr
{	
	private static Map<Integer, TickPriceData> m_tickPrices = new HashMap<Integer, TickPriceData>();
	
	private static Map<Integer, String> m_idToSymbol = new HashMap<Integer, String>();
	private static Map<String, Integer> m_symbolToId = new HashMap<String, Integer>();
	
	
	public static synchronized void tickPrice(int tickerId, int field, double price, int canAutoExecute)
	{
		TickPriceData tpd = m_tickPrices.get(tickerId);
			
		if (null == tpd)
		{
			tpd = new TickPriceData();
		}
		
		tpd.tickPrice(tickerId, field, price, canAutoExecute);
	}
	
	
	public static synchronized int getTickerIdForSymbol(String symbol)
	{
		if (m_symbolToId.containsKey(symbol))
		{
			return m_symbolToId.get(symbol);
		}
		else
		{
			int id = TickIdMgr.getNextTickerId();
			m_idToSymbol.put(id, symbol);
			m_symbolToId.put(symbol, id);
			return id;
		}
	}
	
	public static synchronized String getSymbolForTickerId(int tickerId)
	{
		return m_idToSymbol.get(tickerId);
	}
	
	public static synchronized TickPriceData getTickPriceData(int tickerId)
	{
		return m_tickPrices.get(tickerId).clone();
	}
}
