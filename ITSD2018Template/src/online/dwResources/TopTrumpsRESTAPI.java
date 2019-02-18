package online.dwResources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import online.Card;
import online.GameCalcO;
import online.Player;
import online.configuration.TopTrumpsJSONConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import commandline.dbConnect;


@Path("/toptrumps") // Resources specified here should be hosted at http://localhost:7777/toptrumps
@Produces(MediaType.APPLICATION_JSON) // This resource returns JSON content
@Consumes(MediaType.APPLICATION_JSON) // This resource can take JSON content as input
/**
 * This is a Dropwizard Resource that specifies what to provide when a user
 * requests a particular URL. In this case, the URLs are associated to the
 * different REST API methods that you will need to expose the game commands
 * to the Web page.
 * 
 * Below are provided some sample methods that illustrate how to create
 * REST API methods in Dropwizard. You will need to replace these with
 * methods that allow a TopTrumps game to be controled from a Web page.
 */
public class TopTrumpsRESTAPI {

	/** A Jackson Object writer. It allows us to turn Java objects
	 * into JSON strings easily. */
	ObjectWriter oWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
	ArrayList<GameCalcO> gameCalculator;
	
	/**
	 * Contructor method for the REST API. This is called first. It provides
	 * a TopTrumpsJSONConfiguration from which you can get the location of
	 * the deck file and the number of AI players.
	 * @param conf
	 */
	public TopTrumpsRESTAPI(TopTrumpsJSONConfiguration conf) {
		// ----------------------------------------------------
		// Add relevant initalization here
		// ----------------------------------------------------
		gameCalculator = new ArrayList<GameCalcO>();
		
	}
	
	// ----------------------------------------------------
	// Add relevant API methods here
	// ----------------------------------------------------
	
	@GET
	@Path("/startGame")

	public String startGame(@QueryParam("aiNum") String aiNum) throws IOException {
		//returns if its the humans turn or not in boolean
		gameCalculator.add(new GameCalcO());
		int aiAmount = Integer.parseInt(aiNum);
		int gameNumber = gameCalculator.size()-1;
		gameCalculator.get(gameNumber).StartOfGame(aiAmount+1);
		return ""+gameNumber;
	}
	
	@GET
	@Path("/getRoundInfo")

	public String getRoundInfo(@QueryParam("gameID") String gameID) throws IOException {
		int gameIndex = Integer.parseInt(gameID);
		return gameCalculator.get(gameIndex).beginningOfRoundInfo();
		
	}
	
	@GET
	@Path("/fetchUserInfo")

	public String fetchUserInfo(@QueryParam("gameID") String gameID) throws IOException {
		
		String listAsJSONString = "";
		int gameIndex = Integer.parseInt(gameID);
		
		ArrayList<Card> topCards = new ArrayList<Card>();
		topCards.add(gameCalculator.get(gameIndex).getPlayer(0).getCards().get(0));
		
		if(gameCalculator.get(gameIndex).getPlayer(0) != null) {
			ArrayList<Player> tempPlayers = new ArrayList<Player>();
			tempPlayers.add(gameCalculator.get(gameIndex).getPlayer(0));
			listAsJSONString = oWriter.writeValueAsString(tempPlayers);
		}
		
		return listAsJSONString;
	}
	
	@GET
	@Path("/isTurn")

	public boolean isTurn(@QueryParam("gameID") String gameID) throws IOException {
		//returns if its the humans turn or not in boolean
		int gameIndex = Integer.parseInt(gameID);
		return gameCalculator.get(gameIndex).isPlayerTurn();
	}

	
	@GET
	@Path("/categoryList")

	public String categoryList() throws IOException {
		//returns a json string of a list of strings of the categories
		List<String> categories = Card.attributeHeaders;

		String listAsJSONString = oWriter.writeValueAsString(categories);
		
		return listAsJSONString;
	}
	
	@GET
	@Path("/sendCategory")

	public String sendCategory(@QueryParam("Category") String category,@QueryParam("gameID") String gameID) throws IOException {
		//sends the category selected to game calc class to store
		int gameIndex = Integer.parseInt(gameID);
		gameCalculator.get(gameIndex).roundChoice = 1+Integer.parseInt(category);
		return gameCalculator.get(gameIndex).endOfRoundInfo();
	}
	
	@GET
	@Path("/playersInfo")

	public String playersInfo(@QueryParam("gameID") String gameID) throws IOException {
		String stage2Info ="";
		int gameIndex = Integer.parseInt(gameID);
		if(!gameCalculator.get(gameIndex).isPlayerTurn()) {
			stage2Info = sendCategory(String.valueOf((int) (Math.random() * 5)),gameID)+"<>";
		}
		String listAsJSONString = oWriter.writeValueAsString(gameCalculator.get(gameIndex).Players);
		
		return stage2Info+listAsJSONString;
	}
	
	@GET
	@Path("/gameCount")

	public String gameCount() throws IOException {
		dbConnect d = new dbConnect();
		String gameCount = ""+d.getTotalGames();
		return gameCount;
	}

	@GET
	@Path("/completeTurn")
	/**
	 * Here is an example of a simple REST get request that returns a String.
	 * We also illustrate here how we can convert Java objects to JSON strings.
	 * @return - List of words as JSON
	 * @throws IOException
	 */
	public String completeTurn(@QueryParam("gameID") String gameID) throws IOException {
		int gameIndex = Integer.parseInt(gameID);
		gameCalculator.get(gameIndex).OneRound();
		String endstatement = gameCalculator.get(gameIndex).finish();
		
		if(endstatement.contains("GAME OVER")) {
			try {
				dbConnect d = new dbConnect();
				String winnerString = "";
				Player winner = gameCalculator.get(gameIndex).Players.get(0);
				if(winner.getId() == 0) {
					winnerString = "PLAYER";
				}else {
					winnerString = "AI";
				}
				d.dbInsertGameRow(String.valueOf(gameCalculator.get(gameIndex).drawCounter),winnerString,String.valueOf(gameCalculator.get(gameIndex).roundCounter),gameCalculator.get(gameIndex).playersRoundWins);
				d.closeConnection();
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}else {
			
			gameCalculator.get(gameIndex).roundCounter++;
			
		}
		return endstatement;
		
	}
	
}
