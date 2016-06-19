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

public class UpdateUI extends HttpServlet {

     
    public static int count = 0;
    public static boolean shuffle = true;    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/event-stream;charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        String json = null ;
        
        // to check the state
        if(GameServlet.gState == GameServlet.GameState.START){
            
            if( shuffle ){
                // to shuffle the deck
                shuffle = false;
                UtilityMethods.shuffleCard();               
            }
                    
            
             if(count == 4){
                 //all four playes are connected..
                GameServlet.gState = GameServlet.gState.eval( 
                        UtilityMethods. listOfCards.get(OmiServlet.players[0]), UtilityMethods.score );
                UtilityMethods.setMsg(0);
                count = 0;
                shuffle = true ;
            }
               
             // deal the cards..
            if(session.getId() == OmiServlet.players[0]){
                json = UtilityMethods.createJsonObject(
                        UtilityMethods. listOfCards.get(OmiServlet.players[1]) , 
                            UtilityMethods.msg[0],UtilityMethods.playedCards,true,true);
                count++;             
            }               
            else if(session.getId() == OmiServlet.players[1]){
               json = UtilityMethods.createJsonObject(
                        UtilityMethods. listOfCards.get(OmiServlet.players[2]) ,
                                UtilityMethods.msg[1],
                                    UtilityMethods.rotateArray(UtilityMethods.playedCards, 1),
                                                                                         true,true);
               count++;    
            }               
            else if(session.getId() == OmiServlet.players[2]){
                json = UtilityMethods.createJsonObject(
                                UtilityMethods. listOfCards.get(OmiServlet.players[2]),
                                        UtilityMethods.msg[2],
                                            UtilityMethods.rotateArray(UtilityMethods.playedCards, 2),
                                                                                                true,true);
                count++;            
            }                
            else{
                json = UtilityMethods.createJsonObject(
                                UtilityMethods. listOfCards.get(OmiServlet.players[3]),
                                        UtilityMethods.msg[3],
                                            UtilityMethods.rotateArray(UtilityMethods.playedCards, 3),
                                                                                                true,true);
                count++;            
            }      

            String jsonString = "data:"+json+"\n\n";               
            
            try {
                // send to the clients
               out.write(jsonString);
            } finally {
                             
            }
        } 
        else if(GameServlet.gState == GameServlet.GameState.SCORE)  {
            UtilityMethods.score(UtilityMethods.playedCards);
            UtilityMethods.playedCards = new String[4];
            GameServlet.gState = GameServlet.gState.eval(
                    UtilityMethods. listOfCards.get(OmiServlet.players[3]),
                                UtilityMethods.score );
            
               
        }        
        else if(GameServlet.gState == GameServlet.GameState.WIN)  {
            // To show winner message
            UtilityMethods.findWinner();
            json = UtilityMethods.createJsonObject(
                    UtilityMethods. listOfCards.get(OmiServlet.players[3]),
                        " "+UtilityMethods.winner+" won the game...", 
                                UtilityMethods.rotateArray(UtilityMethods.playedCards, 3),
                                                                                false,false);
            String jsonString = "data:"+json+"\n\n";           
            out.write(jsonString);
                      
        }
        else{
            //to update the user interface
            UtilityMethods.updateUI(request, response);
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
