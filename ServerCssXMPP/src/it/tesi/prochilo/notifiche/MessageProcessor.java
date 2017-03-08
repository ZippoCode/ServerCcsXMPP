package it.tesi.prochilo.notifiche;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles an echo request.
 */
public class MessageProcessor implements PayloadProcessor {

	@Override
	public void handleMessage(CcsMessage msg) {
		PseudoDao dao = PseudoDao.getInstance();
		CcsClient client = CcsClient.getInstance();
		Map<String, String> payload = new HashMap<>();
		payload.put("message_operation", msg.mMessageOperation);
		List<String> topicsList = msg.mTopicsList;
		int i = 0;
		for (String topic : topicsList) {
			payload.put("topic" + i, topic);
			i++;
		}
		client.sendBroadcast(payload, null, null, false, dao.getAllRegistrationIdsForAccount(msg.mToken));
	}

}
