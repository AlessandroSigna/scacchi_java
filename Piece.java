package scacchi_java;
//import java.util.*;
public class Piece {

    private String type;
    private Coord position;
    private boolean white;
    private boolean unmoved;
    
    //Initializes a piece.
    public Piece(String type, Coord position, boolean white)
    {
       
        this.type = type;
        this.position=position;
        this.white = white;
        this.unmoved = true;
    }

    public Piece setPosition(Coord pos)
    {
        this.position = pos;
        return this;
    }

    public String getType()
    {
        return this.type;
    }

    public Coord getPosition()
    {
        return this.position;
    }

    public boolean getColor()
    {
        return this.white;
    }

    public Coord[] move(Board board, Coord pos){
        Coord[] cord = new Coord[64];
        if(this.type == "Knight"){
            return moveKnight(board, pos);
        } 
        else if(this.type == "Rook"){
            return moveRook(board, pos);
        } 
        else if(this.type == "King"){
            return moveKing(board, pos);
        } 
        else if(this.type == "Queen"){
            return moveQueen(board, pos);
        } 
        else if(this.type == "Bishop"){
            return moveBishop(board, pos);
        } 
        else if(this.type == "Pawn"){
            return movePawn(board, pos);
        } 
        return cord;
    }





    public Coord[] moveQueen(Board board, Coord pos){
        
        System.out.println("stai muovendo la regina "+pos.getX()+" "+pos.getY() + " , la regina puo andare");
        Coord[] cord = new Coord[64];
        Coord pos2=new Coord(0,0);
        int z=0, y=0;

                //in alto sx - -
                for(int x=1;((pos.getX()-x)>=0)&&((pos.getY()-x)>=0);x++){
                    pos2.setPosition(pos.getX()-x,pos.getY()-x);
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }
                }

