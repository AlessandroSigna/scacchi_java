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

import scacchi_java.Board;
import scacchi_java.Coord;
import scacchi_java.Piece;

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
                    Boolean cColor = req.getParameter("color");
                    
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
                        String avanzoStr = rs.getString("avanzo");
                        boolean avanzo = false;
                        if("1".equals(avanzoStr))
                            avanzo = true;
                        int cn = 8 - rs.getInt("numero");
                        int cl = letterToNumber(rs.getString("letra"));
                        boolean color = rs.getBoolean("color");
                        Coord c = new Coord(cl, cn);
                        Piece p = new Piece(pieza, c, color, !avanzo);
                        b.add(p);
                    }

                    Coord pieceCoord = new Coord(int_cL, int_cN);
                    Piece selectedPiece = b.getPiece(pieceCoord);
                    
                    /* il json contenente la lista di posizioni possibili sarà del tipo
                     [
                        { "coord": "B4" },
                        { "coord": "D5" }
                     ]
                    */
                    Coord[] posArray = selectedPiece.move(b, pieceCoord);
                    if(posArray == null)
                    {
                        out.print("[]");   //ritorno un array vuoto
                        out.close();
                        return;
                    }
                    else
                    {
                        String jsonList = "[";
                        for (int i = 0; i<posArray.length; i++) {
                            String itemString = "{\"coord\":\"" + numberToLetter(posArray[i].getX()) + "" + (8-posArray[i].getY()) + "\"}" + (i==posArray.length-1)?"":",";
                            jsonList += itemString;
                        }
                        jsonList += "]";
                        
                        out.print(jsonList);
                        out.close();
                        return;
                    }
                }
                else if("1".equals(code)) //Find Moves
                {
                    JSONObject jObj = new JSONObject();
                    String cLin = req.getParameter("cLin");
                    String cNin = req.getParameter("cNin");
                    String cLfin = req.getParameter("cLfin");
                    String cNfin = req.getParameter("cNfin");
                    String cEvol = req.getParameter("cEvol");

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
                    boolean evolving = false;
                    boolean jaque = false;
                    boolean jaquemate = false;
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
                    if(b.isEvolving(in, fin))
                    {
                        if("0".equals(cEvol))
                        {
                            jobj.put("res", 1); //IL CONTROLLO PASSA AL CLIENTE
                            con.close();
                            out.println(jobj.toString());
                            return;
                        }
                        evolving = true;
                    }

                    if(b.isEnroque(in, fin))
                        enroque = true;
                        
                    if(b.esJaque(in, fin))
                        jaque = true;

                    if(b.esJaqueMate(in, fin));
                        jaquemate = true;


                    //muovi pezzo
                    //fase 1: controllo se c'è una pedina nella casella finale ed aggiorno i database
                    if(!b.isEmpty(fin))
                    {
                        st = con.prepareStatement("DELETE FROM tablapartido where partido=? AND letra=? AND numero=?");
                        st.setString(1,partido);
                        st.setString(2, cLfin);
                        st.setString(2, cNfin);
                        st.executeUpdate();
                        st.close();
                    }

                    //fase 2: aggiorno posizione pedina mossa
                    st = conn.prepareStatement("UPDATE tablapartido SET letra=?, numero=?, avanzo=1 where partido=? AND letra=? AND numero=?");
                    st.setString(1, cLfin);
                    st.setString(2, cNfin);
                    st.setString(3, partido);
                    st.setString(4, cLin);
                    st.setString(5, cNin);
                    st.executeUpdate();

                    //fase 3: se ho fatto l'arrocco, preparo il json con la nuova posizione delle torri
                    if(enroque)
                    {
                        jObj.add("enroque", "1");
                        if("C".equals(cLfin))
                        {
                            jObj.add("cIn", "A" + cNin);
                            jObj.add("cFin", "D" + cNin);
                            st.setString(1, "D");
                            st.setString(4, "A");
                        }
                        else
                        {
                            jObj.add("cIn", "H" + cNin);
                            jObj.add("cFin", "F" + cNin);
                            st.setString(1, "F");
                            st.setString(4, "H");
                        }
                        st.executeUpdate();
                        st.close();
                    }

                    if(jaque)
                        jObj.add("jaque", "1");
                    else
                        jObj.add("jaque", "0");

                    if(jaquemate)
                    {
                        jObj.add("jaquemate", "1");
                        //devo aggiornare i database per indicare che la partita è finita
                        //to do...
                    }
                    else
                        jObj.add("jaquemate", "0");
                    jobj.put("res", 0);
                    out.print(jobj.toString());
                }
                else if("SETUP".equals(code))//il client mi richiede lo schema della scacchiera
                {
                    st = con.prepareStatement("SELECT * FROM tablapartido WHERE partido=?");
                    st.setString(1, partido);
                    rs = st.executeQuery();
                    Board b = new Board();
                    while(rs.next())
                    {
                        //addPiece
                        String pieza = rs.getString("pieza");
                        String avanzoStr = rs.getString("avanzo");
                        boolean avanzo = false;
                        if("1".equals(avanzoStr))
                            avanzo = true;
                        int cn = 8 - rs.getInt("numero");
                        int cl = letterToNumber(rs.getString("letra"));
                        boolean color = rs.getBoolean("color");
                        Coord c = new Coord(cl, cn);
                        Piece p = new Piece(pieza, c, color, !avanzo);
                        b.add(p);
                    }
                    
                    String htmlRes = "";
                    for(int x = 0; x < 8; x++)
                    {
                        for(int y = 0; y < 8; y++)
                        {
                            Coord c = new Coord(x,y);
                            Piece p = b.getPiece(c);
                            
                            //manderò una sequenza di div così composti
                            //<div id="G8" class="bg_white black">&#9822;</div>
                            //<div id="A6" class="bg_white"></div>
                            String divId = numberToLetter(x) + "" + (8-y);
                            String divClassBG = (x+y)/2==0?"bg_black":"bg_white"; //se la somma degli indici è pari lo sfondo della casella è nero... o almeno così mi è sembrato
                            String divClassPieceColor = p==null?"":(p.getColor()?"black":"white");
                            String innerHtml = p==null?"":p.getCodeASCII();
                            htmlRes += "<div id=\"" + divId + "\" class=\"" + divClassBG + " " + divClassPieceColor +"\">" + innerHtml + "</div>";
                        }
                        
                        out.print(htmlRes);
                        out.close();
                    }

                }
            }
        }
        catch(Exception e)
        {
            JSONObject jobj = new JSONObject();
            jobj.put("res", 2);
            out.print(jobj.toString());
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