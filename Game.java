package scacchi_java;
//import java.util.*;
//ArrayList<TipoDato> = new ArrayList<TipoDato>();
public class Game {

    public static void main(String [] args)
    {

        //Creates players and board
        Player p1 = new Player();
        Player p2 = new Player();
        Board board = new Board();
        Coord[] command = new Coord[2];
        Piece casella;
        Coord[] pos_Avable;
        Boolean turno=false;
        //int mosse_disp=0;
        while(true){
            //Return players command coordinates (current and target)
            board.displayBoard();

            if(turno ==true){
                System.out.println("TURNO NERI");
                //prima selezione della calsella (selezione della pedina da muovere)
                command[0] = p1.getCommand();
                //System.out.println(command[0].getX()+" "+command[0].getY());
                casella = board.getPiece(command[0]);

                if (casella != null && casella.getColor() == p1.getColor()){
                    
                    pos_Avable = casella.move(board, command[0]);
                    //se non ci sono posizioni disponibili esci dal ciclo
                    if(pos_Avable[0]==null){
                        System.out.println("non puoi muovere questo pezzo");
                        continue;
                    }
                    /*for(int x=0;pos_Avable[x]!=null;x++){
                    System.out.println(x+" "+pos_Avable[x].getX()+" "+pos_Avable[x].getY());
                    }*/
                    //seconda selezione della casella (selezione della casella in cui vuole essere spostata la pedina)
                    command[1]=p1.getCommand();
                    //inserire controllo numeri e lettere
                    for (int x=0; pos_Avable[x]!=null;++x ) {
                                          
                        if((command[1].getX()==pos_Avable[x].getX()) && (command[1].getY()==pos_Avable[x].getY())){
                            //se la casella e' occupata da un pezzo del proprio colore
                            //if deprecato.... da cancellare
                            /*if(board.getPiece(command[1])!=null && board.getPiece(command[1]).getColor()==true){
                                System.out.println("la casella e' gia occupata");
                                break;
                            }
                            else{*/
                                //marca mossi

                                //mossa della pedina 

                                board.movePiece(casella, command[1]);
                                board.getPiece(command[1]).setUnmoved(false);
                                turno=false;
                                break;
                            }
                        /*
                        else
                            System.out.println("mossa non valida1");
                        continue;   
                    }
                }*/
                        else if(pos_Avable[x+1]==null){  
                            System.out.println("coordinate non valide");
                            continue;
                        }
                    }
                
                    continue;
                }
            }
            
            if(turno ==false){    
                    
                System.out.println("TURNO BIANCHI");
                command[0] = p2.getCommand();                    
                //Gets casella from supplied coordinates
                casella = board.getPiece(command[0]);
                
                if (casella != null && casella.getColor() == p2.getColor()){   
                        
                    pos_Avable = casella.move(board, command[0]);
                    //casella.move(board, command[0]);
                    if(pos_Avable[0]==null){
                        System.out.println("non puoi muovere questo pezzo");
                        continue;
                    }
                    command[1] = p2.getCommand();
                    
                    for (int x=0; pos_Avable[x]!=null;++x ) {
                        
                        if((command[1].getX()==pos_Avable[x].getX()) && (command[1].getY()==pos_Avable[x].getY())){
                            
                            board.movePiece(casella, command[1]);
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