                //in basso destra++
                for(int x=1;((pos.getX()+x)<8)&&((pos.getY()+x)<8);x++){
                    pos2.setPosition(pos.getX()+x,pos.getY()+x);
                    
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }
                }

                //in basso a sinistrax-y+
                for(int x=1;((pos.getX()-x)>=0)&&((pos.getY()+x)<8);x++){
                    pos2.setPosition(pos.getX()-x,pos.getY()+x);
                    //se pos2 e' fuori dal tavolo di gioco esci
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    //altrimenti se non c'è nessun pezzo su pos2 o il colore del pezzo su pos2 è diverso dal colore del pezzo su pos 
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }

                    
                }

                //in alto a destra x+y-
                for(int x=1;((pos.getX()+x)<8)&&((pos.getY()-x)>=0);x++){
                    pos2.setPosition(pos.getX()+x,pos.getY()-x);
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }
                }

                //in alto
                for(int x=1;(pos.getY()-x)>=0;x++){
                    pos2.setPosition(pos.getX(),pos.getY()-x);
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }
                }

                //a destra
                for(int x=1;(pos.getX()+x)<8;x++){
                    pos2.setPosition(pos.getX()+x,pos.getY());
                    
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }
                }

                //in basso
                for(int x=1;(pos.getY()+x)<8;x++){
                    pos2.setPosition(pos.getX(),pos.getY()+x);
                    //se pos2 e' fuori dal tavolo di gioco esci
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    //altrimenti se non c'è nessun pezzo su pos2 o il colore del pezzo su pos2 è diverso dal colore del pezzo su pos 
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }   
                }

                //a sinistra
                for(int x=1;(pos.getX()-x)>=0;x++){
                    pos2.setPosition(pos.getX()-x,pos.getY());
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }
                }


                for(int x=0;x<64;x++){
                if(cord[x]!=null){
                System.out.println(x+" "+cord[x].getX()+" "+cord[x].getY());
                y++;
                }        
                }
                Coord[] cord2 = new Coord[y];
                cord2=cord;
                return cord2;  
    }






    public Coord[] moveKing(Board board, Coord pos){
        Coord[] cord = new Coord[8];
        Coord[] cord2 = new Coord[8];
        int y=0;
        System.out.println("stai muovendo il re in " +pos.getX()+" "+pos.getY() + " , il re puo andare");
        cord[0]= new Coord(pos.getX()+1,pos.getY()+1);
        cord[1]= new Coord(pos.getX(),pos.getY()+1);
        cord[2]= new Coord(pos.getX()-1,pos.getY()+1);
        cord[3]= new Coord(pos.getX()-1,pos.getY());
        cord[4]= new Coord(pos.getX()-1,pos.getY()-1);
        cord[5]= new Coord(pos.getX(),pos.getY()-1);
        cord[6]= new Coord(pos.getX()+1,pos.getY()-1);
        cord[7]= new Coord(pos.getX()+1,pos.getY());
        
        for(int x=0;x<8;x++){
            if(cord[x].getX()>=0 && cord[x].getX()<8 && cord[x].getY()>=0 && cord[x].getY()<8){
                if(board.getPiece(cord[x])==null || board.getPiece(cord[x]).getColor()!=board.getPiece(pos).getColor()){
                    System.out.println(cord[x].getX()+" "+cord[x].getY());
                    cord2[y]=cord[x];
                    y++;
                }
                else
                    continue;
            }
            else
                continue;
        }
        return cord2;
    }

    public Coord[] moveBishop(Board board, Coord pos){
        
        System.out.println("stai muovendo l'alfiere "+pos.getX()+" "+pos.getY() + " , l'alfiere puo andare");
        Coord[] cord = new Coord[64];
        Coord pos2=new Coord(0,0);
        int z=0, y=0;
        //in alto sx - -
                for(int x=1;((pos.getX()-x)>=0)&&((pos.getY()-x)>=0);x++){
                    pos2.setPosition(pos.getX()-x,pos.getY()-x);
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }
                }
                //in basso destra++
                for(int x=1;((pos.getX()+x)<8)&&((pos.getY()+x)<8);x++){
                    pos2.setPosition(pos.getX()+x,pos.getY()+x);
                    
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }
                }
                //in basso a sinistrax-y+
                for(int x=1;((pos.getX()-x)>=0)&&((pos.getY()+x)<8);x++){
                    pos2.setPosition(pos.getX()-x,pos.getY()+x);
                    //se pos2 e' fuori dal tavolo di gioco esci
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    //altrimenti se non c'è nessun pezzo su pos2 o il colore del pezzo su pos2 è diverso dal colore del pezzo su pos 
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }

                    
                }
                //in alto a destra x+y-
                for(int x=1;((pos.getX()+x)<8)&&((pos.getY()-x)>=0);x++){
                    pos2.setPosition(pos.getX()+x,pos.getY()-x);
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }
                }
                for(int x=0;x<64;x++){
                if(cord[x]!=null){
                System.out.println(cord[x].getX()+" "+cord[x].getY());
                y++;
                }        
                }
                Coord[] cord2 = new Coord[y];
                cord2=cord;
                return cord2;  
    }






    public Coord[] movePawn(Board board, Coord pos){
        Coord[] cord = new Coord[4];
        
        Coord pos2=new Coord(0,0);
        Coord pos3=new Coord(0,0);
        int z=0, y=0;

        System.out.println("stai muovendo la pedina "+pos.getX()+" "+pos.getY() + " , la pedina puo andare");
        if(board.getPiece(pos).getColor()==true){
            
            cord[0] = new Coord(pos.getX(),pos.getY()+1);
            cord[1]= new Coord(pos.getX(),pos.getY()+2);
            //mangio a dx
            if((pos.getX()+1)<8)
                cord[2]= new Coord(pos.getX()+1,pos.getY()+1);
            else
                cord[2]=null;           
            //mangio a sx
            if((pos.getX()-1)>=0)
                cord[3]= new Coord(pos.getX()-1,pos.getY()+1);
            else
                cord[3]=null;

            //System.out.println("alla posizione 1 "+cord[0].getX()+" "+cord[0].getY());
            //avanza di uno
            if(board.getPiece(cord[0])!=null){
                System.out.println("sta");
                cord[0]=null;
                cord[1]=null;}
            
            //avanza di due
            if (cord[0]!=null) {            
                if ((board.getPiece(cord[0])==null)) {
                    if((board.getPiece(cord[1])!=null) || (pos.getY()>2)){  
                        cord[1]=null;}}}
            //System.out.println("alla posizione 1 "+cord[0].getX()+" "+cord[0].getY());
            //mangia a sx
            if(cord[2]!=null){    
                if (board.getPiece(cord[2])==null) {
                    cord[2]=null;}
                else if((board.getPiece(cord[2]).getColor()==board.getPiece(pos).getColor())){
                        cord[2]=null;
                }
            }
            //System.out.println("alla posizione 1 "+cord[0].getX()+" "+cord[0].getY());
            //mangia a dx
            if(cord[3]!=null){
                if (board.getPiece(cord[3])==null) {
                    cord[3]=null;}
                else if((board.getPiece(cord[3]).getColor()==board.getPiece(pos).getColor())){
                        cord[3]=null;
                }
            }
        }

        if(board.getPiece(pos).getColor()==false){
            
            cord[0] = new Coord(pos.getX(),pos.getY()-1);
            cord[1]= new Coord(pos.getX(),pos.getY()-2);
            //mangio a dx
            if((pos.getX()-1)>=0)
                cord[2]= new Coord(pos.getX()-1,pos.getY()-1);
            else
                cord[2]=null;           
            //mangio a sx
            if((pos.getX()+1)<8)
                cord[3]= new Coord(pos.getX()+1,pos.getY()-1);
            else
                cord[3]=null;

            
            //avanza di uno
            if(board.getPiece(cord[0])!=null){
                System.out.println("sta");
                cord[0]=null;
                cord[1]=null;}
            
            //avanza di due
            if (cord[0]!=null) {            
                if ((board.getPiece(cord[0])==null)) {
                    if((board.getPiece(cord[1])!=null) || (pos.getY()<6)){  
                        cord[1]=null;}}}
                
            //mangia a sx
            if(cord[2]!=null){    
                if (board.getPiece(cord[2])==null) {
                    cord[2]=null;}
                else if((board.getPiece(cord[2]).getColor()==board.getPiece(pos).getColor())){
                        cord[2]=null;
                }
            }
            
            //mangia a dx
            if(cord[3]!=null){
                if (board.getPiece(cord[3])==null) {
                    cord[3]=null;}
                else if((board.getPiece(cord[3]).getColor()==board.getPiece(pos).getColor())){
                        cord[3]=null;
                }
            }
        }

        

        for(int x=0;x<4;x++){
            if(cord[x]!=null)
            System.out.println(cord[x].getX()+" "+cord[x].getY());
            y++;
        }
        Coord[] cord2 = new Coord[y];
        y=0;
        for(int x=0;x<4;x++){
            if(cord[x]!=null){
                cord2[y]=cord[x];
                y++;}
        }

        //DA QUI PROMOZIONE



    return cord2;
    }



    public Coord[] moveRook(Board board, Coord pos){
        Coord[] cord = new Coord[64];
        Coord pos2=new Coord(0,0);
        int z=0, y=0;
        System.out.println("stai muovendo la torre "+pos.getX()+" "+pos.getY() + " , la torre puo andare");
            
            
                //in alto
                for(int x=1;(pos.getY()-x)>=0;x++){
                    pos2.setPosition(pos.getX(),pos.getY()-x);
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }
                }
                //a destra
                for(int x=1;(pos.getX()+x)<8;x++){
                    pos2.setPosition(pos.getX()+x,pos.getY());
                    
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }
                }
                //in basso
                for(int x=1;(pos.getY()+x)<8;x++){
                    pos2.setPosition(pos.getX(),pos.getY()+x);
                    //se pos2 e' fuori dal tavolo di gioco esci
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    //altrimenti se non c'è nessun pezzo su pos2 o il colore del pezzo su pos2 è diverso dal colore del pezzo su pos 
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }

                    
                }
                //a sinistra
                for(int x=1;(pos.getX()-x)>=0;x++){
                    pos2.setPosition(pos.getX()-x,pos.getY());
                    if(pos2.getX()<0 || pos2.getX()>8 || pos2.getY()<0 || pos2.getY()>8)
                        break;
                    else if((board.getPiece(pos2)==null)) {
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;}

                    else if( (board.getPiece(pos2).getColor()!=board.getPiece(pos).getColor())){
                        
                        cord[z]= new Coord(pos2.getX(),pos2.getY());
                        //System.out.println(cord[z].getX()+" "+cord[z].getY());
                        z++;
                        break;
                    }
                    else if (board.getPiece(pos2).getColor()==board.getPiece(pos).getColor()) {
                        break;
                    }
                }
            
        for(int x=0;x<64;x++){
            if(cord[x]!=null){
            System.out.println(cord[x].getX()+" "+cord[x].getY());
            y++;
            }        
        }
        Coord[] cord2 = new Coord[y];
        cord2=cord;
        return cord2;
    }

    public Coord[] moveKnight(Board board, Coord pos){
        Coord[] cord = new Coord[8];
        Coord[] cord2 = new Coord[8];
        int y=0;
        System.out.println("stai muovendo il cavallo in " +pos.getX()+" "+pos.getY() + " , il cavallo puo andare");
        cord[0]= new Coord(pos.getX()+2,pos.getY()-1);
        cord[1]= new Coord(pos.getX()+2,pos.getY()+1);
        cord[2]= new Coord(pos.getX()+1,pos.getY()-2);
        cord[3]= new Coord(pos.getX()+1,pos.getY()+2);
        cord[4]= new Coord(pos.getX()-1,pos.getY()-2);
        cord[5]= new Coord(pos.getX()-1,pos.getY()+2);
        cord[6]= new Coord(pos.getX()-2,pos.getY()+1);
        cord[7]= new Coord(pos.getX()-2,pos.getY()-1);
        
        for(int x=0;x<8;x++){
            if(cord[x].getX()>=0 && cord[x].getX()<8 && cord[x].getY()>=0 && cord[x].getY()<8){
                if(board.getPiece(cord[x])==null || board.getPiece(cord[x]).getColor()!=board.getPiece(pos).getColor()){
                    System.out.println(cord[x].getX()+" "+cord[x].getY());
                    cord2[y]=cord[x];
                    y++;
                }
                else
                    continue;
            }
            else
                continue;
        }
        return cord2;
    }

}