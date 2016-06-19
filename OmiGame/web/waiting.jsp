


<!DOCTYPE html>
<html >
  <head>
    <meta charset="UTF-8">
    <title>Omi waiting screen</title>    

    <link rel="stylesheet" href="css/style.css">  
       
  </head>

  <body onload="start()">

  	<script type="text/javascript">
            var ID = setInterval(start, 5000);
            
	    function start() {	 
	        var eventSource = new EventSource("OmiServlet");	         
	        eventSource.onmessage = function(event) {	         
	            document.getElementById('foo').innerHTML = event.data;	         
	        };	         
	    }

    </script>

    <div class="wrapper">
	<div class="container">
            <h1 id="foo"></h1>
			
            <form class="form" method=GET action="OmiServlet">

                <input type="hidden" name="page" value="game.jsp" />  
                <button type="submit" id="login-button">Enter Game</button>
            </form>
	                
         </div>
		
	<ul class="bg-bubbles">
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
	</ul>
    </div>
       
  </body>
</html>

