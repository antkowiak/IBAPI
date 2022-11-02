/**
 * 
 */
package com.ryanantkowiak.IBAPI;

/**
 * @author Ryan Antkowiak 
 *
 */
public class TickIdMgr
{
	private static int m_nextTickerId = 1;

	public static synchronized int getNextTickerId()
	{
		int next = m_nextTickerId;
		++m_nextTickerId;
		return next;
	}
}
