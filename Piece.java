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

     public void setUnmoved(boolean unmoved)
    {
    	this.unmoved=unmoved;
        return;
    }

    public boolean getUnmoved()
    {
        return this.unmoved;
    }

    public void setType(String type)
    {
    	this.type=type;
        return;
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
        Coord[] cord = new Coord[9];
        
        if(this.type == "Knight"){
            return moveKnight(board, pos);
        } 
        else if("Rook".equals(this.type)){
            return moveRook(board, pos);
        } 
        else if("King".equals(this.type)){
        	cord=moveKing(board, pos);
        	int x=0;
        	while(cord[x]!=null){
        		x++;	
        	}

        	cord[x]=castlingKing(board, pos);            
            return cord;
        } 
        else if("Queen".equals(this.type)){
            return moveQueen(board, pos);
        } 
        else if("Bishop".equals(this.type)){
            return moveBishop(board, pos);
        } 
        else if("Pawn".equals(this.type)){
            return movePawn(board, pos);
        } 
        return null;
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
                System.out.println(cord[x].getX()+" "+cord[x].getY());
                y++;
                }        
                }
                Coord[] cord2 = new Coord[y];
                cord2=cord;
                return cord2;  
    }




    public Coord castlingKing(Board board, Coord pos){
    	
    	Coord pos0=new Coord(0,0);
    	Coord pos1=new Coord(1,0);
    	Coord pos2=new Coord(2,0);
    	Coord pos3=new Coord(3,0);
    	Coord pos5=new Coord(5,0);
    	Coord pos6=new Coord(6,0);
    	Coord pos7=new Coord(7,0);
     	Coord pos0N=new Coord(0,7);
    	Coord pos1N=new Coord(1,7);
    	Coord pos2N=new Coord(2,7);
    	Coord pos3N=new Coord(3,7);
    	Coord pos5N=new Coord(5,7);
    	Coord pos6N=new Coord(6,7);
    	Coord pos7N=new Coord(7,7);

    	//sezione sopra(bianchi nel software neri nella scacchiera)
    	if(board.getPiece(pos).getColor()==true){
    		System.out.println("lungo sopra");
    		//verifico che il re non si sia mai mosso
    		if(board.getPiece(pos).getUnmoved()==true){
    			System.out.println("lungo sopra");
    			//ARROCCO LUNGO SOPRA
    			//verifico che la torre non si sia mai mossa
    			if(board.getPiece(pos0).getUnmoved()==true){
    				
    				//verifico che non ci siano pezzi tra re e torre
    				if(board.getPiece(pos1)==null && board.getPiece(pos2)==null && board.getPiece(pos3)==null){
    					System.out.println("lungo sopra");
    					return pos2;
    				}
    			}
    			//ARROCCO CORTO SOPRA
    			//verifico che la torre non si sia mai mossa
    			else if(board.getPiece(pos7).getUnmoved()==true){
    				//verifico che non ci siano pezzi tra re e torre
    				if(board.getPiece(pos5)==null && board.getPiece(pos6)==null){
    					System.out.println("corto sopra");
    					return pos6;
    				}
    			}
    		}
    	}

    	//sezione sotto(neri nel software bianchi nella scacchiera)
    	if(board.getPiece(pos).getColor()==false){
    		//verifico che il re non si sia mai mosso
    		if(board.getPiece(pos).getUnmoved()==true){
    			//ARROCCO LUNGO sotto
    			//verifico che la torre non si sia mai mossa
    			if(board.getPiece(pos0N).getUnmoved()==true){
    				//verifico che non ci siano pezzi tra re e torre
    				if(board.getPiece(pos1N)==null && board.getPiece(pos2N)==null && board.getPiece(pos3N)==null){
    					System.out.println("lungo sotto");
    					return pos2;
    				}
    			}
    			//ARROCCO CORTO sotto
    			//verifico che la torre non si sia mai mossa
    			else if(board.getPiece(pos7N).getUnmoved()==true){
    				//verifico che non ci siano pezzi tra re e torre
    				if(board.getPiece(pos5N)==null && board.getPiece(pos6N)==null){
    					System.out.println("corto sotto");
    					return pos6N;
    				}
    			}
    		}
    	}
    	return null;
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
        
        int y=0;

        System.out.println("stai muovendo la pedina "+pos.getX()+" "+pos.getY() + " , la pedina puo andare");
        if(board.getPiece(pos).getColor()==true){
            
            cord[0] = new Coord(pos.getX(),pos.getY()+1);
            cord[1]= new Coord(pos.getX(),pos.getY()+2);
            if(!board.IsCoordinateValid(cord[0]))
            {
                cord[0] = cord[1] = cord[2] = cord[3] = null;
            }
            else if(!board.IsCoordinateValid(cord[1]))
            {
                cord[1] = null;
            }

            if(cord[0]!=null)
            {
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
                if (cord[1]!=null) {            
                    if ((board.getPiece(cord[0])==null)) {
                        if((board.getPiece(cord[1])!=null) || (pos.getY()>2)){  
                            cord[1]=null;
                        }
                    }
                }
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
            else
            {
                System.out.println("Non posso muovermi, fine scacchiera");
            }

            // codice promozione
        }

        if(board.getPiece(pos).getColor()==false){
            
            
            cord[0] = new Coord(pos.getX(),pos.getY()-1);
            cord[1]= new Coord(pos.getX(),pos.getY()-2);

            
            if(!board.IsCoordinateValid(cord[0]))
            {
                cord[0] = cord[1] = cord[2] = cord[3] = null;
            }
            else if(!board.IsCoordinateValid(cord[1]))
            {
                cord[1] = null;
            }

            if(cord[0]!=null)
            {
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
                if (cord[1]!=null) {            
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
            else
            {
                System.out.println("Non posso muovermi, fine scacchiera");
            }

            // codice promozione
            
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