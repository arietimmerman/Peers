package nl.arietimmerman.mobilelogin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.arietimmerman.mobilelogin.client.Client;
import nl.arietimmerman.mobilelogin.connector.WebSocketConnector;

public class Store {

	private static final Logger logger = LogManager.getLogger(Store.class);
	
	private static Map<String, Client> store = new HashMap<>();
	
	public static void addClient(Client client) {
		
		logger.trace(String.format("Store client with address %s", client.getAddress()));
		
		store.put(client.getAddress(), client);
	}
	
	public static Client getClient(String address){
		return store.get(address);
	}
	
	
	
}
