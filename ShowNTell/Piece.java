package ShowNTell;

/**
 * Piece
 * Contains the data to draw a halma piece.
 * This file includes damage count pieces and XPiece.
 */

import java.util.*;
import java.awt.Color;

public class Piece extends Rock{
    
    private int x, y, damage, team;
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
            return this.setColor( Color.red, true );
        if( "blue".equalsIgnoreCase(inColor) )
            return this.setColor( Color.blue, true );
        if( "cyan".equalsIgnoreCase(inColor) )
            return this.setColor( Color.cyan, true );
        if( "magenta".equalsIgnoreCase(inColor) )
            return this.setColor( Color.magenta, true );
        if( "black".equalsIgnoreCase(inColor) )
            return this.setColor( Color.black, true );
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
    
    public int getDamage(){
        return damage;
    }
    
    public int getTeam(){
        return team;
    }
    
    @Override
    public String toString(){
        return "P(" + x + "," + y + "," + damage + "," + team + ")"; 
    }
}

class Five extends Piece{}
class Four extends Piece{}
class Three extends Piece{}
class Two extends Piece{}
class One extends Piece{}
class XPiece extends Piece{}
