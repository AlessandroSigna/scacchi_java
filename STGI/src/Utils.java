import java.io.File;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class Utils
{
    private static final String DIGITS = "0123456789abcdef";
    private static final String MYCHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890,.-; :_ò@çà#°ù§+]*èé[{}'ì^?=)(/&%$£\"!\\|<>";
    public static final String dbUrl = "jdbc:mysql://127.0.0.1:3306/ajedrez?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String dbUser = "root";
    public static final String dbPaswd = "";
    
    public static final List<String> piezas = Arrays.asList("Peone", "Torre", "Caballo", "Alfile", "Dama", "Rey");
    
    public static String toHex(byte[] data, int length)
    {
        StringBuilder buf = new StringBuilder();
        
        for (int i = 0; i != length; i++)
        {
            buf.append(toHex(data[i]));
        }
        
        return buf.toString();
    }
    
    public static String toHex(byte b)
    {
        int v = b & 0xff;
        StringBuilder sb = new StringBuilder();
        sb.append(DIGITS.charAt(v >> 4));
        sb.append(DIGITS.charAt(v & 0xf));
        return sb.toString();
    }
    
    public static void printByteString(byte[] array)
    {
        int i = 0;
        while (i < array.length)
        {
            int j = 0;
            while(j < 8)
            {
                if(((array[i]<<j)&128) != 0)
                {
                    System.out.print("1");
                }
                else
                {
                    System.out.print("0");
                }
                j++;
            }
            i++;
            System.out.print(" ");
        }
        System.out.println();
    }
    
    public static void printByte(byte b)
    {
        int j = 0;
            while(j < 8)
            {
                if(((b<<j)&128) != 0)
                {
                    System.out.print("1");
                }
                else
                {
                    System.out.print("0");
                }
                j++;
            }
            System.out.println();
    }
        
    public static String generateString(int length)
    {
        Random rng = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = MYCHARSET.charAt(rng.nextInt(MYCHARSET.length()));
        }
        return new String(text);
    }
    
    public static byte[] fileToByteArray(File file)
    {
        byte[] b = new byte[(int) file.length()];
        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
          }
        catch (FileNotFoundException e)
        {
            System.out.println("File Not Found.");
            e.printStackTrace();
        }
        catch (IOException e1)
        {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }
        return b;
    }
}
