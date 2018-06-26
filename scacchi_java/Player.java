package scacchi_java;
import java.util.Scanner;

public class Player {

    private boolean white;
    private boolean turn;
    static boolean whiteChosen = false;

    public Player()
    {
        if (whiteChosen)
            white = turn = false;
        else
            white = turn = whiteChosen = true;
    }

    public Coord getCommand()
    {   //comando del click sulla pedina che si vuole muovere
        Scanner input = new Scanner(System.in);
        
        int x, y;
        
        System.out.println("X:");
        x=input.nextInt();
        
        System.out.println("Y");
        y=input.nextInt();
        
        Coord pos;

        pos = new Coord(x, y);
        
        return pos;

    }

    boolean validFormat(char[] command)
    {
        return true;
    }

    public boolean getColor()
    {
        return this.white;
    }
}