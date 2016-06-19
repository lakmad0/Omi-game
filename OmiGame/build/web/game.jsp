

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
    <link rel="stylesheet" href="css/styleGame.css">  
    <script src="javaScript/knockout-3.4.0.js"></script>
    <script src="javaScript/jquery-1.12.3.min.js"></script>
</head>


<body onload="start()">
    
    <div id="container">
	<div id="top">
            <div id="leftPart">
                
		<div id="message">
                    <span  style="margin-top: 0%;" data-bind="text: message"></span>	
		</div>
                
		<div id="playedCards" data-bind="visible: shouldShowPlayedCards">
                    
                    <div class = "position" id="card1">
			<img data-bind="attr: { src: mycard }">
		    </div>
                    <div class = "position" id="card2">
			<img data-bind="attr: { src: card1 }">
                    </div>
                    <div class = "position" id="card3">
			<img data-bind="attr: { src: card2 }">
                    </div>
                    <div class = "position" id="card4">
			<img data-bind="attr: { src: card3 }">
                    </div>
                    			 					
		</div>				
            </div>
            
            
            <div id="score" data-bind="visible: shouldShowPlayedCards">
		<div class = "position" id="trump">
                    <img data-bind="attr: { src: trumpSuit  }">
	        </div>
		<p style="margin-left: 20%;">Trump Suit</p>
                <div id="scoreboard">
                    <table cellspacing="15">
                        <tr>
                            <td data-bind="text: player0"></td>
                            <td data-bind="text: mark0" ></td>
                            
                        </tr>
                         <tr>
                            <td data-bind="text: player1"></td>
                            <td data-bind="text: mark1" ></td>
                            
                        </tr>
                         <tr>
                            <td data-bind="text: player2"></td>
                            <td data-bind="text: mark2" ></td>
                            
                        </tr>
                         <tr>
                             <td data-bind="text: player3"></td>
                            <td data-bind="text: mark3" ></td>
                            
                        </tr>
                    </table>
                </div>
            </div>
	</div>
        
	<div id="bottom">
            <div data-bind="foreach: cards , visible: shouldShowHand">
		<img data-bind="attr: { src: image }, click: function(data, event) { PlayCard(image)}"/>
            </div>
	</div>
    </div>


    <script type="text/javascript">
        
        
        var ID = setInterval(start, 1000);
        function start() {
            var eventSource = new EventSource("UpdateUI");
            eventSource.onmessage = function(event) {
                if(event.data.charAt(0) !== '{')
                    alert(event.data);                                        
                else
                    Update(event.data);                   
                   
            };
        }

   
        $(document).ready(function(){
        });
        
               
        
       function PlayCard(card){
            
            $.post("GameServlet", { playedCard : card},
            function(data,status){
                if(data !== "played")
                    alert( data );
            }); 
            
            return false;;
              
       }
       
    /******************************************************/

       // This is a simple *viewmodel* - JavaScript that defines the data and behavior of your UI
       function AppViewModel() {
           var self = this;
           self.cards = ko.observableArray([
               { image: 'cards/0_1.png' },
               { image: 'cards/1_2.png' },
               { image: 'cards/0_3.png' }
           ])
           self.card1 = ko.observable("cards/0_1.png");
           self.card2 = ko.observable("cards/0_1.png");
           self.card3 = ko.observable("cards/0_1.png");
           self.mycard = ko.observable("cards/0_1.png");
           self.trumpSuit = ko.observable("cards/spade.jpg");
           self.shouldShowHand = ko.observable(false);
           self.player0 = ko.observable("player0");
           self.player1 = ko.observable("player1");
           self.player2 = ko.observable("player2");
           self.player3 = ko.observable("player3");
           self.mark0 = ko.observable(0);
           self.mark1 = ko.observable(0);
           self.mark2 = ko.observable(0);
           self.mark3 = ko.observable(0);
           self.shouldShowPlayedCards = ko.observable(false);	    
           self.message = ko.observable("waiting...");
       }

       viewModel = new AppViewModel();
       ko.applyBindings(viewModel);

       function Update(statusJSON)
       {
            var parsed = JSON.parse(statusJSON);
            viewModel.cards(parsed.cards);
            viewModel.card1(parsed.card1);
            viewModel.card2(parsed.card2);
            viewModel.card3(parsed.card3);
            viewModel.mycard(parsed.mycard);
            viewModel.trumpSuit(parsed.trumpSuit);
            viewModel.shouldShowHand(parsed.showHand);
            viewModel.shouldShowPlayedCards(parsed.showCards);
            viewModel.player0(parsed.player0);
            viewModel.player1(parsed.player1);
            viewModel.player2(parsed.player2);
            viewModel.player3(parsed.player3);
            viewModel.mark0(parsed.mark0);
            viewModel.mark1(parsed.mark1);
            viewModel.mark2(parsed.mark2);
            viewModel.mark3(parsed.mark3);              
            viewModel.message(parsed.message);
       }

   </script>

</body>
</html>

