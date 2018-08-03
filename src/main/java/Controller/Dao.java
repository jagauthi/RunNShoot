package Controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/* 	
 	To view database in a browser:
 		Navigate to phpmyadmin.co
 		Use below login credentials to log in
 	
	Database Connection Details:
		Server: sql3.freesqldatabase.com
		Name: sql3205145
		Username: sql3205145
		Password: rDlDd1QCsz
		Port number: 3306
 */

public class Dao {
	Connection conn;

	public Dao() {
		conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Properties properties = new Properties();
			properties.setProperty("user", "sql3205145");
			properties.setProperty("password", "rDlDd1QCsz");
			properties.setProperty("useSSL", "false");
			conn = DriverManager.getConnection("jdbc:mysql://sql3.freesqldatabase.com:3306/sql3205145", properties);
			/*
			 * properties.setProperty("user", "josephdb");
			 * properties.setProperty("password", "Ju2qdwE!?5eW");
			 * properties.setProperty("useSSL", "false"); conn =
			 * DriverManager.getConnection(
			 * "jdbc:mysql://mssql5.gear.host:3306/josephdb", properties);
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int addAccount(String username, String password) {
		try {
			Statement stmt = conn.createStatement();
			String query = "insert into UserAccounts values('" + username + "', '" + password + "');";
			int numRowsAffected = stmt.executeUpdate(query);
			return numRowsAffected;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public String[] getAccount(String username) {
		try {
			Statement stmt = conn.createStatement();
			String query = "select * from UserAccounts where user = '" + username + "';";
			ResultSet rs = stmt.executeQuery(query);

			int resultSetSize = 0;
			try {
				rs.last();
				resultSetSize = rs.getRow();
				rs.beforeFirst();
			} catch (Exception e) {
				e.printStackTrace();
			}

			String[][] response = new String[resultSetSize][];
			int counter = 0;
			while (rs.next()) {
				String[] newRecord = { rs.getString("User"), rs.getString("Pass") };
				response[counter] = newRecord;
				counter++;
			}
			stmt.close();
			return response[0];
		} catch (Exception e) {
			e.printStackTrace();
			String[] errorPacket = { "Error" };
			return errorPacket;
		}
	}

	public String[][] getAccounts() {
		try {
			Statement stmt = conn.createStatement();
			String query = "select * from UserAccounts;";
			ResultSet rs = stmt.executeQuery(query);

			int resultSetSize = 0;
			try {
				rs.last();
				resultSetSize = rs.getRow();
				rs.beforeFirst();
			} catch (Exception e) {
				e.printStackTrace();
			}

			String[][] response = new String[resultSetSize][];
			int counter = 0;
			while (rs.next()) {
				String[] newRecord = { rs.getString("User"), rs.getString("Pass") };
				response[counter] = newRecord;
				counter++;
			}
			stmt.close();
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			String errorPackets[][] = { { "Error" } };
			return errorPackets;
		}
	}

	public ArrayList<String> getCharactersForUser(String user) {
		try {
			ArrayList<String> charList = new ArrayList<String>();
			Statement stmt;
			stmt = conn.createStatement();
			String query = "select a.characterName, a.class, a.level " + "from CharacterDetails a, UserCharacters b "
					+ "where b.user = '" + user + "' " + "and a.characterName = b.characterName;";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				charList.add(rs.getString("characterName") + " " + rs.getString("class") + " " + rs.getString("level"));
			}
			stmt.close();
			return charList;
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	public String getCharacterInfo(String characterName) {
		try {
			String infoList = "";
			Statement stmt;
			stmt = conn.createStatement();
			String query = "select *" + " from CharacterDetails" + " where characterName = '" + characterName + "';";
			ResultSet rs = stmt.executeQuery(query);
			String d = "#";
			if (rs.next()) {
				infoList = rs.getString("characterName") + d + rs.getString("class") + d + rs.getString("level") + d
						+ rs.getString("currentHealth") + d + rs.getString("mana") + d + rs.getString("exp") + d
						+ rs.getString("pointsToSpend") + d + rs.getString("x") + d + rs.getString("y") + d
						+ rs.getString("location") + d + rs.getString("gold") + d + rs.getString("strength") + d
						+ rs.getString("dexterity") + d + rs.getString("stamina") + d + rs.getString("intelligence");
			}
			stmt.close();
			return infoList;
		} catch (SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public void createCharacter(String user, String charName, String chosenClass, String pointsToSpend, String str,
			String dex, String con, String intel) {
		try {
			Statement stmt;
			stmt = conn.createStatement();
			// Create record in UserCharacters tables
			String query = "INSERT INTO UserCharacters" + " VALUES ('" + user + "', '" + charName + "');";
			stmt.executeUpdate(query);

			// Create record in CharacterDetails table
			query = "INSERT INTO CharacterDetails" + " VALUES ('" + charName + "', '" + chosenClass + "',"
					+ " '1', '100', '100', '0', '" + pointsToSpend + "', '100', '100', 'NotSure', '100'," + " " + str
					+ ", " + dex + ", " + con + ", " + intel + ");";
			stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteCharacter(String user, String charName) {
		try {
			Statement stmt;
			stmt = conn.createStatement();
			// delete record in UserCharacters tables
			String query = "DELETE FROM UserCharacters" + " where user = '" + user + "' AND characterName = '"
					+ charName + "';";
			stmt.executeUpdate(query);

			// delete record in CharacterDetails table
			query = "DELETE FROM CharacterDetails" + " where characterName = '" + charName + "';";
			stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
