package nl.arietimmerman.mobilelogin.client;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.tomcat.util.codec.binary.Base64;

import com.owlike.genson.annotation.JsonIgnore;

import nl.arietimmerman.mobilelogin.Message;

abstract public class Client {
	
	private String secret = Base64.encodeBase64URLSafeString(generateId());
	
	private String address = Base64.encodeBase64URLSafeString(generateId());
	
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

	public void addToInbox(Message message) {
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

}
