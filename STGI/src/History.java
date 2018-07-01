import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.Strings;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.json.JSONException;
import org.json.JSONObject;

public class History extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        HttpSession session = req.getSession(false);
        //PrintWriter out = res.getWriter();
        if(session == null)
        {   
            res.sendRedirect("/STGI");
            return;
        }
        
        String partido = req.getParameter("id");
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(Utils.dbUrl, Utils.dbUser, Utils.dbPaswd);
            if(con != null)
            {
                PreparedStatement st = con.prepareStatement("select * from historiapartido WHERE historiapartido.partido=? order by timestamp");
                st.setString(1, partido);
                ResultSet rs = st.executeQuery();
                ArrayList<String> moves = new ArrayList<String>();
                while(rs.next())
                {
                    StringBuilder str = new StringBuilder();
                    if("".equals(rs.getString("color")))
                        str.append("Blanco mueve");
                    else
                        str.append("Negro mueve");
                    int piezaMov = rs.getInt("pieza");
                    int piezaCom = rs.getInt("come");
                    int piezaEvo = rs.getInt("evoluciona");
                    str.append(" " + Utils.piezas.get(piezaMov - 1) + " desde "+ rs.getString("letrainicial") + rs.getString("numeroinicial"));
                    str.append(" hasta " + rs.getString("letrafinal") + rs.getString("numerofinal"));
                    if(piezaCom != 0)
                        str.append(" y come " + Utils.piezas.get(piezaCom - 1));
                    if(piezaEvo != 0)
                        str.append(". " + Utils.piezas.get(piezaMov - 1) + " evoluciona en " + Utils.piezas.get(piezaEvo - 1));
                    if(rs.getInt("jaque") != 0)
                        str.append(". Jaque");
                    if(rs.getInt("jaquemate") != 0)
                        str.append(" mate");
                    str.append(".");
                    moves.add(str.toString());
                }
                rs.close();
                st.close();
                st = con.prepareStatement("SELECT * FROM detallesjugadorpartido INNER JOIN usuarios on detallesjugadorpartido.jugador = usuarios.id WHERE detallesjugadorpartido.partido=?");
                st.setString(1, partido);
                rs = st.executeQuery();
                ArrayList<String> jugadores = new ArrayList<String>();
                while(rs.next())
                {
                    StringBuilder str = new StringBuilder();
                    String jugador = rs.getString("usuarios.username");
                    int resultado = rs.getInt("detallesjugadorpartido.resultado");
                    int color = rs.getInt("detallesjugadorpartido.color");
                    
                    str.append(jugador + " (");
                    if(color == 0)
                        str.append("Blanco)");
                    else
                        str.append("Negro)");
                    if(resultado == 0)
                        str.append(" DERROTA");
                    else if(resultado == 1)
                        str.append(" VICTORIA");
                    else
                        str.append(" ESTABLO");
                    jugadores.add(str.toString());                    
                }
                rs.close();
                st.close();
                con.close();
                createHistoryPage(res, moves, jugadores);
                return;
            }
            else
            {
                createServerErrorPage(res);
            }
        }
        catch(Exception e)
        {
            createServerErrorPage(res);
        }
    }
    
    public void createHistoryPage(HttpServletResponse res, ArrayList<String> moves, ArrayList<String> jugadores) throws IOException, ServletException
    {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
        out.println("<title>STGI AJEDREZ</title>");
        out.println("</HEAD>");
        out.println("<BODY>");
        out.println("<H1 style=\"text-align:center;\">STGI AJEDREZ</H1>");
        out.println("<H4 style=\"text-align:center;\">Baiamonte Giacomo, Pirrello Dario, Tarantino Giovanni Luca</H4>");
        out.println("<div align=\"center\">");
        out.println("<a href=\"/STGI/profile\">Volver</a>");
        out.println("</div>");
        out.println("<br>");
        out.println("<table align=\"center\">");
        
        for(int i = 0; i < jugadores.size(); ++i)
        {
            out.println("<tr>");
            out.println("<td align = \"center\">");
            out.println(jugadores.get(i));
            out.println("</td>");
            out.println("</tr>");
        }

        out.println("</table>");
        
        out.println("<br>");
        
        out.println("<table align=\"center\">");
        for(int i = 0; i < moves.size(); ++i)
        {
            out.println("<tr>");
            out.println("<td>");
            out.println(moves.get(i));
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }
    
    public void createServerErrorPage(HttpServletResponse res) throws IOException
    {
        res.setContentType("text/html");
        
        PrintWriter out = res.getWriter();
        out.println("<HTML><BODY>SERVER ERROR!</BODY></HTML>");
        out.close();
    }
}