/**
 * Send and receive messages with WebSockets 
 */

package nl.arietimmerman.mobilelogin.connector;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.owlike.genson.GensonBuilder;

import nl.arietimmerman.mobilelogin.Message;
import nl.arietimmerman.mobilelogin.Store;
import nl.arietimmerman.mobilelogin.WebSocketMessage;
import nl.arietimmerman.mobilelogin.client.WebSocketClient;
import nl.arietimmerman.mobilelogin.websocket.Decoder;
import nl.arietimmerman.mobilelogin.websocket.Encoder;

@ServerEndpoint(value = "/websocket", encoders = Encoder.class, decoders = Decoder.class)
public class WebSocketConnector extends Connector{
	
	private static final Logger logger = LogManager.getLogger(WebSocketConnector.class);
	
	@OnOpen
	public void onOpen(Session session) {
		
		logger.trace("Open new session");
		
		WebSocketClient client = new WebSocketClient(session);
		Store.addClient(client);
		
		//Send the client its own information
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
	public void message(Session session, String msg) {
		
		WebSocketMessage webSocketMessage = new GensonBuilder().create().deserialize(msg, WebSocketMessage.class);
		
		logger.trace(String.format("Received message with action %s",webSocketMessage.action));
		
		if("readOutbox".equals(webSocketMessage.action)){
			
			try {
				Message message = super.readOutbox(webSocketMessage.address);
				session.getBasicRemote().sendObject(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if("readInbox".equals(webSocketMessage.action)){
			
			try {
				Message message = super.readInbox(webSocketMessage.address, webSocketMessage.secret);
				session.getBasicRemote().sendObject(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if("postToInbox".equals(webSocketMessage.action)){
			
			logger.trace("post message to other");
			super.post(webSocketMessage.address, webSocketMessage.message);
			
		}else if("postToOutbox".equals(webSocketMessage.action)){
			
			logger.trace("post message to own outbox");
			super.post(webSocketMessage.address, webSocketMessage.secret, webSocketMessage.message);
			
		}
		
		
	}
	
	
	@OnClose
	public void onClose(Session session) {
		
	}
}
