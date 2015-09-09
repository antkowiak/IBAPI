/**
 * 
 */
package com.ryanantkowiak.IBAPI;

/**
 * @author Ryan Antkowiak (antkowiak@gmail.com)
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
