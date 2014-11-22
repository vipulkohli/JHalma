package ShowNTell;

/**
 * @(#)Program.java
 * Includes GameBoard class
 *
 * @author Vipul Kohli
 * @author Andrew Socha
 * @version 1.00 2014/4/27
 */

import com.grack.nanojson.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Program extends Thread {
    
    public static void main(String[] args){
        String player1 = "http://lyle.smu.edu/~tbgeorge/cse4345/a1/getMove.php";
     	String player2 = "http://lyle.smu.edu/~sochaa/4345/FinalHalma/finalHalmaWithDamage.php";
     	
     	HalmaGame [] tournament = {
     	
     		new HalmaGame( player1, player2, "Tyler", "Andrew" ),
     		new HalmaGame( player1, player1, "Tyler", "Tyler" )
     	
     	};
    }
    
}

class GameBoard extends OfficialObserver{
    
    private static final Color	
    	TEAM_A_COLOR = new Color(204,0,153),
        TEAM_B_COLOR = new Color(0,102,153),
        TEXT_BGCOLOR = Color.white,
        TEXT_SELECTION_COLOR = Color.red;
        
    
    private static final Font
    	FONT = new Font("Times New Roman", Font.BOLD, 20);
    
    private static final String
        MY_EMAIL = "g",
        TIMER = "Move: ",
        HALMATE = "HALMATE!  ",
        TEAM_A_WINS = "Red Team Victory!",
        TEAM_B_WINS = "Blue Team Victory!",
        SPLIT_PHRASE = "SPLITSPLIT";
        
    private final HalmaWorld
    	WORLD = new HalmaWorld(this);
    
    private static Integer numInstances;
    
    private static final int
    	BOARD_FRAME_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
    	BOARD_FRAME_HEIGHT = 850,
    	CELL_SIZE = BOARD_FRAME_WIDTH / 25,
    	//BOARD_SIZE = super.BOARD_SIZE,
    	TIMER_START = 0;
    	
    private final String
    	mWorldMessage,
    	mTeamA,
    	mTeamB;
   
    private int 
    	mTimer;
    
    private final ArrayList<String> 
    	ALL_MOVES = new ArrayList<String>(),
    	PAST_MOVES = new ArrayList<String>();
    
    public GameBoard(String teamA, String teamB){
    	if(numInstances == null)
    		numInstances = 0;
    	else
    		numInstances++;
    	mTeamA = "\n" + teamA + ": ";
        mTeamB = "\n" + teamB + ": ";
        mWorldMessage = "Press \"Step\" to begin: " + teamA + " vs. " + teamB
        	+ "\n\nCheck internet connection.";
        WORLD.setMessage( mWorldMessage );
    	WORLD.setGrid( new HalmaGrid("") );
    	WORLD.show( BOARD_FRAME_WIDTH, BOARD_FRAME_HEIGHT );
    	setTitle( WORLD, "HalmaWorld - " + teamA + " vs. " + teamB );
    	centerWorldOnScreen( WORLD, numInstances);
    	setTextArea( WORLD, FONT);
        mTimer = TIMER_START;
        setCellSize( WORLD, CELL_SIZE );
    }
    
    
    /** 
     * The following methods are 
     * derived from WorldFrame.java
     * or GridPanel.java
     */
     
    public static void setTitle(HalmaWorld inWorld, String title){
    	inWorld.getFrame().setTitle( title );
    }
    
    public static void setCellSize(HalmaWorld inWorld, int size){
    	inWorld.getFrame().getGridPanel().setCellSize( size );
    }
    
    public static void setZoom(HalmaWorld inWorld, double inFactor){
    	inWorld.getFrame().getGridPanel().zoom(inFactor);
    }
    
    public static void setTextArea(HalmaWorld world, Font font){
    	JTextArea messageArea = world.getFrame().getMessageArea();
    	messageArea.setFont( font );
        messageArea.setEditable( true );
        messageArea.setFocusable( true );
        messageArea.setBackground( TEXT_BGCOLOR );
        messageArea.setSelectionColor( TEXT_SELECTION_COLOR );
    }
    
    private static void centerWorldOnScreen(HalmaWorld world, int instances){
    	world.getFrame().setLocation( BOARD_FRAME_WIDTH * (instances % 2), 0 );
    }
    
    @Override
    protected void handleUpdate(){
        if( !super.checkRecipient( MY_EMAIL ) )
            return;
        if(mTimer == TIMER_START){
        	drawBoard( super.getMessage() );
        	WORLD.setMessage( "Click 'Step' or 'Run' to Continue | " + WORLD.getMessage() );
        }
        ALL_MOVES.add( super.getMessage() ); 
    }
    
    @Override
    public boolean equals(Object o){
    	int size = ALL_MOVES.size();
    	boolean out = super.equals(o);
    	if( "step".equals( o ) && size > 0){
    		if(mTimer == size)
    			return out;
    		drawBoard( ALL_MOVES.get( mTimer ) );
    	}
    	if( "rewind".equals( o ) && size > 0){
    		if(mTimer - 2 < 0)
    			return out;
			mTimer -= 2;
    		drawBoard( ALL_MOVES.get( mTimer ) );
    	}
    	return out;
    }
    
