package ShowNTell;

/**
 * Official
 * Manages the game. Relays communication between the components of the game.
 */

import java.util.*;

public class Official extends Observable{

    private String mBoard, mMove;
    private int mCount;
    private boolean VICTORY;
    private static final double
        DELAY_DEFAULT = 0,
        RUN_COUNT = 200; //maximum moves before aborting game

    public static final int
        BOARD_SIZE = 18;

    private static final String
        SPLIT_PHRASE = "SPLITSPLIT",
        SUPER_SPLIT = "SPLITSPLITSPLIT",
        AI_RELAY = "m",
        COLLISIONS = "c",
        GRID = "g";

    public Official(){
        mBoard = getDefaultStartBoard();
        mCount = 0;
        VICTORY = false;
    }

    public void setVictory(boolean vict){
        VICTORY = vict;
    }

    public String getDefaultStartBoard(){
        ArrayList<Integer> iBoard = new ArrayList<>();
        //build teams
        int size = BOARD_SIZE;
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

    //tell the Halma Messenger to request moves
    private Official getRemoteAIMoves(String inBoard){
        send(AI_RELAY, inBoard);
        return this;
    }

    private Official setBoard(String inBoard){
        mBoard = inBoard;
        mCount++;
        return this;
    }

    public Official delay(double seconds){
        try{
            Thread.sleep( (int) (seconds * 1000) );
        }
        catch(InterruptedException e){}
        return this;
    }

    private Official output(String message){
        System.out.println(message);
        return this;
    }

    private Official setMove(String inMove){
        mMove = inMove;
        return this;
    }

    //receive a reply from an observer, and act accordingly
    public void reply(String sender, String message){
        if( AI_RELAY.equals(sender) )
            output("From M: " + message)
            .setMove(message)
            .send( COLLISIONS , composeForCollisions(message) );
        else if ( COLLISIONS.equals(sender) && mCount < RUN_COUNT){
            output("From C: " + message)
            .setBoard(message.substring(1))
            .send( GRID, composeForGameBoard(message, mMove))
            .delay(DELAY_DEFAULT);
            if (!VICTORY) this.getRemoteAIMoves( message.substring(1) );
        }
    }

    private static String concat(String inFront, String inTail){
        return inFront + SPLIT_PHRASE + inTail;
    }

    private String composeForGameBoard(String inBoard, String inMoves){
        return concat(inBoard, inMoves);
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
