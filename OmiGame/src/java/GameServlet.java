import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import org.json.JSONArray;


public class GameServlet extends HttpServlet {
    
    public static GameState gState = GameState.START ;
    public static int count = 0;
    
    
    // State machine for game
    enum GameState {
		START {
            GameState eval(ArrayList <String> list ,  HashMap <String,Integer> score ){
                return PLAYER0 ;
            }	
	},
        PLAYER0 {
            GameState eval(ArrayList <String> list ,  HashMap <String,Integer> score ){
                return PLAYER1 ;
            }         
	},
        PLAYER1 {			
            GameState eval(ArrayList <String> list ,  HashMap <String,Integer> score ){
                return PLAYER2 ;
            }         
	},
        PLAYER2 {			
            GameState eval(ArrayList <String> list ,  HashMap <String,Integer> score ){
                return PLAYER3 ;
            }         
	},
        PLAYER3 {			
            GameState eval(ArrayList <String> list ,  HashMap <String,Integer> score ){
                return SCORE ;
            }         
	},
        SCORE {			
            GameState eval(ArrayList <String> list ,  HashMap <String,Integer> score ){
                if( Collections.max(score.values()) >= 10 ) //If one player score 10 marks 
                    return WIN ;
                else if(list.size() <= 0)
                    return START;
                else
                    return PLAYER0 ;
            }         
	},
        WIN {			
            GameState eval(ArrayList <String> list ,  HashMap <String,Integer> score ){
                return WIN ;
            }         
	};
        
	abstract GameState eval(ArrayList <String> list ,  HashMap <String,Integer> score);
    }

    // methode for handle the clients requests
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/event-stream;charset=UTF-8");
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        
        if(gState == GameState.START )
            out.write("Please wait ......");
        
        else if(gState == GameState.PLAYER0){  
            
            String card = (String)request.getParameter("playedCard");
            
            if(card == null  )
                return;
            
            //check the player
            if(session.getId() == OmiServlet.players[0] ){
                
                out.write("played");
                 // Add played card
                UtilityMethods.playedCards[0] = card ;
                //remove the card in clients hand
                UtilityMethods. listOfCards.get(OmiServlet.players[0]).remove(card);
                gState = gState.eval( UtilityMethods. listOfCards.get(OmiServlet.players[0]),
                                                                            UtilityMethods.score );
                UtilityMethods.setMsg(1);                
            }
                
            else
                out.write("This is not your turn..");   //if not correct player played show this message            
                                   
        }
        else if(gState == GameState.PLAYER1){
            String card = (String)request.getParameter("playedCard");
            
           if(card == null  )
                return;
                 
           //to check the correct player
            if(session.getId() == OmiServlet.players[1]){
                
                //check the player played correct cards
                if(UtilityMethods.isCorrectlyPlayed( card,
                                                    UtilityMethods. listOfCards.get(OmiServlet.players[1]))){
                    
                    out.write("played");
                     // Add played card
                    UtilityMethods.playedCards[1] = card ;
                    //remove the card in clients hand
                    UtilityMethods. listOfCards.get(OmiServlet.players[1]).remove(card);
                    gState = gState.eval( UtilityMethods. listOfCards.get(OmiServlet.players[1]),
                                                                                        UtilityMethods.score );
                    UtilityMethods.setMsg(2);
                    
                }
                else{                   
                    out.write("please play according to the rules");    
                }                       
            }                
            else
                out.write("This is not your turn..");               
           
        }
        else if(gState == GameState.PLAYER2){
            
            String card = (String)request.getParameter("playedCard");
            
            if(card == null)
                return;
                        
            if(session.getId() == OmiServlet.players[2]){
                 
                   //check the player played correct cards
                if( UtilityMethods.isCorrectlyPlayed(card,
                             UtilityMethods. listOfCards.get(OmiServlet.players[2]))){
                    out.write("played");
                    // Add played card
                    UtilityMethods.playedCards[2] = card ;
                    //remove the card in clients hand
                    UtilityMethods. listOfCards.get(OmiServlet.players[2]).remove(card);
                    gState = gState.eval( UtilityMethods. listOfCards.get(OmiServlet.players[2]),
                                                                                UtilityMethods.score );
                    UtilityMethods.setMsg(3);                    
                }
                else{
                   
                    out.write("please play according to the rules");    
                }                           
            }
                
            else
                 out.write("This is not your turn..");               
                    
        }
        
        else if(gState == GameState.PLAYER3){
            String card = (String)request.getParameter("playedCard");
            
            if(card == null  )
                return;
                        
            if(session.getId() == OmiServlet.players[3]){
                
                 //check the player played correct cards
                if( UtilityMethods.isCorrectlyPlayed( card, UtilityMethods. listOfCards.get(OmiServlet.players[3]) )){
                    out.write("played");
                    // Add played card
                    UtilityMethods.playedCards[3] = card ;
                    //remove the card in clients hand
                    UtilityMethods. listOfCards.get(OmiServlet.players[3]).remove(card);
                   
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(UpdateUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    gState = gState.eval( UtilityMethods. listOfCards.get(OmiServlet.players[3]) , UtilityMethods.score );  
                    
                }
               else{
                   
                    out.write("please play according to the rules");    
                }     
            }
                
            else
                 out.write("This is not your turn..");           
            
        }
        
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
