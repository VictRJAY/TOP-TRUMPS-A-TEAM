
package dbConnect;
import java.io.*;
import java.sql.*;
public class main {
 static Connection connection = null;
 static PreparedStatement statement = null;
 public static void main(String[] args) throws SQLException, SQLException
 {
  // --------------------------------------VARIABLE
  // DECLARATION-------------------------------------
  String username = "m_18_2413233s";
  String password = "2413233s";
  String connectionAddress = "jdbc:postgresql://yacata.dcs.gla.ac.uk:5432/";
  BufferedReader br = null;
  // --------------------------------------CONNECTING TO A
  // DATABASE-------------------------------------
  try {
   Class.forName("org.postgresql.Driver");
  } catch (ClassNotFoundException e) {
   System.out.println("JDBC Driver not found1");
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
 // --------------------------------------READING A FILE TO INSERT IT'S
 // VALUES-------------------------------------
 public static void fileValuesImport(BufferedReader br) throws SQLException {
  try {
   // buffer and file readers declared
   br = new BufferedReader(new FileReader("myFile.txt"));
   String line = null;
   // then go to a next line and insert the values resulting from a finished game
   while ((line = br.readLine()) != null) {
    String fileLine[] = line.split(",");
    String gameId = fileLine[0];
    String gameCount = fileLine[1];
    String drawsPerGame = fileLine[2];
    String winner = fileLine[3];
    String roundCount = fileLine[4];
    String humanWins = fileLine[5];
    String ai1Wins = fileLine[6];
    String ai2Wins = fileLine[7];
    String ai3Wins = fileLine[8];
    String ai4Wins = fileLine[9];
    // --------------------------------------SQL CREATE
    // STATEMENTS-------------------------------------
    // CREATE TABLE GAME
    // (
    // GAME_ID INT PRIMARY KEY,
    // GAME_COUNT INT,
    // DRAWS_PER_GAME INT,
    // WINNER VARCHAR (64),
    // ROUND_COUNT INT,
    // HUMAN_WINS INT,
    // AI1_WINS INT,
    // AI2_WINS INT,
    // AI3_WINS INT,
    // AI4_WINS INT,
    // );
    // turning postgreSQL statement into a string of values
    String instertQueryForGameFinished = "INSERT INTO GAME (GAME_ID, GAME_COUNT, DRAWS_PER_GAME, WINNER, ROUND_COUNT, HUMAN_WINS, AI1_WINS, AI2_WINS, AI3_WINS, AI4_WINS) VALUES ('"
      + gameId + "','" + gameCount + "','" + drawsPerGame + "','" + winner + "','" + roundCount
      + "','" + humanWins + "','" + ai1Wins + "','" + ai2Wins + "','" + ai3Wins + "','" + ai4Wins
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
    ResultSet gamesTotalSQL = statement.executeQuery("SELECT SUM(game_Count)" + "FROM GAME");
    // add up all the wins that AI achieved
    ResultSet a1iWnisOverallSQL = statement.executeQuery("SELECT SUM(AI1_WINS)" + "FROM GAME");
    // add up all the wins that AI achieved
    ResultSet ai2WnisOverallSQL = statement.executeQuery("SELECT SUM(AI2_WINS)" + "FROM GAME");
    // add up all the wins that AI achieved
    ResultSet ai3WnisOverallSQL = statement.executeQuery("SELECT SUM(AI3_WINS)" + "FROM GAME");
    // add up all the wins that AI achieved
    ResultSet a4iWnisOverallSQL = statement.executeQuery("SELECT SUM(AI4_WINS)" + "FROM GAME");
    // calculates the sum of wins for the player
    ResultSet humanWinsOverallSQL = statement.executeQuery("SELECT SUM(HUMAN_WINS)" + "FROM GAME");
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
  {
  }
 }
}
// public static void getOverallStats(Connection connection) {
//  if (connection != null) {
//   try {
//    System.out.println("Connection to the database has been established");
//    Statement statement = connection.createStatement();
//    ResultSet resultSet = statement.executeQuery(
//      "SELECT GAMES_OVERALL, AI_OVERALL_WINS, PLAYER_OVERALL_WINS, DRAWS_AVG, LONGEST_GAME"
//        + "FROM OVERALL_STATS");
//    resultSet.next();
//    int gamesOverall = resultSet.getInt("GAMES_OVERALL");
//    int aiOverallWins = resultSet.getInt("AI_OVERALL_WINS");
//    int playerOverallWins = resultSet.getInt("PLAYER_OVERALL_WINS");
//    double drawsAvg = resultSet.getDouble("DRAWS_AVG");
//    int longestGame = resultSet.getInt("LONGEST_GAME");
//    // displays the information about all of the games upon user request
//    System.out.println("Game Statistics" + "\r\n" + "Number of Games: " + gamesOverall
//      + "Number of Human Wins: " + playerOverallWins + "Number of AI Wins: " + aiOverallWins
//      + "Average Number of Draws: " + drawsAvg + "Longest Game: " + longestGame);
//
//    connection.close();
//    statement.close();
//    resultSet.close();
//
//   } catch (SQLException e) {
//    e.printStackTrace();
//   }
//  } else {
//   System.out.println("Connection failed");
//  }
//  {
//  }
// }
// --------------------------------------SQL
// CALCULATIONS-------------------------------------
// --------------------------------------GETTING GAME FINISHED
// INFO-------------------------------------
// public static void getGameFinishedInfo(Connection connection) {
//  if (connection != null) {
//   try {
//    Statement statement = connection.createStatement();
//    System.out.println("Connection to the database has been established");
//    ResultSet resultSet = statement.executeQuery(
//      "SELECT GAME_ID, DRAWS_GAME, WINNER, ROUND_COUNT, HUMAN_WINS, AI_WINS" + "FROM GAME");
//    resultSet.next();
//    String winner = resultSet.getString("WINNER");
//    int humanWins = resultSet.getInt("HUMAN_WINS");
//    int aiWins = resultSet.getInt("AI_WINS");
//    // displays the information about the most recent game upon user request
//    System.out.println("Game End" + "\r\n" + "\r\n" + "The overall winner was" + winner + "\r\n" + "Scores: " + "\r\n" + "AI PLayer 1 : " + aiWins + "\r\n" + "AI PLayer 2 : " + "\r\n" + "AI PLayer 3 : " + "\r\n" + "AI PLayer 4 : "
//      + "You: " + humanWins);
//    connection.close();
//    statement.close();
//    resultSet.close();
//   } catch (SQLException e) {
//    e.printStackTrace();
//   }
//  }
// }