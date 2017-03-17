package it.tesi.prochilo.notifiche;

public class DeleterProcessor implements PayloadProcessor {

	/**
	 * Provvede ad eliminare il dispositivo identificato da From al Token	 */
	@Override
	public void handleMessage(CcsMessage msg) {
		PseudoDao.getInstance().deleteRegistration(msg.mFrom, msg.mToken);
	}
}
