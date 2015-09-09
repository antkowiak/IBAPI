/**
 * 
 */
package com.ryanantkowiak.IBAPI;


/**
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 *
 */
public class Main
{
	public static void main(String [] args)
	{
		IBMessageReceiver ibmr = new IBMessageReceiver();
		IBMessageSender ibms = new IBMessageSender(ibmr);

		LogView lv = new LogView();
		ibmr.addIBCommmunicationListener(lv);
		ibmr.addIBErrorMessageListener(lv);
		
		ibms.eConnect("localhost", 7496, 0);
		
		IBRequestLoopThread loopThread = new IBRequestLoopThread(ibms);
		loopThread.start();
		
		IBLadderTraderConfig config = new IBLadderTraderConfig();
		config.defaultQuantity = 200;
		config.symbol = "GM";
		config.clientId = 0;
		
		while (ManagedAccountsMgr.isEmpty())
		{
			try { Thread.sleep(100); } catch (Exception exception) { }
		}
		
		config.account = ManagedAccountsMgr.getAccountList().get(0);

		@SuppressWarnings("unused")
		IBLadderTrader iblt = new IBLadderTrader(ibms, ibmr, config);

	}
}
