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
 * @author APCS Development Committee
 * @author Cay Horstmann
 */

package ShowNTell;
import info.gridworld.grid.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
/**
 * A <code>BoundedGrid</code> is a rectangular grid with a finite number of
 * rows and columns. <br />
 * The implementation of this class is testable on the AP CS AB exam.
 */
public class HalmaGrid<E> extends BoundedGrid<E>
{

    /**
     * Constructs an empty bounded grid with the given dimensions.
     * (Precondition: <code>rows > 0</code> and <code>cols > 0</code>.)
     * @param rows number of rows in BoundedGrid
     * @param cols number of columns in BoundedGrid
     */
    public HalmaGrid(String args)
    {
        super(18, 18);
        zoomOut();
        startGame(args);
    }
    private void startGame(String args){
    	String url1, url2, name1, name2;
    	String [] parts = args.replace(" ", "").split(",");
        if(parts.length == 4){
        	name1 = parts[0];
	        url1 = parts[1];
	        name2 = parts[2];
	        url2 = parts[3];
	        //System.out.println( name1+ url1 + name2 );
	        new HalmaGame(url1, url2, name1, name2);
        }
    }
	private HalmaGrid zoomOut(){
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_PAGE_DOWN);
			robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
			robot.keyRelease(KeyEvent.VK_CONTROL);
		} catch (AWTException ex) {
			Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
		}
		return this;
	}
	
    
}
