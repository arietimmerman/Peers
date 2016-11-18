/**
 * Send and receive messages with REST
 * 
 * Everyone can read messages from everyone's outbox
 * Everyone can post messages to everyone's inbox
 * 
 * Only if you know the "secret" you can read messages from someone's inbox
 * Only if you know the "secret" you can post messages to your outbox
 * 
 */

package nl.arietimmerman.mobilelogin.connector;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nl.arietimmerman.mobilelogin.Message;
import nl.arietimmerman.mobilelogin.Status;
import nl.arietimmerman.mobilelogin.Store;
import nl.arietimmerman.mobilelogin.client.Client;
import nl.arietimmerman.mobilelogin.client.RestClient;


@Path("/message")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RestConnector extends Connector {
	
	/**
	 * Creates a new client with inbox
	 * @return A Client object with the client's (public) address and (private) secret
	 */
	@GET
	@Path("start")
	public Client start(){
		
		Client client = new RestClient();
		Store.addClient(client);
		
		return client;
	}
	
	
	/**
	 * Read the oldest unread message from the outbox
	 * @param address
	 * @return
	 */
	@GET
	@Path("readOutbox/{address}")
	public Message readOutbox(@PathParam("address") String address){
		
		return super.readOutbox(address);
		
	}
	
	@GET
	@Path("readInbox/{address}/{secret}")
	public Message readInbox(@PathParam("address") String address, @PathParam("secret") String secret) throws Exception{
		return super.readInbox(address, secret);
	}
	
	/**
	 * Adds a message to someone's inbox
	 * @param message
	 * @return
	 */
	@POST
	@Path("postToInbox/{address}")
	@Produces(MediaType.APPLICATION_JSON)
	public Status postToInbox(@PathParam("address") String address, Message message){
		
		return super.post(address, message);
		
	}
	
	/**
	 * Adds a message to someone's outbox
	 * @param message
	 * @return
	 */
	@POST
	@Path("postToOutbox/{address}/{secret}")
	@Produces(MediaType.APPLICATION_JSON)
	public Status postToOutbox(@PathParam("address") String address, @PathParam("secret") String secret, Message message){
		
		return super.post(address, secret, message);
		
	}
	
	
}
