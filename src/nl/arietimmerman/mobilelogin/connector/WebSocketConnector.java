/**
 * Send and receive messages with WebSockets 
 */

package nl.arietimmerman.mobilelogin.connector;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.arietimmerman.mobilelogin.Message;
import nl.arietimmerman.mobilelogin.Store;
import nl.arietimmerman.mobilelogin.WebSocketMessage;
import nl.arietimmerman.mobilelogin.client.Client;
import nl.arietimmerman.mobilelogin.client.WebSocketClient;
import nl.arietimmerman.mobilelogin.websocket.Decoder;
import nl.arietimmerman.mobilelogin.websocket.Encoder;

@ServerEndpoint(value = "/websocket", encoders = Encoder.class, decoders = Decoder.class)
public class WebSocketConnector extends Connector{
	
	private static final Logger logger = LogManager.getLogger(WebSocketConnector.class);
	
	@OnOpen
	public void onOpen(Session session) {
		
		WebSocketClient client = new WebSocketClient(session);
		Store.addClient(client);
				
		session.getUserProperties().put("client", client.getAddress());
				
		//TODO: reply with the replyToIdentifier and secret
		client.sendObject(client);
	}
	
	/**
	 * Receives a message
	 * 
	 * returns a new message with the identifier of the stored message
	 * 
	 * @param session
	 * @param msg
	 */
	@OnMessage
	public void message(Session session, WebSocketMessage webSocketMessage) {
		
		if("readOutbox".equals(webSocketMessage.action)){
			
			try {
				Message message = super.readOutbox(webSocketMessage.address);
				session.getBasicRemote().sendObject(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//webSocketMessage.address;
			
		}else if("readInbox".equals(webSocketMessage.action)){
			
			try {
				Message message = super.readInbox(webSocketMessage.address, webSocketMessage.secret);
				session.getBasicRemote().sendObject(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if("post".equals(webSocketMessage.action)){
			
			if(webSocketMessage.secret == null){
				super.post(webSocketMessage.address, webSocketMessage.message);
			}else{
				super.post(webSocketMessage.address, webSocketMessage.secret, webSocketMessage.message);
			}
			
		}
		
		
	}
	
	
	@OnClose
	public void onClose(Session session) {
		
	}
}
