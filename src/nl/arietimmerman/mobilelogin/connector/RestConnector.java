/**
 * Send and receive messages with REST
 */

package nl.arietimmerman.mobilelogin.connector;

import javax.ws.rs.FormParam;
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
public class RestConnector extends Connector {
	
	/**
	 * Creates a new client with inbox
	 * @return A Client object with the client's (public) address and (private) secret
	 */
	@GET
	@Path("start")
	@Produces(MediaType.APPLICATION_JSON)
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
	@Produces(MediaType.APPLICATION_JSON)
	public Message readOutbox(@PathParam("address") String address){
		
		return super.readOutbox(address);
		
	}
	
	@GET
	@Path("readInbox/{address}/{secret}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message readInbox(@PathParam("address") String address, @PathParam("secret") String secret) throws Exception{
		
		return super.readInbox(address, secret);
		
		
	}
	
	/**
	 * Adds a message to someone's inbox
	 * @param message
	 * @return
	 */
	@POST
	@Path("post/{address}")
	@Produces(MediaType.APPLICATION_JSON)
	public Status post(@PathParam("address") String address, @FormParam("message") Message message){
		
		return super.post(address, message);
		
	}
	
	/**
	 * Adds a message to someone's outbox
	 * @param message
	 * @return
	 */
	@POST
	@Path("post/{address}/{secret}")
	@Produces(MediaType.APPLICATION_JSON)
	public Status post(@PathParam("address") String address, @PathParam("secret") String secret, @FormParam("message") Message message){
		
		return super.post(address, secret, message);
		
	}
	
	
}
