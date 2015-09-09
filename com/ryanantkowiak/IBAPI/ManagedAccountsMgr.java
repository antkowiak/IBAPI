package com.ryanantkowiak.IBAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManagedAccountsMgr
{
	public static List<String> m_accounts = new ArrayList<String>();
	
	public static synchronized void managedAccounts(String accountsList)
	{
		List<String> newList = Arrays.asList(accountsList.split("\\s*,\\s*"));
		
		m_accounts = newList;
	}
	
	public static synchronized List<String> getAccountList()
	{
		List <String> list = new ArrayList<String>();

		for (String s : m_accounts)
		{
			list.add(s);
		}
		
		return list;
	}
	
	public static synchronized boolean isEmpty()
	{
		return (m_accounts.isEmpty());
	}
	
	public static synchronized String getString()
	{
		StringBuilder sb = new StringBuilder();
		
		if (!isEmpty())
		{
			sb.append(m_accounts.get(0));
		}
		
		for (int i = 1 ; i < m_accounts.size() ; ++i)
		{
			sb.append(",");
			sb.append(m_accounts.get(i));
		}
		
		return sb.toString();
	}
}
