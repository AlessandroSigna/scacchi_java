
import java.util.ArrayList;

public class Main
{
    public static void main(String args[])
    {
        Board b = new Board();
        costruisciScacchiera(b);
        stampaScacchiera(b);
        ArrayList<ArrayList<Coord>> al = b.getMovesFilteredAllPieces(false);
        for(int i = 0; i < al.size(); ++i)
            stampaArrayList(al.get(i));
    }
    
    public static void costruisciScacchiera(Board b)
    {
        Piece pedone_bianco = new Piece(1, false, false);
        Piece torre_bianco = new Piece(2, false, false);
        Piece cavallo_bianco = new Piece(3, false, false);
        Piece alfiere_bianco = new Piece(4, false, false);
        Piece regina_bianco = new Piece(5, false, false);
        Piece re_bianco = new Piece(6, false, false);        
        Piece pedone_nero = new Piece(1, true, false);
        Piece torre_nero = new Piece(2, true, false);
        Piece cavallo_nero = new Piece(3, true, false);
        Piece alfiere_nero = new Piece(4, true, false);
        Piece regina_nero = new Piece(5, true, false);
        Piece re_nero = new Piece(6, true, false);
        
        for(int i = 0; i < 8; ++i)
        {
            b.addPiece(pedone_bianco, new Coord(i, 1));
            b.addPiece(pedone_nero, new Coord(i, 6));
        }
        b.addPiece(torre_bianco, new Coord(0,0));
        b.addPiece(torre_bianco, new Coord(7,0));
        b.addPiece(torre_nero, new Coord(0, 7));
        b.addPiece(torre_nero, new Coord(7, 7));
        b.addPiece(cavallo_bianco, new Coord(1, 0));
        b.addPiece(cavallo_bianco, new Coord(6, 0));
        b.addPiece(cavallo_nero, new Coord(1, 7));
        b.addPiece(cavallo_nero, new Coord(6, 7));
        b.addPiece(alfiere_bianco, new Coord(2, 0));
        b.addPiece(alfiere_bianco, new Coord(5, 0));
        b.addPiece(alfiere_nero, new Coord(2, 7));
        b.addPiece(alfiere_nero, new Coord(5, 7));
        b.addPiece(regina_bianco, new Coord(3, 0));
        b.addPiece(regina_nero, new Coord(3, 7));
        b.addPiece(re_bianco, new Coord(4, 0));
        b.addPiece(re_nero, new Coord(4, 7));
    }
    
    public static void stampaScacchiera(Board b)
    {
        for(int i = 7; i >= 0; --i)
        {
            for(int j = 7; j >= 0; --j)
            {
                Piece tmp = b.getPiece(new Coord(j, i));
                if(tmp == null)
                    System.out.print("0\t");
                else
                {
                    System.out.print(tmp.getType());
                    boolean c = tmp.getColor();
                    if(c == false)
                        System.out.print("b\t");
                    else
                        System.out.print("n\t");
                }
            }
            System.out.println("");
        }
    }
    
    public static void stampaArrayList(ArrayList<Coord> a)
    {
        for(int i = 0; i < a.size(); ++i)
        {
            System.out.print(a.get(i).getX() + a.get(i).getY() + " ");
        }
        System.out.println("");
    }
}
