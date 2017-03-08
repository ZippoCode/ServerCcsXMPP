package it.tesi.prochilo.notifiche;

public class ProcessorFactory {

	/**
	 * Rappresenta il package dei messaggi, dunque quello di Firebase
	 */
	private static final String PACKAGE = "it.tesi.prochilo.notifiche";
	/**
	 * Questa opzione registra il dispositivo sul Server FCM
	 */
	private static final String ACTION_REGISTER = PACKAGE + ".registraId";
	/**
	 * Risponde con un messaggio di ECHO
	 */
	private static final String ACTION_ECHO = PACKAGE + ".echo";
	/**
	 * Un semplice messaggio, dunque contiene i topic su cui l'utente ha
	 * effettuato una nuova iscrizione o disiscrizione
	 */
	private static final String ACTION_MESSAGE = PACKAGE + ".messaggio";

	/**
	 * Effettua uno switch ritornando l'implementazione corretta
	 * 
	 * @param action
	 * @return
	 */
	public static PayloadProcessor getProcessor(String action) {
		if (action == null) {
			throw new IllegalStateException("action must not be null");
		}
		if (action.equals(ACTION_REGISTER)) {
			return new RegisterProcessor();
		} else if (action.equals(ACTION_ECHO)) {
			return new EchoProcessor();
		} else if (action.equals(ACTION_MESSAGE)) {
			return new MessageProcessor();
		}
		throw new IllegalStateException("Action " + action + " is unknown");
	}
}
