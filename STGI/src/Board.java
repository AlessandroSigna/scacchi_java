 
import java.util.ArrayList;
import java.util.Iterator;
public class Board
{
    private Piece board[][] = new Piece[8][8];
    




    public void displayBoard()
    {   System.out.println(" ");
        System.out.println();
        System.out.println("NERI");
        System.out.println();
        for (int y = 0; y < 8; y++)
        {
            if(y==0){
                 System.out.println("  A0 B1 C2 D3 E4 F5 G6 H7");
                 System.out.println();
            }
            for (int x = 0; x < 8; x++)
            {
                if(x==0){
                    System.out.print(y+"  ");        
                }
                if (this.board[x][y] != null)
                    System.out.print(this.board[x][y].getType() + "  ");
                else
                    System.out.print("N  ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("BIANCHI");
        System.out.println(" ");   
    }





    public Board()
    {
        for(int i = 0; i < 8; ++i)
            for(int j = 0; j < 8; ++j)
                board[i][j] = null;
    }

    public Board(Board a)
    {
        for(int i = 0; i < 8; ++i)
            for(int j = 0; j < 8; ++j)
                this.board[i][j] = a.board[i][j];
    }

    public void addPiece(Piece piece, Coord c)
    {
        board[c.getX()][c.getY()] = piece;
        return;
    }

    public Piece getPiece(Coord c)
    {
        return board[c.getX()][c.getY()];
    }

    Coord findKingPosition(boolean color)
    {
        //CERCO IL RE DEL COLORE IN ARGOMENTO
        for(int i = 0; i < 8; ++i)
        {
            for(int j = 0; j < 8; ++j)
            {
                Piece tmp = board[i][j];
                if(tmp != null && tmp.getType() == 6 && tmp.getColor() == color) //RE TROVATO!
                {
                    return new Coord(i, j);
                }
            }
        }
        return null;

    }

    public int move(boolean myColor, Coord in, Coord fin, boolean opponentAlreadyInChess, int evol)
    {
        if(isCastling(in, fin))
        {
            moveWithoutCheck(in, fin);
            return 5; //arrocco
        }
        Board tmp = new Board(this);
        tmp.moveWithoutCheck(in, fin);
        if(tmp.isEvolving(fin))
        {
            if(evol == 0)
                return 3;
            else
            {
                tmp.board[in.getX()][in.getY()].setType(evol);
            }
        }
        Coord posKing = findKingPosition(!myColor); //posizione del re avversario
        if(!isInChess(posKing, myColor)) //se non sto mettendo il re avversario sotto scacco
        {
            if(checkIfMovesAreAvailable(!myColor)) //re avversario libero dallo scacco e con mosse disponibili
            {
                return 0; //mossa valida
            }
            else
            {
                return 2; //stallo
            }
        }
        if(checkIfMovesAreAvailable(!myColor)) //avversario sotto scacco e con mosse disponibili
            return 4; //dichiaro scacco
        if(opponentAlreadyInChess) //avversario sotto scacco, senza mosse diponibili e precedentemente sotto scacco
            return 1; //vittoria
        return 2; //stallo
    }

    public void moveWithoutCheck(Coord in, Coord fin)
    {
        boolean res = false;
        int posX_in = in.getX();
        int posY_in = in.getY();
        int posX_fin = fin.getX();
        int posY_fin = fin.getY();
        Piece tmp = board[posX_in][posY_in];
        if(tmp != null)
        {
            board[posX_fin][posY_fin] = board[posX_in][posY_in];
            board[posX_in][posY_in] = null;
        }
    }

    public boolean isInChess(Coord posKing, boolean myColor)
    {
        for(int i = 0; i < 8; ++i)
            for(int j = 0; j < 8; ++j)
            {
                if(board[i][j] != null && board[i][j].getColor() != myColor)
                {
                    ArrayList<Coord> moves = getMoves(new Coord(i, j));
                    Iterator it = moves.iterator();
                    while(it.hasNext())
                    {
                        if(posKing.equals((Coord) it.next()))
                            return true;
                    }
                }
            }
        return false;
    }

    public ArrayList<Coord> getMovesFilteredSinglePiece(Coord pos)
    {
        int posX = pos.getX();
        int posY = pos.getY();
        ArrayList<Coord> theorical = getMoves(pos);
        if(theorical == null)
            return null;
        boolean myColor = board[posX][posY].getColor();
        Coord posMyKing = findKingPosition(myColor);

        //SCORRO TUTTE LE MOSSE VALIDE E VERIFICO SE, PER OGNI MOVIMENTO CHE FACCIO, IL MIO AVVERSARIO PUÒ MANGIARE IL MIO RE
        Iterator it1 = theorical.iterator();
        while(it1.hasNext())
        {
            Board tmpBoard = new Board(this);
            Coord newPos = (Coord) it1.next();
            tmpBoard.moveWithoutCheck(pos, newPos);
            if(tmpBoard.isInChess(posMyKing, myColor)) //se con questa mossa sono sotto scacco, la elimino dalla lista delle mosse valide
                it1.remove();
        }
        return theorical; //CONTIENE L'ELENCO DI TUTTE LE MOSSE CHE POSSO FARE SENZA FINIRE SOTTO SCACCO
    }

    
    public ArrayList<ArrayList<Coord>> getMovesFilteredAllPieces(boolean myColor)
    {
        ArrayList<ArrayList<Coord>> res = new ArrayList<ArrayList<Coord>>();
        for(int i = 0; i < 8; ++i)
        {
            for(int j = 0; j < 8; ++j)
            {
                Piece tmp = board[i][j];
                if(tmp != null && tmp.getColor()== myColor) //PER TUTTI I MIEI PEZZI CALCOLO L'ELENCO DELLE MOSSE DISPONIBILI CHE NON MI METTONO SOTTO SCACCO
                {
                    Coord tmp2 = new Coord(i, j);
                    ArrayList<Coord> tmp3 = getMovesFilteredSinglePiece(tmp2);
                    if(!tmp3.isEmpty())
                    {
                        tmp3.add(0, tmp2); //metto in cima alla lista di mosse disponibili le coordinate della pedina che può muoversi. Mi serve nella servlet
                        res.add(tmp3);
                    }
                }
            }
        }
        return res;
    }

    public boolean checkIfMovesAreAvailable(boolean myColor)
    {
        for(int i = 0; i < 8; ++i)
        {
            for(int j = 0; j < 8; ++j)
            {
                Piece tmp = board[i][j];
                if(tmp != null && tmp.getColor()!= myColor)
                {
                    Coord tmp2 = new Coord(i, j);
                    ArrayList<Coord> tmp3 = getMovesFilteredSinglePiece(tmp2);
                    if(tmp3 != null)
                        return true;
                }
            }
        }
        return false;
    }

    public boolean isEvolving(Coord pos)
    {
        int posX = pos.getX();
        int posY = pos.getY();
        if((posY == 0 || posY == 7) && board[posX][posY].getType() == 1)
            return true;
        return false;
    }

    
    //DA RIFARE
    public boolean isCastling(Coord in, Coord fin)
    {
        boolean castling = false;           
        int posXin, posYin, posXfin, posYfin;
        posXin = in.getX();
        posYin = in.getY();
        posXfin = fin.getX();
        posYfin = fin.getY();
        if((board[posXin][posYin].getType()) == 6 && Math.abs((posXin-posYfin))==2)
        {   
            castling=true;
            if(posXfin == 6)
            {
                if(posYfin == 7)
                    moveWithoutCheck(new Coord(7, 7), new Coord(5, 7));
                else
                    moveWithoutCheck(new Coord(7, 0), new Coord(5, 0));
            }
            else
            {
                if(posYfin == 7)
                    moveWithoutCheck(new Coord(0, 7), new Coord(3, 7));
                else
                    moveWithoutCheck(new Coord(0, 0), new Coord(3, 0));
            }
        }
        return castling;
    }

    public ArrayList<Coord> getMoves(Coord pos)
    {
        int posX = pos.getX();
        int posY = pos.getY();
        if(board[posX][posY] == null)
            return null;
        int type = board[posX][posY].getType();
        switch(type)
        {
            case 1: //Pawn
                return getMovesPawn(pos);
            case 2: //Rook
                return getMovesRook(pos);
            case 3: //Knight
                return getMovesKnight(pos);
            case 4:
                return getMovesBishop(pos);
            case 5:
                return getMovesQueen(pos);
            case 6:
                return getMovesKing(pos);
            default:
                return null;
        }
    }
    
    public ArrayList<Coord> getMovesQueen(Coord pos)
    {
        int posX = pos.getX();
        int posY = pos.getY();
        ArrayList<Coord> res = new ArrayList<Coord>();
        boolean flag_i = true;
        boolean flag_j = true;
        boolean flag_k = true;

        for(int i = posX - 1, j = posY -1, k = posY + 1; i >= 0; --i, --j, ++k)
        {
            if(j >= 0 && flag_j == true)
            {
                if(board[i][j] == null)
                    res.add(new Coord(i, j));
                else
                {
                    if(board[i][j].getColor() != board[posX][posY].getColor())
                        res.add(new Coord(i, j));
                    flag_j = false;
                }                
            }
                
            if(k <= 7 && flag_k == true)
            {
                if(board[i][k] == null)
                    res.add(new Coord(i, k));
                else
                {
                    if(board[i][k].getColor() != board[posX][posY].getColor())
                        res.add(new Coord(i, k));
                    flag_k = false;
                }
            }
                
            if(flag_i == true)
            {
                if(board[i][posY] == null)
                {
                    res.add(new Coord(i, posY));
                }
                else
                {
                    if(board[i][posY].getColor() != board[posX][posY].getColor())
                        res.add(new Coord(i, posY));
                    flag_i = false;
                }
            }
        }

        flag_i = true;
        flag_j = true;
        flag_k = true;

        for(int i = posX + 1, j = posY - 1, k = posY + 1; i <= 7; ++i, --j, ++k)
        {
            if(j >= 0 && flag_j == true)
            {
                if(board[i][j] == null)
                    res.add(new Coord(i, j));
                else
                {
                    if(board[i][j].getColor() != board[posX][posY].getColor())
                        res.add(new Coord(i, j));
                    flag_j = false;
                }
                
            }
                
            if(k <= 7 && flag_k == true)
            {
                if(board[i][k] == null)
                    res.add(new Coord(i, k));
                else
                {
                    if(board[i][k].getColor() != board[posX][posY].getColor())
                        res.add(new Coord(i, k));
                    flag_k = false;
                }
            }
                
            if(flag_i == true)
            {
                if(board[i][posY] == null)
                {
                    res.add(new Coord(i, posY));
                }
                else
                {
                    if(board[i][posY].getColor() != board[posX][posY].getColor())
                        res.add(new Coord(i, posY));
                    flag_i = false;
                }
            }
        }

        flag_i = true;

        for(int i = posY - 1; flag_i == true && i >= 0; --i)
        {
            if(board[posX][i] == null)
                res.add(new Coord(posX, i));
            else
            {
                if(board[posX][i].getColor() != board[posX][posY].getColor())
                    res.add(new Coord(posX, i));
                flag_i = false;
            }
        }

        flag_i = true;

        for(int i = posY + 1; flag_i == true && i < 8; ++i)
        {
            if(board[posX][i] == null)
                res.add(new Coord(posX, i));
            else
            {
                if(board[posX][i].getColor() != board[posX][posY].getColor())
                    res.add(new Coord(posX, i));
                flag_i = false;
            }
        }
        return res;
    }

    public ArrayList<Coord> getMovesPawn(Coord pos)
    {
        int posX = pos.getX();
        int posY = pos.getY();
        ArrayList<Coord> res = new ArrayList<Coord>();
        int dir = -1;
        boolean pieceColor = board[posX][posY].getColor();
        if(pieceColor == false)
            dir = 1;        
        if(board[posX][posY+dir] == null)
        {
            res.add(new Coord(posX, posY + dir));
            if(board[posX][posY].getUnmoved() == true && board[posX][posY + 2*dir] == null)
                res.add(new Coord(posX, posY + 2*dir));
        }
        if(posX > 0)
        {
            Piece tmp = board[posX - 1][posY + dir];
            if(tmp != null && tmp.getColor() != pieceColor)
                res.add(new Coord(posX - 1, posY + dir));
        }
        if(posX < 7)
        {
            Piece tmp = board[posX + 1][posY + dir];
            if(tmp != null && tmp.getColor() != pieceColor)
                res.add(new Coord(posX + 1, posY + dir));
        }
        return res;
    }
    
    public ArrayList<Coord> getMovesRook(Coord pos)
    {
        int posX = pos.getX();
        int posY = pos.getY();
        ArrayList<Coord> res = new ArrayList<Coord>();
        boolean flag_i = true;

        for(int i = posX - 1; i >= 0 && flag_i == true; --i)
        {
                       
            if(board[i][posY] == null)
            {
                res.add(new Coord(i, posY));
            }
            else
            {
                if(board[i][posY].getColor() != board[posX][posY].getColor())
                    res.add(new Coord(i, posY));
                flag_i = false;
            }
        }

        flag_i = true;

        for(int i = posX + 1; i <= 7 && flag_i == true; ++i)
        {                
            if(board[i][posY] == null)
            {
                res.add(new Coord(i, posY));
            }
            else
            {
                if(board[i][posY].getColor() != board[posX][posY].getColor())
                    res.add(new Coord(i, posY));
                flag_i = false;
            }
        }

        flag_i = true;

        for(int i = posY - 1; flag_i == true && i >= 0; --i)
        {
            if(board[posX][i] == null)
                res.add(new Coord(posX, i));
            else
            {
                if(board[posX][i].getColor() != board[posX][posY].getColor())
                    res.add(new Coord(posX, i));
                flag_i = false;
            }
        }

        flag_i = true;

        for(int i = posY + 1; flag_i == true && i < 8; ++i)
        {
            if(board[posX][i] == null)
                res.add(new Coord(posX, i));
            else
            {
                if(board[posX][i].getColor() != board[posX][posY].getColor())
                    res.add(new Coord(posX, i));
                flag_i = false;
            }
        }
        return res;
    }

    public ArrayList<Coord> getMovesKnight(Coord pos)
    {
        ArrayList<Coord> res = new ArrayList<Coord>();
        int posX = pos.getX();
        int posY = pos.getY();
        if(posX + 2 < 8 && posY - 1 >= 0)
        {
            if(board[posX + 2][posY - 1] == null)
                res.add(new Coord(posX + 2, posY - 1));
            else
            {
                if(board[posX + 2][posY - 1].getColor() != board[posX][posY].getColor())
                    res.add(new Coord(posX + 2, posY - 1));
            }
        }

        if(posX + 2 < 8 && posY + 1 < 8)
        {
            if(board[posX + 2][posY + 1] == null)
                res.add(new Coord(posX + 2, posY + 1));
            else
            {
                if(board[posX + 2][posY + 1].getColor() != board[posX][posY].getColor())
                    res.add(new Coord(posX + 2, posY + 1));
            }
        }

        if(posX + 1 < 8 && posY - 2 >= 0)
        {
            if(board[posX + 1][posY - 2] == null)
                res.add(new Coord(posX + 1, posY - 2));
            else
            {
                if(board[posX + 1][posY - 2].getColor() != board[posX][posY].getColor())
                    res.add(new Coord(posX + 1, posY - 2));
            }
        }

        if(posX + 1 < 8 && posY + 2 < 8)
        {
            if(board[posX + 1][posY + 2] == null)
                res.add(new Coord(posX + 1, posY + 2));
            else
            {
                if(board[posX + 1][posY + 2].getColor() != board[posX][posY].getColor())
                    res.add(new Coord(posX + 1, posY + 2));
            }
        }

        if(posX - 1 >= 0 && posY - 2 >= 0)
        {
            if(board[posX - 1][posY - 2] == null)
                res.add(new Coord(posX - 1, posY - 2));
            else
            {
                if(board[posX - 1][posY - 2].getColor() != board[posX][posY].getColor())
                    res.add(new Coord(posX - 1, posY - 2));
            }
        }

        if(posX - 1 >= 0 && posY + 2 < 8)
        {
            if(board[posX - 1][posY + 2] == null)
                res.add(new Coord(posX - 1, posY + 2));
            else
            {
                if(board[posX - 1][posY + 2].getColor() != board[posX][posY].getColor())
                    res.add(new Coord(posX - 1, posY + 2));
            }
        }

        if(posX - 2 >= 0 && posY + 1 < 8)
        {
            if(board[posX - 2][posY + 1] == null)
                res.add(new Coord(posX - 2, posY + 1));
            else
            {
                if(board[posX - 2][posY + 1].getColor() != board[posX][posY].getColor())
                    res.add(new Coord(posX - 2, posY + 1));
            }
        }

        if(posX - 2 >=0 && posY - 1 >= 0)
        {
            if(board[posX - 2][posY - 1] == null)
                res.add(new Coord(posX - 2, posY - 1));
            else
            {
                if(board[posX - 2][posY - 1].getColor() != board[posX][posY].getColor())
                    res.add(new Coord(posX - 2, posY - 1));
            }
        }
        return res;
    }

    public ArrayList<Coord> getMovesBishop(Coord pos)
    {
        int posX = pos.getX();
        int posY = pos.getY();
        ArrayList<Coord> res = new ArrayList<Coord>();
        boolean flag_j = true;
        boolean flag_k = true;

        for(int i = posX - 1, j = posY -1, k = posY + 1; i >= 0; --i, --j, ++k)
        {
            if(j >= 0 && flag_j == true)
            {//basso sx
                if(board[i][j] == null)
                    res.add(new Coord(i, j));
                else
                {
                    if(board[i][j].getColor() != board[posX][posY].getColor())
                        res.add(new Coord(i, j));
                    flag_j = false;
                }
                
            }
                
            if(k <= 7 && flag_k == true)
            {//alto sx
                if(board[i][k] == null)
                    res.add(new Coord(i, k));
                else
                {
                    if(board[i][k].getColor() != board[posX][posY].getColor())
                        res.add(new Coord(i, k));
                    flag_k = false;
                }
            }
        }

        flag_j = true;
        flag_k = true;

        for(int i = posX + 1, j = posY - 1, k = posY + 1; i <= 7; ++i, --j, ++k)
        {
            if(j >= 0 && flag_j == true)
            {//basso dx
                if(board[i][j] == null)
                    res.add(new Coord(i, j));
                else
                {
                    if(board[i][j].getColor() != board[posX][posY].getColor())
                        res.add(new Coord(i, j));
                    flag_j = false;
                }  
            }
                
            if(k <= 7 && flag_k == true)
            {//alto dx
                if(board[i][k] == null)
                    res.add(new Coord(i, k));
                else
                {
                    if(board[i][k].getColor() != board[posX][posY].getColor())
                        res.add(new Coord(i, k));
                    flag_k = false;
                }
            }    
          
        }
        return res;
    }

    public ArrayList<Coord> getMovesKing(Coord pos)
    {
        ArrayList<Coord> res = new ArrayList<Coord>();
        int posX = pos.getX();
        int posY = pos.getY();
        Piece king = board[posX][posY];
        boolean pieceColor = king.getColor();
        if(posX > 0)
        {
            Piece ceil = board[posX - 1][posY];
            if(ceil == null || (ceil != null && ceil.getColor() != pieceColor))
                res.add(new Coord(posX - 1, posY));
            
            if(posY > 0)
            {
                ceil = board[posX - 1][posY - 1];
                if(ceil == null || (ceil != null && ceil.getColor() != pieceColor))
                    res.add(new Coord(posX - 1, posY - 1));
            }            
            
            if(posY < 7)
            {
                ceil = board[posX - 1][posY + 1];
                if(ceil == null || (ceil != null && ceil.getColor() != pieceColor))
                    res.add(new Coord(posX - 1, posY + 1));
            }
        }
        if(posX < 7)
        {
            Piece ceil = board[posX + 1][posY];
            if(ceil == null || (ceil != null && ceil.getColor() != pieceColor))
                res.add(new Coord(posX + 1, posY));
            
            if(posY > 0)
            {
                ceil = board[posX +1][posY - 1];
                if(ceil == null || (ceil != null && ceil.getColor() != pieceColor))
                    res.add(new Coord(posX +1, posY - 1));
            }            
            
            if(posY < 7)
            {
                ceil = board[posX + 1][posY + 1];
                if(ceil == null || (ceil != null && ceil.getColor() != pieceColor))
                    res.add(new Coord(posX + 1, posY + 1));
            }
        }
        if(posY > 0)
        {
            Piece ceil = board[posX][posY - 1];
                if(ceil == null || (ceil != null && ceil.getColor() != pieceColor))
                    res.add(new Coord(posX, posY - 1));
        }
        if(posY < 7)
        {
            Piece ceil = board[posX][posY + 1];
                if(ceil == null || (ceil != null && ceil.getColor() != pieceColor))
                    res.add(new Coord(posX, posY + 1));
        }
        return res;
    }
}
       