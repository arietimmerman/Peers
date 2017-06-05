package nl.arietimmerman.mobilelogin.client;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Queue;


import com.owlike.genson.annotation.JsonIgnore;

import nl.arietimmerman.mobilelogin.Message;

abstract public class Client {
	
	public final static String ADDRESS = "address";
	
	private final String secret = Base64.getUrlEncoder().encodeToString(generateId());
	
	private final String address = Base64.getUrlEncoder().encodeToString(generateId());
	
	private Boolean isPublic = false;
	private String tag = null;
	
	private Queue<Message> inbox = new LinkedList<>();
	
	/**
	 * The outbox is used for storing messages that are to be retrieved
	 */
	private Queue<Message> outbox = new LinkedList<>();
	
	
	public String getSecret() {
		return secret;
	}
	
	public String getAddress() {
		return address;
	}

	public void addToInbox(Message message) throws ClientException {
		this.inbox.add(message);
	}

	@JsonIgnore
	public Message getFromInbox() {
		return this.inbox.poll();
	}
	
	public void addToOutbox(Message message) {
		this.outbox.add(message);
	}

	@JsonIgnore
	public Message getFromOutbox() {
		return this.outbox.poll();
	}
	
	private static byte[] generateId() {
		byte[] s = new byte[32];
		new SecureRandom().nextBytes(s);
		return s;
	}

	public class ClientException extends Exception{

		private static final long serialVersionUID = -2934526822613038366L;
		
		public ClientException(String message) {
			super(message);
		}
		
		public ClientException(Exception e) {
			super(e);
		}
		
	}
	
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
	
	public Boolean getIsPublic() {
		return isPublic;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public String getTag() {
		return tag;
	}
}
