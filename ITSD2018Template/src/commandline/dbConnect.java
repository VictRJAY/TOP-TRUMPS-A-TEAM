package commandline;

import java.io.*;
import java.sql.*;

public class dbConnect 
{
	/*
	 * This class and it's associated functionalities have a basic function of
	 * communicating with a database to get the desired variables to be stored in a
	 * database or to be calculated and displayed as a "game statistics"
	 * functionality.
	 *
	 * The logic is as follows:
	 * 
	 * The db Connect class is using varibables from the gameCalc class which are then imported
	 * into the database using SQL statements 
	 * The code then uses SELECT statements to get the desired variables as per specifications
	 */
	Connection connection = null;
	PreparedStatement statement = null;
	GameCalc c = new GameCalc();
	
	public dbConnect() {
		try {
			connection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	public void connection() throws SQLException 
	{

		String username = "m_18_2413233s";
		String password = "2413233s";
		String connectionAddress = "jdbc:postgresql://yacata.dcs.gla.ac.uk:5432/";
		BufferedReader br = null;
		// connecting to a database
		try 
		{
			Class.forName("org.postgresql.Driver");
		} 
		catch (ClassNotFoundException e) 
		{
			System.out.println("JDBC Driver not found");
			e.printStackTrace();
			return;
		}
		try 
		{
			connection = DriverManager.getConnection(connectionAddress, username, password);
		} catch (SQLException e) 
		{
			System.out.println("Connection to the database was not established");
			e.printStackTrace();
			return;
		}
	}

	//insert statements
	public void dbInsertGameRow(String drawsPerGame, String winner, String roundCounter, int[] plysRndWins)
			throws SQLException 
			{
		Statement statement;
// turning postgreSQL statement into a string of values
		String gameInfoQuery = "INSERT INTO game (draws_per_game, WINNER, ROUND_COUNTER, USER_ROUND_WIN, "
				+ "AI1_ROUND_WIN, AI2_ROUND_WIN, AI3_ROUND_WIN, AI4_ROUND_WIN) "
				+ "VALUES ('" + drawsPerGame + "','" + winner + "','" + roundCounter + "','"+plysRndWins[0]+"','"+plysRndWins[1]+"'"
				+ ",'"+plysRndWins[2]+"','"+plysRndWins[3]+"','"+plysRndWins[4]+"')";
// inserting postgreSQL query to the database
		
		try {
			statement = connection.createStatement();
			int rs = statement.executeUpdate(gameInfoQuery);
			statement.close();
			System.out.println("The game information has been saved!");
		} catch (SQLException e) {
			System.err.println("error executing query " + gameInfoQuery);
			e.printStackTrace();
		}

	}
	
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//retrieve statements
	public int getTotalGames() {
		Statement statement;
		int result = 0;
		try {
			statement = connection.createStatement();
			ResultSet rSet = statement.executeQuery("SELECT * FROM GAME");
			while(rSet.next()) {
				result = rSet.getRow();
			}
			statement.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;

	}
	
	public int getTotalUserVictories() {
		Statement statement;
		int result = 0;
		try {
			statement = connection.createStatement();
			ResultSet rSet = statement.executeQuery("SELECT * FROM GAME WHERE WINNER = 'User'");
			while(rSet.next()) {
				result = rSet.getRow();
			}
		}catch(Exception e) {
			
		}
		
		return result;
	}
	
	public int getTotalAIVictories() {
		Statement statement;
		int result = 0;
		try {
			statement = connection.createStatement();
			ResultSet rSet = statement.executeQuery("SELECT * FROM GAME WHERE WINNER <> 'User'");
			while(rSet.next()) {
				result = rSet.getRow();
			}
		}catch(Exception e) {
			
		}
		
		return result;
	}
	
	public int getHighestRoundNumber() {
		Statement statement;
		int result = 0;
		try {
			statement = connection.createStatement();
			ResultSet rSet = statement.executeQuery("SELECT * FROM GAME");
			while(rSet.next()) {
				if(result<rSet.getInt("ROUND_COUNTER")) {
					result = rSet.getInt("ROUND_COUNTER");
				}
			}
		}catch(Exception e) {
			
		}
		
		return result;
	}
	
	public int getAverageDraws() {
		Statement statement;
		int result = 0;
		int drawSum = 0;
		try {
			statement = connection.createStatement();
			ResultSet rSet = statement.executeQuery("SELECT * FROM GAME");
			while(rSet.next()) {
					drawSum += rSet.getInt("DRAWS_PER_GAME");	
			}
			result = drawSum / getTotalGames();
		}catch(Exception e) {
			
		}
		
		return result;
	}
}
