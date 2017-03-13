package it.tesi.prochilo.notifiche;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Questa classe dialoga con il DataBase salvando e restituendo le varie
 * istanze.
 * 
 * @author Prochilo
 * @version 1.0.0
 */
public class DBServerCssXMPP {

	private static final DBServerCssXMPP istance = new DBServerCssXMPP();
	private final String url = "jdbc:mysql://localhost:3306/servertest?autoReconnect=true";
	private Connection mConnection;

	private DBServerCssXMPP() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			mConnection = DriverManager.getConnection(url, "root", "1234");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
		}
	}

	public static DBServerCssXMPP getIstance() {
		return istance;
	}

	/**
	 * Ritorna la lista dei Devices associati al token
	 * 
	 * @param token
	 * @return La lista dei dispositivi
	 */
	public List<String> getDevicesId(String token) {
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

	/**
	 * Salva l'identificativo del dispositivo associato al token
	 * 
	 * @param token
	 * @param devideId
	 */
	public void saveIstanceDeviceId(String token, String devideId) {
		try {
			String query = " INSERT INTO userxmpp (token, devicesId) VALUES (?, ?)";
			PreparedStatement preparedStatement = mConnection.prepareStatement(query);
			preparedStatement.setString(1, token);
			preparedStatement.setString(2, devideId);
			preparedStatement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
