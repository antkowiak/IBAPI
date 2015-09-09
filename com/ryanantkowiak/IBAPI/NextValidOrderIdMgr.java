/**
 * 
 */
package com.ryanantkowiak.IBAPI;

/**
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 *
 */
public class NextValidOrderIdMgr
{

	public static final int INVALID_ORDER_ID = -1;
	
	private static int m_nextValidOrderId = INVALID_ORDER_ID;
	
	public static synchronized void setNextValidId(int orderId)
	{
		m_nextValidOrderId = orderId;
	}
	
	public static synchronized int getNextValidId()
	{	
		int nextId = m_nextValidOrderId;
		++m_nextValidOrderId;
		return nextId;
	}
	
}
