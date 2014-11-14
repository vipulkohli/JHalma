import java.util.*;
import com.grack.nanojson.*;

public class GameBoard extends OfficialObserver{
    
    private static final String
        MY_EMAIL = "g";
    
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
    
    protected void drawBoard(String inDimensions){
        ArrayList<Piece>pieces;
        pieces = toPieceList( inDimensions ) ;
        for (Piece p : pieces){
            if(p.team == 0)
                System.out.println( p.setColor( "red" ));
            else
                System.out.println( p.setColor( "blue" ));
        }
    }
}