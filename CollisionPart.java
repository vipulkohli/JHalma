import java.io.*;
import java.util.*;
import java.net.*;

public class CollisionPart{
    
    private static final String SPLIT_PHRASE = "SPLITSPLIT";
    private static final int DAMAGE_START = 5;
       /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String inMove = 
            "[-1,-2,4,9,8,1]SPLITSPLIT[-1, -2, 1, 2, 3, 4]SPLITSPLIT[-1, -2, 1, 2, 3, 4]";
        handleUpdate(inMove);
    }
    
    protected static void handleUpdate(String multiMove){
        respondWithNewBoardPosition(multiMove);
    }
    
    private static String [] toStrArray(String multiData){
        return multiData.replace(" ", "").split(SPLIT_PHRASE);
    }
    
    private static void respondWithNewBoardPosition(String multiData){
        ArrayList<String>playerMoves = new ArrayList<String>();
        String [] data = toStrArray( multiData );
        String board = data[0];
        playerMoves.add(data[1]);
        playerMoves.add(data[2]);
      	String outBoard = getNewPieceData( board, playerMoves );
        System.out.println(outBoard);
    }
    
    public static String getNewPieceData(String oldBoard, ArrayList<String>movesList ){
    	Location fromLoc0 = getFromLocation( movesList.get(0) );
    	Location toLoc0 = getToLocation( movesList.get(0) );
        Location fromLoc1 = getFromLocation( movesList.get(1) );
        Location toLoc1 = getToLocation( movesList.get(1) );
        boolean isCollision = toLoc0.equals(toLoc1);
    	ArrayList<XYDLocation> nextBoard = getXYDList(oldBoard);
    	for(XYDLocation xyd : nextBoard){
            if(!isCollision){
                if(xyd.equals(fromLoc0))
                    xyd.setXY(toLoc0).heal();
                else if (xyd.equals(fromLoc1))
                    xyd.setXY(toLoc1).heal();
            }
            else{
                if(xyd.equals(fromLoc0)){
                    xyd.setXYD(toLoc0, DAMAGE_START);
                }
                else if (xyd.equals(fromLoc1)){
                    xyd.setXYD(toLoc1, DAMAGE_START);
                }
            }
    	}
        return nextBoard.toString().replace(" ", "");
    }
    
    private static ArrayList<XYDLocation> getXYDList( String inBoard ){
    	int[]coords = toIntArray(inBoard);
    	ArrayList<Integer>coordList = new ArrayList<Integer>();
    	ArrayList<XYDLocation> xydlist = new ArrayList<XYDLocation>();
    	for(int coordinate : coords){
    		coordList.add( coordinate );
    	}
    	Iterator<Integer> itr = coordList.iterator();
    	while( itr.hasNext() )
    		xydlist.add( new XYDLocation( itr.next(), itr.next(), itr.next() ) );
    	return xydlist;
    }
    
    public static int [] toIntArray(String inStr){
    	String [] nums 
    		= inStr.replace("[", "").replace("]", "")
    			.replace(" ", "").split(",");
    	ArrayList<Integer>coords = new ArrayList<Integer>();
    	for(String num : nums){
    		coords.add(Integer.parseInt(num, 10));
    	}
    	int [] outArray = new int[coords.size()];
    	Iterator <Integer> itr = coords.iterator();
    	for(int k = 0; k < outArray.length; k++)
    		outArray[k] = itr.next();
    	return outArray;
    }
    
    public static Location getFromLocation(String move){
    	int [] moveArray = toIntArray( move );
    	return new Location( moveArray[0] , moveArray[1]);
    }
    
    public static Location getToLocation(String move){
    	int [] moveArray = toIntArray( move );
    	return new Location( moveArray[moveArray.length - 2] , moveArray[moveArray.length - 1]);
    }
    
    private static class XYDLocation{
    	
    	int mDamage;
    	Location mLoc;
    	
    	public XYDLocation(int x, int y, int d){
    		mLoc = new Location( x , y );
    		mDamage = d;
    	}
        
        public XYDLocation heal(){
            if (mDamage > 0)
                mDamage--;
            return this;
        }
        
        public XYDLocation setD(int d){
            mDamage = d;
            return this;
        }
        public XYDLocation setXYD(Location moveLoc, int d){
            setD(d);
            setXY(moveLoc);
            return this;
        }
        public XYDLocation setXY(Location moveLoc){
            setXY(moveLoc.getRow(), moveLoc.getCol());
            return this;
        }
        
        public XYDLocation setXY(int x, int y){
            setX(x);
            setY(y);
            return this;
        }
        public void setX(int x){
            int y = getY();
            mLoc = new Location(x, y);
        }
        public void setY(int y){
            int x = getX();
            mLoc = new Location(x, y);
        }
    	public int getX(){
    		return mLoc.getRow();
    	}
    	public int getY(){
    		return mLoc.getCol();
    	}
    	public int getD(){
    		return mDamage;
    	}
    	public boolean equals(Location other){
    		return mLoc.equals(other);
    	}
    	public String toString(){
    		return getX() + "," + getY() + "," + getD();  
    	}
    }

}
/* 
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2002-2006 College Entrance Examination Board 
 * (http://www.collegeboard.com).
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * @author Alyce Brady
 * @author Chris Nevison
 * @author APCS Development Committee
 * @author Cay Horstmann
 */



/**
 * A <code>Location</code> object represents the row and column of a location
 * in a two-dimensional grid. <br />
 * The API of this class is testable on the AP CS A and AB exams.
 */
class Location implements Comparable
{
    private int row; // row location in grid
    private int col; // column location in grid

