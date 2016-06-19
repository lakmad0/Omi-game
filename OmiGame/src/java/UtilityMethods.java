
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;


public class UtilityMethods {
    
    // To store all cards
     private static  String [] cards = { "cards/0_1.png","cards/0_2.png","cards/0_3.png","cards/0_4.png","cards/0_5.png","cards/0_6.png","cards/0_7.png","cards/0_8.png","cards/0_9.png","cards/0_10.png","cards/0_11.png","cards/0_12.png","cards/0_13.png",
                                        "cards/1_1.png","cards/1_2.png","cards/1_3.png","cards/1_4.png","cards/1_5.png","cards/1_6.png","cards/1_7.png","cards/1_8.png","cards/1_9.png","cards/1_10.png","cards/1_11.png","cards/1_12.png","cards/1_13.png",
                                        "cards/2_1.png","cards/2_2.png","cards/2_3.png","cards/2_4.png","cards/2_5.png","cards/2_6.png","cards/2_7.png","cards/2_8.png","cards/2_9.png","cards/2_10.png","cards/2_11.png","cards/2_12.png","cards/2_13.png",
                                        "cards/3_1.png","cards/3_2.png","cards/3_3.png","cards/3_4.png","cards/3_5.png","cards/3_6.png","cards/3_7.png","cards/3_8.png","cards/3_9.png","cards/3_10.png","cards/3_11.png","cards/3_12.png","cards/3_13.png"};
     
    //To store trump card images
    public static String [] trumps = {"cards/0_1.png","cards/1_1.png","cards/2_1.png","cards/3_1.png"};
    //Initial messages
    public static String [] msg = {"let's play omi","let's play omi","let's play omi","let's play omi","let's play omi"};  
    //To store cards 
    public static HashMap <String,ArrayList> listOfCards  = new HashMap<String,ArrayList>();
    //To store score
    public static HashMap <String,Integer> score  = new HashMap<String,Integer>();
    public static String [] playedCards = new String[4];
    public static String winner = null;    
   
    public static String trumpSuit = null;
        
     //create json object to return to front end
    public static String createJsonObject(ArrayList<String> list,String msg, String [] cards,boolean showHand,boolean showCards){
        
        JSONObject json = new JSONObject();
        JSONArray  array = new JSONArray();
             
        try{
           
            for(int i= 0; i < list.size() ; i++){  
                JSONObject image = new JSONObject();
                image.put("image", list.get(i) );
                array.put(image);         
            }
            json.put("cards",array);
            json.put("showHand" , showHand);
            
            if(cards[0] != null)
                json.put("mycard" , cards[0]);
            if(cards[1] != null)
                json.put("card1" , cards[1]);
            if(cards[2] != null)
                json.put("card2" , cards[2]);
            if(cards[3] != null)
                json.put("card3" , cards[3]);
                
            json.put("showCards", showCards);
            json.put("trumpSuit", trumpSuit);
            json.put("player0", OmiServlet.playersId.get(OmiServlet.tempPlayers[0]) );
            json.put("player1", OmiServlet.playersId.get(OmiServlet.tempPlayers[1]) );
            json.put("player2", OmiServlet.playersId.get(OmiServlet.tempPlayers[2]) );
            json.put("player3", OmiServlet.playersId.get(OmiServlet.tempPlayers[3]) );
            json.put("mark0", score.get(OmiServlet.tempPlayers[0]) );
            json.put("mark1", score.get(OmiServlet.tempPlayers[1]) );
            json.put("mark2", score.get(OmiServlet.tempPlayers[2]) );
            json.put("mark3", score.get(OmiServlet.tempPlayers[3]) );
            json.put("message",msg); 
        
        
        }catch(Exception e){
            System.out.println("e");
        }
        
        return json.toString();   
       
    }
    
    //shuffle the deck and deal
    public static void shuffleCard(){
        
        ArrayList<String> list= new ArrayList<String>(Arrays.asList(cards));
        Collections.shuffle(list);
        cards  = list.toArray(new String[list.size()]);
        
        listOfCards.put(OmiServlet.players[0], new ArrayList<String>());
        listOfCards.put(OmiServlet.players[1], new ArrayList<String>());
        listOfCards.put(OmiServlet.players[2], new ArrayList<String>());
        listOfCards.put(OmiServlet.players[3], new ArrayList<String>());
        
        
        for(int i = 0 ; i< cards.length ; i++){
            if(i<13)
                listOfCards.get(OmiServlet.players[0]).add(cards[i]);
            else if(i<26)
                 listOfCards.get(OmiServlet.players[1]).add(cards[i]);
            else if( i< 39)
                 listOfCards.get(OmiServlet.players[2]).add(cards[i]);
            else
                listOfCards.get(OmiServlet.players[3]).add(cards[i]);               
        }
        
        trumpSuit = trumps[Character.getNumericValue(cards[cards.length-1].charAt(6))];
    
    }
    
