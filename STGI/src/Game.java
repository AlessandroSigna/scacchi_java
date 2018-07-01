import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.Strings;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class Game extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        HttpSession session = req.getSession(false);
        if(session != null)
            createChessboardPage(res);
        else
            res.sendRedirect("/STGI");
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
        String code = (String) req.getParameter("code");        
        
        Connection con;
        PreparedStatement st;
        ResultSet rs;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(Utils.dbUrl, Utils.dbUser, Utils.dbPaswd);
            if(con != null)
            {
                //Costruisco la scacchiera
                st = con.prepareStatement("SELECT * FROM tablapartido WHERE partido=?");
                st.setString(1, partido);
                rs = st.executeQuery();
                Board b = new Board();
                while(rs.next())
                {
                    String pieza = rs.getString("pieza");
                    String avanzoStr = rs.getString("avanzo");
                    boolean avanzo = false;
                    if("1".equals(avanzoStr))
                        avanzo = true;
                    int cn = rs.getInt("numero") - 1;
                    int cl = letterToNumber(rs.getString("letra").charAt(0));
                    boolean color = rs.getBoolean("color");
                    Coord c = new Coord(cl, cn);
                    Piece p = new Piece(Integer.valueOf(pieza), color, !avanzo);
                    b.addPiece(p, c);
                }
                rs.close();
                st.close();
                if("SETUP".equals(code))
                {
                    String htmlRes = "";
                    for(int y = 7; y >= 0; y--)
                    {
                        for(int x = 0; x < 8; x++)
                        {
                            Coord c = new Coord(x,y);
                            Piece p = b.getPiece(c);
                            
                            //manderò una sequenza di div così composti
                            //<div id="G8" class="bg_white black">&#9822;</div>
                            //<div id="A6" class="bg_white"></div>
                            String divId = numberToLetter(x) + "" + (y);
                            String divClassBG = (x+y)%2!=0?"bg_black":"bg_white"; //se la somma degli indici è pari lo sfondo della casella è nero... o almeno così mi è sembrato
                            String divClassPieceColor = p==null?"":(p.getColor()?"black":"white");
                            String innerHtml = p==null?"":p.getCodeASCII();
                            htmlRes += "<div id=\"" + divId + "\" class=\"" + divClassBG + " " + divClassPieceColor +"\">" + innerHtml + "</div>";
                        }
                    }
                    JSONObject jobj = new JSONObject();
                    jobj.put("page", htmlRes);
                    st = con.prepareStatement("SELECT * FROM listadopartidos WHERE id=?");
                    st.setString(1, partido);
                    rs = st.executeQuery();
                    rs.next();
                    if(!id.equals(rs.getString("turno")))
                    {
                        jobj.put("turno", 0);
                        out.print(jobj.toString());
                        rs.close();
                        st.close();
                        con.close();
                        return;
                    }
                    jobj.put("turno", 1);
                    rs.close();
                    st.close();
                    st = con.prepareStatement("SELECT * FROM detallesjugadorpartido WHERE partido=? AND jugador=?");
                    st.setString(1, partido);
                    st.setString(2, id);
                    rs = st.executeQuery();
                    rs.next();
                    boolean myColor = rs.getBoolean("color");
                    rs.close();
                    st.close();
                    ArrayList<ArrayList<Coord>> moves = b.getMovesFilteredAllPieces(myColor);
                    
                    JSONArray jar1 = new JSONArray();
                    for(int i = 0; i < moves.size(); ++i)
                    {
                        ArrayList<Coord> tmpArray = moves.get(i);
                        JSONObject coordinatePedineConMosse = new JSONObject();
                        JSONArray jar2 = new JSONArray();
                        Coord iniziale = tmpArray.get(0);
                        String str = numberToLetter(tmpArray.get(0).getX()) + String.valueOf(tmpArray.get(0).getY() + 1);
                        coordinatePedineConMosse.put("posizioneIniziale", str);
                        for(int j = 1; j < tmpArray.size(); ++j)
                        {
                            str = numberToLetter(tmpArray.get(j).getX()) + String.valueOf(tmpArray.get(j).getY() + 1);
                            JSONObject tmp2 = new JSONObject();
                            tmp2.put("coord", str);
                            jar2.put(tmp2);
                        }
                        coordinatePedineConMosse.put("posizioniFinali", jar2);
                        jar1.put(coordinatePedineConMosse);
                    }
                    jobj.put("coordinatePedineConMosse", jar1);
                    out.print(jobj.toString());
                    out.close();
                }
                else if("MOVE_DONE".equals(code))
                {
                    String posIniziale = req.getParameter("posIniziale");
                    String posFinale = req.getParameter("posFinale");

                    int cL_in = letterToNumber(posIniziale.charAt(0));
                    int cN_in = 8 - Character.getNumericValue(posIniziale.charAt(1));
                    int cL_fin = letterToNumber(posFinale.charAt(0));
                    int cN_fin = 8 - Character.getNumericValue(posFinale.charAt(1));
                    
                    Coord in = new Coord(cL_in, cN_in);
                    Coord fin = new Coord(cL_fin, cN_fin);
                    
                    st = con.prepareStatement("SELECT * FROM detallesjugadorpartido WHERE partido=? AND jugador<>?");
                    st.setString(1, partido);
                    st.setString(2, id);
                    rs = st.executeQuery();
                    rs.next();
                    boolean enjaqueAdversario = rs.getBoolean("enjaque");
                    boolean miColor = !rs.getBoolean("color");
                    String idAdversario = rs.getString("jugador");
                    rs.close();
                    st.close();
                    int evol = 0;
                    int resCode = b.move(miColor, in, fin, enjaqueAdversario, evol);
                    JSONObject resjobj = new JSONObject();
                    switch(resCode)
                    {
                        case 0: //mossa normale
                            st = con.prepareStatement("DELETE FROM tablapartido WHERE partido=? AND letra=? AND numero=?");
                            st.setString(1, partido);
                            st.setString(2, String.valueOf(posFinale.charAt(0)));
                            st.setString(3, String.valueOf(posFinale.charAt(1)));
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE tablapartido SET letra=?, numero=?, avanzo=1 WHERE partido=?");
                            st.setString(1, String.valueOf(posFinale.charAt(0)));
                            st.setString(2, String.valueOf(posFinale.charAt(1)));
                            st.setString(3, partido);
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE detallesjugadorpartido SET enjaque=0 WHERE partido=? AND jugador=?");
                            st.setString(1, partido);
                            st.setString(2, idAdversario);
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE listadopartidos SET turno=? WHERE partido=?");
                            st.setString(1, idAdversario);
                            st.setString(2, partido);
                            st.executeUpdate();
                            st.close();
                            con.close();
                            resjobj.put("result", 0);
                            out.print(resjobj.toString());
                            out.close();
                            return;
                        case 1: //vittoria
                            st = con.prepareStatement("DELETE FROM tablapartido WHERE partido=? AND letra=? AND numero=?");
                            st.setString(1, partido);
                            st.setString(2, String.valueOf(posFinale.charAt(0)));
                            st.setString(3, String.valueOf(posFinale.charAt(1)));
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE tablapartido SET letra=?, numero=?, avanzo=1 WHERE partido=?");
                            st.setString(1, String.valueOf(posFinale.charAt(0)));
                            st.setString(2, String.valueOf(posFinale.charAt(1)));
                            st.setString(3, partido);
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE detallesjugadorpartido SET resultado=1 WHERE partido=? AND jugador=?");
                            st.setString(1, partido);
                            st.setString(2, id);
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE listadopartidos SET terminato=1 WHERE id=?");
                            st.setString(1, partido);
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE estatisticasusuarios SET victorias=victorias+1 WHERE usuario=?");
                            st.setString(1, id);
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE estatisticasusuarios SET derrotas=derrotas+1 WHERE usuario=?");
                            st.setString(1, idAdversario);
                            st.executeUpdate();
                            st.close();
                            resjobj.put("result", 1);
                            out.print(resjobj.toString());
                            out.close();
                            return;
                        case 2:
                            st = con.prepareStatement("DELETE FROM tablapartido WHERE partido=? AND letra=? AND numero=?");
                            st.setString(1, partido);
                            st.setString(2, String.valueOf(posFinale.charAt(0)));
                            st.setString(3, String.valueOf(posFinale.charAt(1)));
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE tablapartido SET letra=?, numero=?, avanzo=1 WHERE partido=?");
                            st.setString(1, String.valueOf(posFinale.charAt(0)));
                            st.setString(2, String.valueOf(posFinale.charAt(1)));
                            st.setString(3, partido);
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE detallesjugadorpartido SET resultado=2 WHERE partido=? AND jugador=?");
                            st.setString(1, partido);
                            st.setString(2, id);
                            st.executeUpdate();
                            st.setString(2, idAdversario);
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE listadopartidos SET terminato=1 WHERE id=?");
                            st.setString(1, partido);
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE estatisticasusuarios SET establos=establos+1 WHERE usuario=?");
                            st.setString(1, id);
                            st.executeUpdate();
                            st.setString(1, idAdversario);
                            st.executeUpdate();
                            st.close();
                            resjobj.put("result", 2);
                            out.print(resjobj.toString());
                            out.close();
                            return;
                        case 3:
                            resjobj.put("result", 3);
                            out.print(resjobj.toString());
                            out.close();
                            return;
                        case 4:
                            st = con.prepareStatement("DELETE FROM tablapartido WHERE partido=? AND letra=? AND numero=?");
                            st.setString(1, partido);
                            st.setString(2, String.valueOf(posFinale.charAt(0)));
                            st.setString(3, String.valueOf(posFinale.charAt(1)));
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE tablapartido SET letra=?, numero=?, avanzo=1 WHERE partido=?");
                            st.setString(1, String.valueOf(posFinale.charAt(0)));
                            st.setString(2, String.valueOf(posFinale.charAt(1)));
                            st.setString(3, partido);
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE detallesjugadorpartido SET enjaque=1 WHERE partido=? AND jugador=?");
                            st.setString(1, partido);
                            st.setString(2, idAdversario);
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE listadopartidos SET turno=? WHERE partido=?");
                            st.setString(1, idAdversario);
                            st.setString(2, partido);
                            st.executeUpdate();
                            st.close();
                            con.close();                            
                            resjobj.put("result", 4);
                            out.print(resjobj.toString());
                            out.close();
                            return;
                        case 5:
                            st = con.prepareStatement("DELETE FROM tablapartido WHERE partido=? AND letra=? AND numero=?");
                            st.setString(1, partido);
                            st.setString(2, String.valueOf(posFinale.charAt(0)));
                            st.setString(3, String.valueOf(posFinale.charAt(1)));
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE tablapartido SET letra=?, numero=?, avanzo=1 WHERE partido=?");
                            st.setString(1, String.valueOf(posFinale.charAt(0)));
                            st.setString(2, String.valueOf(posFinale.charAt(1)));
                            st.setString(3, partido);
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE detallesjugadorpartido SET enjaque=0 WHERE partido=? AND jugador=?");
                            st.setString(1, partido);
                            st.setString(2, idAdversario);
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("UPDATE listadopartidos SET turno=? WHERE partido=?");
                            st.setString(1, idAdversario);
                            st.setString(2, partido);
                            st.executeUpdate();
                            st.close();
                            con.close();
                            resjobj.put("result", 5);
                            if("G1".equals(posFinale))
                            {
                                resjobj.put("in", "H1");
                                resjobj.put("fin", "F1");
                            }
                            else if("C1".equals(posFinale))
                            {
                                resjobj.put("in", "A1");
                                resjobj.put("fin", "D1");
                            }
                            else if("G8".equals(posFinale))
                            {
                                resjobj.put("in", "H8");
                                resjobj.put("fin", "F8");
                            }
                            else
                            {
                                resjobj.put("in", "A8");
                                resjobj.put("fin", "D8");
                            }
                            out.print(resjobj.toString());
                            out.close();
                        default:
                            out.print("false");
                            break;
                    }
                }
            }
        }
        catch(Exception e)
        {
            
            out.print(e.toString());
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
        
    int letterToNumber(char a)
    {
        char c = 'A';
        int b = a - c;

        return b;
    }
    
    public void createChessboardPage(HttpServletResponse res) throws IOException, ServletException
    {
        res.setContentType("text/html");
        
        String filename = "/WEB-INF/staticPages/chessboard.html";
        ServletContext context = getServletContext();
        InputStream is = context.getResourceAsStream(filename);
        
        if (is != null)
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            PrintWriter out = res.getWriter();
            String text;
            
            while((text = reader.readLine()) != null)
            {
                out.println(text);
            }
            
            reader.close();
            isr.close();
            out.close();
        }
        else
        {
            createServerErrorPage(res);
            return;
        }
    }

    public void createServerErrorPage(HttpServletResponse res) throws IOException
    {
        res.setContentType("text/html");
        
        PrintWriter out = res.getWriter();
        out.println("<HTML><BODY>SERVER ERROR!</BODY></HTML>");
        out.close();
    }
}