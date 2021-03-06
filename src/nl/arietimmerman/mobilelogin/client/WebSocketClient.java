package nl.arietimmerman.mobilelogin.client;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import nl.arietimmerman.mobilelogin.Message;

public class WebSocketClient extends Client {

	private Session session = null;
	
	public WebSocketClient(Session session) {
		this.session = session;
	}
	
	@Override
	public void addToInbox(Message message) throws ClientException {
		sendObject(message);
	}
	
	public void sendObject(Object object) throws ClientException{
		try {
			session.getBasicRemote().sendObject(object);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e){
			
			throw new Client.ClientException(e);
			
		}
	}
	
	

}
