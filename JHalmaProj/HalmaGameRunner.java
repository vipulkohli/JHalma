/* 
 * JHalma
 * SMU CSE 4345 Fall 2014 Team Project
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
 * @author Vipul Kohli
 * @author Andrew Socha
 * @author Cay Horstmann
 * @author Chris Nevison
 * @author Barbara Cloud Wells
 */

import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.*;

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
			new CollisionAnalyst(),
			new GameBoard()
		};
		for( Observer keeper : array )
			o.addObserver(keeper);
		o.startGame();
	}
	
}
abstract class OfficialObserver implements Observer{
	
	private Official m_official;
	private String m_message, m_recipient;
	
	@Override
	public void update(Observable o, Object arg){
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
class GameBoard extends OfficialObserver{
	
	private final static int BOARD_SIZE = 18, MAX_COORD = BOARD_SIZE - 1;
	private ArrayList<Piece> m_pieces;
	private ActorWorld m_world;
	Grid m_grid;

	
	private ArrayList<Move> splitPlayerMoves(String inMoves){
		ArrayList<Move>moves = new ArrayList<Move>();
		JsonArray array = null;
		try{ array = JsonParser.array().from(inMoves);  }
		catch(Exception e){ e.printStackTrace(); }
		for(Object o : array){
			Move m = new Move(o.toString());
			moves.add(m);
		}
		return moves;
	}

	public boolean isCollision(ArrayList<Move> moves){
		Iterator<Move>moveItr = moves.iterator();
		return moveItr.next().sameToAs( moveItr.next() );
	}
	
	@Override 
	public void handleUpdate(){
		if( "grid".equalsIgnoreCase( getMessageRecipient() ) ){
			ArrayList<Move> playerMoves = this.splitPlayerMoves( super.getMessage() );
			System.out.println("Collsion: " + isCollision(playerMoves));
			Move move = playerMoves.get(0);
			Location toLoc = new Location( move.getToRow(), move.getToColumn() );
			m_pieces.get(0).moveTo( toLoc );
			m_pieces.get(0).setColor(Color.ORANGE);
			m_world.setMessage( "Move to " + toLoc );
			try{Thread.sleep(1000); }
			catch(Exception e) {}
			m_pieces.get(0).setColor(Color.GREEN);
			m_world.setMessage( "Collision complete" );
			movePiece();
		}
	}
	
	public void movePiece(){
		for (int count = 0; count < 10; count++){
				try{Thread.sleep(1000); }
				catch(Exception e) {}
				m_world.setMessage( "Timer: " + count );
				for(int k = 0; k < 1; k++){
					Piece p = m_pieces.get(k);
					int row = (int) (Math.random() * 10);
					int column = (int) (Math.random() * 10);
					if(p.getGrid() != null)
						p.moveTo(new Location (row, column));
				}
			}
	}
	
	public GameBoard(){
		m_world = new ActorWorld();
		m_grid = new BoundedGrid( BOARD_SIZE , BOARD_SIZE);
		m_world.setGrid( m_grid );
		m_pieces = new ArrayList<Piece>();
		addPieces(); //populates m_pieces & m_world with Pieces!	
		m_world.show();
	}
	private void addPieces(){
		Piece p = null;
		Location loc = null;
		Color red = Color.red, blue = Color.blue;
		//add Red Team
		for(int row = MAX_COORD; row >= MAX_COORD - 2; row--){
			for(int column = 0; column <= 2; column++){
				p = new Piece(red);
				loc = new Location(row, column);
				//world.add(location, actor)
				m_world.add(loc, p);
				m_pieces.add(p);
			}
		}
		//add Blue Team
		for(int column = MAX_COORD; column >= MAX_COORD - 2; column--){
			for(int row = 0; row <= 2; row++){
				p = new Piece(blue);
				loc = new Location(row, column);
				m_world.add(loc, p);
				m_pieces.add(p);
			}
		}
	}
	
	
	
	
	
	
	private class Move{
	
		private ArrayList<Integer>mList;
	
		public Move(String inMove){
			mList = new ArrayList<Integer>();
			String [] coordinates = 
				inMove.replace("[", "").replace("]", "")
				.replace(" ", "").split(",");
			for(String point : coordinates)
				mList.add( toInt( point ) );
		}
		private int toInt(String s){
			return Integer.parseInt(s);
		}
		private ArrayList<Integer> getSequence(){
			return mList;
		}
		public int getFromRow(){
			return mList.get(0);
		}
		public int getFromColumn(){
			return mList.get(1);
		}
		public int getToRow(){
			return mList.get( getSequence().size() - 2 );
		}
		public int getToColumn(){
			return mList.get( getSequence().size() - 1 );
		}
		public boolean sameToAs(Move other){
			return this.getToRow() == other.getToRow()
				 && this.getToColumn() == other.getToColumn();
		}
		@Override
		public String toString(){
			return mList.toString();
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
	}//end class Move
	
}//end GameBoard

class HalmaMessenger extends OfficialObserver{
	private String m_url1, m_url2;
	
	
	public HalmaMessenger(String inPlayer1addy, String inPlayer2addy){
		m_url1 = inPlayer1addy;
		m_url2 = inPlayer2addy;
	}
	@Override
	//**TODO: SEND MESSAGE VIA POST
	protected void handleUpdate(){
		if(!"m".equalsIgnoreCase(getMessageRecipient()))
			return;
		//System.out.println(getMessageRecipient() + getMessage());
		String [] replyArray = 
		{ 
		  getData(m_url1), 
		  getData(m_url2) 
		};
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
		if( "m".equalsIgnoreCase(sender) ){
			outstr = messengerCase(messages);
			send("grid", outstr);
		}
		else if( "grid".equalsIgnoreCase(sender) ){
			
		}
	}
	
	private void send(String recipient, String message){
		setChanged();
		notifyObservers(recipient + "SPLITSPLIT" + message);
	}
	
	
	private String messengerCase(String [] messages){
		if(messages.length < 2)
			throw new IllegalArgumentException("Not enough players");
		ArrayList<String>playerMoves = new ArrayList<String>();
		playerMoves.add( this.getMovesFromJSON(messages[0]) );
		playerMoves.add( this.getMovesFromJSON(messages[1]) );
		System.out.println(messages[1]);
		return playerMoves.toString();
	}
	
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
	
}
