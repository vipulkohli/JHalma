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
import info.gridworld.actor.*;
import info.gridworld.grid.*;
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
     * Constructs a Piece of a given color.
     * @param PieceColor the color for this Piece
     */
    public Piece(Color PieceColor)
    {
        setColor(PieceColor);
    }
}
