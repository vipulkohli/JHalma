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

package ShowNTell;

public class HalmaWorld extends ActorWorld{
	 
	 private Object mBoard;
	 
	 public HalmaWorld( Object board ){
	 	super();
	 	mBoard = board;
	 }
	 
	 //messages received from GUIController.java 
	 public void rewind(){
	 	mBoard.equals( "rewind" );
	 }
	
	 //messages received from GUIController.java 
	 @Override
	 public void step(){	
	 	if(mBoard != null)
        	mBoard.equals( "step" );
	 }
	
	
	//messages received from GUIController.java 
	@Override
	public boolean equals( Object o ){
		if( "rewind".equals(o) )
			rewind();
		return super.equals(o);
	}
	 
	 
}