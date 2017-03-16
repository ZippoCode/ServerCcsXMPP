package it.tesi.prochilo.notifiche;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Rappresenta un messaggio inviato da Firebase Cloud Messaging. Il messaggio
 * deve essere strutturato nella seguente forma: {"data": { "topic0" :
 * "nameTopic", "topic1" : "nameTopic", ... "topic-iesimo":nameTopic",
 * "message_type":"tipoMessaggio", "token":"identificativo Token" }
 * "timeToLive":"tempo", "from":"Firebase.getIstance().getToken(),
 * "category":"categoria di Firebase", "message_id" : "identificativo univoco" }
 */
public class CcsMessage {

	/**
	 * Il Token di Firebase
	 */
	public final String mFrom;
	/**
	 * Package app
	 */
	public final String mCategory;
	/**
	 * Identificativo univoco messaggio
	 */
	public final String mMessageId;
	/**
	 * Token di chi invia il messaggio
	 */
	public final String mToken;
	/**
	 * Rappresenta la tipologia di operazioni da effettuare come registrazione
	 * dispositivo, invio messaggi
	 */
	public final String mMessageType;
	/**
	 * Il tipo di messaggio, per il momento può essere solo di due tipi,
	 * "subscribe" e "unsubscribe"
	 */
	public final String mMessageOperation;
	/**
	 * Lista di Topic contenuti dentro il messaggio
	 */
	public final List<String> mTopicsList;

	private CcsMessage(final String from, final String category, final String messageId, final String token,
			final String messageType, final String messageOperation, final List<String> topicsList) {
		this.mFrom = from;
		this.mCategory = category;
		this.mMessageId = messageId;
		this.mToken = token;
		this.mMessageType = messageType;
		this.mMessageOperation = messageOperation;
		this.mTopicsList = new LinkedList<String>(topicsList);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Message ID: " + mMessageId).append('\n');
		sb.append("From: " + mFrom).append('\n');
		sb.append("Category: " + mCategory).append('\n');
		sb.append("Token: " + mToken).append('\n');
		sb.append("Message Type: " + mMessageType).append('\n');
		sb.append("Message Operation:" + mMessageOperation).append('\n');
		sb.append("Topic: [");
		Iterator<String> it = mTopicsList.iterator();
		while (it.hasNext()) {
			sb.append(it.next());
			if (it.hasNext())
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	public static class Builder {
		private String from;
		private String category;
		private String messageId;
		private String token;
		private String messageType;
		private String messageOperation;
		private List<String> topicsList;

		private Builder(final String from, final String category, final String messageId) {
			this.from = from;
			this.category = category;
			this.messageId = messageId;
		}

		public static Builder create(final String from, final String category, final String messageId) {
			return new Builder(from, category, messageId);
		}

		public Builder setToken(final String token) {
			this.token = token;
			return this;
		}

		public Builder setMessageType(final String messageType) {
			this.messageType = messageType;
			return this;
		}

		public Builder setMessageOperation(final String messageOperation) {
			this.messageOperation = messageOperation;
			return this;
		}

		public Builder setTopicsList(final List<String> topicsList) {
			this.topicsList = new LinkedList<>(topicsList);
			return this;
		}

		public CcsMessage build() {
			return new CcsMessage(from, category, messageId, token, messageType, messageOperation, topicsList);
		}
	}

}
