import java.util.*;

public class Official extends Observable{
    
    private String mBoard;
    private int mCount;
    private static final String
        SPLIT_PHRASE = "SPLITSPLIT",
        SUPER_SPLIT = "SPLITSPLITSPLIT",
        START_BOARD = "[-1,-2,4,0,-1,-2,4,1]",
        AI_RELAY = "m",
        COLLISIONS = "c",
        GRID = "g";
    
    public Official(){
        mBoard = getDefaultStartBoard();
        mCount = 0;
    }
    
    public String getDefaultStartBoard(){
        return START_BOARD;
    }
    
    public void startBoard(String board){
        mBoard = board;
    }
    public void startGame(){
        getRemoteAIMoves( mBoard );
    }
    
    private Official getRemoteAIMoves(String inBoard){
        send( AI_RELAY, inBoard);
        return this;
    }
    
    private Official upCount(){
        mCount++; 
        return this;
    }
    
    public void reply(String sender, String message){
        if( AI_RELAY.equals(sender) ){ 
            send( COLLISIONS , composeForCollisions(message) );
            System.out.println(message);
        }
        else if ( COLLISIONS.equals(sender) && mCount < 5)
            upCount().send( GRID, message).getRemoteAIMoves( message );
    }
    
    private static String concat(String inFront, String inTail){
        return inFront + SPLIT_PHRASE + inTail;
    }
    
    private String composeForCollisions(String AIMoves){
        return concat(mBoard , AIMoves);
    }
    
    protected Official send(String recipient, String message){
		setChanged();
		notifyObservers(recipient + SUPER_SPLIT + message);
        return this;
	}
	
}
