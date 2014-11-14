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
        ArrayList<Integer> iBoard = new ArrayList<Integer>();
        //build teams
        int size = 6;
        for(int x = 0; x < 3; x++){
            for(int y = size-3; y < size; y++){
                Piece red = new Piece(x, y, 0, 0);
                Piece blue = new Piece(y, x, 0, 1);
                iBoard.addAll( red.toIntList() );
                iBoard.addAll( blue.toIntList() );
                break;
            }
        }
        return iBoard.toString();
    }
    
    public void startBoard(String board){
        mBoard = board;
    }
    
    public void startGame(){
        getRemoteAIMoves( mBoard );
    }
    
    private Official getRemoteAIMoves(String inBoard){
        //send( AI_RELAY, inBoard);
        String [] moves = 
        {
            concat("[1,3,3,2]", "[5,5,4,4]"),
            concat("[3,2,1,2]", "[4,5,5,5]")
        };
        reply(AI_RELAY, moves[mCount % moves.length] );
        return this;
    }
    
    private Official setBoard(String inBoard){
        mBoard = inBoard;
        mCount++; 
        return this;
    }
    public Official delay(int seconds){
    	try{
    		Thread.sleep(seconds * 1000);
    	}
    	catch(Exception e){e.printStackTrace();}
    	return this;
    }
    public void reply(String sender, String message){
        if( AI_RELAY.equals(sender) ){ 
            send( COLLISIONS , composeForCollisions(message) );
        }
        else if ( COLLISIONS.equals(sender) && mCount < 10)
            setBoard(message).send( GRID, message).delay(1)
            	.getRemoteAIMoves( message );
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