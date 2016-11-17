package nl.arietimmerman.mobilelogin.websocket;

import javax.websocket.DecodeException;
import javax.websocket.EndpointConfig;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import nl.arietimmerman.mobilelogin.Message;

public class Decoder implements javax.websocket.Decoder.Text<Message> {

	private final static Genson genson = new GensonBuilder().create();

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public Message decode(String fromSource) throws DecodeException {
		return genson.deserialize(fromSource, Message.class);
	}

	@Override
	public boolean willDecode(String arg0) {
		// TODO Auto-generated method stub
		return true;
	}
	
 
}
