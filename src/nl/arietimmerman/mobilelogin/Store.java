package nl.arietimmerman.mobilelogin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.arietimmerman.mobilelogin.client.Client;

public class Store {

	private static final Logger logger = LogManager.getLogger(Store.class);
	
	private static Map<String, Client> store = new HashMap<>();
	
	public static void addClient(Client client) {
		
		logger.trace(String.format("Store client with address %s", client.getAddress()));
		
		store.put(client.getAddress(), client);
	}
	
	public static Client removeClient(String address){
		return store.remove(address);
	}
	
	public static Client getClient(String address){
		return store.get(address);
	}
	
	public static List<Client> getClients(){
		return new ArrayList<>(store.values());
	}
	
}
