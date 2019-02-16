<html>

	<head>
		<!-- Web page title -->
    	<title>Top Trumps</title>
    	
    	<!-- Import JQuery, as it provides functions you will probably find useful (see https://jquery.com/) -->
    	<script src="https://code.jquery.com/jquery-2.1.1.js"></script>
    	<script src="https://code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
    	<link rel="stylesheet" href="https://code.jquery.com/ui/1.11.1/themes/flick/jquery-ui.css">

		<!-- Optional Styling of the Website, for the demo I used Bootstrap (see https://getbootstrap.com/docs/4.0/getting-started/introduction/) -->
		<link rel="stylesheet" href="http://dcs.gla.ac.uk/~richardm/TREC_IS/bootstrap.min.css">
    	<script src="http://dcs.gla.ac.uk/~richardm/vex.combined.min.js"></script>
    	<script>vex.defaultOptions.className = 'vex-theme-os';</script>
    	<link rel="stylesheet" href="http://dcs.gla.ac.uk/~richardm/assets/stylesheets/vex.css"/>
    	<link rel="stylesheet" href="http://dcs.gla.ac.uk/~richardm/assets/stylesheets/vex-theme-os.css"/>
    	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">

	</head>

    <body style="background-image: linear-gradient(brown, white);" onload="startGameAPI()">
    	
    	<div class="container-fluid">
	    	<div class="row m-2">
				<div class="col-sm p-2" style="background: black; color: white; text-align: center;">
					Playing Top Trump
				</div>
	    	</div>
	    	
	    	<div class="row m-2">
				<div id="info-text" class="col-sm p-2" style="background: brown; color: white; font-weight: bold;">
					
				</div>
	    	</div>
	    	
	    	<div class="row m-1">
		    	<div class="col-sm-2 next-round">
			    	<div class="row">
			    		<button id="game-button" type="button" onclick="NextRound(); return false;" class="btn btn-success">Next: Category Selection</button>
			    	</div>
		    		<div class="row" id="category-list">
		    			
		    		</div>
		    	</div>
		    	<div class="cards-display row">
		    		
		    	</div>
	    	</div>
	    	
		</div>
		
		<script type="text/javascript">
		
			var categoryList;
			var roundStage = 0;
		
			function GoHome() {		
				window.location = "http://localhost:7777/toptrumps/";
			}
			
			function startGameAPI() {
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/startGame");
				if (!xhr) {
  					alert("CORS not supported");
				}
				xhr.onload = function(e) {
 					var booleanResponse = xhr.response;
 					NextRound();
				};
				xhr.send();		
			}
			
			function getRoundInfo(){
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/getRoundInfo");
				if (!xhr) {
  					alert("CORS not supported");
				}
				xhr.onload = function(e) {
 					var roundInfo = xhr.response;
 					$('#info-text').html(roundInfo);
 					//Round 1: AI Player 4 turn
				};
				xhr.send();	
			
			}
			
			function fetchUserCard() {
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/fetchUserInfo");
				if (!xhr) {
  					alert("CORS not supported");
				}
				xhr.onload = function(e) {
 					var userCardJSON = xhr.response;
 					var userCard = JSON.parse(userCardJSON);
					displayUserCard(userCard);
				};
				xhr.send();		
			}
			
			function displayUserCard(user){
				$('.cards-display').html("");
				if(categoryList == null){
					fetchCategories(user);
				}else{
					if(user.name != null){
						var cards ="<div class='m-4 card'  style='width: 12rem; height: 10rem;'>";
						cards +="<h5>"+user.name+"</h5>";
						cards +="<img class='card-img-top' src='/assets/cardPicture0.jpg' alt='Card image cap'>";
						cards +="<h5 class='card-title'><b>"+user.cards[0].attributeValues[0]+"</b></h5>";
						var j=1;
						for(var i in user.cards[0].attributeValues){
							if(i<5){
								cards += "<p class='card-text'>"+categoryList[i]+": "+user.cards[0].attributeValues[j]+"</p>";
								j++;
							}
						}
						cards += "</div>";
						$('.cards-display').html(cards);					
					}

				}
			}
			
			function NextRound(){
				if(!$('#info-text').html().includes("GAME OVER!") || $('#game-button').html().includes("New Game")){
					if(roundStage == 0){
				 		getRoundInfo();
	 					fetchUserCard();
	 					roundStage++;
	 					$('#game-button').html("Next: Category Selection");
					}else if(roundStage == 1){
						isPlayerTurn();
						roundStage++;
					}else if(roundStage == 2){
						completeTurn();
						roundStage = 0;
						$('#game-button').html('Next: Round');
					}
				}else{
					startGameAPI();
					$('#game-button').html('Next: New Game');
				}
			}
			
			function isPlayerTurn() {
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/isTurn");
				if (!xhr) {
  					alert("CORS not supported");
				}
				xhr.onload = function(e) {
 					var booleanResponse = xhr.response;
 					if(booleanResponse == "true"){
 						$('#game-button').prop('disabled', true);
 						displayCategories();
 					}else{
 						loadCards();
 					}
				};
				xhr.send();		
			}
			
			function fetchCategories(user) {
				if(categoryList == null){
					var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/categoryList");
					if (!xhr) {
	  					alert("CORS not supported");
					}
					xhr.onload = function(e) {
	 					var categoryJSON = xhr.response;
	 					categoryList = JSON.parse(categoryJSON);
	 					displayUserCard(user)
					};
					xhr.send();
				}
			}
			
			function displayCategories(){
				var categories = "" 
				for(var i in categoryList){
					categories += '<tr><td><a onclick="sendSelectedCategory('+i+'); false;">'+categoryList[i]+'</a></td></tr>';
				}
				$('#category-list').html('<table class="table table-bordered mt-1" style="background: grey; color: white;"><thead><tr style="background: black;"><th scope="col">Select A Category</th></tr></thead><tbody>'+categories+'</tbody></table>');
			}

			
			function sendSelectedCategory(category) {
				//enable button
				 $('#game-button').prop('disabled', false);
				// First create a CORS request, this is the message we are going to send (a get request in this case)
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/sendCategory?Category="+category); // Request type and URL+parameters
				
				// Message is not sent yet, but we can check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
				// to do when the response arrives 
				xhr.onload = function(e) {
 					var roundInfo = xhr.response; // the text of the response
 					$('#info-text').html(roundInfo);
 					//Round 1: AI Player 4 selected firePower
					loadCards();
				};
				
				// We have done everything we need to prepare the CORS request, so send it
				xhr.send();		
			}
			
			function loadCards(){
			
				$('#game-button').html("Next: Show Winner");
				// First create a CORS request, this is the message we are going to send (a get request in this case)
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/playersInfo"); // Request type and URL+parameters
				
				// Message is not sent yet, but we can check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
				// to do when the response arrives 
				xhr.onload = function(e) {
					var playersInfo  = xhr.response;
					if(playersInfo.search("<>") != -1){
						var seperate = playersInfo.split("<>");
						$('#info-text').html(seperate[0]);
						playersInfo = JSON.parse(seperate[1]);
					}else{
						playersInfo = JSON.parse(xhr.response);
					}
 					populateCards(playersInfo);
					$('#category-list').html("");
				};
				
				// We have done everything we need to prepare the CORS request, so send it
				xhr.send();
			}

			function populateCards(playersInfo){
					var cards="";
					var cardID = 0;
 					for(var i in playersInfo){
 						cards +="<div class='m-2 card'  style='width: 12rem; height: 10rem;'>";
 						cards +="<h5>"+playersInfo[i].name+". <b>"+playersInfo[i].cards.length+"</b></h5>";
 						cards +="<img class='card-img-top' src='/assets/cardPicture"+cardID+".jpg' alt='Card image cap'>";
 						cards +="<h5 class='card-title'>"+playersInfo[i].cards[0].attributeValues[0]+"</h5>";
 						var k = 1;
 						for(var j in playersInfo[i].cards[0].attributeValues){
	 						if(j<5){
	 							cards += "<p class='card-text'>"+categoryList[j]+": "+playersInfo[i].cards[0].attributeValues[k]+"</p>";
	 							k++;
	 						}
 						}
 						cards += "</div>";
 						cardID++;
 						if(cardID == 5){
 							cardID=0;
 						}
 					}
 					$('.cards-display').html(cards);
					
			}
			
			function completeTurn(){
				// First create a CORS request, this is the message we are going to send (a get request in this case)
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/completeTurn"); // Request type and URL+parameters
				
				// Message is not sent yet, but we can check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
				// to do when the response arrives 
				xhr.onload = function(e) {
 					var responseText = xhr.response; // the text of the response
					$('#info-text').html(responseText);
				};
				
				// We have done everything we need to prepare the CORS request, so send it
				xhr.send();	
			
			}		
			
			// This is a reusable method for creating a CORS request. Do not edit this.
			function createCORSRequest(method, url) {
  				var xhr = new XMLHttpRequest();
  				if ("withCredentials" in xhr) {

    				// Check if the XMLHttpRequest object has a "withCredentials" property.
    				// "withCredentials" only exists on XMLHTTPRequest2 objects.
    				xhr.open(method, url, true);

  				} else if (typeof XDomainRequest != "undefined") {

    				// Otherwise, check if XDomainRequest.
    				// XDomainRequest only exists in IE, and is IE's way of making CORS requests.
    				xhr = new XDomainRequest();
    				xhr.open(method, url);

 				 } else {

    				// Otherwise, CORS is not supported by the browser.
    				xhr = null;

  				 }
  				 return xhr;
			}
			
		</script>

		
		</body>
		<footer>
			<div class="row m-5 fixed-bottom">
	    		<button type="button" class="btn btn-primary" onclick="GoHome(); return false;">Quit</button>
	    	</div>
		</footer>
</html>