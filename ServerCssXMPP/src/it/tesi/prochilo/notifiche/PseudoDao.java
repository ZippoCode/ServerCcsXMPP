package it.tesi.prochilo.notifiche;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
 * con le stesse credenziali da pi� devices. In particolare salva una
 * Map<String, List<String> dove la chiave � il token e la lista sono tutti i
 * devices associati alla chiave
 */
public class PseudoDao {

	private final static PseudoDao instance = new PseudoDao();
	/**
	 * Utilizzata per generare l'ID dei messaggi
	 */
	private final static Random sRandom = new Random();
	/**
	 * Salva tutti gli ID utilizzato poich� devono essere univoci
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
	/**
	 * 
	 */
	private final String url = "jdbc:mysql://localhost:3306/servertest?autoReconnect=true";
	private Connection mConnection;

	private PseudoDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			mConnection = DriverManager.getConnection(url, "root", "1234");
		} catch (Exception e) {
			e.printStackTrace();
		}
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
				// La lista con i devices a cui l'utente si � gi� connesso
				// precedentemente
				List<String> regIdList = getDevicesId(accountName);
				/**
				 * List<String> regIdList = mUserMap.get(accountName); if
				 * (regIdList == null) { // Viene chiamata la prima volta che
				 * questo utente accede regIdList = new ArrayList<String>();
				 * mUserMap.put(accountName, regIdList); }
				 **/
				if (!regIdList.contains(regId)) {
					// Se il dispositivo non � gi� stato associato lo aggiungo
					regIdList.add(regId);
					// Lo salvo in modo permanente
					try {
						String query = " insert into userxmpp (token, devicesId) values (?, ?)";
						PreparedStatement preparedStatement = mConnection.prepareStatement(query);
						preparedStatement.setString(1, accountName);
						preparedStatement.setString(2, regId);
						preparedStatement.execute();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					System.out.println(accountName + " ha il  dispositivo: " + regId + " gi� registrato");
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
		// List<String> regIds = mUserMap.get(account);
		List<String> regIds = getDevicesId(account);
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

	/**
	 * Ritorno la lista dei dispositivi associati all'utente speficiato nel
	 * token
	 */
	private List<String> getDevicesId(String token) {
		String sql = "SELECT devicesId FROM userxmpp WHERE token = '" + token + "'";
		List<String> result = new LinkedList<String>();
		try {
			PreparedStatement preparedStatement = mConnection.prepareStatement(sql);
			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				result.add(results.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
