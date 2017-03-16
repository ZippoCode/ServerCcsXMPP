package it.tesi.prochilo.notifiche;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;

/**
 * Questa classe dialoga con il DataBase salvando e restituendo le varie
 * istanze.
 * 
 * @author Prochilo
 * @version 1.0.0
 */
public class DBServerCcsXMPP {

	private static final DBServerCcsXMPP istance = new DBServerCcsXMPP();
	private static String url;
	private static String user;
	private static String password;
	private static Connection mConnection;

	private DBServerCcsXMPP() {
	}

	public static void create(JSONObject credenziali) {
		try {
			url = (String) credenziali.get("db_url");
			user = (String) credenziali.get("db_user");
			password = (String) credenziali.get("db_pass");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			mConnection = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = mConnection.prepareStatement("SHOW TABLES LIKE 'userccs'");
			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				preparedStatement = mConnection.prepareStatement("CREATE TABLE `servertest`."
						+ "`userccs` ( `id` INT NOT NULL AUTO_INCREMENT," + "`token` VARCHAR(255) NULL, "
						+ "`devicesId` VARCHAR(255) NULL," + "PRIMARY KEY (`id`));");
				preparedStatement.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DBServerCcsXMPP getIstance() {
		return istance;
	}

	/**
	 * Ritorna la lista dei Devices associati al token
	 * 
	 * @param token
	 * @return La lista dei dispositivi
	 */
	public List<String> getDevicesId(String token) {
		String sql = "SELECT devicesId FROM userccs WHERE token = '" + token + "'";
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

	/**
	 * Salva l'identificativo del dispositivo associato al token
	 * 
	 * @param token
	 * @param devideId
	 */
	public void saveIstanceDeviceId(String token, String devideId) {
		try {
			String query = " INSERT INTO userccs (token, devicesId) VALUES (?, ?)";
			PreparedStatement preparedStatement = mConnection.prepareStatement(query);
			preparedStatement.setString(1, token);
			preparedStatement.setString(2, devideId);
			preparedStatement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
