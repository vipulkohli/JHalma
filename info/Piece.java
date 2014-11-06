/* 
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2005-2006 Cay S. Horstmann (http://horstmann.com)
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
 * @author Cay Horstmann
 */
import java.util.*;
import java.awt.Color;
import java.text.*;
/**
 * A <code>Piece</code> is an actor that can move and turn. It drops flowers as
 * it moves. <br />
 * The implementation of this class is testable on the AP CS A and AB exams.
 */
public class Piece extends Actor
{
    private int number;
   	private static String finished;
   	private static Location target;
    /**
     * Constructs a red Piece.
     */
    public Piece(int inval, int dir)
    {
        number = inval;
        finished = "0";
        setDirection(dir);
        //setColor(Color.RED);
    }

    /**
     * Constructs a Piece of a given color.
     * @param PieceColor the color for this Piece
     */
    public Piece(Color PieceColor)
    {
        setColor(PieceColor);
    }

    /**
     * Moves if it can move, turns otherwise.
     */
    public void act()
    {
        //System.out.println(canMove());
        if (canMove(getDirection()))
            move();
    }

    /**
     * Turns the Piece 45 degrees to the right without changing its location.
     */
    public void turn()
    {
        setDirection(getDirection() + Location.HALF_RIGHT);
    }

    /**
     * Moves the Piece forward, putting a flower into the location it previously
     * occupied.
     */
    public boolean isEmpty(Location inLoc){
    	if(!getGrid().isValid(inLoc))
    		return false;
    	for(Location compLoc : getGrid().getOccupiedLocations()){
    		if(inLoc.equals(compLoc))
    			return false;
    	}
    	return true;
    }
    
    public void move()
    {
        Grid<Actor> gr = getGrid();
        if (gr == null)
            return;
        Location loc = getLocation();
        Location next = loc.getAdjacentLocation(getDirection());
        if (gr.isValid(next)){
        	moveTo(next);
        	finished = isDone();
        }
        else
            removeSelfFromGrid();
    }

    /**
     * Tests whether this Piece can move forward into a location that is empty or
     * contains a flower.
     * @return true if this Piece can move.
     */
    public String isDone(){
    	if(number == getMover() && !canMove() ){
    		setColor(Color.GREEN);
    		return "" + (int)(number + 1);
    	}
    	else
    		return "" + getMover();
    }
    public int getMover(){
    	return Integer.parseInt(finished);
    }
    public boolean checkNumber(){
    	return number == Integer.parseInt(finished);
    }
    public boolean canMove()
    {
        return canMove(getDirection());
    }
    public int getNumber(){
    	return number;
    }
     /**
     * Tests whether this Piece can move forward into a location that is empty or
     * contains a flower.
     * @return true if this Piece can move.
     */
    public boolean canMove(int dir)
    {
        return checkNumber() && isEmpty( getLocation().getAdjacentLocation(dir) );
    }
}
