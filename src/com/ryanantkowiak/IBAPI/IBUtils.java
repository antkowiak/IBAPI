/**
 * IBUtils - Utility functions, mostly for deep copying/cloning of IB data structures
 */
package com.ryanantkowiak.IBAPI;

import java.lang.reflect.Field;
import java.util.Vector;

import com.ib.client.ComboLeg;
import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.Execution;
import com.ib.client.ExecutionFilter;
import com.ib.client.Order;
import com.ib.client.OrderComboLeg;
import com.ib.client.OrderState;
import com.ib.client.ScannerSubscription;
import com.ib.client.TagValue;
import com.ib.client.UnderComp;

/**
 * @author 	Ryan Antkowiak (antkowiak@gmail.com)
 * 
 * @brief	Utility functions, mostly for deep copying/cloning of IB data structures
 */
public class IBUtils
{

	/*
	 * @brief	Deep copy Execution object
	 * @param	src - the source object to clone/copy
	 * @return	the cloned object
	 */
	public static Execution clone(Execution src)
	{
		if (null == src)
		{
			return null;
		}
		
		Execution execution = new Execution();

		Class<?> c = src.getClass();
		
		Field [] fields = c.getDeclaredFields();
		
		for (Field f : fields)
		{
			if (null != f)
			{
				try
				{
					f.setAccessible(true);
					Object value = f.get(src);
					f.set(execution, value);
				}
				catch (Exception e)
				{
				}
			}
		}

		return execution;
	}
	
	
	/*
	 * @brief	Deep copy ExecutionFilter object
	 * @param	src - the source object to clone/copy
	 * @return	the cloned object
	 */
	public static ExecutionFilter clone(ExecutionFilter src)
	{
		if (null == src)
		{
			return null;
		}
		
		ExecutionFilter executionFilter = new ExecutionFilter();

		Class<?> c = src.getClass();
		
		Field [] fields = c.getDeclaredFields();
		
		for (Field f : fields)
		{
			if (null != f)
			{
				try
				{
					f.setAccessible(true);
					Object value = f.get(src);
					f.set(executionFilter, value);
				}
				catch (Exception e)
				{
				}
			}
		}

		return executionFilter;
	}
	
	/*
	 * @brief	Deep copy CommissionReport object
	 * @param	src - the source object to clone/copy
	 * @return	the cloned object
	 */
	public static CommissionReport clone(CommissionReport src)
	{
		if (null == src)
		{
			return null;
		}
		
		CommissionReport commissionReport = new CommissionReport();

		Class<?> c = src.getClass();

		Field[] fields = c.getDeclaredFields();

		for (Field f : fields)
		{
			if (null != f)
			{
				try
				{
					f.setAccessible(true);
					Object value = f.get(src);
					f.set(commissionReport, value);
				}
				catch (Exception e)
				{
				}
			}
		}

		return commissionReport;
	}
	
	/*
	 * @brief	Deep copy ComboLeg object
	 * @param	src - the source object to clone/copy
	 * @return	the cloned object
	 */
	public static ComboLeg clone(ComboLeg src)
	{
		if (null == src)
		{
			return null;
		}

		ComboLeg comboLeg = new ComboLeg();

		Class<?> c = src.getClass();

		Field[] fields = c.getDeclaredFields();

		for (Field f : fields)
		{
			if (null != f)
			{
				try
				{
					f.setAccessible(true);
					Object value = f.get(src);
					f.set(comboLeg, value);
				}
				catch (Exception e)
				{
				}
			}

		}

		return comboLeg;
	}
	
	
	
	/*
	 * @brief	Deep copy Contract object
	 * @param	src - the source object to clone/copy
	 * @return	the cloned object
	 */
	public static Contract clone(Contract src)
	{
		if (null == src)
		{
			return null;
		}

		Contract contract = new Contract();

		Class<?> c = src.getClass();

		Field[] fields = c.getDeclaredFields();

		for (Field f : fields)
		{
			if (null != f)
			{
				try
				{
					f.setAccessible(true);
					Object value = f.get(src);
					f.set(contract, value);
				}
				catch (Exception e)
				{
				}
			}
		}
		
		contract.m_comboLegs = null;
		
		if (src.m_comboLegs != null)
		{
			contract.m_comboLegs = new Vector<ComboLeg>();
			for (ComboLeg cl : src.m_comboLegs)
			{
				contract.m_comboLegs.add(IBUtils.clone(cl));
			}
		}

		return contract;
	}
	
