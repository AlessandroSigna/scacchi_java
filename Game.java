package scacchi_java;
//import java.util.*;
//ArrayList<TipoDato> = new ArrayList<TipoDato>();
public class Game {

    public static void main(String [] args)
    {

        //Creates players and board
        Player p1 = new Player();
        Player p2 = new Player();
        Boolean turno1=false;
        Board board = new Board(turno1);
        Coord[] command = new Coord[2];
        Piece casella;
        Coord[] pos_Avable;
        Boolean turno=false;
        
        //inizia il gioco
        while(true){
            //stampa la plancia
            board.displayBoard();
            //commento solo per il nero, per il bianco è la stessa cosa
            if(turno ==true){
                System.out.println("TURNO NERI");
                //prima selezione della calsella (selezione della pedina da muovere)
                command[0] = p1.getCommand();
                casella = board.getPiece(command[0]);
                //se la casella selezionata è diversa da null ed è dello stesso colore del giocatore
                if (casella != null && casella.getColor() == p1.getColor()){
                    System.out.println("stai muovendo il pezzo "+ casella.getType()+" con coordinata "+command[0].getX()+" "+command[0].getY() + " ,  puo andare");
                    //array delle mosse disponibili per il pezzo selezionato
                    pos_Avable = casella.move(board, command[0]);
                    //stampa mosse
                    for(int x=0;x<pos_Avable.length;x++){
                			System.out.println(pos_Avable[x].getX()+" "+pos_Avable[x].getY());	        
                	}
                    System.out.println("la dimensione dell array e' "+pos_Avable.length);
                    //se non ci sono posizioni disponibili esci dal ciclo
                    if(pos_Avable[0]==null){
                        System.out.println("non puoi muovere questo pezzo");
                        continue;
                    }
                    //seconda selezione della casella (selezione della casella in cui vuole essere spostata la pedina)
                    command[1]=p1.getCommand();
                    //cicla per le posizioni disponibili
                    for (int x=0; pos_Avable[x]!=null;++x ) {
                    	//se il comando inserito è tra le posizioni disponibili 
                        if((command[1].getX()==pos_Avable[x].getX()) && (command[1].getY()==pos_Avable[x].getY())){
                          	
                          	//mossa della pedina, se torna false qualcosa è andato storto e ricomincia da TURNO NERI
                            if(!board.movePiece(casella.getPosition(), command[1])){
                            	
                            	break;
                            }
                            board.checkmateKing(turno);
                            board.checkmateKing(!turno);
                            //se la mossa è andata a buon fine segna la pedina come mossa e passa il turno all'avversario
                            board.getPiece(command[1]).setUnmoved(false);
                            turno=false;
                            break;
                            }
                        //credo non si esegua mai...
                        else if(pos_Avable[x+1]==null){  
                            System.out.println("coordinate non valide");
                            continue;
                        }
                    }
                	//nel caso in cui nulla di sopra si verifica riesegui il tutto
                    continue;
                }
            }
            
            if(turno ==false){    
                    
                System.out.println("TURNO BIANCHI");
                command[0] = p2.getCommand();                    
                
                casella = board.getPiece(command[0]);
                
                if (casella != null && casella.getColor() == p2.getColor()){   
                	System.out.println("stai muovendo il pezzo "+ casella.getType()+" con coordinata "+command[0].getX()+" "+command[0].getY() + " ,  puo andare");     
                    pos_Avable = casella.move(board, command[0]);
                    
                    for(int x=0;x<pos_Avable.length;x++){
                		System.out.println(pos_Avable[x].getX()+" "+pos_Avable[x].getY());	        
                	}
					System.out.println("la dimensione dell array e' "+pos_Avable.length);
                    if(pos_Avable[0]==null){
                        System.out.println("non puoi muovere questo pezzo");
                        continue;
                    }
                    command[1] = p2.getCommand();
                    
                    for (int x=0; pos_Avable[x]!=null;++x ) {
                          
                        if((command[1].getX()==pos_Avable[x].getX()) && (command[1].getY()==pos_Avable[x].getY())){  
                            if(!board.movePiece(casella.getPosition(), command[1])){
                            	
                            	break;
                            }
                            board.checkmateKing(turno);
                            board.checkmateKing(!turno);
                            board.getPiece(command[1]).setUnmoved(false);
                            turno=true;
                            break;
                        }
                        
                        else if(pos_Avable[x+1]==null){  
                            System.out.println("coordinate non valide");
                            continue;
                        }
                    }
                continue;
                }
            }
        }
    }
}