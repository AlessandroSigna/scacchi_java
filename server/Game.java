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
                    int int_cN = 8 - Integer.valueOf(cN);
                    
                    st = con.prepareStatement("SELECT * FROM tablapartido WHERE partido=?");
                    st.setString(1, partido);
                    rs = st.executeQuery();
                    Board b = new Board();
                    while(rs.next())
                    {
                        //addPiece
                        String pieza = rs.getString("pieza");
                        int cn = 8 - rs.getInt("numero");
                        int cl = letterToNumber(rs.getString("letra"));
                        boolean color = rs.getBoolean("color");
                        Coord c = new Coord(cl, cn);
                        Piece p = new Piece(pieza, c, color);
                        b.add(p);
                    }
                }
                if("1".equals(code)) //Find Moves
                {
                    //prendo coordinata inizio
                    //prendo coorinata fine
                    //faccio mossa ->
                                    // se è normale: aggiorno posizione database
                                    // restituisco json con codice 0
                                    // se è un arrocco
                                    // aggiorno posizione due pezzi database
                                    //resttuisco json con codice 1, posizine iniziale torre, posizione finale torre

                    JSONObject jObj = new JSONObjet();
                    String cLin = req.getParameter("cLin");
                    String cNin = req.getParameter("cNin");
                    String cLfin = req.getParameter("cLfin");
                    String cNfin = req.getParameter("cNfin");

                    int int_cLin = letterToNumber(cLin);
                    int int_cNin = 8 - Integer.valueOf(cNin);
                    int int_cLfin = letterToNumber(cLfin);
                    int int_cNfin = 8 - Integer.valueOf(cNfin);
                    Coord in = new Coord(int_cLin, int_cNin);
                    Coord fin = new Coord(int_cLfin, int_CNfin);
                    
                    //Costruisco board
                    st = con.prepareStatement("SELECT * FROM tablapartido WHERE partido=?");
                    st.setString(1, partido);
                    rs = st.executeQuery();
                    Board b = new Board();
                    boolean enroque = false;
                    while(rs.next())
                    {
                        //addPiece
                        String pieza = rs.getString("pieza");
                        int cn = 8 - rs.getInt("numero");
                        int cl = letterToNumber(rs.getString("letra"));
                        boolean color = rs.getBoolean("color");
                        Coord c = new Coord(cl, cn);
                        Piece p = new Piece(pieza, c, color);
                        b.add(p);
                    }
                    rs.close();
                    st.close();
                    if(b.isEnroque(in, fin))
                        enroque = true;
                    //muovi pezzo
                    st = conn.prepareStatement("UPDATE tablapartido SET letra=?, numero=?, avanzo=1 where partido=? AND letra=? AND numero=?");
                    st.setString(1, cLfin);
                    st.setString(2, cNfin);
                    st.setString(3, partido);
                    st.setString(4, cLin);
                    st.setString(5, cNin);
                    st.executeUpdate();
                    if(enroque)
                    {
                        if("C".equals(cLfin))
                        {
                            st.setString(1, "D");
                            st.setString(4, "A");
                        }
                        else
                        {
                            st.setString(1, "F");
                            st.setString(4, "H");
                        }
                        st.executeUpdate();
                    }
                }
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