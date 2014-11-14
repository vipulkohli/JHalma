import java.util.*;

public class Official extends Observable{
    
    private String mBoard;
    private static final String
        SPLIT_PHRASE = "SPLITSPLIT",
        SUPER_SPLIT = "SPLITSPLITSPLIT",
        START_BOARD = "[-1,-2,4,0,-1,-2,4,1]",
        AI_RELAY = "m",
        COLLISIONS = "c",
        GRID = "g";
    
    public Official(){
        mBoard = START_BOARD;
    }
    
    public void startGame(){
        getRemoteAIMoves( "inBoard" );
    }
    
    private void getRemoteAIMoves(String inBoard){
        send( AI_RELAY, inBoard);
    }
    
    public void reply(String sender, String message){
        if( AI_RELAY.equals(sender) ){ 
            send( COLLISIONS , composeForCollisions(message) );
        }
        else if ( COLLISIONS.equals(sender) )
            send( GRID, message);
    }
    
    private static String concat(String inFront, String inTail){
        return inFront + SPLIT_PHRASE + inTail;
    }
    
    private String composeForCollisions(String AIMoves){
        return concat(mBoard , AIMoves);
    }
    
    protected void send(String recipient, String message){
		setChanged();
		notifyObservers(recipient + SUPER_SPLIT + message);
	}
	
}