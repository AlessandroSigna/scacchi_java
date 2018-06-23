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

public class Dashboard extends HttpServlet
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
        ArrayList<ArrayList<String>> partidosDisponibles = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> partidosEnCurso = new ArrayList<ArrayList<String>>();
        ArrayList<String> partidosApiertos = new ArrayList<String>();
        if(getDBData(id, partidosDisponibles, partidosEnCurso, partidosApiertos))
            createDashboardPage(res, username, id, partidosDisponibles, partidosEnCurso, partidosApiertos);
        else
            createServerErrorPage(res);
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        HttpSession session = req.getSession(false);
        PrintWriter out = res.getWriter();        
        if(session == null)
        {
            out.print("1");
            out.close();
            return;
        }
        String id = (String) session.getAttribute("id");
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
                    if("0".equals(code))
                    {
                        String color = req.getParameter("color");
                        st = con.prepareStatement("INSERT INTO partidosdisponibles (jugador, color) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                        st.setString(1, id);
                        st.setString(2, color);
                        st.executeUpdate();
                        rs = st.getGeneratedKeys();
                        String result;
                        if(!rs.next())
                            result = "1";
                        else
                            result = "0";
                        rs.close();
                        st.close();
                        con.close();
                        out.write(result);
                    }
                    else if("1".equals(code))
                    {
                        String username = (String) session.getAttribute("user");
                        ArrayList<ArrayList<String>> partidosDisponibles = new ArrayList<ArrayList<String>>();
                        ArrayList<ArrayList<String>> partidosEnCurso = new ArrayList<ArrayList<String>>();
                        ArrayList<String> partidosApiertos = new ArrayList<String>();
                        if(!getDBData(id, partidosDisponibles, partidosEnCurso, partidosApiertos))
                        {
                            out.print("1");
                            out.close();
                            return;
                        }
                        //CREO IL JSON DI RITORNO
                        JSONObject result = new JSONObject();
                        JSONArray disponibles = new JSONArray();
                        JSONArray opened = new JSONArray();
                        JSONArray started = new JSONArray();
                        for(int i = 0; i < partidosDisponibles.size(); ++i)
                        {
                            JSONObject tmp = new JSONObject();
                            ArrayList<String> tmp2 = partidosDisponibles.get(i);
                            tmp.put("usuario", tmp2.get(0));
                            tmp.put("color", tmp2.get(1));
                            tmp.put("partido", tmp2.get(2));
                            disponibles.put(tmp);
                        }
                        result.put("ndisponibles", partidosDisponibles.size());
                        result.put("disponibles", disponibles);
                        
                        for(int i = 0; i < partidosApiertos.size(); ++i)
                        {
                            JSONObject tmp = new JSONObject();
                            String tmp2 = partidosApiertos.get(i);
                            tmp.put("color", tmp2);
                            opened.put(tmp);
                        }
                        result.put("nopened", partidosApiertos.size());
                        result.put("opened", opened);
                        
                        for(int i = 0; i < partidosEnCurso.size(); ++i)
                        {
                            JSONObject tmp = new JSONObject();
                            ArrayList<String> tmp2 = partidosEnCurso.get(i);
                            tmp.put("usuario", tmp2.get(0));
                            tmp.put("turno", tmp2.get(1));
                            tmp.put("partido", tmp2.get(2));
                            started.put(tmp);
                        }
                        result.put("started", started);
                        result.put("nstarted", partidosEnCurso.size());
                        out.print(result.toString());
                    }
                    else if("2".equals(code))
                    {
                        session.invalidate();
                        out.print("0");
                        out.close();
                        return;
                    }
                    else if("3".equals(code))
                    {
                        
                        try
                        {
                            String partido = (String) req.getParameter("partido");
                            con.setAutoCommit(false);
                            st = con.prepareStatement("SELECT * FROM partidosdisponibles WHERE id=?");
                            st.setString(1, partido);
                            rs = st.executeQuery();
                            rs.next();
                            String jugador = rs.getString("jugador");
                            int color = rs.getInt("color");
                            rs.close();
                            st.close();
                            st = con.prepareStatement("DELETE FROM partidosdisponibles WHERE id=?");
                            st.setString(1, partido);
                            st.executeUpdate();
                            String j1, j2;
                            if(color == 0)
                            {
                                j1 = jugador;
                                j2 = id;
                            }
                            else
                            {
                                j1 = id;
                                j2 = jugador;
                            }
                            st.close();
                            st = con.prepareStatement("INSERT INTO listadopartidos (turno) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                            st.setString(1, j1);
                            st.executeUpdate();
                            rs = st.getGeneratedKeys();
                            rs.next();
                            String idNuevoPartido = rs.getString(1);
                            rs.close();
                            st.close();
                            st = con.prepareStatement("INSERT INTO detallesjugadorpartido (partido, jugador, color) VALUES (?, ?, ?)");
                            st.setString(1, idNuevoPartido);
                            st.setString(2, j1);
                            st.setString(3, "0");
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("INSERT INTO detallesjugadorpartido (partido, jugador, color) VALUES (?, ?, ?)");
                            st.setString(1, idNuevoPartido);
                            st.setString(2, j2);
                            st.setString(3, "1");
                            st.executeUpdate();
                            st.close();
                            st = con.prepareStatement("INSERT INTO tablapartido (partido, pieza, letra, numero, color) VALUES (?,?,?,?,?)");
                            st.setString(1, idNuevoPartido);
                            //Inserto Peones
                            st.setString(2, "1");
                            st.setString(4, "2");
                            st.setString(5, "0");
                            for(int i = 0; i < 8; ++i)
                            {
                                char c = 'A';
                                c += i;
                                st.setString(3, Character.toString(c));
                                st.executeUpdate();
                            }
                            st.setString(4, "7");
                            st.setString(5, "1");
                            for(int i = 0; i < 8; ++i)
                            {
                                char c = 'A';
                                c += i;
                                st.setString(3, Character.toString(c));
                                st.executeUpdate();
                            }
                            //Inserto Torres
                            st.setString(2, "2");
                            st.setString(5, "0");
                            st.setString(4, "1");
                            st.setString(3, "A");
                            st.executeUpdate();
                            st.setString(3, "H");
                            st.executeUpdate();
                            st.setString(5, "1");
                            st.setString(4, "8");
                            st.executeUpdate();
                            st.setString(3, "A");
                            st.executeUpdate();
                            //Inserto Caballos
                            st.setString(2, "3");
                            st.setString(5, "0");
                            st.setString(4, "1");
                            st.setString(3, "B");
                            st.executeUpdate();
                            st.setString(3, "G");
                            st.executeUpdate();
                            st.setString(5, "1");
                            st.setString(4, "8");
                            st.executeUpdate();
                            st.setString(3, "B");
                            st.executeUpdate();
                            //Inserto Alfies
                            st.setString(2, "4");
                            st.setString(5, "0");
                            st.setString(4, "1");
                            st.setString(3, "C");
                            st.executeUpdate();
                            st.setString(3, "F");
                            st.executeUpdate();
                            st.setString(5, "1");
                            st.setString(4, "8");
                            st.executeUpdate();
                            st.setString(3, "C");
                            st.executeUpdate();
                            //Inserto Damas
                            st.setString(5, "0");
                            st.setString(2, "5");
                            st.setString(4, "1");
                            st.setString(3, "D");
                            st.executeUpdate();
                            st.setString(5, "1");
                            st.setString(4, "8");
                            st.executeUpdate();
                            //Inserto Reys
                            st.setString(5, "0");
                            st.setString(2, "6");
                            st.setString(4, "1");
                            st.setString(3, "E");
                            st.executeUpdate();
                            st.setString(5, "1");
                            st.setString(4, "8");
                            st.executeUpdate();
                            st.close();
                            con.commit();
                            con.setAutoCommit(true);
                            con.close();
                            JSONObject jobj = new JSONObject();
                            jobj.put("newgame", idNuevoPartido);
                            out.print(jobj.toString());
                        }
                        catch(Exception e)
                        {
                            if(!con.getAutoCommit())
                            {
                                con.rollback();
                                out.print("2");
                                return;
                            }
                        }
                    }
                }
                else
                {
                    out.print("1");
                }
                out.close();
                return;            
        }
        catch(Exception e)
        {
            out.print(e.toString());
            //out.print("1");
        }
        out.close();
    }
    
    public boolean getDBData(String id, ArrayList<ArrayList<String>> partidosDisponibles, ArrayList<ArrayList<String>> partidosEnCurso, ArrayList<String> partidosApiertos)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(Utils.dbUrl, Utils.dbUser, Utils.dbPaswd);
            if(con != null)
            {
                PreparedStatement st = con.prepareStatement("SELECT * FROM partidosdisponibles INNER JOIN usuarios on partidosdisponibles.jugador = usuarios.id WHERE jugador<>?");
                st.setString(1, id);
                ResultSet rs = st.executeQuery();
                while(rs.next())
                {
                    ArrayList<String> tmp = new ArrayList<String>();
                    String usuario = rs.getString("usuarios.username");
                    String partido = rs.getString("partidosdisponibles.id");
                    String color;
                    if(rs.getInt("partidosdisponibles.color") == 0)
                        color = "Negro";
                    else
                        color = "Blanco";
                    tmp.add(usuario);
                    tmp.add(color);
                    tmp.add(partido);
                    partidosDisponibles.add(tmp);
                }
                rs.close();
                st.close();
                st = con.prepareStatement("SELECT * FROM listadopartidos INNER JOIN ( detallesjugadorpartido INNER JOIN usuarios ON detallesjugadorpartido.jugador = usuarios.id) ON detallesjugadorpartido.partido = listadopartidos.id WHERE listadopartidos.terminato=0 AND detallesjugadorpartido.jugador<>?");
                st.setString(1, id);
                rs = st.executeQuery();
                while(rs.next())
                {
                    ArrayList<String> tmp = new ArrayList<String>();
                    String usuario = rs.getString("usuarios.username");
                    String partido = rs.getString("listadopartidos.id");
                    String turno;
                    if(id.equals(rs.getString("listadopartidos.turno")))
                        turno = "0";
                    else
                        turno = "1";
                    tmp.add(usuario);
                    tmp.add(turno);
                    tmp.add(partido);
                    partidosEnCurso.add(tmp);
                }
                rs.close();
                st.close();
                st = con.prepareStatement("SELECT * FROM partidosdisponibles WHERE jugador=?");
                st.setString(1, id);
                rs = st.executeQuery();
                while(rs.next())
                {
                    int color = rs.getInt("color");
                    if(color == 0)
                        partidosApiertos.add("Blanco");
                    else
                        partidosApiertos.add("Negro");
                }
                rs.close();
                st.close();
                con.close();
                return true;
            }   
        }
        catch(Exception e)
        {
            return false;
        }
        return false;
    }
    
    public void createDashboardPage(HttpServletResponse res, String username, String userID, ArrayList<ArrayList<String>> partidosDisponibles, ArrayList<ArrayList<String>> partidosEnCurso, ArrayList<String> partidosApiertos) throws IOException, ServletException
    {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
        
        out.println("<script src = \"http://code.jquery.com/jquery-1.11.1.min.js\"></script>");
        
        //SHOW/HYDE Create Game Section
        out.println("<script type=\"text/javascript\">");
        out.println("function show(elementId)");    
        out.println("{");
            out.println("if(elementId === \"0\")");
            out.println("{");
                out.println("document.getElementById(\"divtables\").style.display=\"none\"");
                out.println("document.getElementById(\"divinteraction\").style.display=\"none\"");
                out.println("document.getElementById(\"divnewgame\").style.display=\"block\"");
            out.println("}");
            out.println("else");
            out.println("{");
                out.println("document.getElementById(\"divtables\").style.display=\"block\"");
                out.println("document.getElementById(\"divinteraction\").style.display=\"block\"");
                out.println("document.getElementById(\"divnewgame\").style.display=\"none\"");
            out.println("}");
        out.println("}");
        out.println("</script>");

        //CreateGame
        out.println("<script>");
        out.println("$(function()");
        out.println("{");
        out.println("$(\"#newgame\").on(\"submit\", function(event)");
        out.println("{");
        out.println("event.preventDefault();");
        out.println("var c = document.forms.newgame.color.value;");
        out.print("var formData = { 'code': 0, 'color': c };");
        out.println("$.ajax(");
        out.println("{");
        out.println("url: \"/STGI/\",");
        out.println("type: \"post\",");
        out.println("data: formData,");
        out.println("success: function(d)");
        out.println("{");
        out.println("if(d === \"0\")");
        out.println("{");
        out.println("alert(\"Partido abierto correctamente\");");
        out.println("show(\"1\");");
        out.println("refresh();");
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
        
        
        
        //REFRESH PAGE
        out.println("<script type=\"text/javascript\">");
        out.println("function refresh()");    
        out.println("{");
            out.print("var formData = { 'code': 1 };");
            out.println("$.ajax(");
            out.println("{");
                out.println("url: \"/STGI/\",");
                out.println("type: \"post\",");
                out.println("data: formData,");
                out.println("success: function(d)");
                out.println("{");
                    out.println("if(d === \"1\")");
                    out.println("{");
                        out.println("alert(\"Error del servidor\");");
                        out.println("window.location = \"/STGI\"");
                    out.println("}");
                    out.println("else");
                    out.println("{");
                        out.println("var tableDisponibles = document.getElementById(\"tableDisponibles\");");
                        out.println("var tableOpened = document.getElementById(\"tableOpened\");");
                        out.println("var tableGame = document.getElementById(\"tableGame\");");
                                                
                        //FLUSH TableDisponibles
                        out.println("var rowCount = tableDisponibles.rows.length;");
                        out.println("for(var x = rowCount - 1; x > 0; x--)");
                        out.println("{");
                            out.println("tableDisponibles.deleteRow(x);");
                        out.println("}");
                        
                        //FLUSH TableOpened
                        out.println("rowCount = tableOpened.rows.length;");
                        out.println("for(var x = rowCount - 1; x > 0; x--)");
                        out.println("{");
                            out.println("tableOpened.deleteRow(x);");
                        out.println("}");
                        
                        //FLUSH TableGame
                        out.println("var rowCount = tableGame.rows.length;");
                        out.println("for(var x = rowCount - 1; x > 0; x--)");
                        out.println("{");
                            out.println("tableGame.deleteRow(x);");
                        out.println("}");
                        
                        

                        out.println("var result = JSON.parse(d);");
                        //CREATE TABLE DISPONIBLES
                        out.println("var ndisponibles = result.ndisponibles;");
                        out.println("for(var i = 0; i < ndisponibles; ++i)");
                        out.println("{");
                            out.println("var usuario = result.disponibles[i].usuario;");    
                            out.println("var color = result.disponibles[i].color;");    
                            out.println("var partido = result.disponibles[i].partido;");
                            out.println("var row = tableDisponibles.insertRow(i+1);");
                            out.println("var cell1 = row.insertCell(0);");
                            out.println("var cell2 = row.insertCell(1);");
                            out.println("var cell3 = row.insertCell(2);");
                            out.println("cell1.innerHTML = usuario;");
                            out.println("cell2.innerHTML = color;");
                            out.println("cell3.innerHTML = '<button onClick=agregarse(' + partido + ')>Juega!</button>';");
                        out.println("}");
                        
                        //CREATE TABLE OPENED
                        out.println("var nopened = result.nopened;");
                        out.println("for(var i = 0; i < nopened; ++i)");
                        out.println("{");
                            out.println("var color = result.opened[i].color");
                            out.println("var row = tableOpened.insertRow(i+1);");
                            out.println("var cell1 = row.insertCell(0);");
                            out.println("cell1.innerHTML = color;");
                        out.println("}");
                        
                        //CREATE TABLE GAME
                        out.println("var nstarted = result.nstarted;");
                        out.println("for(var i = 0; i < nstarted; ++i)");
                        out.println("{");
                            out.println("var usuario = result.started[i].usuario");
                            out.println("var turno = result.started[i].turno;");    
                            out.println("var partido = result.started[i].partido;");
                            out.println("var row = tableGame.insertRow(i+1);");
                            out.println("var cell1 = row.insertCell(0);");
                            out.println("var cell2 = row.insertCell(1);");
                            out.println("var cell3 = row.insertCell(2);");
                            out.println("cell1.innerHTML = usuario;");
                            out.println("cell2.innerHTML = turno;");
                            
                            out.println("var v0 = '<button onClick=\"';");
                            out.println("var v1 = \"location.href='/STGI/game?partido=\";");
                            out.println("var r0 = v0.concat(v1);");
                            out.println("var v2 = partido + \"'\"");
                            out.println("var r1 = r0.concat(v2);");
                            out.println("var v3 = '\">Juega!</button>'");
                            out.println("var r2 = r1.concat(v3);");
                           
                            out.println("cell3.innerHTML = r2;");
                        out.println("}");
                    out.println("}");
                out.println("}");
             out.println("});");
        out.println("}");
        out.println("</script>");
        
        //LOGOUT FUNCTION
        out.println("<script type=\"text/javascript\">");
        out.println("function logout()");    
        out.println("{");
            out.print("var formData = { 'code': 2 };");
            out.println("$.ajax(");
            out.println("{");
                out.println("url: \"/STGI/\",");
                out.println("type: \"post\",");
                out.println("data: formData,");
                out.println("success: function(d)");
                out.println("{");
                    out.println("if(d === \"1\")");
                    out.println("{");
                        out.println("alert(\"Error del servidor\");");
                        out.println("window.location = \"/STGI\"");
                    out.println("}");
                    out.println("else");
                    out.println("{");
                        out.println("window.location = \"/STGI\"");
                    out.println("}");
                out.println("}");
             out.println("});");
        out.println("}");
        out.println("</script>");
        
        //JOIN GAME FUNCTION
        out.println("<script type=\"text/javascript\">");
        out.println("function agregarse(partido)");    
        out.println("{");
            out.print("var formData = { 'code': 3, 'partido': partido };");
            //out.println("alert(\"DENTRO!\");");
            out.println("$.ajax(");
            out.println("{");
                out.println("url: \"/STGI/\",");
                out.println("type: \"post\",");
                out.println("data: formData,");
                out.println("success: function(d)");
                out.println("{");
                    //out.println("alert(d);");
                    out.println("if(d === \"1\")");
                    out.println("{");
                        out.println("alert(\"Error del servidor\");");
                        out.println("window.location = \"/STGI\"");
                    out.println("}");
                    out.println("else if(d === \"2\")");
                    out.println("{");
                        out.println("alert(\"Error: acceso simultáneo a la base de datos\");");
                        out.println("refresh();");
                    out.println("}");
                    out.println("else");
                    out.println("{");
                        out.println("var result = JSON.parse(d);");
                        out.println("var newgame = result.newgame;");
                        out.println("alert(\"Agregado!\");");
                        out.println("var url = '/STGI/game?partido=' + newgame;");
                        out.println("window.location=url;");
                    out.println("}");
                out.println("}");
             out.println("});");
        out.println("}");
        out.println("</script>");
        
        out.println("<title>STGI AJEDREZ</title>");
        out.println("</HEAD>");
        out.println("<BODY>");
        out.println("<H1 style=\"text-align:center;\">STGI AJEDREZ</H1>");
        out.println("<H4 style=\"text-align:center;\">Baiamonte Giacomo, Pirrello Dario, Tarantino Giovanni Luca</H4>");
        out.println("<H2 style=\"text-align:center;\">");
        out.println("DASHBOARD");
        out.println("</H2>");
        out.println("<H2 style=\"text-align:center;\">");
        out.print(username);
        out.println("</H2>");
        out.println("<div id=\"divinteraction\" name=\"divinteraction\" align=\"center\">");
        out.println("<button onClick=\"location.href ='/STGI/profile';\">Ver detalles perfil</button>");
        out.println("<button onClick=\"logout()\">Disconectarse</button><br><br>");
        out.println("<button id=\"creategame\" name=\"creategame\" onClick=\"show('0')\">Aprir Nuevo Partido</button>");
        out.println("</div>");
        
        //CreateGame
        out.println("<div id=\"divnewgame\" align=\"center\" style=\"display:none\">");
        out.println("<form id=\"newgame\" name=\"newgame\" method=\"post\">");
        out.println("Seleccionar color:<br>");
        out.println("<input type=\"radio\" id=\"color\" name=\"color\" value=\"0\" checked>Blanco</input>");
        out.println("<input type=\"radio\" id=\"color\" name=\"color\" value=\"1\" >Negro</input><br>");
        out.println("<input type=\"submit\" value=\"Aprir Partido\"/>");
        out.println("</form>");
        out.println("<button id=\"default\" onclick=\"show('1')\">Cancelar</button><br>");
        out.println("</div>");

        out.println("<div id=\"divtables\" name=\"divtables\" align=\"center\">");
        out.println("<div style=\"float: right\">");
        out.println("<h2>Partidos Disponible</h2>");
        out.println("<table id=\"tableDisponibles\" name=\tableDisponibles\"");
        out.println("<tr>");
        out.println("<th align=\"center\">");
        out.println("Adversario");
        out.println("</th>");
        out.println("<th align=\"center\">");
        out.println("Tu color");
        out.println("</th>");
        out.println("<th align=\"center\">");
        out.println("</th>");
        out.println("</tr>");
        for(int i = 0; i < partidosDisponibles.size(); ++i)
        {
            ArrayList<String> tmp = partidosDisponibles.get(i);
            out.println("<tr>");
            out.println("<td align=\"center\">");
            out.print(tmp.get(0));
            out.println("</td>");
            out.println("<td align=\"center\">");
            out.print(tmp.get(1));
            out.println("</td>");
            out.println("<td align=\"center\">");
            out.print("<button onClick=\"agregarse(" + tmp.get(2) + ")\">Juega!</button>");
            out.println("</td>");
            out.println("</tr>");
        }
        
        out.println("</table>");
        out.println("</div>");
        
        

        out.println("<div style=\"float: right\">");
        out.println("<h2>Esperando Jugador</h2>");
        out.println("<table  id=\"tableOpened\" name=\"tableOpened\" style=\"float: left\"");
        out.println("<tr>");
        out.println("<th align=\"center\">");
        out.println("Color Eligido");
        out.println("</th>");
        out.println("</tr>");
        for(int i = 0; i < partidosApiertos.size(); ++i)
        {
            String tmp = partidosApiertos.get(i);
            out.println("<tr>");
            out.println("<td align=\"center\">");
            out.print(tmp);
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</div>");
        

        out.println("<div style=\"float: left\">");
        out.println("<h2>Partidos en progreso</h2>");
        out.println("<table id=\"tableGame\" name=\"tableGame\" style=\"float: left\"");
        out.println("<tr>");
        out.println("<th align=\"center\">");
        out.println("Adversario");
        out.println("</th>");
        out.println("<th align=\"center\">");
        out.println("Turno");
        out.println("</th>");
        out.println("<th align=\"center\">");
        out.println("</th>");
        out.println("</tr>");
        
        
        for(int i = 0; i < partidosEnCurso.size(); ++i)
        {
            ArrayList<String> tmp = partidosEnCurso.get(i);
            out.println("<tr>");
            out.println("<td align=\"center\">");
            out.print(tmp.get(0));
            out.println("</td>");
            out.println("<td align=\"center\">");
            out.print(tmp.get(1));
            out.println("</td>");
            out.println("<td align=\"center\">");
            out.print("<button onClick=\"location.href='/STGI/game?partido=" + tmp.get(2) + "'\">Juega!</button>");
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</div>");
        
        
        out.println("</div>");
        out.close();
    }
    
    public void createServerErrorPage(HttpServletResponse res) throws IOException
    {
        res.setContentType("text/html");
        
        PrintWriter out = res.getWriter();
        out.println("<HTML><BODY>SERVER ERROR!</BODY></HTML>");
        out.close();
    }
}