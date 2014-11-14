import info.gridworld.grid.*;
import java.awt.Color;

public class Piece{
    
    public int x, y, damage, team;
    private String mColor;    
    private static final String DEFAULT_COLOR_STRING = "default";
    
    public Piece(int x, int y, int d, int t){
        this.x = x;
        this.y = y;
        this.damage = d;
        this.team = t;
        mColor = DEFAULT_COLOR_STRING;
    }
    
    public String setColor(Color c, boolean local){
        return c.toString();
    }
    public String setColor(String inColor){
        if( "red".equalsIgnoreCase(inColor) )
            return setColor( Color.red, true );
        if( "blue".equalsIgnoreCase(inColor) )
            return setColor( Color.blue, true );
        return mColor;
    }
    public String toString(){
        return "P(" + x +"," + y +"," + damage + "," + team + ")"; 
    }
}