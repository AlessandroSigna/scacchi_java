import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class Game extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        HttpSession session = req.getSession(false);
        if(session == null)
        {
            res.sendRedirect("login");
            return;
        }
        String id = (String) session.getAttribute("id");
        String username = (String) session.getAttribute("user");
        String partido = (String) req.getParameter("partido");
        PrintWriter out = res.getWriter();
        out.println(partido);
        out.close();
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        HttpSession session = req.getSession(false);
        PrintWriter out = res.getWriter();
        if(session == null)
        {
            out.print("5");
            out.close();
            return;
        }
        String id = (String) session.getAttribute("id");
        String username = (String) session.getAttribute("user");
        String partido = (String) req.getParameter("partido");
        String code = req.getParameter("code");
        
        Connection con;
        PreparedStatement st;
        ResultSet rs;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(Utils.dbUrl, Utils.dbUser, Utils.dbPaswd);
            if(con != null)
            {
                if("0".equals(code)) //FindMoves
                {
                    String cL = req.getParameter("cL");
                    String cN = req.getParameter("cN");
                    
                    //Istanzio Classe Scacchiera
                    //Interrogo Database per avere lista pezzi
                    //Inserisco i pezzi nella scacchiera
                    //Calcolo lista mosse disponibili
                    //Restituisco il JSON
                    
                    int int_cL = letterToNumber(cL);
                    int int_cN = Integer.valueOf(cN) - 1;
                    
                    st = con.prepareStatement("SELECT * FROM tablapartido WHERE partido=?");
                    st.setString(1, partido);
                    rs = st.executeQuery();
                    Board b = new Board();
                    while(rs.next())
                    {
                        //addPiece
                        String pieza = rs.getString("pieza");
                        int cn = rs.getInt("numero") - 1;
                        int cl = letterToNumber(rs.getString("letra"));
                        boolean color = !rs.getBoolean("color");
                        Coord c = new Coord(cl, cn);
                        Piece p = new Piece(pieza, color);
                        b.add(p);
                    }
                }
                if("1".equals(code))
                {
                    
                }
            }
            if("0".equals(code)) //FindMoves
            {
                String cL = req.getParameter("cL");
                String cN = req.getParameter("cN");
                
                //devo ottenere la scacchiera dal DB e metterla nella forma
                //
                
                int int_cL = letterToNumber(cL);
                int int_cN = Integer.valueOf(cN) - 1;
                
            }
        }
        catch(Exception e)
        {
            out.print("5");
            out.close();
            return;
        }
        
        
    }
    
    String numberToLetter(int a)
    {
       char c = 'A';
       c += a;
        
       return Character.toString(c);
    }
        
    int letterToNumber(String a)
    {
        char t = a.charAt(0);
        char c = 'A';
        int b = t - c;
        System.out.println(b);
    }
}