package it.tesi.prochilo.notifiche;

/**
 * Handles a user registration.
 */
public class RegisterProcessor implements PayloadProcessor {

	/**
	 * Provvede a registrare il dispositivo identificato da From al Token
	 */
	@Override
	public void handleMessage(CcsMessage msg) {
		PseudoDao.getInstance().addRegistration(msg.mFrom, msg.mToken);
	}

}
