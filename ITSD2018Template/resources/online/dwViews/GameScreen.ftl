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

		<link  rel="stylesheet" href="/assets/stylesheet/custom.css">
	</head>

    <body class="page-background" onload="startGame()">
    
    	<div class="top-panel">
			<div class="row pl-5 pb-1 panel-text">
				<b>TOP TRUMPS: In Game</b>
			</div>
			<div class="row pr-5 pb-1 panel-buttons">
			  <ul class="nav">
			    <li class="mr-3"><a href="/toptrumps/" class="m-1 panel-button">Back</a></li>
			  </ul>
			</div>
    	</div>
    	
    	<div class="container-fluid">
	    	
	    	<div class="row m-2">
				<div id="info-text" class="col-sm p-2">
					
				</div>
	    	</div>
	    	<div class="row center">
	    		<button id="game-button" type="button" onclick="NextRound(); return false;" class="btn btn-success game-btn">Next: Category Selection</button>
	    	</div>
	    	
	    	<div id="categoryModal" class="modal" tabindex="-1" role="dialog">
			  	<div id="category-list">
				      
				</div>
			</div>

		    <div class="cards-display row mt-3 center">
		    		
		    </div>
		</div>
		
		<div id="aiModal" class="modal" tabindex="-1" role="dialog">
  	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header top-panel">
	        <h5 class="modal-title">Amount of AI</h5>
	        <button type="button" class="close" onclick="GoHome(); return;" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
		       <div class="form-group">
				  <label for="usr">Enter Number of AI:</label>
				  <input type="number" max="4" min="1" class="form-control" id="ai-amount" placeholder="4">
				</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-success game-btn" onclick="startGameAPI(); return;">Start</button>
	        <button type="button" class="btn btn-secondary default-btn" onclick="GoHome(); return;">Back</button>
	      </div>
	    </div>
	  </div>
			</div>
		
		<script type="text/javascript">
		
			var categoryList;
			var roundStage = 0;
			var gameID = -1;
		
			function GoHome() {		
				window.location = "http://localhost:7777/toptrumps/";
			}
			
			function startGame() {
				$('#aiModal').modal('toggle');	
			}
			
			function startGameAPI(){
				var aiNum = $('#ai-amount').val();
				if(aiNum != null){
					if(aiNum<1){
						aiNum = 1;
					}
					if(aiNum>4){
					aiNum = 4;}
				}else{
					aiNum = 4;
				}
				$('#aiModal').modal('toggle');
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/startGame?aiNum="+aiNum);
				if (!xhr) {
  					alert("CORS not supported");
				}
				xhr.onload = function(e) {
 					gameID = xhr.response;
 					NextRound();
				};
				xhr.send();	
			
			}
			
			function getRoundInfo(){
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/getRoundInfo?gameID="+gameID);
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
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/fetchUserInfo?gameID="+gameID);
				if (!xhr) {
  					alert("CORS not supported");
				}
				xhr.onload = function(e) {
 					var userCardJSON = xhr.response;
 					var userCard = JSON.parse(userCardJSON);
					displayCards(userCard);
				};
				xhr.send();		
			}
			
			function displayCards(playersInfo){
					var cards="";
					var cardID = 0;
					if(categoryList == null){
						fetchCategories(playersInfo);
					}else{
						if(playersInfo != null){
		 					for(var i in playersInfo){
		 						cards +="<div class='m-2 card card-shape'>";
		 						cards +="<div class='row player-name'><div class='col'>"+playersInfo[i].name+"</div></div>";
		 						cards +="<div class='row card-name'><div class='col'><b>"+playersInfo[i].cards[0].attributeValues[0]+"</b></div></div>";
		 						cards +="<div class='row' ><div class='col'><img class='card-img-top image-size' src='/assets/images/cardPicture"+cardID+".jpg' alt='Card image cap'></div></div>";
		 						cards += "<div class='row' ><div class='col'><p class='card-text'>"
		 						var k = 1;
		 						for(var j in playersInfo[i].cards[0].attributeValues){
			 						if(j<5){
			 							cards += categoryList[j]+": <b>"+playersInfo[i].cards[0].attributeValues[k]+"</b></br>";
			 							k++;
			 						}
		 						}
		 						cards += "</p></div></div>";
		 						cards +="<div class='row deck-size'><div class='col'>Cards Left: <b>"+playersInfo[i].cards.length+"</b></div></div>";
		 						cards += "</div>";
		 						cardID++;
		 						if(cardID == 5){
		 							cardID=0;
		 						}
		 					}
	 					}
	 				}
 					$('.cards-display').html(cards);
					
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
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/isTurn?gameID="+gameID);
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
	 					displayCards(user)
					};
					xhr.send();
				}
			}
			
			function displayCategories(){
				var categories = "" 
				for(var i in categoryList){
					categories += '<tr onclick="sendSelectedCategory('+i+'); false;"><td><a>'+categoryList[i]+'</a></td></tr>';
				}
				$('#category-list').html('<table class="table table-striped table-dark table-center" ><thead><tr><th scope="col">SELECT A CATEGORY</th></tr></thead><tbody>'+categories+'</tbody></table>');
				$('#categoryModal').modal('toggle');
			}

			
			function sendSelectedCategory(category) {
				//enable button
				 $('#game-button').prop('disabled', false);
				 $('#categoryModal').modal('hide');
				// First create a CORS request, this is the message we are going to send (a get request in this case)
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/sendCategory?Category="+category+"&gameID="+gameID); // Request type and URL+parameters
				
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
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/playersInfo?gameID="+gameID); // Request type and URL+parameters
				
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
 					displayCards(playersInfo);
					$('#category-list').html("");
				};
				
				// We have done everything we need to prepare the CORS request, so send it
				xhr.send();
			}


			function completeTurn(){
				// First create a CORS request, this is the message we are going to send (a get request in this case)
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/completeTurn?gameID="+gameID); // Request type and URL+parameters
				
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
</html>