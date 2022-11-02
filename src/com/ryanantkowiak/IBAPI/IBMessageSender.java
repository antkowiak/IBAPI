package com.ryanantkowiak.IBAPI;
/**
 * 
 */


import java.util.List;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.ExecutionFilter;
import com.ib.client.Order;
import com.ib.client.TagValue;

/**
 * @author Ryan Antkowiak 
 *
 */
public class IBMessageSender
{
	EClientSocket m_clientSocket;
	
	@SuppressWarnings("unused")
	private IBMessageSender()
	{
	}
	
	public IBMessageSender(IBMessageReceiver ibmr)
	{
		m_clientSocket = new EClientSocket(ibmr);
	}
	
	public void eConnect(String host, int port, int clientId)
	{
		m_clientSocket.eConnect(host, port, clientId);
	}
	
	public void reqMktData(int tickerId, Contract contract, String genericTickList, boolean snapshot, List<TagValue> mktDataOptions)
	{	
		m_clientSocket.reqMktData(tickerId, contract, genericTickList, snapshot, mktDataOptions);
	}
	
	public void reqPositions()
	{
		m_clientSocket.reqPositions();
	}
	
	public void reqOpenOrders()
	{
		m_clientSocket.reqOpenOrders();
	}
	
	public void managedAccounts()
	{
		m_clientSocket.reqManagedAccts();
	}
	
	public void reqAccountUpdates(boolean subscribe, String acctCode)
	{
		m_clientSocket.reqAccountUpdates(subscribe, acctCode);
	}
	
	public void requestStockExecutions(int reqId, ExecutionFilter filter)
	{
		m_clientSocket.reqExecutions(reqId, filter);
	}
	
	
	public void placeOrder(int id, Contract contract, Order order)
	{
		m_clientSocket.placeOrder(id, contract, order);
	}
	
	public void cancelOrder(int orderId)
	{
		m_clientSocket.cancelOrder(orderId);
	}
	
	public void reqGlobalCancel()
	{
		m_clientSocket.reqGlobalCancel();
	}


}
