package dbConnect_New;
import java.io.*;
import java.sql.*;

public class dbConnect {
/*
 * This class and it's associated functionalities have a basic function of communicating with a database
 * to get the desired variables to be stored in a database or to be calculated and displayed as a "game
 * statistics" functionality.
 *
 * The logic is as follows:
 * 
 * 1)In the GameCalc class, variables that are needed to be stored in a database are created and put into an
 * ArrayList.
 * 2)Then the code crates a .txt file in the code directory, the .txt file will be populated and 
 * updated after each game iteration.
 * 3)Subsequently, connection to a database is established.
 * 4)Further, the previously created .txt file is read and variables that are to be imported to the database
 * are imported, database is now populated with data. 
 * 5)Lastly, SQL statements are used to get the required information which is calculated based on the data 
 * inserted in previous entries. 
 * 6)Using getOverallStats(); method, data is now ready to be used in other parts of the program.
 */
 static Connection connection = null;
 static PreparedStatement statement = null;
 public static void connection() throws SQLException
 {
 // --------------------------------------VARIABLE DECLARATION FOR CONNECTION-------------------------------------
 // for uni database
//	 String username = "m_18_2413233s";
//	  String password = "2413233s";
//	  String connectionAddress = "jdbc:postgresql://yacata.dcs.gla.ac.uk:5432/";
	 
// for localhost
		String username = "localhost";
		String password = "M+Og2UoADed";
		String connectionAddress = "jdbc:postgresql://localhost:5432/game";
		BufferedReader br = null;
  // --------------------------------------CONNECTING TO A DATABASE-------------------------------------
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
  fileValuesImport(br);
  getOverallStats(connection);
 }
//--------------------------------------CREATE A .TXT FILE AND WRITE INTO IT-------------------------------------
//public static void createFile() {
//	 Writer writer = null;
//
//	 try {
//		 //initiate a new file writer
//	     writer = new BufferedWriter(new OutputStreamWriter(
//	           new FileOutputStream("sqlImportVariables.txt"), "utf-8"));
//	     //writes the variables stored in an array to a .txt file which then can be read
//	     writer.write(exportVariables);
//	 } catch (IOException ex) {
//	     
//	 } finally {
//	    try {writer.close();} catch (Exception ex) {/*ignore*/}
//	 }
//}

//--------------------------------------GETTING THE VARIABLES-------------------------------------
 //connected
// public int getGameCounter()
// {
//	 return gameCounter;
// }
////connected
// public int getDrawCounter()
// {
//	 return drawCounter;
// }
// //not connected yet, game winner is not resolved as a variable
// public int getGameWinner() 
// {
//	 return gameWinner;
// }
////connected
//public int getRoundCounter()
//{
//return roundCounter;
//}
  // --------------------------------------READING A FILE TO INSERT IT'S VALUES-------------------------------------
 public static void fileValuesImport(BufferedReader br) throws SQLException {
  try {
   // buffer and file readers declared
   br = new BufferedReader(new FileReader("sqlImportVariables.txt"));
   String line = null;
   // then go to a next line and insert the values resulting from a finished game
   while ((line = br.readLine()) != null) {
    String fileLine[] = line.split(",");
    String gameCount = fileLine[0];
    String drawsPerGame = fileLine[1];
    String winner = fileLine[2];
    String roundCounter = fileLine[3];
    // turning postgreSQL statement into a string of values
    String instertQueryForGameFinished = 
    		"INSERT INTO GAME (GAME_COUNTER, DRAWS_PER_GAME, WINNER, ROUND_COUNTER) " + 
    		"VALUES ('" + gameCount + "','" + drawsPerGame + "','" + winner + "','" + roundCounter
      + "')";
    // test
    // System.out.println("Game End" + "\r\n" + "\r\n" + "The overall winner was" +
    // winner + "\r\n" + "Scores: " + "\r\n" + "AI PLayer 1 : " + aiWins + "\r\n" +
    // "AI PLayer 2 : " + "\r\n" + "AI PLayer 3 : " + "\r\n" + "AI PLayer 4 : "
//      + "You: " + humanWins);
    // inserting postgreSQL query to the database
    statement = connection.prepareStatement(instertQueryForGameFinished);
    statement.executeUpdate();
   }
//   while ((line = br.readLine()) != null) {
//    String fileLine[] = line.split(",");
//    // variables set to match a position in a array in the .txt file
//    //first read the values for the overallStatistics, reads the first line in the .txt file 
//    String overallStatsId = fileLine[0];
//    String gamesOverall = fileLine[1];
//    String aiOverallWins = fileLine[2];
//    String playerOverallWins = fileLine[3];
//    String drawsAvg = fileLine[4];
//    String longestGame = fileLine[5];
//    // turning postgreSQL statement into a string of values
//    String insertQueryForGameStats = "INSERT INTO OVERALL_STATS (GAME_STATS_ID, GAMES_OVERALL_WINS, PLAYER_OVERALL_WINS, DRAWS_AVG, LONGEST_GAME) VALUES ('" + overallStatsId + "','"
//      + gamesOverall + "','" + aiOverallWins + "','" + playerOverallWins + "','" + drawsAvg + "','"
//      + longestGame + "')";
//    // not displaying any information here in a console as this function is included in a getOverallStats();
//    //inserting postgreSQL query to the database
//    PreparedStatement statement = connection.prepareStatement(insertQueryForGameStats);
//    statement.executeUpdate();
//   }
   br.close();
   connection.close();
   statement.close();
  } catch (IOException e) {
   e.printStackTrace();
  }
 }
 // --------------------------------------CALCULATING OVERALL STATISTICS-------------------------------------
 // note: to get the overall ai wins just add up all the individual ai sums
 public static void getOverallStats(Connection connection) {
  if (connection != null) {
   try {
    Statement statement = connection.createStatement();
    // add up all the games
    ResultSet gamesTotalSQL = statement.executeQuery("SELECT SUM(GAME_COUNT)" + "FROM GAME");
    // add up all the wins that AI achieved
    //this method requires attention as we do not know the variable for how to get the ais togetherVVVVVVVVVVVVVVV
    ResultSet a1iWnisOverallSQL = statement.executeQuery("SELECT COUNT(WINNER)" + "FROM GAME" + "WHERE WINNER = AI");
    // calculates the sum of wins for the player
    //this method requires attention as we do not know the variable for player togetherVVVVVVVVVVVVVVV
    ResultSet humanWinsOverallSQL = statement.executeQuery("SELECT COUNT(WINNER)" + "FROM GAME" + "WHERE WINNER = PLAYER");
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
