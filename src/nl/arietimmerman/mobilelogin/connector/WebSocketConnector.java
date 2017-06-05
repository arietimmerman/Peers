/**
 * Send and receive messages with WebSockets 
 */

package nl.arietimmerman.mobilelogin.connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.buf.StringUtils;

import com.owlike.genson.GensonBuilder;
import com.owlike.genson.JsonBindingException;

import nl.arietimmerman.mobilelogin.Message;
import nl.arietimmerman.mobilelogin.Store;
import nl.arietimmerman.mobilelogin.WebSocketMessage;
import nl.arietimmerman.mobilelogin.client.Client.ClientException;
import nl.arietimmerman.mobilelogin.client.Client;
import nl.arietimmerman.mobilelogin.client.WebSocketClient;
import nl.arietimmerman.mobilelogin.websocket.Decoder;
import nl.arietimmerman.mobilelogin.websocket.Encoder;

@ServerEndpoint(value = "/websocket", encoders = Encoder.class, decoders = Decoder.class)
public class WebSocketConnector extends Connector {

	private static final Logger logger = LogManager.getLogger(WebSocketConnector.class);

	Map<String, List<String>> subscribedToOutbox = new HashMap<>();

	@OnOpen
	public void onOpen(Session session) {

		logger.trace("Open new session");

		WebSocketClient client = new WebSocketClient(session);
		Store.addClient(client);

		session.getUserProperties().put(Client.ADDRESS, client.getAddress());

		// Send the client its own information
		try {
			client.sendObject(client);
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

		try {
			WebSocketMessage webSocketMessage = new GensonBuilder().create().deserialize(msg, WebSocketMessage.class);
			
			logger.trace(String.format("Received message with action %s", webSocketMessage.action));

			// subscribeToOutbox (address)"

			if ("readOutbox".equals(webSocketMessage.action)) {

				try {
					Message message = super.readOutbox(webSocketMessage.address);
					session.getBasicRemote().sendObject(message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if ("readInbox".equals(webSocketMessage.action)) {

				try {
					Message message = super.readInbox(webSocketMessage.address, webSocketMessage.secret);
					session.getBasicRemote().sendObject(message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if ("postToInbox".equals(webSocketMessage.action)) {

				logger.trace("post message to other");
				
				if(webSocketMessage.tag != null){
					super.postToTag(webSocketMessage.tag, webSocketMessage.message);
				}else{
					super.post(webSocketMessage.address, webSocketMessage.message);
				}

			} else if ("postToOutbox".equals(webSocketMessage.action)) {

				logger.trace("post message to own outbox");
				super.post(webSocketMessage.address, webSocketMessage.secret, webSocketMessage.message);

				// TODO: loop over clients subscribed to webSocketMessage.address
				// super.post(webSocketMessage.address, webSocketMessage.message);
			
			} else if ("tag".equals(webSocketMessage.action)) {

				// logger.trace("post message to own outbox");
				// super.post(webSocketMessage.address, webSocketMessage.secret, webSocketMessage.message);

				Client client = Store.getClient(webSocketMessage.address);

				if (client.getSecret().equals(webSocketMessage.secret)) {
					client.setTag(webSocketMessage.tag);
				}

			} else if ("getPublic".equals(webSocketMessage.action)) {

				// logger.trace("post message to own outbox");
				// super.post(webSocketMessage.address, webSocketMessage.secret, webSocketMessage.message);

				List<Client> publicClients = Store.getPublicClients();

				List<String> addresses = publicClients.stream().map(c -> c.getAddress()).collect(Collectors.toList());

				try {
					Message message = new Message();
					message.setContent("public: " + StringUtils.join(addresses, ','));

					session.getBasicRemote().sendObject(message);

				} catch (IOException | EncodeException e) {

				}

			} else if ("broadcast".equals(webSocketMessage.action)) {

				super.post(webSocketMessage.message);

			} else if ("ping".equals(webSocketMessage.action)) {

				try {
					Message message = new Message();
					message.setContent("pong");

					session.getBasicRemote().sendObject(message);

				} catch (IOException | EncodeException e) {

				}

			} else {

				logger.trace("Ignore message");

			}

		} catch (JsonBindingException e) {

		}

	}

	@OnClose
	public void onClose(Session session) {

	}
}
