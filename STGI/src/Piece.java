 
import java.util.*;
public class Piece
{

    int type; //1 PEDONE; 2 TORRE; 3 CAVALLO; 4 ALFIERE; 5 REGINA; 6 RE
    private boolean color; //FALSE BIANCO; TRUE NERO;
    private boolean unmoved; //TRUE NON MOSSO; FALSE MOSSO;

    public Piece(int type, boolean color, boolean unmoved)
    {
       this.type = type;
        this.color = color;
        this.unmoved = unmoved;
    }

    public void setUnmoved(boolean unmoved)
    {
    	this.unmoved=unmoved;
        return;
    }

    public boolean getUnmoved()
    {
        return this.unmoved;
    }

    public void setType(int type)
    {
    	this.type=type;
        return;
    }

    public int getType()
    {
        return this.type;
    }

    public boolean getColor()
    {
        return this.color;
    }

    public String getCodeASCII()
    {
        Integer code;
        switch (type)
        {
            case 2:
                code = 9814;
            break;
        
            case 3:
                code = 9816;
            break;
        
            case 4:
                code = 9815;
            break;
        
            case 6:
                code = 9812;
            break;
        
            case 5:
                code = 9813;
            break;
        
            case 1:
                code = 9817;
            break;
            
            default:
                code = 0;
        }
        code = color?code+6:code; //il codice delle pedine nere e' +6 rispetto quello delle bianche
        return "&#" + code.toString() + ";"; 
    }
}