/**
 * IBTickListener - Interfaec class that listens for tick information (such as prices, sizes, etc)
 * from the IB TWS.
 */
package com.ryanantkowiak.IBAPI;

/**
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 *
 */
public interface IBTickListener
{

	public abstract void tickPrice(int tickerId, int field, double price, int canAutoExecute);

	public abstract void tickSize(int tickerId, int field, int size);

	public abstract void tickOptionComputation(	int tickerId,
												int field,
												double impliedVol,
												double delta,
												double optPrice,
												double pvDividend,
												double gamma,
												double vega,
												double theta,
												double undPrice);

	public abstract void tickGeneric(int tickerId, int tickType, double value);

	void tickString(int tickerId, int tickType, String value);

	public abstract void tickEFP(	int tickerId,
									int tickType,
									double basisPoints,
									String formattedBasisPoints,
									double impliedFuture,
									int holdDays,
									String futureExpiry,
									double dividendImpact,
									double dividendsToExpiry);

}
