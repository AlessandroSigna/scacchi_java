package scacchi_java;
import java.util.Scanner;
public class Board {

    
    public final int DIM = 8;
    //private final String[] backline = {"Piece", "Piece", "Piece", "Piece", "Piece", "Piece", "Piece", "Piece"};
    public Piece casella[][] = new Piece[8][8];
    Coord cordinata[] = new Coord[32];
    boolean whitecheck = false;
    boolean blackcheck = false;
    Piece re = new Piece("King", cordinata[4], true, true);;
    //Sets up the board
   	public Board()
    {

    }
    public Board(boolean buleano)
    {
                    //add stringa pezzo e cordinata 
                    cordinata[0] = new Coord(0,0);
                    casella[0][0] = new Piece("Rook", cordinata[0], true, true);
                    cordinata[1]= new Coord(1,0);
                    casella[1][0] = new Piece("Knight", cordinata[1], true, true);
                    cordinata[2]= new Coord(2,0);
                    casella[2][0] = new Piece("Bishop", cordinata[2], true, true);
                    cordinata[3]= new Coord(3,0);
                    casella[3][0] = new Piece("Queen", cordinata[3], true, true);
                    cordinata[4]= new Coord(4,0);
                    casella[4][0] = new Piece("King", cordinata[4], true, true);
                    cordinata[5]= new Coord(5,0);
                    casella[5][0] = new Piece("Bishop", cordinata[5], true, true);
                    cordinata[6]= new Coord(6,0);
                    casella[6][0] = new Piece("Knight", cordinata[6], true, true);
                    cordinata[7]= new Coord(7,0);
                    casella[7][0] = new Piece("Rook", cordinata[7], true, true);
                
                for(int x=0;x<8;x++){
                	for (int y=2;y<6;y++ ) {
                		casella[x][y] = null;
                   	}
                }

                for(int x=0;x<8;x++){
                    cordinata[x+8]= new Coord(x,1);
                    casella[x][1] = new Piece("Pawn", cordinata[x+8], true, true);
                    cordinata[x+16]= new Coord(x,6);
                    casella[x][6] = new Piece("Pawn", cordinata[x+16], false, true);
                }
                
                    cordinata[24]= new Coord(0,7);
                    casella[0][7] = new Piece("Rook", cordinata[24], false, true);
                    cordinata[25]= new Coord(1,7);
                    casella[1][7] = new Piece("Knight", cordinata[25], false, true);
                    cordinata[26]= new Coord(2,7);
                    casella[2][7] = new Piece("Bishop", cordinata[26], false, true);
                    cordinata[27]= new Coord(3,7);
                    casella[3][7] = new Piece("Queen", cordinata[27], false, true);
                    cordinata[28]= new Coord(4,7);
                    casella[4][7] = new Piece("King", cordinata[28], false, true);
                    cordinata[29]= new Coord(5,7);
                    casella[5][7] = new Piece("Bishop", cordinata[29], false, true);
                    cordinata[30]= new Coord(6,7);
                    casella[6][7] = new Piece("Knight", cordinata[30], false, true);
                    cordinata[31]= new Coord(7,7);
                    casella[7][7] = new Piece("Rook", cordinata[31], false, true);
    }

