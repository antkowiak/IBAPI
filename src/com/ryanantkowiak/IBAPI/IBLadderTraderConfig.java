/**
 * 	@brief	Implementation of the IBLatterTraderConfig class
 * 
 * 	@author Ryan Antkowiak 
 * 
 */
package com.ryanantkowiak.IBAPI;

/**
 *	@author Ryan Antkowiak 
 *
 *	@class	IBLadderTraderConfig - 	Configuration of IB Ladder Trader, contains the account
 *									number, symbol, default quantity, and clientId
 */
public class IBLadderTraderConfig
{
	/*
	 * The account ID to trade with
	 */
	public String account;
	
	/*
	 * The symbol to trade
	 */
	public String symbol;
	
	/*
	 * The default quantity per button click
	 */
	public int defaultQuantity;
	
	/*
	 * The client ID for connection to TWS
	 */
	public int clientId;
	
	/*
	 * @brief	Default constructor
	 */
	public IBLadderTraderConfig()
	{
	}
	
	/*
	 *	@brief	Constructor that accepts default values for the parameters
	 *	@param	account - the account ID to trade with
	 *	@param	clientId - the client ID for connection to TWS
	 *	@param	symbol - the symbol to trade
	 *	@param	defaultQuantity - the default quantity per button click
	 */
	public IBLadderTraderConfig(String account, int clientId, String symbol, int defaultQuantity)
	{
		this.account = account;
		this.clientId = clientId;
		this.symbol = symbol;
		this.defaultQuantity = defaultQuantity;
	}
	
	/*
	 * 	@brief	Constructor to clone/copy itself
	 * 	@param	cfg - the object to clone/copy
	 */
	public IBLadderTraderConfig(IBLadderTraderConfig cfg)
	{
		account = cfg.account;
		clientId = cfg.clientId;
		symbol = cfg.symbol;
		defaultQuantity = cfg.defaultQuantity;
	}

}
