import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.Strings;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;


public class Signup extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        HttpSession session = req.getSession(false);
        if(session == null)
            createSignupPage(res);
        else
            res.sendRedirect("/STGI");
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        String user = req.getParameter("usuario");
        String passwd = req.getParameter("contrasena1");
        
        PrintWriter out = res.getWriter();        
        
        SHA256Digest sd = new SHA256Digest();
        byte[] passwdByte = passwd.getBytes();
        sd.update(passwdByte, 0, passwdByte.length);
        byte[] passwdByteSHA256 = new byte[sd.getDigestSize()];
        sd.doFinal(passwdByteSHA256, 0);
        
        String passwdSHA256 = Utils.toHex(passwdByteSHA256, passwdByteSHA256.length);
        
        Connection con;
        PreparedStatement st;
        ResultSet rs;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(Utils.dbUrl, Utils.dbUser, Utils.dbPaswd);
            st = con.prepareStatement("INSERT INTO usuarios(username,password,nombre,apellido) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1,user);
            st.setString(2,passwdSHA256);
            st.setString(3, user);
            st.setString(4, user);
            st.executeUpdate();
            rs = st.getGeneratedKeys();
            if(!rs.next())
            {
                rs.close();
                st.close();
                con.close();
                out.write("1");
                return;
            }
            String id = rs.getString(1);
            rs.close();
            st.close();
            st = con.prepareStatement("INSERT INTO estatisticasusuarios(usuario) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, id);
            st.executeUpdate();
            st.close();
            con.close();
            HttpSession session = req.getSession();
            session.setAttribute("id", id);
            session.setAttribute("user", user);
            session.setAttribute("nombre", user);
            session.setAttribute("apellido", user);
            out.write("0");
        }
        catch(Exception e)
        {
            out.write("2");
        }
        out.close();
    }
    
    public void createSignupPage(HttpServletResponse res) throws IOException, ServletException
    {
        res.setContentType("text/html");
       
        String filename = "/WEB-INF/staticPages/signup.txt";
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
    
    public void createSignupErrorPage(HttpServletResponse res) throws IOException, ServletException
    {
        res.setContentType("text/html");        

        String filename = "/WEB-INF/staticPages/signuperror.txt";
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
    
    public void createServerErrorPage(HttpServletResponse res) throws IOException, ServletException
    {
        res.setContentType("text/html");
        
        PrintWriter out = res.getWriter();
        out.println("<HTML><BODY>SERVER ERROR!</BODY></HTML>");
        out.close();
    }
    
    /*public void createServerErrorPage(HttpServletResponse res, Exception e) throws IOException, ServletException
    {
        res.setContentType("text/html");
        
        PrintWriter out = res.getWriter();
        out.println(e);
        out.close();
    }*/
}