    public void addPiece(Piece pezzo){
    	this.casella[pezzo.getPosition().getX()][pezzo.getPosition().getY()] = pezzo;

    	return;
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
        if(this.casella[pos.getX()][pos.getY()] != null){

            return this.casella[pos.getX()][pos.getY()];
        }
        else
            return null;
    }
    //casella iniziale e casella finale
    //Moves piece to given coordinates
    public boolean movePiece(Coord init, Coord pos)
    //public void movePiece(Piece piece, Coord pos)
    {
    	
    	Piece piece =  this.casella[init.getX()][init.getY()];

    	//giacomo
		if(isCastling(init, pos)==true){
        	System.out.println("e' un arrocco!");
        }
       
        //prevedo il suicidio del re
        if("King".equals(piece.getType())){
	        for (int x=0;x<DIM;x++) {
				for (int y=0;y<DIM;y++) {
					if(this.casella[x][y]!=null){
						if(piece.getColor()!=this.casella[x][y].getColor()){
							for (int z=0;z<(this.casella[x][y].move(this,this.casella[x][y].getPosition()).length);z++ ) {
								if ("Pawn".equals(this.casella[x][y].getType()) && (this.casella[x][y].move(this, this.casella[x][y].getPosition())[z].getY()==(y+1) ||
									 this.casella[x][y].move(this, this.casella[x][y].getPosition())[z].getY()==(y-1))){ 	
									continue;
								}
								else if(this.casella[x][y].move(this, this.casella[x][y].getPosition())[z].getX()==pos.getX() &&
									 this.casella[x][y].move(this, this.casella[x][y].getPosition())[z].getY()==pos.getY()){
									System.out.println("vuoi fare suicidare il re??");
									return false;
								}
							}
						}
					}	        			        		
	        	}
        	}
        }
        

        
        Piece temp = this.casella[pos.getX()][pos.getY()];
        //sposta il pezzo
        this.casella[piece.getPosition().getX()][piece.getPosition().getY()] = null;
        this.casella[pos.getX()][pos.getY()] = piece.setPosition(pos);
        
        //trova la posizione del re avversario
        for (int x=0;x<DIM;x++) {
			for (int y=0;y<DIM;y++) {
				if(this.casella[x][y]!=null){
					if(piece.getColor()==this.casella[x][y].getColor()){
				        if("King".equals(casella[x][y].getType())){
				        	re = this.casella[x][y];
				        }
				    }
			    }
			}
		}
        
        //controllo che la mossa che faccio levi dalla merda il re
        // da verificare con la pedina che va avanti

        for (int x=0;x<DIM;x++) {
			for (int y=0;y<DIM;y++) {
				if(this.casella[x][y]!=null){
					if(piece.getColor()!=this.casella[x][y].getColor()){
						for (int z=0;z<(this.casella[x][y].move(this,this.casella[x][y].getPosition()).length);z++ ) {
							if(this.casella[x][y].move(this,this.casella[x][y].getPosition())[z].getX() == re.getPosition().getX() && 
								this.casella[x][y].move(this,this.casella[x][y].getPosition())[z].getY() == re.getPosition().getY()){
								System.out.println("non puoi fare questa mossa perche metti il re nella merda");
								this.casella[init.getX()][init.getY()] = piece.setPosition(init);
        						this.casella[pos.getX()][pos.getY()] = temp;
								return false;
								//se nessuna mossa è disponibile, allora stallo, se prima era scacco e nessuna mossa è disponibile allora scacco matto
							}
						}
					}
				}	        			        		
	       	}
        }


        String promozione;
        Scanner input = new Scanner(System.in);
        
        //qui funzione che verifica se è ancora in scacco e mette check a false
        //bianco


        //qui la verifica dello scacco
      /*  if(checkmateKing(pos)==true){
        	System.out.println("SCACCO");
        	//bianco
        	if(piece.getColor()==false){
        		this.blackcheck=true;
        	}
        	else if(piece.getColor()==true) {
        		this.whitecheck=true;	
        	}
        }
*/
        //public boolean isEvolving(Coord init, Coord pos)
        if(this.casella[pos.getX()][pos.getY()].getType()=="Pawn"){
        	if (pos.getY()==7 || pos.getY()==0) {
        		System.out.println("ecco la promozione");
        		promozione=input.nextLine();
        		this.casella[pos.getX()][pos.getY()].setType(promozione);
        	}
        	/*else if (pos.getY()==0) {
        		System.out.println("ecco la promozione");
        		promozione=input.nextLine();
        		this.casella[pos.getX()][pos.getY()].setType(promozione);
        	}*/
        }
        return true;
    }
    
