package scacchi_java;
import java.util.Scanner;
public class Board {

    
    public final int DIM = 8;
    //private final String[] backline = {"Piece", "Piece", "Piece", "Piece", "Piece", "Piece", "Piece", "Piece"};
    Piece casella[][] = new Piece[8][8];
    Coord cordinata[] = new Coord[32];
    //Sets up the board
    public Board()
    {
        

                    
                    cordinata[0] = new Coord(0,0);
                    casella[0][0] = new Piece("Rook", cordinata[0], true);
                    cordinata[1]= new Coord(1,0);
                    casella[1][0] = new Piece("Knight", cordinata[1], true);
                    cordinata[2]= new Coord(2,0);
                    casella[2][0] = new Piece("Bishop", cordinata[2], true);
                    cordinata[3]= new Coord(3,0);
                    casella[3][0] = new Piece("King", cordinata[3], true);
                    cordinata[4]= new Coord(4,0);
                    casella[4][0] = new Piece("Queen", cordinata[4], true);
                    cordinata[5]= new Coord(5,0);
                    casella[5][0] = new Piece("Bishop", cordinata[5], true);
                    cordinata[6]= new Coord(6,0);
                    casella[6][0] = new Piece("Knight", cordinata[6], true);
                    cordinata[7]= new Coord(7,0);
                    casella[7][0] = new Piece("Rook", cordinata[7], true);
                
                for(int x=0;x<8;x++){
                    cordinata[x+8]= new Coord(x,1);
                    casella[x][1] = new Piece("Pawn", cordinata[x+8], true);
                    cordinata[x+16]= new Coord(x,6);
                    casella[x][6] = new Piece("Pawn", cordinata[x+16], false);
                }
                
                    cordinata[24]= new Coord(0,7);
                    casella[0][7] = new Piece("Rook", cordinata[24], false);
                    cordinata[25]= new Coord(1,7);
                    casella[1][7] = new Piece("Knight", cordinata[25], false);
                    cordinata[26]= new Coord(2,7);
                    casella[2][7] = new Piece("Bishop", cordinata[26], false);
                    cordinata[27]= new Coord(3,7);
                    casella[3][7] = new Piece("King", cordinata[27], false);
                    cordinata[28]= new Coord(4,7);
                    casella[4][7] = new Piece("Queen", cordinata[28], false);
                    cordinata[29]= new Coord(5,7);
                    casella[5][7] = new Piece("Bishop", cordinata[29], false);
                    cordinata[30]= new Coord(6,7);
                    casella[6][7] = new Piece("Knight", cordinata[30], false);
                    cordinata[31]= new Coord(7,7);
                    casella[7][7] = new Piece("Rook", cordinata[31], false);
    }

    //Returns true if the given coordinate is a valid one (inside the board)
    public Boolean IsCoordinateValid(Coord pos)
    {
        if(pos.getX()<0 || pos.getX() >= DIM || pos.getY()<0 || pos.getY()>=DIM)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    //Returns piece at given coordinates
    public Piece getPiece(Coord pos)
    {
        if(casella[pos.getX()][pos.getY()] != null){

            return casella[pos.getX()][pos.getY()];
        }
        else
            return null;
    }

    //Moves piece to given coordinates
    public void movePiece(Piece piece, Coord pos)
    {
    	
        this.casella[piece.getPosition().getX()][piece.getPosition().getY()] = null;
        this.casella[pos.getX()][pos.getY()] = piece.setPosition(pos);
        

        String promozione;
        Scanner input = new Scanner(System.in);

        if(this.casella[pos.getX()][pos.getY()].getType()=="Pawn"){
        	if (pos.getY()==7) {
        		System.out.println("ecco la promozione per il bianco");
        		promozione=input.nextLine();
        		this.casella[pos.getX()][pos.getY()].setType(promozione);
        	}
        	else if (pos.getY()==0) {
        		System.out.println("ecco la promozione per il nero");
        		promozione=input.nextLine();
        		this.casella[pos.getX()][pos.getY()].setType(promozione);
        	}
        }
    }
    
    public void displayBoard()
    {   System.out.println(" ");
        System.out.println();
        System.out.println("BIANCHI");
        System.out.println();
        for (int y = 0; y < DIM; y++)
        {
            if(y==0){
                 System.out.println("    0     1     2     3     4     5     6     7");
                 System.out.println();
            }
            for (int x = 0; x < DIM; x++)
            {
                if(x==0){
                    System.out.print(y+" ");        
                }
                if (casella[x][y] != null)
                    System.out.print(casella[x][y].getType() + " ");
                else
                    System.out.print("NULL ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("NERI");
        System.out.println(" ");
        
    }

}