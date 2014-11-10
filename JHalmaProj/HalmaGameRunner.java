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
 * @author Chris Nevison
 * @author Barbara Cloud Wells
 */

import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.Location;

import com.grack.nanojson.*;
import java.awt.Color;
import java.util.*;
import java.net.*;
/**
 * This class runs a world that contains box bugs. <br />
 * This class is not tested on the AP CS A and AB exams.
 */ 
// 	password z00mz00m
public class HalmaGameRunner
{
    public static void main(String[] args)
    {
     	String player1 = "http://helloworldss.net/halma/jsontest.php";
     	String player2 = "http://helloworldss.net/halma/jsontest.php";
     	new HalmaGame(player1, player2);   
    }
}
class HalmaGame {
	
	public HalmaGame(String url1, String url2){
		Official o = new Official();
		OfficialObserver [] array = 
		{
			new HalmaMessenger(url1, url2),
			new CollisionAnalyst()
		};
		for( Observer keeper : array )
			o.addObserver(keeper);
		o.startGame();
	}
	
}
class Board extends OfficialObserver{
	@Override
	protected void handleUpdate(){
		
	}
}
abstract class OfficialObserver implements Observer{
	
	private Official m_official;
	private String m_message, m_recipient;
	
	@Override
	public void update(Observable o, Object arg){
		System.out.println(arg);
		if(arg instanceof String && o instanceof Official){
			String string = arg.toString();
			String[] parts = string.split("SPLITSPLIT");
			if(parts.length == 2){
				m_official = (Official) o;
				m_recipient = parts[0]; // 004
				m_message = parts[1]; // 034556
				handleUpdate();
			}
		}
	}
	protected void reply(String sender, String [] message){
		m_official.reply(sender, message);
	}
	protected Official getOfficial(){
		return m_official;
	}
	protected String getMessageRecipient(){
		return new String(m_recipient);
	}
	protected String getMessage(){
		return new String(m_message);
	}
	protected abstract void handleUpdate();
}

class CollisionAnalyst extends OfficialObserver{
	@Override
	protected void handleUpdate(){
		Official o = getOfficial();
		String movesStr = getMessage();	
		System.out.println(MoveParser.toMoveList(movesStr));		
	}
}

class HalmaMessenger extends OfficialObserver{
	private String m_url1, m_url2;
	public HalmaMessenger(String inPlayer1addy, String inPlayer2addy){
		m_url1 = inPlayer1addy;
		m_url2 = inPlayer2addy;
	}
	@Override
	protected void handleUpdate(){
		if(!"m".equalsIgnoreCase(getMessageRecipient()))
			return;
		String jsonMovesAI1 = getData(m_url1);
		System.out.println(getMessageRecipient() + getMessage());
		String [] replyArray = { jsonMovesAI1 , getData(m_url2) };
		this.reply( "m",  replyArray);
	}
	public String getData(String address){
    	try{
    		URL url = new URL(address);
			Scanner scanner = new Scanner(url.openStream());
			return scanner.useDelimiter("\\z").next();
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		return "";
    	}
	}
}
class Official extends Observable{
	//The official knows the rules including what a halma move is
	
	public void startGame(){
		System.out.println("started");
		getNextMove();
	}
	
	private void getNextMove(){
		send("m", "hi");
	}
	
	public void reply(String sender, String [] messages){
		String outstr = "";
		if( "m".equalsIgnoreCase(sender) )
			outstr = messengerCase(messages);
		send("c", outstr);
	}
	
	private void send(String recipient, String message){
		setChanged();
		notifyObservers(recipient + "SPLITSPLIT" + message);
	}
	
	public boolean isValid(ArrayList<Move>inMoves){
		return true;
	}
	
	private String messengerCase(String [] messages){
		if(messages.length < 2)
			throw new IllegalArgumentException("Not enough players");
		ArrayList<String>playerMoves = new ArrayList<String>();
		playerMoves.add( MoveParser.getMovesFromJSON(messages[0]) );
		playerMoves.add( MoveParser.getMovesFromJSON(messages[1]) );
		System.out.println(playerMoves);
		return playerMoves.toString();
	}
	
	
}
class Move{
	private int fr, fc, fd, tr, tc, td;
	private String str;
	
	public Move(int [] data, String instr){
		fr = data[0];
		fc = data[1];
		fd = data[2];
		tr = data[3];
		tc = data[4];
		td = data[5];
		str = instr;
	}
	public int getFromDamage(){
		return fd;
	}
	public int getFromRow(){
		return fr;
	}
	public int getFromColumn(){
		return fc;
	}
	public int getToDamage(){
		return td;
	}
	public int getToRow(){
		return tr;
	}
	public int getToColumn(){
		return tc;
	}
	public boolean sameToAs(Move other){
		return this.getToRow() == other.getToRow()
			 && this.getToColumn() == other.getToColumn();
	}
	@Override
	public String toString(){
		return new String(str);
	}
	public boolean isNorthMove(){
		return comp( getFromColumn(), getToColumn() ) == 0 
			&& comp( getFromRow(), getToRow() ) < 0;
	}
	public boolean isStepMove(){
		boolean [] bools = 
		{
			comp( getFromColumn(), getToColumn() ) == 0 
				&& comp( getFromRow(), getToRow() ) ==  -1,
			comp( getFromColumn(), getToColumn() ) == 0 
				&& comp( getFromRow(), getToRow() ) ==  1,
			comp( getFromColumn(), getToColumn() ) == -1 
				&& comp( getFromRow(), getToRow() ) ==  0,
			comp( getFromColumn(), getToColumn() ) == 1 
				&& comp( getFromRow(), getToRow() ) ==  0,
			comp( getFromColumn(), getToColumn() ) == 1 
				&& comp( getFromRow(), getToRow() ) ==  1,
			comp( getFromColumn(), getToColumn() ) == -1 
				&& comp( getFromRow(), getToRow() ) ==  -1,
			comp( getFromColumn(), getToColumn() ) == 1 
				&& comp( getFromRow(), getToRow() ) ==  -1,
			comp( getFromColumn(), getToColumn() ) == -1 
				&& comp( getFromRow(), getToRow() ) ==  1
		};
		for(boolean b : bools)
			if (b)
				return true;
		return false;
	}
	private int comp(int a, int b){
		return a - b;
	}
}

class MoveParser{
	
	public static String getMovesFromJSON(String json){
		ArrayList<Integer>sequence = new ArrayList<Integer>();
		JsonObject obj = null;
		try{ obj = JsonParser.object().from(json); }
		catch(Exception e){ e.printStackTrace();}
		JsonObject fromObj = obj.getObject("from");
		JsonArray toArray = obj.getArray("to");
		int fromRow = fromObj.getInt("row");
		int fromColumn = fromObj.getInt("column");
		sequence.add(fromRow);
		sequence.add(fromColumn);
		for(Object o : toArray){
			obj = (JsonObject) o;
			sequence.add(obj.getInt("row"));
			sequence.add(obj.getInt("column"));
		}
		return sequence.toString();
	}
	public static ArrayList<Move> toMoveList(String inMoves){
		System.out.println(inMoves);
		return null;
	}

}