    //board colore
    public boolean checkmateKing(boolean color){
    	boolean check = false;
    	Coord[] pos_Avable;
    	Piece king = null;
    	//trova il re del colore scelto
    	for (int x=0;x<DIM;x++) {
			for (int y=0;y<DIM;y++) {
				if(this.casella[x][y]!=null){
					if(color==this.casella[x][y].getColor()){
				        if("King".equals(casella[x][y].getType())){
				        	king = this.casella[x][y];
				        }
				    }
			    }
			}
		}
		//verifica che è sotto scacco
    	//confronto le caselle di colore opposto con la posizione del re e se è uguale ritorno true ovvero scacco al mio re di colore color
    	for (int x=0;x<DIM;x++) {
			for (int y=0;y<DIM;y++) {
				if(this.casella[x][y]!=null){
			    	if(color!=this.casella[x][y].getColor()){
						for (int z=0;z<(this.casella[x][y].move(this,this.casella[x][y].getPosition()).length);z++ ) {	
							if ("Pawn".equals(this.casella[x][y].getType()) && (this.casella[x][y].move(this, this.casella[x][y].getPosition())[z].getX()==(x) &&
									(this.casella[x][y].move(this, this.casella[x][y].getPosition())[z].getY()==(y+1) ||
									 this.casella[x][y].move(this, this.casella[x][y].getPosition())[z].getY()==(y-1)))){ 
								continue;
								}

							else if(this.casella[x][y].move(this, this.casella[x][y].getPosition())[z].getX()==king.getPosition().getX() &&
									 this.casella[x][y].move(this, this.casella[x][y].getPosition())[z].getY()==king.getPosition().getY()){
								check = true;
							}
						}
					}
				}
			}
		}
    	return check;
    }


    //giacomo cordinata finale 
    public boolean isEvolving(Coord pos){
    	boolean isEvolving = false;

    	if("Pawn".equals(this.casella[pos.getX()][pos.getY()].getType())){
        	if (pos.getY()==7 || pos.getY()==0) {
        	isEvolving=true;	
        	}
        }
    	return isEvolving;
    }

    //giacomo
    public boolean isEmpty(Coord pos){
    	boolean empty = false;
    	if ((this.casella[pos.getX()][pos.getY()])==null) {
    		empty=true;
    	}
    	return empty;
	}

   	public boolean isCastling(Coord init, Coord pos){
   		boolean castling = false;   		
   		//seleziona re - mai toccato - distanza due -> muovi torre
    	if("King".equals(this.casella[init.getX()][init.getY()].getType()) && this.casella[init.getX()][init.getY()].getUnmoved()==true && Math.abs((init.getX()-pos.getX()))==2){
    		
    		castling=true;
			//colore
			if(this.getPiece(init).getColor()==true){
	    		if (pos.getX()==2) {
	      			//arroco lungo
	       			this.casella[pos.getX()+1][pos.getY()]=this.casella[0][0];
	    	        this.casella[0][0] = null;
	    		}
	    		else if (pos.getX()==6) {
	    			//arroco corto
	    			this.casella[pos.getX()-1][pos.getY()]=this.casella[7][0];
	        		this.casella[7][0] = null;
	       		}
	    	}
	    	//colore
	    	else if (this.getPiece(init).getColor()==false){
	    		if (pos.getX()==2) {
	       			//arroco lungo
	    			this.casella[pos.getX()+1][pos.getY()]=this.casella[0][7];
	        		this.casella[0][7] = null;
	    		}
	    		else if (pos.getX()==6) {
	    			//arroco corto
	    			this.casella[pos.getX()-1][pos.getY()]=this.casella[7][7];
	        		this.casella[7][7] = null;
	     		}
	    	}
    	}
   		return castling;
   	}


	/*
	sCACCO = SE TERMINO LA MIA MOSSA E NELLA PROSSIMA MOSSA, DOPO QUELLA AVVERSA POSSO MANGIA IL RE ALLORA E' SCACCO
	SCACCO MATTO = SE IL RE ERA SOTTO SCACCO E
	*/

    public void displayBoard()
    {   System.out.println(" ");
        System.out.println();
        System.out.println("NERI");
        System.out.println();
        for (int y = 0; y < DIM; y++)
        {
            if(y==0){
                 System.out.println("    A0    B1    C2    D3    E4    F5    G6    H7");
                 System.out.println();
            }
            for (int x = 0; x < DIM; x++)
            {
                if(x==0){
                    System.out.print(y+" ");        
                }
                if (this.casella[x][y] != null)
                    System.out.print(this.casella[x][y].getType() + " ");
                else
                    System.out.print("NULL ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("BIANCHI");
        System.out.println(" ");   
    }
}