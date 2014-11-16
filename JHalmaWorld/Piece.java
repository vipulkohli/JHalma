import java.util.*;
import java.awt.Color;
import info.gridworld.actor.*;
import info.gridworld.grid.*;

public class Piece extends Actor{
    
    public int x, y, damage, team;
    private String mColor;    
    private static final String DEFAULT_COLOR_STRING = "default";
    
    public Piece(){
    }
    
    public Piece(int x, int y, int d, int t){
        this.x = x;
        this.y = y;
        this.damage = d;
        this.team = t;
        mColor = DEFAULT_COLOR_STRING;
    }
    
    public String setColor(Color c, boolean local){
    	super.setColor(c);
        return c.toString();
    }
    public String setColor(String inColor){
        if( "red".equalsIgnoreCase(inColor) )
            return setColor( Color.red, true );
        if( "blue".equalsIgnoreCase(inColor) )
            return setColor( Color.blue, true );
        if( "cyan".equalsIgnoreCase(inColor) )
            return setColor( Color.cyan, true );
        if( "magenta".equalsIgnoreCase(inColor) )
        	return setColor( Color.magenta, true );
        return mColor;
    }
    
    public ArrayList<Integer> toIntList(){
        int [] arr = { x, y, damage, team};
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i : arr)
            list.add( i );
        return list;
    }
    
    public Location getXYLocation(){
    	return new Location(y, x);
    }
    
    public String toString(){
        return "P(" + x +"," + y +"," + damage + "," + team + ")"; 
    }
}

class Five extends Piece{
	
	public Five(){
		setColor(Color.black);
	}
    
}
class Four extends Five{}
class Three extends Five{}
class Two extends Five{}
class One extends Five{}
class XPiece extends Five{}

