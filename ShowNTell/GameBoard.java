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
    	BOARD_SIZE = 18; //also change size in Official -> default board
    
    public GameBoard(){
    	WORLD.setGrid( new BoundedGrid(BOARD_SIZE, BOARD_SIZE) );
    	WORLD.show();
    }
    
    @Override
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
    
    protected void drawBoard(String inDimensions){
    	clearBoard();
    	WORLD.setMessage("Pieces removed");
        ArrayList<Piece> pieces;
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