    /**
     * The turn angle for turning 90 degrees to the left.
     */
    public static final int LEFT = -90;
    /**
     * The turn angle for turning 90 degrees to the right.
     */
    public static final int RIGHT = 90;
    /**
     * The turn angle for turning 45 degrees to the left.
     */
    public static final int HALF_LEFT = -45;
    /**
     * The turn angle for turning 45 degrees to the right.
     */
    public static final int HALF_RIGHT = 45;
    /**
     * The turn angle for turning a full circle.
     */
    public static final int FULL_CIRCLE = 360;
    /**
     * The turn angle for turning a half circle.
     */
    public static final int HALF_CIRCLE = 180;
    /**
     * The turn angle for making no turn.
     */
    public static final int AHEAD = 0;

    /**
     * The compass direction for north.
     */
    public static final int NORTH = 0;
    /**
     * The compass direction for northeast.
     */
    public static final int NORTHEAST = 45;
    /**
     * The compass direction for east.
     */
    public static final int EAST = 90;
    /**
     * The compass direction for southeast.
     */
    public static final int SOUTHEAST = 135;
    /**
     * The compass direction for south.
     */
    public static final int SOUTH = 180;
    /**
     * The compass direction for southwest.
     */
    public static final int SOUTHWEST = 225;
    /**
     * The compass direction for west.
     */
    public static final int WEST = 270;
    /**
     * The compass direction for northwest.
     */
    public static final int NORTHWEST = 315;

    /**
     * Constructs a location with given row and column coordinates.
     * @param r the row
     * @param c the column
     */
    public Location(int r, int c)
    {
        row = r;
        col = c;
    }

    /**
     * Gets the row coordinate.
     * @return the row of this location
     */
    public int getRow()
    {
        return row;
    }

    /**
     * Gets the column coordinate.
     * @return the column of this location
     */
    public int getCol()
    {
        return col;
    }

    /**
     * Gets the adjacent location in any one of the eight compass directions.
     * @param direction the direction in which to find a neighbor location
     * @return the adjacent location in the direction that is closest to
     * <tt>direction</tt>
     */
    public Location getAdjacentLocation(int direction)
    {
        // reduce mod 360 and round to closest multiple of 45
        int adjustedDirection = (direction + HALF_RIGHT / 2) % FULL_CIRCLE;
        if (adjustedDirection < 0)
            adjustedDirection += FULL_CIRCLE;

        adjustedDirection = (adjustedDirection / HALF_RIGHT) * HALF_RIGHT;
        int dc = 0;
        int dr = 0;
        if (adjustedDirection == EAST)
            dc = 1;
        else if (adjustedDirection == SOUTHEAST)
        {
            dc = 1;
            dr = 1;
        }
        else if (adjustedDirection == SOUTH)
            dr = 1;
        else if (adjustedDirection == SOUTHWEST)
        {
            dc = -1;
            dr = 1;
        }
        else if (adjustedDirection == WEST)
            dc = -1;
        else if (adjustedDirection == NORTHWEST)
        {
            dc = -1;
            dr = -1;
        }
        else if (adjustedDirection == NORTH)
            dr = -1;
        else if (adjustedDirection == NORTHEAST)
        {
            dc = 1;
            dr = -1;
        }
        return new Location(getRow() + dr, getCol() + dc);
    }

    /**
     * Returns the direction from this location toward another location. The
     * direction is rounded to the nearest compass direction.
     * @param target a location that is different from this location
     * @return the closest compass direction from this location toward
     * <code>target</code>
     */
    public int getDirectionToward(Location target)
    {
        int dx = target.getCol() - getCol();
        int dy = target.getRow() - getRow();
        // y axis points opposite to mathematical orientation
        int angle = (int) Math.toDegrees(Math.atan2(-dy, dx));

        // mathematical angle is counterclockwise from x-axis,
        // compass angle is clockwise from y-axis
        int compassAngle = RIGHT - angle;
        // prepare for truncating division by 45 degrees
        compassAngle += HALF_RIGHT / 2;
        // wrap negative angles
        if (compassAngle < 0)
            compassAngle += FULL_CIRCLE;
        // round to nearest multiple of 45
        return (compassAngle / HALF_RIGHT) * HALF_RIGHT;
    }

    /**
     * Indicates whether some other <code>Location</code> object is "equal to"
     * this one.
     * @param other the other location to test
     * @return <code>true</code> if <code>other</code> is a
     * <code>Location</code> with the same row and column as this location;
     * <code>false</code> otherwise
     */
    public boolean equals(Object other)
    {
        if (!(other instanceof Location))
            return false;

        Location otherLoc = (Location) other;
        return getRow() == otherLoc.getRow() && getCol() == otherLoc.getCol();
    }

    /**
     * Generates a hash code.
     * @return a hash code for this location
     */
    public int hashCode()
    {
        return getRow() * 3737 + getCol();
    }

    /**
     * Compares this location to <code>other</code> for ordering. Returns a
     * negative integer, zero, or a positive integer as this location is less
     * than, equal to, or greater than <code>other</code>. Locations are
     * ordered in row-major order. <br />
     * (Precondition: <code>other</code> is a <code>Location</code> object.)
     * @param other the other location to test
     * @return a negative integer if this location is less than
     * <code>other</code>, zero if the two locations are equal, or a positive
     * integer if this location is greater than <code>other</code>
     */
    public int compareTo(Object other)
    {
        Location otherLoc = (Location) other;
        if (getRow() < otherLoc.getRow())
            return -1;
        if (getRow() > otherLoc.getRow())
            return 1;
        if (getCol() < otherLoc.getCol())
            return -1;
        if (getCol() > otherLoc.getCol())
            return 1;
        return 0;
    }

    /**
     * Creates a string that describes this location.
     * @return a string with the row and column of this location, in the format
     * (row, col)
     */
    public String toString()
    {
        return "(" + getRow() + ", " + getCol() + ")";
    }
}
