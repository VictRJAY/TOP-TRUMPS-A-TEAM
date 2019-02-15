package commandline;

import java.io.*;
import java.sql.*;

public class dbConnect {
	/*
	 * This class and it's associated functionalities have a basic function of
	 * communicating with a database to get the desired variables to be stored in a
	 * database or to be calculated and displayed as a "game statistics"
	 * functionality.
	 *
	 * The logic is as follows:
	 * 
	 * 1)In the GameCalc class, variables that are needed to be stored in a database
	 * are created and put into an ArrayList. 2)Then the code crates a .txt file in
	 * the code directory, the .txt file will be populated and updated after each
	 * game iteration. 3)Subsequently, connection to a database is established.
	 * 4)Further, the previously created .txt file is read and variables that are to
	 * be imported to the database are imported, database is now populated with
	 * data. 5)Lastly, SQL statements are used to get the required information which
	 * is calculated based on the data inserted in previous entries. 6)Using
	 * getOverallStats(); method, data is now ready to be used in other parts of the
	 * program.
	 */
	Connection connection = null;
	PreparedStatement statement = null;
	GameCalc c = new GameCalc();

	public void connection() throws SQLException {
		// --------------------------------------VARIABLE DECLARATION FOR
		// CONNECTION-------------------------------------
//  for uni database
		String username = "m_18_2413233s";
		String password = "2413233s";
		String connectionAddress = "jdbc:postgresql://yacata.dcs.gla.ac.uk:5432/";
		BufferedReader br = null;
		// --------------------------------------CONNECTING TO A
		// DATABASE-------------------------------------
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC Driver not found");
			e.printStackTrace();
			return;
		}
		try {
			connection = DriverManager.getConnection(connectionAddress, username, password);
		} catch (SQLException e) {
			System.out.println("Connection to the database was not established");
			e.printStackTrace();
			return;
		}
		// calling methods for reading a file > getting the overall statistics > getting
		// the statistics after the game
		//dbValuesImport();
	}

	// --------------------------------------READING A FILE TO INSERT IT'S
	// VALUES-------------------------------------
	public void dbValuesImport(String drawsPerGame, String winner, String roundCounter)
			throws SQLException {
		
		String gameCount = "1";
// turning postgreSQL statement into a string of values
		String instertQueryForGameFinished = "INSERT INTO GAME (GAME_COUNTER, DRAWS_PER_GAME, WINNER, ROUND_COUNTER) "
				+ "VALUES ('" + gameCount + "','" + drawsPerGame + "','" + winner + "','" + roundCounter + "')";
// inserting postgreSQL query to the database
		statement = connection.prepareStatement(instertQueryForGameFinished);
		statement.executeUpdate();
		connection.close();
		statement.close();
	}

	// --------------------------------------CALCULATING OVERALL
	// STATISTICS-------------------------------------
	// note: to get the overall ai wins just add up all the individual ai sums
	public void getOverallStats(Connection connection) {
		if (connection != null) {
			try {
				Statement statement = connection.createStatement();
				// add up all the games
				ResultSet gamesTotalSQL = statement.executeQuery("SELECT SUM(GAME_COUNT)" + "FROM GAME");
				// add up all the wins that AI achieved
				// this method requires attention as we do not know the variable for how to get
				// the ais together
				ResultSet a1iWnisOverallSQL = statement
						.executeQuery("SELECT COUNT(WINNER)" + "FROM GAME" + "WHERE WINNER = AI");
				// calculates the sum of wins for the player
				// this method requires attention as we do not know the variable for player
				// togetherVVVVVVVVVVVVVVV
				ResultSet humanWinsOverallSQL = statement
						.executeQuery("SELECT COUNT(WINNER)" + "FROM GAME" + "WHERE WINNER = PLAYER");
				// calculates the average of all of the draws form all games
				ResultSet drawsAvgSQL = statement.executeQuery("SELECT AVG(DRAWS_PER_GAME)" + "FROM GAME");
				// selects the highest number from where the round counts are stored
				ResultSet mostRoundsSQL = statement.executeQuery("SELECT MAX(ROUND_COUNT)" + "FROM GAME");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Connection failed");
		}
	}
}
//--------------------------------------SQL CREATE STATEMENTS-------------------------------------
// CREATE TABLE GAME
// (
// GAME_COUNT INT PRIMARY KEY,
// DRAWS_PER_GAME INT,
// WINNER VARCHAR (64),
// ROUND_COUNTER INT
// );
