/**
 * @(#)Program.java
 * Creates one or more Halma games.
 * 
 * Includes GameBoard class,
 * which represents the board UI.
 *
 * @author Vipul Kohli
 * @author Andrew Socha
 * @version 11-27-2014
 */
package ShowNTell;
import com.grack.nanojson.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Program{
    
    public static void main(String[] args){
        String player1 = "http://lyle.smu.edu/~tbgeorge/cse4345/a1/getMove.php";
     	String player2 = "http://lyle.smu.edu/~sochaa/4345/FinalHalma/finalHalmaWithDamage.php";
        String player1Name = "Tyler";
        String player2Name = "Andrew";
        
        JTextField pfield1 = new JTextField(35);
        JTextField pfield2 = new JTextField(35);
        pfield1.setText(player1);
        pfield2.setText(player2);
        JTextField nfield1 = new JTextField(35);
        JTextField nfield2 = new JTextField(35);
        nfield1.setText(player1Name);
        nfield2.setText(player2Name);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        myPanel.add(new JLabel("Player 1 URL:    "));
        myPanel.add(pfield1);
        myPanel.add(new JLabel("Player 2 URL:  "));
        myPanel.add(pfield2);
        c.gridy = 1;
        myPanel.add(new JLabel("Player 1 Name: "), c);
        myPanel.add(nfield1, c);
        myPanel.add(new JLabel("  Player 2 Name: "), c);
        myPanel.add(nfield2, c);

        int result = JOptionPane.showConfirmDialog(null, myPanel, 
               "Please Enter Player Info", JOptionPane.DEFAULT_OPTION);

        player1 = pfield1.getText();
        player2 = pfield2.getText();
        player1Name = nfield1.getText();
        player2Name = nfield2.getText();
        
     	HalmaGame [] tournament = {
     		new HalmaGame( player1, player2, player1Name, player2Name ),
     		new HalmaGame( player1, player1, player1Name, player1Name )
     	};
    }
    
}

class GameBoard extends OfficialObserver{
    
    @Override
    protected void handleUpdate(){
        if( !super.checkRecipient( MY_EMAIL ) )
            return;
        if(mTimer == TIMER_START)
        	this.startGame();
        ALL_MOVES.add( super.getMessage() ); 
    }
    
    private static final Color	
    	TEAM_A_COLOR = new Color(204,0,153),
        TEAM_B_COLOR = new Color(0,102,153),
        TEXT_BGCOLOR = Color.white,
        TEXT_SELECTION_COLOR = Color.red;
   	
