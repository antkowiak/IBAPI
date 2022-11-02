/**
 * 
 */
package com.ryanantkowiak.IBAPI;

import java.util.HashMap;
import java.util.Map;

import com.ib.client.Contract;

/**
 * @author Ryan Antkowiak 
 *
 */
public class PositionData
{
	public String account;
	public Map<Contract, Integer> positionMap;
	public Map<Contract, Double> avgCostMap;
	
	public PositionData()
	{
		positionMap = new HashMap<Contract, Integer>();
		avgCostMap = new HashMap<Contract, Double>();
	}
	
	public PositionData clone()
	{
		PositionData pd = new PositionData();
		pd.account = account;
		
		for (Map.Entry<Contract, Integer> entry : positionMap.entrySet())
		{
			pd.positionMap.put(IBUtils.clone(entry.getKey()), new Integer(entry.getValue()));
		}
		
		for (Map.Entry<Contract, Double> entry : avgCostMap.entrySet())
		{
			pd.avgCostMap.put(IBUtils.clone(entry.getKey()), new Double(entry.getValue()));
		}
		
		return pd;
	}

	public void position(String account, Contract contract, int pos, double avgCost)
	{
		this.account = account;
		positionMap.put(IBUtils.clone(contract), pos);
		avgCostMap.put(IBUtils.clone(contract), avgCost);
	}
}