    // To update the UI 
    public static void updateUI(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        String json = null ;
        
        if(session.getId() == OmiServlet.players[0]){
            json = createJsonObject(  listOfCards.get(OmiServlet.players[0]),
                                                    msg[0],playedCards,true,true);
                          
        }               
        else if(session.getId() == OmiServlet.players[1]){
            json = createJsonObject(  listOfCards.get(OmiServlet.players[1]),
                                            msg[1],rotateArray(playedCards, 1),true,true);
                   
        }               
        else if(session.getId() == OmiServlet.players[2]){
            json = createJsonObject(  listOfCards.get(OmiServlet.players[2]),
                                                msg[2], rotateArray(playedCards, 2),true,true);
                            
        }                
        else{
            json = createJsonObject( listOfCards.get(OmiServlet.players[3]),
                                            msg[3], rotateArray(playedCards, 3),true,true);
                           
        }      

        String jsonString = "data:"+json+"\n\n";               
            
        try {
            out.write(jsonString);
        } finally {
            out.close();              
        }
        
    }
    
    // To check game rules
    public static boolean isCorrectlyPlayed(String card , ArrayList <String> player){
        
        char suit = playedCards[0].charAt(6);
               
        if(card.charAt(6) == suit )
            return true ;
        else{
            
            for(int i = 0 ; i < player.size() ;i++){
                if(player.get(i).charAt(6) == suit )
                    return false;   
            }          
        }         
        return true;
    }
    
    //To set messages for clients
    public static void setMsg(int no){
        
        for(int i =0 ; i<4 ;i++){
            if(i == no)
               msg[i] =  "This is your turn...";
            else
                msg[i] = "It's "+OmiServlet.playersId.get(OmiServlet.players[no])+"'s turn";          
        }
    
    }
    
    //To rotate a String array    
    public static String [] rotateArray( String [] array , int count){
        
        String [] temp  = new String[array.length] ;
        
        for (int i = 0; i < array.length; i++) {
            temp[i] = array[ ( count++ % array.length ) ];
        }    
        return temp;    
    }
    
    // To Calculate score
    public static void score(String [] card){
        int winner = 0 ;
        if( isAvailableTump( card ) )
            winner = getMax(card ,trumpSuit.charAt(6) );
        else{
            winner = getMax(card , card[0].charAt(6) );        
        }    
       
        int marks = score.get(OmiServlet.players[winner]);
        score.put(OmiServlet.players[winner] ,++marks);
        OmiServlet.players = rotateArray(OmiServlet.players ,winner);
        if( listOfCards.get(OmiServlet.players[0]).size() > 0 )
            UtilityMethods.setMsg(0);
                     
    }
    
    // check trump availability
    public static boolean isAvailableTump( String [] card ){
        for(int i = 0 ; i < 4 ; i++ ){
            if( card[i].charAt(6) == trumpSuit.charAt(6) )
                return true ;        
        }
        return false ;
    }
    
    
    // to get the winner card
    public static int getMax(String [] card, char suit){
        
        ArrayList <String> temp = new ArrayList<String>();
        String temp1 = "cards/"+suit+"_1.png";
        int maxValue = 0 ;
        String max ;
        int maxIndex = 0 ;
        
        for(int i = 0 ; i < 4 ; i++){
            if( card[i].charAt(6) == suit){
               temp.add(card[i]);            
            }
            if( card[i].equalsIgnoreCase(temp1) ){
                return i ;
            }
        }
                       
        for (int i = 0; i < temp.size(); i++) {
            
            int val ;
            
            if( Character.isDigit(temp.get(i).charAt(9)))
                val = Integer.parseInt( temp.get(i).substring(8,10));
            else
                val = Character.getNumericValue( temp.get(i).charAt(8) );
                 
            
            if( val > maxValue )
                maxValue = val ;
            
        }
        
        max  = "cards/"+suit+"_"+maxValue+".png";
                
        for(int i = 0 ; i < 4 ; i++){
            
            if( card[i].equalsIgnoreCase(max) ){
                maxIndex = i ; 
                break ;         
            }                    
                           
        }
        
        return maxIndex ;
    
    }
    
    
    // To update the winner
    public static void findWinner(){
        int max =Collections.max(score.values());
        
        for (Entry<String, Integer> entry : score.entrySet()) {  // Itrate through hashmap
            if (entry.getValue()==max) {
                winner = OmiServlet.playersId.get(entry.getKey());     // Print the key with max value
            }
        }   
    
    }
    
    
    
    
}