    private static final int
    	BOARD_FRAME_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
    	BOARD_FRAME_HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.98),
    	CELL_SIZE = BOARD_FRAME_WIDTH / 25,
    	TIMER_START = 0;
    
    private static final Font
    	FONT = new Font("Times New Roman", Font.BOLD, 20);
    
    private static final String
        MY_EMAIL = "g",
        TIMER = "Move: ",
        HALMATE = "HALMATE!  ",
        TEAM_A_WINS = "Red Team Victory!",
        TEAM_B_WINS = "Blue Team Victory!",
        START_MESSAGE = "Click 'Step' or 'Run' to Continue | ",
        SPLIT_PHRASE = "SPLITSPLIT";
        
    private final HalmaWorld
    	mWorld = new HalmaWorld(this);
    
    private static Integer numInstances;
    	
    private final String
    	mWorldMessage,
    	mTeamA,
    	mTeamB;
   
    private String mStart;
    
    private int 
    	mTimer;
    
    private final ArrayList<String> 
    	ALL_MOVES = new ArrayList<String>();
    
    public GameBoard(String teamA, String teamB){
    	if(numInstances == null)
    		numInstances = 0;
    	else
    		numInstances++;
    	mTeamA = "\n" + teamA + ": ";
        mTeamB = "\n" + teamB + ": ";
        mWorldMessage = "Press \"Step\" to begin: " + teamA + " vs. " + teamB
        	+ "\n\nCheck internet connection.";
        mWorld.setMessage( mWorldMessage );
    	mWorld.setGrid( new HalmaGrid("") );
    	mWorld.show( BOARD_FRAME_WIDTH, BOARD_FRAME_HEIGHT );
    	this.setTitle( mWorld, "HalmaWorld - " + teamA + " vs. " + teamB );
    	this.centerWorldOnScreen( mWorld, numInstances);
    	this.setTextArea( mWorld, FONT);
        mTimer = TIMER_START;
        this.setCellSize( mWorld, CELL_SIZE );
    }
    
    protected void startGame(){
    	mStart = super.getMessage();
        drawBoard( mStart );
        mWorld.setMessage( START_MESSAGE + mWorld.getMessage() );
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
    
    public static void setTextArea(HalmaWorld inWorld, Font inFont){
    	JTextArea messageArea = inWorld.getFrame().getMessageArea();
    	messageArea.setFont( inFont );
        messageArea.setEditable( true );
        messageArea.setFocusable( true );
        messageArea.setBackground( TEXT_BGCOLOR );
        messageArea.setSelectionColor( TEXT_SELECTION_COLOR );
    }
    
    private static void centerWorldOnScreen(HalmaWorld inWorld, int numInstances){
    	inWorld.getFrame().setLocation( BOARD_FRAME_WIDTH * (numInstances % 2), 0 );
    }

    
    protected void stepAhead(){
    	if( mTimer < ALL_MOVES.size() )
    		drawBoard( ALL_MOVES.get( mTimer ) );
    }
    
    protected void rewindMove(){
    	if( mTimer <= TIMER_START )
    		return;
		mTimer-=2;
		drawBoard( ALL_MOVES.get( mTimer ) );
    }
    
    protected void restartGame(){
    	mTimer = TIMER_START;
    	drawBoard( mStart );
    	clearFlowers( mWorld );
    }
    
    @Override
    public boolean equals(Object o){
    	boolean out = super.equals(o);
    	if( ALL_MOVES.size() <= 0 )
    		return out;
    	if( "step".equals( o ) )
    		this.stepAhead();
    	if( "restart".equals( o ) )
    		this.restartGame();
    	if( "rewind".equals( o ) )
    		this.rewindMove();
    	return out;
    }
    
    private static ArrayList<Piece> toPieceList(String officialData){
        ArrayList<Piece> list = new ArrayList<Piece>();
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
    
    public static void highlightDestinations( HalmaWorld world ){
    	for(int x = 0; x < 3; x++){
    		for(int y = 0; y < 3; y++){
    			Glitter g = new Glitter();
    			g.setColor( TEAM_B_COLOR );
    			world.add(new Location( x, y ), g);
    		}
    	}
    	for(int row = 0; row < 3; row++){
    		for(int col = BOARD_SIZE - 1; col >= BOARD_SIZE - 3; col--){
    			Glitter g = new Glitter();
    			g.setColor( TEAM_A_COLOR );
    			world.add(new Location( row , col), g);
    		}
    	}
    }
    
    public static void clearFlowers( HalmaWorld world ){
    	for(int x = 0; x < BOARD_SIZE; x++){
    		for(int y = 0; y < BOARD_SIZE; y++){
    			Object obj = world.getGrid().get( new Location(y,x) );
    			if(obj instanceof Flower){
    				world.remove( new Location(y,x) );
    			}
    		}
    	}
    }
    
    public static void clearBoard( HalmaWorld world ){
    	for(int x = 0; x < BOARD_SIZE; x++){
    		for(int y = 0; y < BOARD_SIZE; y++){
    			Object obj = world.remove( new Location(y,x) );
    			if(obj instanceof Piece){
    				Piece p = (Piece) obj;
    				Flower a = new Flower();
    				a.setColor( p.getColor() );
    				world.add(new Location(y,x), a);
    			}
    		}
    	}
    }
    
    
    
    //need to correct for winner situation
    public static int getWinner( HalmaWorld world, Object marker ){
    	Grid grid = world.getGrid();
    	int blues = 0, reds = 0;
    	for(int x = 0; x < grid.getNumCols(); x++){
    		for(int y = 0; y < grid.getNumRows(); y++){
    			Object o = grid.get( new Location(y, x) );
    			if( o != null && marker.getClass().equals( o.getClass() ) 
    				&& x < 3)
    				blues++;
    			if( o != null && marker.getClass().equals( o.getClass() )  
    				&& x > 3)
    				reds++;
    		}
    	}
    	if(reds == 0)
    		return 1;
    	if(blues == 0)
    		return 2;	
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
    
    private static void addToPieces(String team1Move, String team2Move, HalmaWorld world){
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
        if( !itr.hasNext() )
        	return locs;
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
    	try{ 
    		array = JsonParser.array().from(move); 
    	}
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
    /*
     * clears board, highlights destinations, declares move/winner
     */
    protected void drawBoard(String inData){
    	String onMessageField, p1Move, p2Move, pieceStr;
    	int winner;
    	ArrayList<Piece> pieces;
    	clearBoard( mWorld );
    	highlightDestinations( mWorld );
    	String [] data = inData.split( SPLIT_PHRASE );
    	pieceStr = data[0];
    	p1Move = data[1];
    	p2Move = data[2];
    	onMessageField = TIMER + upTimer() + mTeamA 
    		+ formatMove(p1Move) + mTeamB + formatMove(p2Move);
        pieces = toPieceList( pieceStr ) ;
        print( pieces.toString() );
        for (Piece p : pieces){
            if(p.team == 0)
                p.setColor( TEAM_A_COLOR );
            else
                p.setColor( TEAM_B_COLOR );

            if(p.damage > 0)
    		mWorld.add(p.getXYLocation(), this.createDamagedPiece( p.damage, p.getColor() ));
            else
                mWorld.add(p.getXYLocation(), p);
        }//end for loop
        addToPieces(p1Move, p2Move, mWorld);
        winner = getWinner( mWorld, new Glitter() );
    	if( winner == 1)
    		onMessageField = HALMATE + TEAM_A_WINS;
        else if( winner == 2 )
    		onMessageField = HALMATE + TEAM_B_WINS;
        mWorld.setMessage( onMessageField );
    }

}
