package nl.arietimmerman.mobilelogin.connector;

import javax.ws.rs.PathParam;

import nl.arietimmerman.mobilelogin.Message;
import nl.arietimmerman.mobilelogin.Status;
import nl.arietimmerman.mobilelogin.Store;
import nl.arietimmerman.mobilelogin.client.Client;

abstract public class Connector {

	public Status post(String address, Message message){
		
		Client client = Store.getClient(address);
		
		if(client != null){
			client.addToInbox(message);
		}
		
		return new Status();
		
	}
	
	public Message readInbox(String address, String secret) throws Exception{
		
		Client client = Store.getClient(address);
		
		//For reading the inbox, you should know the client's secret
		if(client.getSecret().equals(secret)){
			return client.getFromInbox();	
		}else{
			return null;
		}
		
		
	}

	public Message readOutbox(String address){
		
		Client client = Store.getClient(address);
		
		return client.getFromOutbox();
	}
	
	public Status post(String address, String secret, Message message) {
		
		Client client = Store.getClient(address);
		
		if(client != null && secret.equals(client.getSecret())){
			client.addToOutbox(message);
		}
		
		return new Status();
	}
	
}
