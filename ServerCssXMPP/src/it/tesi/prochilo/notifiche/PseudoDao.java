package it.tesi.prochilo.notifiche;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Questa classe gestisce gli account che si interfacciano al SERVER, in
 * particolare salva tutti i dispositivi su una lista che vengono identificati
 * tramite il Token di Firebase ossia l'identificativo fornito dal metodo
 * FirebaseIstanceIdService.getIstance().getToken(). Per info:
 * https://firebase.google.com/docs/reference/android/com/google/firebase/iid/FirebaseInstanceId
 * Inoltre gestisce il token custom, ossia il nome dell'utente che si collega
 * con le stesse credenziali da più devices. In particolare salva una
 * Map<String, List<String> dove la chiave è il token e la lista sono tutti i
 * devices associati alla chiave
 * 
 * @author Prochilo
 * @version 2.0
 */
public class PseudoDao {

	private final static PseudoDao instance = new PseudoDao();
	/**
	 * Utilizzata per generare l'ID dei messaggi
	 */
	private final static Random sRandom = new Random();
	/**
	 * Salva tutti gli ID utilizzato poiché devono essere univoci
	 */
	private final Set<Integer> mMessageIds = new HashSet<Integer>();
	/**
	 * Salva i dispositivi con la stessa chiave in una mappa
	 */
	private final Map<String, List<String>> mUserMap = new HashMap<String, List<String>>();
	/**
	 * Tiene traccia di tutti gli utenti che hanno inviato un messaggio al
	 * Server
	 */
	private final List<String> mRegisteredUsers = new ArrayList<String>();
	private final Map<String, String> mNotificationKeyMap = new HashMap<String, String>();

	private PseudoDao() {
	}

	public static PseudoDao getInstance() {
		return instance;
	}

	public void addRegistration(String regId, String accountName) {
		synchronized (mRegisteredUsers) {
			if (!mRegisteredUsers.contains(regId)) {
				mRegisteredUsers.add(regId);
			}
			if (accountName != null && !accountName.equals("")) {
				// La lista con i devices a cui l'utente si è già connesso
				// precedentemente
				List<String> regIdList = DBServerCssXMPP.getIstance().getDevicesId(accountName);
				if (!regIdList.contains(regId)) {
					// Se il dispositivo non è già stato associato lo aggiungo
					regIdList.add(regId);
					// Lo salvo in modo permanente
					DBServerCssXMPP.getIstance().saveIstanceDeviceId(accountName, regId);
				}
			}
		}
	}

	/**
	 * 
	 * @return Ritorna la lista di tutti i Disposiviti che hanno effettuato
	 *         l'accesso
	 */
	public List<String> getAllRegistrationIds() {
		return Collections.unmodifiableList(mRegisteredUsers);
	}

	/**
	 * 
	 * @param account
	 *            Identifica un tipo di account
	 * @return Ritorna tutti i dispositivi associati all'account
	 */
	public List<String> getAllRegistrationIdsForAccount(String account) {
		List<String> regIds = DBServerCssXMPP.getIstance().getDevicesId(account);
		if (regIds != null) {
			return Collections.unmodifiableList(regIds);
		}
		return null;
	}

	public String getNotificationKeyName(String accountName) {
		return mNotificationKeyMap.get(accountName);
	}

	public void storeNotificationKeyName(String accountName, String notificationKeyName) {
		mNotificationKeyMap.put(accountName, notificationKeyName);
	}

	public Set<String> getAccounts() {
		return Collections.unmodifiableSet(mUserMap.keySet());
	}

	public String getUniqueMessageId() {
		int nextRandom = sRandom.nextInt();
		while (mMessageIds.contains(nextRandom)) {
			nextRandom = sRandom.nextInt();
		}
		return Integer.toString(nextRandom);
	}
}
