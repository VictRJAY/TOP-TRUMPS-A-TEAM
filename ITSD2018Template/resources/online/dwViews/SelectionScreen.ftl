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

    <body class="page-background">
    	
		<div class="top-panel">
			<div class="row pl-5 pb-1 panel-text">
				<b>TOP TRUMPS</b>
			</div>
			<div class="row pr-5 pb-1 panel-buttons">
			  <ul class="nav">
			    <li class="mr-3"><a href="/toptrumps/game" class="m-1 panel-button">NEW GAME</a></li>
			    <li class=""><a href="/toptrumps/stats" class="m-1 panel-button">STATISTICS</a></li>
			  </ul>
			</div>
    	</div>
    	
    	<div>
    	
    		<div class="container ">
    			<div class="row">
	    			<div class="col">
	    			<h1 class="game-logo">TOP TRUMPS</h1>
	    			</div>
    			</div>
	    		<div class="row">
	    			<div class="col">
			    		<p class="game-description">Top Trumps is a simple card game in which decks of 
			    		cards are based on a theme.  
			    		The objective of the game is to 'trump' your opponent by selecting a category (e.g. firepower)
			    		 and having a "better" value for your card than the opponent does in their current card.
			    		 Start a new game or check the games records.
			    		</p>
		    		</div>
		    	</div>
		    	<div class="row center">
		    		<div class="col">
		    			<button type="button" onclick="game(); return false;" class="btn btn-success game-btn">New Game</button>
		    		</div>
		    		<div class="col">
		    			<button type="button" onclick="stats(); return false;" class="btn btn-success game-btn">Statistics</button>
		    		</div>

		    	</div>
    		</div>
    	        
		</div>
		
		<script type="text/javascript">
			
			function game(){
			
				window.location = "http://localhost:7777/toptrumps/game";
			}
			
			function stats(){
			
				window.location = "http://localhost:7777/toptrumps/stats";
			}

					
			
		</script>
		</body>
</html>