	/*
	 * @brief	Deep copy TagValue object
	 * @param	src - the source object to clone/copy
	 * @return	the cloned object
	 */
	public static TagValue clone(TagValue src)
	{
		if (null == src)
		{
			return null;
		}

		TagValue tagValue = new TagValue();

		Class<?> c = src.getClass();

		Field[] fields = c.getDeclaredFields();

		for (Field f : fields)
		{
			if (null != f)
			{
				try
				{
					f.setAccessible(true);
					Object value = f.get(src);
					f.set(tagValue, value);
				}
				catch (Exception e)
				{
				}
			}

		}

		return tagValue;
	}
	
	
	/*
	 * @brief	Deep copy ContractDetails object
	 * @param	src - the source object to clone/copy
	 * @return	the cloned object
	 */
	public static ContractDetails clone(ContractDetails src)
	{
		if (null == src)
		{
			return null;
		}

		ContractDetails contractDetails = new ContractDetails();

		Class<?> c = src.getClass();

		Field[] fields = c.getDeclaredFields();

		for (Field f : fields)
		{
			if (null != f)
			{
				try
				{
					f.setAccessible(true);
					Object value = f.get(src);
					f.set(contractDetails, value);
				}
				catch (Exception e)
				{
				}
			}

		}
		
		contractDetails.m_secIdList = null;
		if (src.m_secIdList != null)
		{
			contractDetails.m_secIdList = new Vector<TagValue>();
			for (TagValue tv : src.m_secIdList)
			{
				contractDetails.m_secIdList.add(IBUtils.clone(tv));
			}
		}

		return contractDetails;
	}
	
	
	/*
	 * @brief	Deep copy OrderComboLeg object
	 * @param	src - the source object to clone/copy
	 * @return	the cloned object
	 */
	public static OrderComboLeg clone(OrderComboLeg src)
	{
		if (null == src)
		{
			return null;
		}

		OrderComboLeg orderComboLeg = new OrderComboLeg();

		Class<?> c = src.getClass();

		Field[] fields = c.getDeclaredFields();

		for (Field f : fields)
		{
			if (null != f)
			{
				try
				{
					f.setAccessible(true);
					Object value = f.get(src);
					f.set(orderComboLeg, value);
				}
				catch (Exception e)
				{
				}
			}
		}
		
		return orderComboLeg;
	}
	
	/*
	 * @brief	Deep copy Order object
	 * @param	src - the source object to clone/copy
	 * @return	the cloned object
	 */
	public static Order clone(Order src)
	{
		if (null == src)
		{
			return null;
		}

		Order order = new Order();

		Class<?> c = src.getClass();

		Field[] fields = c.getDeclaredFields();

		for (Field f : fields)
		{
			if (null != f)
			{
				try
				{
					f.setAccessible(true);
					Object value = f.get(src);
					f.set(order, value);
				}
				catch (Exception e)
				{
				}
			}
		}
	
		order.m_algoParams = null;
		if (src.m_algoParams != null)
		{
			order.m_algoParams = new Vector<TagValue>();
			for (TagValue tv : src.m_algoParams)
			{
				order.m_algoParams.add(IBUtils.clone(tv));
			}
		}
		
		order.m_orderComboLegs = null;
		if (src.m_orderComboLegs != null)
		{
			order.m_orderComboLegs = new Vector<OrderComboLeg>();
			for (OrderComboLeg ocl : src.m_orderComboLegs)
			{
				order.m_orderComboLegs.add(IBUtils.clone(ocl));
			}
		}
		
		order.m_orderMiscOptions = null;
		if (src.m_orderMiscOptions != null)
		{
			order.m_orderMiscOptions = new Vector<TagValue>();
			for (TagValue tv : src.m_orderMiscOptions)
			{
				order.m_orderMiscOptions.add(IBUtils.clone(tv));
			}
		}
		
		order.m_smartComboRoutingParams = null;
		if (src.m_smartComboRoutingParams != null)
		{
			order.m_smartComboRoutingParams = new Vector<TagValue>();
			for (TagValue tv : src.m_smartComboRoutingParams)
			{
				order.m_smartComboRoutingParams.add(IBUtils.clone(tv));
			}
		}
		
		return order;
	}

	
	/*
	 * @brief	Deep copy OrderState object
	 * @param	src - the source object to clone/copy
	 * @return	the cloned object
	 */
	public static OrderState clone(OrderState src)
	{
		if (null == src)
		{
			return null;
		}

		OrderState orderState = new OrderState();

		Class<?> c = src.getClass();

		Field[] fields = c.getDeclaredFields();

		for (Field f : fields)
		{
			if (null != f)
			{
				try
				{
					f.setAccessible(true);
					Object value = f.get(src);
					f.set(orderState, value);
				}
				catch (Exception e)
				{
				}
			}
		}
		
		return orderState;
	}
	
	/*
	 * @brief	Deep copy ScannerSubscription object
	 * @param	src - the source object to clone/copy
	 * @return	the cloned object
	 */
	public static ScannerSubscription clone(ScannerSubscription src)
	{
		if (null == src)
		{
			return null;
		}

		ScannerSubscription scannerSubscription = new ScannerSubscription();

		Class<?> c = src.getClass();

		Field[] fields = c.getDeclaredFields();

		for (Field f : fields)
		{
			if (null != f)
			{
				try
				{
					f.setAccessible(true);
					Object value = f.get(src);
					f.set(scannerSubscription, value);
				}
				catch (Exception e)
				{
				}
			}
		}
		
		return scannerSubscription;
	}
	
	/*
	 * @brief	Deep copy UnderComp object
	 * @param	src - the source object to clone/copy
	 * @return	the cloned object
	 */
	public static UnderComp clone(UnderComp src)
	{
		if (null == src)
		{
			return null;
		}

		UnderComp underComp = new UnderComp();

		Class<?> c = src.getClass();

		Field[] fields = c.getDeclaredFields();

		for (Field f : fields)
		{
			if (null != f)
			{
				try
				{
					f.setAccessible(true);
					Object value = f.get(src);
					f.set(underComp, value);
				}
				catch (Exception e)
				{
				}
			}
		}
		
		return underComp;
	}
}
