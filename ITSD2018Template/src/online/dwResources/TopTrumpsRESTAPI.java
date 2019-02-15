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
	GameCalcO gameCalculator;
	ArrayList<Player> playersRoundInfo;
	
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
		
		
	}
	
	// ----------------------------------------------------
	// Add relevant API methods here
	// ----------------------------------------------------
	
	@GET
	@Path("/startGame")

	public boolean startGame() throws IOException {
		//returns if its the humans turn or not in boolean
		gameCalculator = new GameCalcO();
		gameCalculator.StartOfGame(5);
		return true;
	}
	
	@GET
	@Path("/getRoundInfo")

	public String getRoundInfo() throws IOException {
		
		return gameCalculator.beginningOfRoundInfo();
		
	}
	
	@GET
	@Path("/fetchUserInfo")

	public String fetchUserInfo() throws IOException {
		
		ArrayList<Card> topCards = new ArrayList<Card>();
		topCards.add(gameCalculator.getPlayer(0).getCards().get(0));
		
		Player player = new Player(gameCalculator.getPlayer(0).getId(),gameCalculator.getPlayer(0).getName(),topCards);
		
		String listAsJSONString = oWriter.writeValueAsString(player);
		
		return listAsJSONString;
	}
	
	@GET
	@Path("/isTurn")

	public boolean isTurn() throws IOException {
		//returns if its the humans turn or not in boolean
		return gameCalculator.isPlayerTurn();
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

	public String sendCategory(@QueryParam("Category") String category) throws IOException {
		//sends the category selected to game calc class to store
		gameCalculator.roundChoice = 1+Integer.parseInt(category);
		playersRoundInfo = gameCalculator.Players;
		return gameCalculator.endOfRoundInfo();
	}
	
	@GET
	@Path("/playersInfo")

	public String playersInfo() throws IOException {
		String stage2Info ="";
		if(!gameCalculator.isPlayerTurn()) {
			stage2Info = sendCategory(String.valueOf((int) (Math.random() * 5)))+"<>";
		}
		String listAsJSONString = oWriter.writeValueAsString(playersRoundInfo);
		
		return stage2Info+listAsJSONString;
	}

	@GET
	@Path("/completeTurn")
	/**
	 * Here is an example of a simple REST get request that returns a String.
	 * We also illustrate here how we can convert Java objects to JSON strings.
	 * @return - List of words as JSON
	 * @throws IOException
	 */
	public String completeTurn() throws IOException {

		gameCalculator.OneRound();
		String endstatement = gameCalculator.finish();
		
		if(endstatement.contains("GAME OVER")) {
			try {
				dbConnect d = new dbConnect();
				//d.connection();
				String winnerString = "";
				Player winner = gameCalculator.Players.get(0);
				if(winner.getId() == 0) {
					winnerString = "PLAYER";
				}else {
					winnerString = "AI";
				}
				d.dbValuesImport(String.valueOf(gameCalculator.drawCounter),winnerString,String.valueOf(gameCalculator.roundCounter));
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}else {
			
			gameCalculator.roundCounter++;
			
		}
		return endstatement;
		
	}
	
}
