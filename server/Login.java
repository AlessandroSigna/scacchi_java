import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.Strings;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        HttpSession session = req.getSession(false);
        if(session == null)
            createLoginPage(res);
        else
            res.sendRedirect("/STGI");
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        String user = req.getParameter("usuario");
        String passwd = req.getParameter("contrasena");

        PrintWriter out = res.getWriter();        
        SHA256Digest sd = new SHA256Digest();
        byte[] passwdByte = passwd.getBytes();
        sd.update(passwdByte, 0, passwdByte.length);
        byte[] passwdByteSHA256 = new byte[sd.getDigestSize()];
        sd.doFinal(passwdByteSHA256, 0);
        
        String passwdSHA256 = Utils.toHex(passwdByteSHA256, passwdByteSHA256.length);
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(Utils.dbUrl, Utils.dbUser, Utils.dbPaswd);
            if(con != null)
            {
                PreparedStatement st = con.prepareStatement("SELECT * FROM usuarios WHERE username=? AND password=?");
                st.setString(1, user);
                st.setString(2, passwdSHA256);
                ResultSet rs = st.executeQuery();
                if(rs.next())
                {
                    String id = rs.getString("id");
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    HttpSession session = req.getSession();
                    session.setAttribute("id", id);
                    session.setAttribute("user", user);
                    session.setAttribute("nombre", nombre);
                    session.setAttribute("apellido", apellido);
                    rs.close();
                    st.close();
                    con.close();
                    out.write("0");
                    //res.sendRedirect("/STGI");
                    //return;
                }
                else
                {
                    //modificare
                    //createLoginErrorPage(res);
                    rs.close();
                    st.close();
                    con.close();
                    out.write("1");
                    //return;
                }
            }
            else
            {
                out.write("2");
                //createServerErrorPage(res);
                //return;
            }
        }
        catch(Exception e)
        {
            out.write("2");
            //createServerErrorPage(res);
            //return;
        }
        out.close();
    }
    
    public void createLoginPage(HttpServletResponse res) throws IOException, ServletException
    {
        res.setContentType("text/html");
        

        String filename = "/WEB-INF/staticPages/login.txt";
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
    
    public void createLoginErrorPage(HttpServletResponse res) throws IOException, ServletException
    {
        res.setContentType("text/html");        

        String filename = "/WEB-INF/staticPages/loginerror.txt";
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