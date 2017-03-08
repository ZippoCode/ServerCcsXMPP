package it.tesi.prochilo.notifiche;

/**
 * Handles an echo request.
 */
public class EchoProcessor implements PayloadProcessor {

	@Override
	public void handleMessage(CcsMessage msg) {
		PseudoDao dao = PseudoDao.getInstance();
		CcsClient client = CcsClient.getInstance();
		String msgId = dao.getUniqueMessageId();
		// Da fare: modificare i campi
		String jsonRequest = CcsClient.createJsonMessage(msg.mFrom, msgId, null, null, null, false);
		client.send(jsonRequest);
	}

}
