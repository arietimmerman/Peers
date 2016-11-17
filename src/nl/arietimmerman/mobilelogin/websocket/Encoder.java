package nl.arietimmerman.mobilelogin.websocket;

import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

public class Encoder implements javax.websocket.Encoder.Text<Object> {

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
	public String encode(Object object) throws EncodeException {
		// TODO Auto-generated method stub
		
		
		return genson.serialize(object);
	}
 
}
