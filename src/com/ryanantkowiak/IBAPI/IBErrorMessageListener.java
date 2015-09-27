/**
 * 
 */
package com.ryanantkowiak.IBAPI;

/**
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 *
 */
public interface IBErrorMessageListener
{
	/*
	 * 	@brief	Interface method to handle IB API error messages
	 * 	@param	text - text information about the IB API error message
	 */
	public abstract void handleErrorMessage(String text);
}
