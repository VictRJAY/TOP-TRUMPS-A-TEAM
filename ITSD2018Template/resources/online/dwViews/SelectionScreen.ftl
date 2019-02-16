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

    <body style="background-image: linear-gradient(brown, white);">
    	
    	<div class="container" >
    		<div class="row m-2">
				<div class="col p-2" style="background: grey; color: white; text-align: center;">
					<b>TOP TRUMPS</b>
				</div>
	    	</div>
			<div class= "row mt-1">
				<button type="button" class="btn btn-secondary col-sm mr-1" onclick="startGame(); return false;"> New Game </button>
				<button type="button" class="btn btn-secondary col-sm ml-1" onclick="getStats(); return false;"> Show Statistics </button>
	    	</div>
		</div>
		
		<script type="text/javascript">
			
			function startGame(){
				 window.location = 'http://localhost:7777/toptrumps/game';
			}
			
			function getStats(){
				 window.location = 'http://localhost:7777/toptrumps/stats';
			}
			
		</script>
		</body>
</html>