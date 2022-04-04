package utils;

//import cryptoTrader.utils.AvailableCryptoList;
import java.io.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

public class TradeStrategy {
	
//	private static TradeStrategy instance = null;
//	
//	/**
//	 * Creates a global point of access to AvailableCryptoList using singleton design pattern  
//	 */
//	public static TradeStrategy getInstance() {
//		if (instance == null)
//			instance = new TradeStrategy();
//		
//		return instance;
//	}

	private List<String> TradeExecution = new ArrayList<>();
	private List<Double> prices = new ArrayList<>();
	
	
	/**
	 * Gets coin prices for the broker from API using DataFetcher class
	 * @param traderCoins: array of coins of interest for broker
	 */
	private void coinPrices(String traderCoins[]) {
		DataFetcher coinGecko = new DataFetcher();
		for(int i = 0; i < traderCoins.length; i++) {
			//example parameters: ("bitcoin", "04-02-2022")
			String fullCoinName = AvailableCryptoList.getInstance().getCryptoIDfromTicker(traderCoins[i]);
			prices.add(coinGecko.getPriceForCoin(fullCoinName, date())); 
		}
	}
	
	
	private Double coinPrices(String coin) {
		DataFetcher coinGecko = new DataFetcher();
		return coinGecko.getPriceForCoin(coin, date());
	}
	
	/**
	 * @param strategy
	 * @param coins
	 * @param name
	 * @return
	 */
	public List<String> getExecution(String strategy, String[] coins, String name) { //selection object
		TradeExecution.add(name);
		TradeExecution.add(strategy);
		
		coinPrices(coins);
		
		if(strategy.equals("Strategy-A")) {
			StrategyA(coins, prices);
		} else if(strategy.equals("Strategy-B")) {
			StrategyB(coins, prices);
		} else if(strategy.equals("Strategy-C")) {
			StrategyC(coins, prices);
		} else if(strategy.equals("Strategy-D")) {
			StrategyD(coins, prices);
		}
		
		TradeExecution.add(date());
	
		// final format of TradeExecution: {name, strategy, action, coin, quantity, price}	
		return TradeExecution;
	}
	
	//class returns today's date such that when prices are added to String array coin, 
	//coin prices are up to date and accurate
	private static String date() {
		Format f = new SimpleDateFormat("MM-dd-20yy");
		String strDate = f.format(new Date());
		return (strDate);
	}
	
	//A: if Bitcoin (BTC) > 46k and Ethereum (ETH) < 3800 then buy 800 units Cardano (ADA), else sell 2 Bitcoin (BTC)
	private void StrategyA(String[] coins, List<Double> prices) {
		
		Double Bitcoin = null;
		Double Ethereum = null;	
		
		for (int i = 0; i < coins.length; i++) {
			if("BTC".equals(coins[i])) {
				Bitcoin = coinPrices("bitcoin");
			} else if("ETH".equals(coins[i])) {
				Ethereum = coinPrices("ethereum");
			}
		}
		
		if (Bitcoin != null && Ethereum != null) { // if both coins are in the list of requested coins
			Double Cardano = coinPrices("cardano");
			
			if(Bitcoin > 58000 && Ethereum < 4000) {
				TradeExecution.add("buy");
				TradeExecution.add("ADA");
				TradeExecution.add("800");
				TradeExecution.add(Double.toString(Cardano)); 
			} else {
				TradeExecution.add("sell");
				TradeExecution.add("BTC");
				TradeExecution.add("2");
				TradeExecution.add(Double.toString(Bitcoin));
			}
		} else {
			FailTrade();
		}
	}

	//B: if Cardano (ADA) > $1 then buy 10 LUNA, else sell 3 Bitcoin
	private void StrategyB(String[] coins, List<Double> prices) {
		
		Double Cardano = null;
		Double Luna = null;
		Double Bitcoin = null;		
		
		for (int i = 0; i < coins.length; i++) {
			//If Cardano can be found in request 
			if("ADA".equals(coins[i])) {
				Cardano = coinPrices("cardano");
			} 
		}
		
		if (Cardano != null) { // if both coins are in the list of requested coins
			Bitcoin = coinPrices("bitcoin");
			Luna = coinPrices("terra-luna");
			
			if(Cardano > 1) {
				TradeExecution.add("buy");
				TradeExecution.add("LUNA"); //what is the shorthand 
				TradeExecution.add("10");
				TradeExecution.add(Double.toString(Luna)); 
			} else {
				TradeExecution.add("sell");
				TradeExecution.add("BTC");
				TradeExecution.add("3");
				TradeExecution.add(Double.toString(Bitcoin));
			}
		} else {
			FailTrade();
		}
	}
	//C: if Ethereum (ETH) > $3800 and Cardano (ADA) < $1 buy 200 Fantom (FTM), else sell 500 Cardano (ADA)
	private void StrategyC(String[] coins, List<Double> prices) {
		
		Double Ethereum = null;
		Double Cardano = null;	
		
		for (int i = 0; i < coins.length; i++) {
			if("ETH".equals(coins[i])) {
				Ethereum = coinPrices("ethereum");
			} else if("ADA".equals(coins[i])) {
				Cardano = coinPrices("cardano");
			}
		}
		
		if (Ethereum != null && Cardano != null) { // if both coins are in the list of requested coins
			Double Fantom = coinPrices("fantom");	
			
			if(Ethereum > 38000 && Cardano < 1) {
				TradeExecution.add("buy");
				TradeExecution.add("FTM");
				TradeExecution.add("200");
				TradeExecution.add(Double.toString(Fantom)); 
			} else {
				TradeExecution.add("sell");
				TradeExecution.add("ADA");
				TradeExecution.add("500");
				TradeExecution.add(Double.toString(Cardano));
			}
		} else {
			FailTrade();
		}
	}
	
	//D: if price of Cardano (ADA) > price Fantom (FTM), then buy 100 FTM, else buy 100 Cardano
	private void StrategyD(String[] coins, List<Double> prices) {
		
		Double Cardano = null;
		Double Fantom = null;	
		
		for (int i = 0; i < coins.length; i++) {
			if("ADA".equals(coins[i])) {
				Cardano = coinPrices("cardano");
			} else if("FTM".equals(coins[i])) {
				Fantom = coinPrices("fantom");
			}
		}
		
		if (Cardano != null && Fantom != null) { // if both coins are in the list of requested coins			
			if(Cardano > Fantom) {
				TradeExecution.add("buy");
				TradeExecution.add("FTM");
				TradeExecution.add("100");
				TradeExecution.add(Double.toString(Cardano)); 
			} else {
				TradeExecution.add("buy");
				TradeExecution.add("ADA");
				TradeExecution.add("100");
				TradeExecution.add(Double.toString(Fantom));
			}
		} else {
			FailTrade();
		}
	}
	
	private void FailTrade() {
		TradeExecution.add("Null");
		TradeExecution.add("Fail");
		TradeExecution.add("Null");
		TradeExecution.add("Null");
	}
}

