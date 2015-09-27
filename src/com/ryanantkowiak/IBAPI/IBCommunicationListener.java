/**
 * IBCommunicationListener - Interface for communication listeners
 */
package com.ryanantkowiak.IBAPI;

/**
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 *
 */
public interface IBCommunicationListener
{
	
	/*
	 *	@brief	Interface method to handle IB API communication text strings
	 *	@param	text - the text string for the IB API communication
	 */
	public abstract void handleCommmunicationText(String text);
	
}
