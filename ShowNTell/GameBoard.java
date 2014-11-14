import java.util.*;
import com.grack.nanojson.*;
import info.gridworld.grid.*;
import info.gridworld.actor.*;

public class GameBoard extends OfficialObserver{
    
    private static final String
        MY_EMAIL = "g";
    private static final ActorWorld
    	WORLD = new ActorWorld();
    private static final int
    	BOARD_SIZE = 6;
    
    public GameBoard(){
    	WORLD.setGrid( new BoundedGrid(BOARD_SIZE, BOARD_SIZE) );
    	WORLD.show();
    }
    
    protected void handleUpdate(){
        if( !super.checkRecipient( MY_EMAIL ) )
            return;
        drawBoard( super.getMessage() ); 
    }
    
    private ArrayList<Piece> toPieceList(String officialData){
        ArrayList<Piece> list = new ArrayList<Piece>();
        JsonArray array = null;
		try{ array = JsonParser.array().from(officialData);  }
		catch(Exception e){ e.printStackTrace(); return null; }
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
    	for(int x = 0; x < BOARD_SIZE; x++)
    		for(int y = 0; y < BOARD_SIZE; y++)
    			WORLD.remove( new Location(x,y) );
    }
    
    protected void drawBoard(String inDimensions){
    	clearBoard();
        ArrayList<Piece>pieces;
        pieces = toPieceList( inDimensions ) ;
        System.out.println(pieces);
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
      		WORLD.setMessage(p.toString());
      		switch(p.damage){
      			case 5:
    				WORLD.add(p.getXYLocation(), new Five());
    				break;
    			case 4:
    				WORLD.add(p.getXYLocation(), new Four());
    				break;
    			case 3:
    				WORLD.add(p.getXYLocation(), new Three());
    				break;
    			case 2:
    				WORLD.add(p.getXYLocation(), new Two());
    				break;
    			case 1:
    				WORLD.add(p.getXYLocation(), new One());
    				break;
    		}
      	}
    		
    }

}