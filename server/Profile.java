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

public class Profile extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        HttpSession session = req.getSession(false);
        if(session == null)
        {
            res.sendRedirect("/STGI");
            return;
        }
        String id = (String) session.getAttribute("id");
        String username = (String) session.getAttribute("user");
        String nombre = (String) session.getAttribute("nombre");
        String apellido = (String) session.getAttribute("apellido");
        String fecha, jugados, victorias, derrotas, establos;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(Utils.dbUrl, Utils.dbUser, Utils.dbPaswd);
            if(con != null)
            {
                PreparedStatement st = con.prepareStatement("SELECT fecha FROM usuarios WHERE id=?");
                st.setString(1, id);
                ResultSet rs = st.executeQuery();
                rs.next();
                fecha = rs.getString("fecha");
                rs.close();
                st.close();
                st = con.prepareStatement("SELECT * FROM estatisticasusuarios WHERE usuario=?");
                st.setString(1, id);
                rs = st.executeQuery();
                rs.next();
                jugados = rs.getString("jugados");
                victorias = rs.getString("victorias");
                derrotas = rs.getString("derrotas");
                establos = rs.getString("establos");
                rs.close();
                st.close();
                st = con.prepareStatement("SELECT * FROM usuarios INNER JOIN (detallesjugadorpartido INNER JOIN listadopartidos on detallesjugadorpartido.partido = listadopartidos.id) on usuarios.id = detallesjugadorpartido.jugador where jugador<>? AND partido IN(SELECT detallesjugadorpartido.partido FROM detallesjugadorpartido INNER JOIN listadopartidos on detallesjugadorpartido.partido = listadopartidos.id WHERE listadopartidos.terminato = TRUE AND detallesjugadorpartido.jugador = ?)");
                st.setString(1, id);
                st.setString(2, id);
                rs = st.executeQuery();
                ArrayList<ArrayList<String>> historico = new ArrayList<ArrayList<String>>();
                while(rs.next())
                {
                    ArrayList<String> partido = new ArrayList<String>();
                    partido.add(rs.getString("usuarios.username"));
                    if(rs.getInt("detallesjugadorpartido.color") == 1)
                        partido.add("Blanco");
                    else
                        partido.add("Negro");
                    int result = rs.getInt("detallesjugadorpartido.resultado");
                    if(result == 0)
                        partido.add("Victoria");
                    else if(result == 1)
                        partido.add("Derrota");
                    else
                        partido.add("Establo");
                    partido.add(rs.getString("listadopartidos.fecha"));
                    partido.add(rs.getString("detallesjugadorpartido.partido"));
                    historico.add(partido);
                }
                createPerfilPage(res, username, nombre, apellido, fecha, jugados, victorias, derrotas, establos, historico);
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
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        HttpSession session = req.getSession(false);
        PrintWriter out = res.getWriter();
        if(session == null)
            out.print("2");
        String id = (String) session.getAttribute("id");
        String code = req.getParameter("code");        
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(Utils.dbUrl, Utils.dbUser, Utils.dbPaswd);
            PreparedStatement st;
            ResultSet rs;
            if(con != null)
            {
                if("0".equals(code)) //edit name and surname
                {
                    String nombre = req.getParameter("nombre");
                    String apellido = req.getParameter("apellido");
                    st = con.prepareStatement("UPDATE usuarios SET nombre=?, apellido=? WHERE id=?");
                    st.setString(1, nombre);
                    st.setString(2, apellido);
                    st.setString(3, id);
                    int count = st.executeUpdate();
                    if(count > 0)
                    {
                        session.setAttribute("nombre", nombre);
                        session.setAttribute("apellido", apellido);
                        out.print("0");
                    }
                    else
                    {
                        out.print("3");
                    }
                }
                else if("1".equals(code)) //edit password
                {
                    String passVieja = req.getParameter("passVieja");
                    String passNueva = req.getParameter("passNueva");
                    
                    SHA256Digest sd = new SHA256Digest();
                    byte[] passViejaByte = passVieja.getBytes();
                    sd.update(passViejaByte, 0, passViejaByte.length);
                    byte[] passViejaByteSHA256 = new byte[sd.getDigestSize()];
                    sd.doFinal(passViejaByteSHA256, 0);
                    String passViejaSHA256 = Utils.toHex(passViejaByteSHA256, passViejaByteSHA256.length);
                    
                    byte[] passNuevaByte = passNueva.getBytes();
                    sd.update(passNuevaByte, 0, passNuevaByte.length);
                    byte[] passNuevaByteSHA256 = new byte[sd.getDigestSize()];
                    sd.doFinal(passNuevaByteSHA256, 0);
                    String passNuevaSHA256 = Utils.toHex(passNuevaByteSHA256, passNuevaByteSHA256.length);
                    
                    st = con.prepareStatement("UPDATE usuarios SET password=? WHERE id=? AND password=?");
                    st.setString(1, passNuevaSHA256);
                    st.setString(2, id);
                    st.setString(3, passViejaSHA256);
                    int count = st.executeUpdate();
                    if(count > 0)
                        out.print("0");
                    else
                        out.print("1");
                }
            }
            else
            {
                out.print("3");
            }
        }
        catch(Exception e)
        {
            out.print("3");
        }
        
        out.close();
    }
    
    public void createPerfilPage(HttpServletResponse res, String username, String nombre, String apellido, String fecha, String jugados, String victorias, String derrotas, String establos, ArrayList<ArrayList<String>> historico) throws IOException, ServletException
    {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
        out.println("<script src = \"http://code.jquery.com/jquery-1.11.1.min.js\"></script>");
       
        
        //FORM INTERACTION
        out.println("<script type=\"text/javascript\">");
        out.println("function show(elementId)");    
        out.println("{");
            out.println("if(elementId === \"editprofile\")");
            out.println("{");
                out.println("document.getElementById(\"edit\").style.display=\"none\"");
                out.println("document.getElementById(\"passworddiv\").style.display=\"none\"");
                out.println("document.getElementById(\"namediv\").style.display=\"block\"");
            out.println("}");
            out.println("else if(elementId === \"editpassword\")");
            out.println("{");
                out.println("document.getElementById(\"edit\").style.display=\"none\"");
                out.println("document.getElementById(\"namediv\").style.display=\"none\"");
                out.println("document.getElementById(\"passworddiv\").style.display=\"block\"");
            out.println("}");
            out.println("else");
            out.println("{");
                out.println("document.getElementById(\"nameform\").reset();");
                out.println("document.getElementById(\"passwordform\").reset();");
                out.println("document.getElementById(\"namediv\").style.display=\"none\"");
                out.println("document.getElementById(\"passworddiv\").style.display=\"none\"");
                out.println("document.getElementById(\"edit\").style.display=\"block\"");
            out.println("}");
        out.println("}");
        out.println("</script>");

        //EDIT NAME AND SURNAME AJAX FUNCTION
        out.println("<script>");
        out.println("$(function()");
        out.println("{");
        out.println("$(\"#nameform\").on(\"submit\", function(event)");
        out.println("{");
        out.println("event.preventDefault();");
        out.println("var formData = { 'code': 0, 'nombre': $('input[name=nombre]').val(), 'apellido': $('input[name=apellido]').val() };");
        out.println("$.ajax(");
        out.println("{");
        out.println("url: \"/STGI/profile\",");
        out.println("type: \"post\",");
        out.println("data: formData,");
        out.println("success: function(d)");
        out.println("{");
        out.println("if(d === \"0\")");
        out.println("{");
        out.println("$(\"#nombreapellido\").text($('input[name=nombre]').val() + \", \" + $('input[name=apellido]').val());");
        out.println("alert(\"Nombre y apellido actualizados\");");
        out.println("show(\"0\")");
        out.println("}");
        out.println("else");
        out.println("{");
        out.println("alert(\"Error del servidor\");");
        out.println("window.location = \"/STGI\"");
        out.println("}");
        out.println("}");
        out.println("});");
        out.println("});");
        out.println("})");          
        out.println("</script>");
        
        
        //EDIT PASSWORD AJAX FUNCTION
        out.println("<script>");
        out.println("$(function()");
        out.println("{");
        out.println("$(\"#passwordform\").on(\"submit\", function(event)");
        out.println("{");
        out.println("event.preventDefault();");
        out.println("var p1 = document.getElementById(\"pass1\").value;");
        out.println("var p2 = document.getElementById(\"pass2\").value;");
        out.println("var p3 = document.getElementById(\"pass3\").value;");
        out.println("if(p2 != p3)");
        out.println("{");
        out.println("alert(\"Las contraseñas no coinciden\");");
        out.println("}");
        out.println("else if(p1 == p2)");
        out.println("{");
        out.println("alert(\"La contraseña anterior y la nueva son las mismas\");");
        out.println("}");
        out.println("else");
        out.println("{");
        out.println("var formData = { 'code': 1, 'passVieja': $('input[name=pass1]').val(), 'passNueva': $('input[name=pass2]').val() };");
        out.println("$.ajax(");
        out.println("{");
        out.println("url: \"/STGI/profile\",");
        out.println("type: \"post\",");
        out.println("data: formData,");
        out.println("success: function(d)");
        out.println("{");
        out.println("if(d === \"0\")");
        out.println("{");
        out.println("alert(\"Contraseña actualizada!\");");
        out.println("show(\"0\")");
        out.println("}");
        out.println("else if(d === \"1\")");
        out.println("{");
        out.println("alert(\"La contraseña anterior no es válida\");");
        out.println("}");
        out.println("else");
        out.println("{");
        out.println("alert(\"Error del servidor\");");
        out.println("window.location = \"/STGI\"");
        out.println("}");
        out.println("}");
        out.println("});");
        out.println("}");
        out.println("});");
        out.println("})");          
        out.println("</script>");
        
        
        out.println("<title>STGI AJEDREZ</title>");
        out.println("</HEAD>");
        out.println("<BODY>");
        out.println("<H1 style=\"text-align:center;\">STGI AJEDREZ</H1>");
        out.println("<H4 style=\"text-align:center;\">Baiamonte Giacomo, Pirrello Dario, Tarantino Giovanni Luca</H4>");
        out.println("<H2 style=\"text-align:center;\">");
        out.print(username);
        out.println("</H2>");
        out.println("<H3 id=\"nombreapellido\" name=\"nombreapellido\" style=\"text-align:center;\">");
        out.print(nombre);
        out.print(", ");
        out.print(apellido);
        out.println("</H3>");
        out.println("<H4 style=\"text-align:center;\">Registrado el ");
        out.print(fecha);
        out.println("</H4>");
        out.println("<div id=\"edit\" align=\"center\">");
        out.println("<button id=\"editprofile\" onclick=\"show('editprofile')\">Editar detalles</button><br><br>");
        out.println("<button id=\"editpassword\" onclick=\"show('editpassword')\">Cambiar contraseña</button><br><br>");
        out.println("<button onclick=\"window.location.href='/STGI'\">Volver a la página de inicio</button><br><br>");
        
        out.println("<table align=\"center\">");
        out.println("<tr>");
        out.println("<td>Jugados</td>");
        out.println("<td>Ganados</td>");
        out.println("<td>Perdidos</td>");
        out.println("<td>Establos</td>");
        out.println("</tr>");
        out.println("<tr>");
        out.print("<td align=\"center\">");
        out.print(jugados);
        out.println("</td>");
        out.println("<td align=\"center\">");
        out.print(victorias);
        out.println("</td>");
        out.print("<td align=\"center\">");
        out.print(derrotas);  
        out.println("</td>");
        out.print("<td align=\"center\">");
        out.print(establos);
        out.println("</td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("<H4 style=\"text-align:center;\">Historico</H4>");
        if(historico.isEmpty())
            out.println("<H4 style=\"text-align:center;\">Vacio</H4>");
        else
        {
            out.println("<table align=\"center\">");
            out.println("<tr>");
            out.println("<td align=\"center\">Adversario</td>");
            out.println("<td align=\"center\">Color</td>");
            out.println("<td>Resultado</td>");
            out.println("<td align=\"center\">Fecha</td>");
            out.println("<td align=\"center\">Detalles</td>");
            out.println("</tr>");
            for(int i = 0; i < historico.size(); ++i)
            {
                ArrayList<String> partido = historico.get(i);
                out.println("<tr>");
                int j;
                for(j = 0; j < partido.size()-1; ++j)
                    out.println("<td align=\"center\">" + partido.get(j) + "</td>");
                out.println("<td align=\"center\"><a href=\"/STGI/history?id=" + partido.get(j) + "\">Ver</a></td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }
        out.println("</div>");
        
        out.println("<div id=\"namediv\" align=\"center\" style=\"display:none\">");
        out.println("<form id=\"nameform\" name=\"nameform\" method=\"post\">");
    out.println("Nombre:<br>");
    out.println("<input type=\"text\" id=\"nombre\" name=\"nombre\" pattern=\"[a-zA-Z]+\" title=\"Solamente letras\" required/><br>");
    out.println("Apellido:<br>");
    out.println("<input type=\"text\" id=\"apellido\" name=\"apellido\" pattern=\"[a-zA-Z]+\" title=\"Solamente letras\" required/><br>");
    out.println("<input type=\"submit\" value=\"Actualizar\"/>");
    out.println("</form>");
        out.println("<button id=\"default\" onclick=\"show('default')\">Cancelar</button><br>");
        out.println("</div>");
        
        
        out.println("<div id=\"passworddiv\" align=\"center\" style=\"display:none\">");
        out.println("<form id=\"passwordform\" name=\"passwordform\" method=\"post\">");
    out.println("Insertar contraseña vieja:<br>");
    out.println("<input type=\"password\" id=\"pass1\" name=\"pass1\" pattern=\"[a-zA-Z0-9]+\" title=\"Solamente letras y numeros\" required/><br>");
    out.println("Insertar contraseña nueva:<br>");
    out.println("<input type=\"password\" id=\"pass2\" name=\"pass2\" pattern=\"[a-zA-Z0-9]+\" title=\"Solamente letras y numeros\" required/><br>");
    out.println("Repetir contraseña nueva:<br>");
    out.println("<input type=\"password\" id=\"pass3\" name=\"pass3\" pattern=\"[a-zA-Z0-9]+\" title=\"Solamente letras y numeros\" required/><br>");
    out.println("<input type=\"submit\" value=\"Editar contraseña\"/>");
    out.println("</form>");
        out.println("<button id=\"default\" onclick=\"show('default')\">Cancelar</button><br>");
        out.println("</div>");
        
        out.println("</BODY>");
        out.println("</HTML>");
    }
        
    public void createServerErrorPage(HttpServletResponse res) throws IOException
    {
        res.setContentType("text/html");
        
        PrintWriter out = res.getWriter();
        out.println("<HTML><BODY>SERVER ERROR!</BODY></HTML>");
        out.close();
    }
}