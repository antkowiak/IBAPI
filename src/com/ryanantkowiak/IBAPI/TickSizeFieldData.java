/**
 * 
 */
package com.ryanantkowiak.IBAPI;

/**
 * @author Ryan Antkowiak 
 *
 */
public class TickSizeFieldData
{
	public static final int TICK_SIZE_FIELD_BID = 0;
	public static final int TICK_SIZE_FIELD_ASK = 3;
	public static final int TICK_SIZE_FIELD_LAST = 5;
	public static final int TICK_SIZE_FIELD_VOLUME = 8;
	
	public int tickerId;
	public int field;
	public int size;
	
	public TickSizeFieldData()
	{
	}
	
	public TickSizeFieldData clone()
	{
		TickSizeFieldData tsfd = new TickSizeFieldData();
		tsfd.tickerId = tickerId;
		tsfd.field = field;
		tsfd.size = size;
		return tsfd;
	}
	
	public void tickSize(int tickerId, int field, int size)
	{
		this.tickerId = tickerId;
		this.field = field;
		this.size = size;
	}
	
}
