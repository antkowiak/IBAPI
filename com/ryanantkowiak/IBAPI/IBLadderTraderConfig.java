/**
 * 
 */
package com.ryanantkowiak.IBAPI;

/**
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 *
 */
public class IBLadderTraderConfig
{
	public String account;
	public String symbol;
	public int defaultQuantity;
	public int clientId;
	
	public IBLadderTraderConfig()
	{

	}
	
	public IBLadderTraderConfig(String account, int clientId, String symbol, int defaultQuantity)
	{
		this.account = account;
		this.clientId = clientId;
		this.symbol = symbol;
		this.defaultQuantity = defaultQuantity;
	}
	
	public IBLadderTraderConfig(IBLadderTraderConfig cfg)
	{
		account = cfg.account;
		clientId = cfg.clientId;
		symbol = cfg.symbol;
		defaultQuantity = cfg.defaultQuantity;
	}


	
}