    private static ArrayList<Piece> toPieceList(String officialData){
        ArrayList<Piece> list = new ArrayList<>();
        JsonArray array;
        try{ array = JsonParser.array().from(officialData);  }
        catch(JsonParserException e){ return null; }
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
    
    private void highlightDestinations(){
    	for(int x = 0; x < 3; x++){
    		for(int y = 0; y < 3; y++){
    			Glitter g = new Glitter();
    			g.setColor( TEAM_B_COLOR );
    			WORLD.add(new Location( x, y ), g);
    		}
    	}
    	for(int row = 0; row < 3; row++){
    		for(int col = BOARD_SIZE - 1; col >= BOARD_SIZE - 3; col--){
    			Glitter g = new Glitter();
    			g.setColor( TEAM_A_COLOR );
    			WORLD.add(new Location( row , col), g);
    		}
    	}
    }
    
    private void clearBoard(){
    	for(int x = 0; x < BOARD_SIZE; x++){
    		for(int y = 0; y < BOARD_SIZE; y++){
    			Object obj = WORLD.remove( new Location(y,x) );
    			if(obj instanceof Piece){
    				Piece p = (Piece) obj;
    				Flower a = new Flower();
    				a.setColor( p.getColor() );
    				WORLD.add(new Location(y,x), a);
    			}
    		}
    	}
    }
    
    //need to correct for winner situation
    private static int isNewMove(String team1Move, String team2Move, ArrayList<String>past){
    	if(true)
    		return 0;
    	for (String oldMove : past){
    		if( oldMove.equals(team1Move) )
    			return 1;
    		if( oldMove.equals(team2Move) )
    			return 2;
    	}
    	if( past.isEmpty() ){
    		past.add(team1Move);
    		past.add(team2Move);
    	}
    	past.set(0, team1Move);
    	past.set(1, team2Move);
    	return 0;
    }
    
    private String upTimer(){
    	mTimer++;
    	return "" + mTimer;
    }
    
    private static Location getToLocation(String move){
    	ArrayList<Location> moveLocs = toLocationList(move);
    	Location target = moveLocs.get( moveLocs.size() - 1 );
    	return new Location(target.getRow(), target.getCol());
    }
    
    private void addToPieces(String team1Move, String team2Move, HalmaWorld world){
    	Location
    		redLoc = getToLocation( team1Move ), 
    		blueLoc = getToLocation( team2Move );
    	XPiece
    		redPiece = new XPiece(),
    		bluePiece = new XPiece();
    	redPiece.setColor( TEAM_A_COLOR );
    	bluePiece.setColor( TEAM_B_COLOR );
    	world.add(redLoc, redPiece);
    	world.add(blueLoc, bluePiece);
    }
    
    private static ArrayList<Location> toLocationList(String move){
    	JsonArray array;
    	ArrayList<Location> locs = new ArrayList<Location>();
    	try{ array = JsonParser.array().from(move); }
    	catch(JsonParserException e){
    		return null;
    	}
        int x;
    	ArrayList<Integer> coordList = new ArrayList<Integer>();
        for(int k = 0; k < array.size(); k++)
                coordList.add( array.getInt(k)  );
        Iterator<Integer> itr = coordList.iterator();
        
        x = itr.next();
        locs.add( new Location(itr.next(), x) );
        itr.next(); //skip damage
        while(itr.hasNext()){
                x = itr.next();
                locs.add( new Location(itr.next(), x) );
        }
        return locs;
    }
    
    private static String formatMove(String move){
    	JsonArray array;
    	try{ array = JsonParser.array().from(move); }
    	catch(JsonParserException e){
    		return move;
    	}
        int x;
    	ArrayList<Integer> coordList = new ArrayList<Integer>();
        for(int k = 0; k < array.size(); k++)
                coordList.add( array.getInt(k)  );
        Iterator<Integer> itr = coordList.iterator();
        ArrayList<Location> locs = new ArrayList<Location>();
        
        x = itr.next();
        locs.add( new Location(itr.next(), x) );
        itr.next(); //skip damage
        while(itr.hasNext()){
                x = itr.next();
                locs.add( new Location(itr.next(), x) );
        }
        return locs.toString();
    }
    
    public static Piece createDamagedPiece(int damage, Color color){
    	Piece [] damageCounts ={
    		new One(),
    		new Two(),
    		new Three(),
    		new Four(),
    		new Five()	
    	};
    	if(damage < 5)
    		damageCounts[ damage - 1 ].setColor(color);
    	return damageCounts[ damage - 1 ];
    }
    
    protected void drawBoard(String inData){
    	clearBoard();
    	highlightDestinations();
    	String onMessageField, p1Move, p2Move, pieceStr;
    	int winner;
    	ArrayList<Piece> pieces;
    	String [] data = inData.split( SPLIT_PHRASE );
    	pieceStr = data[0];
    	p1Move = data[1];
    	p2Move = data[2];
    	onMessageField = TIMER + upTimer() + mTeamA 
    		+ formatMove(p1Move) + mTeamB + formatMove(p2Move);
    	winner = isNewMove(p1Move, p2Move, PAST_MOVES );
    	if( winner == 1)
    		onMessageField = HALMATE + TEAM_A_WINS;
    	if( winner == 2 )
    		onMessageField = HALMATE + TEAM_B_WINS;
        pieces = toPieceList( pieceStr ) ;
        print( pieces.toString() );
        for (Piece p : pieces){
            if(p.team == 0)
                p.setColor( TEAM_A_COLOR );
            else
                p.setColor( TEAM_B_COLOR );
            //GridWorld locations are (row, column);
            Location cell = new Location( p.y , p.x );
            WORLD.add(cell, p);
        }//end for loop
		addToPieces(p1Move, p2Move, WORLD);
      	for (Piece p : pieces){
      		WORLD.setMessage( onMessageField );
      		if(p.damage > 0)
    			WORLD.add(p.getXYLocation(), this.createDamagedPiece( p.damage, p.getColor() ));
      	}
    }

}
