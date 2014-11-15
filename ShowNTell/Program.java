/**
 * @(#)Program.java
 *Includes GameBoard class
 *
 * @author 
 * @version 1.00 2014/4/27
 */
import com.grack.nanojson.*;
import info.gridworld.actor.*;
import info.gridworld.grid.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Program {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        String player1 = "http://lyle.smu.edu/~sochaa/4345/FinalHalma/finalHalmaWithDamage.php";
     	String player2 = "http://lyle.smu.edu/~sochaa/4345/FinalHalma/finalHalmaWithDamage.php";
     	new HalmaGame(player1, player2);  
    }
}

class GameBoard extends OfficialObserver{
    
    private static final String
        MY_EMAIL = "g",
        TIMER = "Move: ",
        TEAM_A = "\nRED: ",
        TEAM_B = "  BLUE: ",
        HALMATE = "HALMATE!",
        SPLIT_PHRASE = "SPLITSPLIT";
    private static final ActorWorld
    	WORLD = new ActorWorld();
    private static final int
    	TIMER_START = 0,
    	BOARD_SIZE = 18;
    private int mTimer;
    private String mMove;
    public GameBoard(){
    	WORLD.setGrid( new BoundedGrid(BOARD_SIZE, BOARD_SIZE) );
    	WORLD.show();
        mTimer = TIMER_START;
        mMove = new String();
        //the most complicated way to ZOOM OUT ever
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_PAGE_DOWN);
            robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        } catch (AWTException ex) {
            Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void handleUpdate(){
        if( !super.checkRecipient( MY_EMAIL ) )
            return;
        drawBoard( super.getMessage() ); 
    }
    
    private ArrayList<Piece> toPieceList(String officialData){
        ArrayList<Piece> list = new ArrayList<>();
        JsonArray array;
		try{ array = JsonParser.array().from(officialData);  }
		catch(JsonParserException e){ e.printStackTrace(); return null; }
        int offset = 4;
        for(int k = 0; k < array.size(); k += offset)
            list.add( new Piece(
                array.getInt(k),
                array.getInt(k + 1),
                array.getInt(k + 2),
                array.getInt(k + 3)
            ) );
        return list;
    }    
    
    private void clearBoard(){
    	for(int x = 0; x < BOARD_SIZE; x++){
    		for(int y = 0; y < BOARD_SIZE; y++){
    			Object obj = WORLD.remove( new Location(x,y) );
    			if(obj instanceof Piece){
    				Piece p = (Piece) obj;
    				Actor a = new Flower();
    				a.setColor( p.getColor() );
    				WORLD.add(new Location(x,y), a);
    			}
    		}
    	}
    }
    
    private boolean isNewMove(String team1Move, String team2Move){
    	String inMoves = team1Move + team2Move;
    	if( mMove.equals(inMoves) )
    		return false;
    	mMove = inMoves;
    	return true;
    }
    
    private String upTimer(){
    	mTimer++;
    	return "" + mTimer;
    }
    protected void drawBoard(String inData){
    	clearBoard();
    	String onMessageField;
    	ArrayList<Piece> pieces;
    	String [] data = inData.split( SPLIT_PHRASE );
    	onMessageField = TIMER + upTimer() + TEAM_A + data[1] + TEAM_B + data[2];
    	if( !isNewMove(data[1], data[2] ) )
    		onMessageField = HALMATE;
        pieces = toPieceList( data[0] ) ;
        print( pieces.toString() );
        for (Piece p : pieces){
            switch (p.team){
                case 0: p.setColor( "red" );
                break;
                default: p.setColor( "blue" );
                break;
            }
            //GridWorld locations are (row, column);
            Location cell = new Location( p.y , p.x );
            WORLD.add(cell, p);
        }//end for loop
      	for (Piece p : pieces){
      		WORLD.setMessage( onMessageField );
      		switch(p.damage){
      			case 5:
    				Actor a = new Five();
    				WORLD.add(p.getXYLocation(), a);
    				break;
    			case 4:
    				a = new Four();
    				a.setColor(p.getColor());
    				WORLD.add(p.getXYLocation(), a);
    				break;
    			case 3:
    				a = new Three();
    				a.setColor(p.getColor());
    				WORLD.add(p.getXYLocation(), a);
    				break;
    			case 2:
    				a = new Two();
    				a.setColor(p.getColor());
    				WORLD.add(p.getXYLocation(), a);
    				break;
    			case 1:
    				a = new One();
    				WORLD.add(p.getXYLocation(), a);
    				break;
    		}
      	}
    		
    }

}
