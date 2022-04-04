package utils;

import java.util.ArrayList;
import java.util.List;

/*
 *  contains a cumulative list of all trade actions performed thus far, updated after every trade performed;
 *  each time a trade is performed:
 *  	1. the trade is added to this cumulative list
 *  	2. the cumulative list is sent to DataVisulationCreator to create an updated histogram and table 
 *  		- this is done by fetching (with getter method) getCumulativeTrades to use as argument when createCharts is called in MainUI
 */
public class ExecuteTrade {

	List<List<String>> cumulativeTrades = new ArrayList<List<String>>(); // stores list of cumulative trading history for log table
	
	// breaks down each strategies frequencies for histogram
	List<List<String>> strategyFrequencies = new ArrayList<List<String>>();
	
	TradeStrategy trader = new TradeStrategy(); // used to perform trades
	
	// executes one round of trading, iterating through each broker and updating cumulative trades for each buy/sell action performed
	public void performTrade(UserSelection traderList) {
		
		for (int i=0; i < traderList.getNumBrokers(); i++) {
			Broker currBroker = traderList.getBrokerList().get(i);
			cumulativeTrades.add(trader.getExecution(currBroker.getStrategy(), currBroker.getCoinList(), currBroker.getName()));
	// format of getExecution: {name, strategy, action, coin, quantity, price}
		};
	}
	
	public List<List<String>> getCumulativeTrades () {
		return cumulativeTrades;	
	}
}
