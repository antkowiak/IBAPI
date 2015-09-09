/**
 * 
 */
package com.ryanantkowiak.IBAPI;

import java.util.HashMap;
import java.util.Map;

import com.ib.client.Contract;

/**
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 *
 */
public class PositionMgr
{
	private static Map<String, PositionData> m_positionsClean = new HashMap<String, PositionData>();
	private static Map<String, PositionData> m_positionsDirty = new HashMap<String, PositionData>();
	
	public static synchronized void position(String account, Contract contract, int pos, double avgCost)
	{
		PositionData pd = new PositionData();
		pd.position(account, contract, pos, avgCost);
		
		m_positionsDirty.put(account, pd);
	}
	
	public static synchronized void positionEnd()
	{
		m_positionsClean = m_positionsDirty;
		m_positionsDirty = new HashMap<String, PositionData>();
	}
	
	public static synchronized PositionData getPositions(String account)
	{
		PositionData data =  m_positionsClean.get(account);
		
		if (data != null)
		{
			return data.clone();
		}

		return null;
	}
}
