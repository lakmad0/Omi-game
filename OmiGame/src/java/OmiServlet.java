import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class OmiServlet extends HttpServlet {
       
    //to store session id and user name
    public static HashMap <String,String> playersId  = new HashMap<String,String>();
    public static String[] players = new String[4]; 
    public static String[] tempPlayers = new String[4]; 
    public static int connectedPlayers = 0 ;    
    
    
    //check the is user exists
    public static boolean isUserExists(String id){   
        
        if(playersId.containsKey(id))
            return true;   
        
        return false;
    }
    
    //for check sessions and connected players 
    public static void checkConnectedPlayers(HttpServletRequest request, HttpServletResponse response)
                            throws ServletException, IOException{      
        
        HttpSession session = request.getSession(false);
        String userName = request.getParameter("userName");
        String page = request.getParameter("page");
          
        if(connectedPlayers < 4 && userName != null){               
            
            if (session == null) {                
                session = request.getSession();
                playersId.put(session.getId(), userName);
                players[connectedPlayers++] = session.getId(); 
                response.sendRedirect("waiting.jsp");                
            } 
            else if( !isUserExists(session.getId()) ){
               
                playersId.put(session.getId(), userName);
                session.setAttribute("name", userName);
                players[connectedPlayers++] = session.getId();
                response.sendRedirect("waiting.jsp");
            }       
        }
        //redirect to the game page
        else if( connectedPlayers == 4 && isUserExists(session.getId()) && page!= null ){
            for(int i = 0 ; i< 4 ; i++){
                UtilityMethods.score.put( players[i], 0);
                tempPlayers[i] = players[i] ;
            }
            response.sendRedirect("game.jsp");
        }       
                  
    }       
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //content type must be set to text/event-stream
        response.setContentType("text/event-stream");  
        //encoding must be set to UTF-8
        response.setCharacterEncoding("UTF-8");
        
         checkConnectedPlayers(request, response);
                
        PrintWriter writer = response.getWriter();
        if(connectedPlayers == 1)
            writer.write("data: Waiting for others to connect. "+ connectedPlayers + " player is connected .. \n\n");
        else if(connectedPlayers == 4)
            writer.write("data: four players are cannected press enter to start  game .. \n\n");
        else
            writer.write("data: Waiting for others to connect. Only "+ connectedPlayers +" players are connected .. \n\n");            
        
        writer.close();
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
