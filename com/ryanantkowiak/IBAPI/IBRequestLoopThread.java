package com.ryanantkowiak.IBAPI;

public class IBRequestLoopThread extends Thread
{
	private IBMessageSender m_ibms;
	private int m_delayMS;
	
	public IBRequestLoopThread(IBMessageSender ibms)
	{
		m_ibms = ibms;
		m_delayMS = 1000;
	}
	
	public IBRequestLoopThread(IBMessageSender ibms, int delayMS)
	{
		m_ibms = ibms;
		m_delayMS = delayMS;	
	}
	
	
	@Override
	public void run()
	{
		while (true)
		{
			m_ibms.reqPositions();
			m_ibms.reqOpenOrders();
			
			try
			{
				Thread.sleep(m_delayMS);
			}
			catch (Exception e)
			{
				System.err.println("IBRequestLoopThread Exception - " + e.getMessage());
			}
		}
	}
}
