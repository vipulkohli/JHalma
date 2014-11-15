import java.util.*;

public class Official extends Observable{
    
    /*private static final 
    	String [] TEST_MOVES = 
        {
            //concat(redTeamMove, blueTeamMove)
            concat("[0,0,0,0]", "[0,0,0,0]"),
            concat("[2,3,2,2]", "[3,2,2,2]"),
            concat("[1,3,3,1]", "[3,0,2,0]"),
            concat("[3,1,2,1]", "[3,1,3,0]"),
            concat("[0,3,0,2]", "[2,0,1,0]"),
            concat("[0,2,0,1]", "[2,2,3,3]"),
            concat("[0,1,0,0]", "[1,0,0,0]"),
            concat("[0,0,0,1]", "[3,3,3,2]"),
            concat("[2,3,2,2]", "[3,2,2,2]"),
            concat("[0,1,0,2]", "[2,2,1,3]"),
            concat("[0,2,1,3]", "[0,0,1,0]")		
        };*/
    
    private String mBoard;
    private int mCount;
    private static final int
    	DELAY_DEFAULT = 3,
    	RUN_COUNT = 1000;//TEST_MOVES.length;
    private static final String
        SPLIT_PHRASE = "SPLITSPLIT",
        SUPER_SPLIT = "SPLITSPLITSPLIT",
        //START_BOARD = "[-1,-2,4,0,-1,-2,4,1]",
        AI_RELAY = "m",
        COLLISIONS = "c",
        GRID = "g";
    
    public Official(){
        mBoard = getDefaultStartBoard();
        mCount = 0;
    }
    
    public String getDefaultStartBoard(){
        ArrayList<Integer> iBoard = new ArrayList<>();
        //build teams
        int size = 18;
        for(int x = 0; x < 3; x++){
            for(int y = 0; y < 4; y++){
                Piece red = new Piece(x, size-1-y, 0, 0);
                Piece blue = new Piece(size-1-x, size-1-y, 0, 1);
                iBoard.addAll( red.toIntList() );
                iBoard.addAll( blue.toIntList() );
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
        send(AI_RELAY, inBoard);
        
        //reply(AI_RELAY, TEST_MOVES[mCount % TEST_MOVES.length] ); //use test moves (comment send too)
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
    	catch(InterruptedException e){e.printStackTrace();}
    	return this;
    }
    
    public void reply(String sender, String message){
        if( AI_RELAY.equals(sender) ){ 
            send( COLLISIONS , composeForCollisions(message) );
        }
        else if ( COLLISIONS.equals(sender) && mCount < RUN_COUNT)
            setBoard(message).send( GRID, message).delay(DELAY_DEFAULT)
            	.getRemoteAIMoves( message );
    }
    
    private static String concat(String inFront, String inTail){
        return inFront + SPLIT_PHRASE + inTail;
    }
    
    private String composeForCollisions(String AIMoves){
        return concat(mBoard, AIMoves);
    }
    
    protected Official send(String recipient, String message){
        setChanged();
        notifyObservers(recipient + SUPER_SPLIT + message);
        return this;
    }
	
}
