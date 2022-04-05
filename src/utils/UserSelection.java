package utils;

import java.util.ArrayList;
import java.util.List;

import gui.MainUI;

public class UserSelection {
	private static UserSelection instance;
	
	private ArrayList<Broker> brokerList = new ArrayList<Broker>(); 
	private ArrayList<String> strategyList = new ArrayList<String>();
	private ArrayList<String[]> coinsList = new ArrayList<String[]>();
	private int numBrokers;
	
	private static List<List<String>> frequency = new ArrayList<List<String>>();
		
	//implementing a singleton design pattern 
	private UserSelection getInstance() {
		if (instance == null) {
			instance = new UserSelection();
		}
		
		return instance;
	}
	
	private static boolean containsBroker (ArrayList<Broker> brokerList, String name) {
    	for (int i = 0; i < brokerList.size(); i++) {
    		if (brokerList.get(i).getName().equals(name)) {
    			return true;
    		}
    	}
    	return false;
    }
	
	public boolean addBroker(String name, String strategy, String[] coinList) {
		List<String> current = null;
		int update;
		boolean found = false;
		
		//either you find it or you don't 
		//check if it's there
		for (int i = 0; i < frequency.size(); i++) {
			current = frequency.get(i);
			
			if (current.get(1).equals(name) && current.get(2).equals(strategy)) {
				found = true;
				
				update = Integer.parseInt(current.get(0));
				update++;
				current.set(0, Integer.toString(update));
				
				break;
			}
		}
	
		//if there is no pre-existing array and strategy is not None
		if (!found && !strategy.equals("None")) {
			current = new ArrayList<String>();
			current.add(Integer.toString(1));
			current.add(name);
			current.add(strategy);
			
			frequency.add(current);
		}
		
		if (!containsBroker(brokerList, name)) { //if broker is not in list yet
			Broker newBroker = new Broker(name, strategy, coinList);
			
			brokerList.add(newBroker);
			strategyList.add(strategy);
			numBrokers++;
	        coinsList.add(coinList);
	        
	        return true;
	        
		} else {
			System.out.println("Broker already in list");
			return false;
		}
	}
	
	public void removeBroker(String name) {
		if (brokerList.contains(name)) {//check if broker is in list
			int index = brokerList.indexOf(name);
			strategyList.remove(index);
			coinsList.remove(index);
			brokerList.remove(index);
			numBrokers--;
		} else {
			System.out.println("Broker not in list");
		}
	}
	
    public ArrayList<Broker> getBrokerList() {
        return brokerList;
    }
    public ArrayList<String> getStrategyList() {
        return strategyList;
    }
    
    public ArrayList<String[]> getCoinLists() {
        return coinsList;
    }
	
    public int getNumBrokers() {
	    return numBrokers;
    }   
    
    public static List<List<String>> getFrequencies() {
    	return frequency;
    }
}
