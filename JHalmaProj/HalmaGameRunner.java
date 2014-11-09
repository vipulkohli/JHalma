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
     	String player1 = "http://lyle.smu.edu/~sochaa/4345/Homework11/HW11v2.php";
     	String player2 = "http://lyle.smu.edu/~sochaa/4345/Homework11/HW11v2.php";
     	new HalmaGame(player1, player2);   
    }
}
class HalmaGame {
	public HalmaGame(String url1, String url2){
		HalmaMessenger m = new HalmaMessenger(url1, url2);
		Official o = new Official();
		o.addObserver(m);
		o.startGame();
	}
}
abstract class OfficialObserver implements Observer{
	private Official m_official;
	private String m_message, m_recipient;
	
	@Override
	public void update(Observable o, Object arg){
		System.out.println("update ok");
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
		m_url1 = "http://helloworldss.net/halma/jsontest.php";
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
	public void startGame(){
		System.out.println("started");
		getNextMove();
	}
	private void getNextMove(){
		send("m", "hi");
	}
	public void reply(String sender, String [] messages){
		if( "m".equalsIgnoreCase(sender) ){
			//messages[0] player 1 moves
			getMoveArrayFromJSON( messages[0] );
		}
	}
	private void send(String recipient, String message){
		setChanged();
		notifyObservers(recipient + "SPLITSPLIT" + message);
	}
	private void getMoveArrayFromJSON(String json){
		try{
					 System.out.println("=======decode=======");
		 JsonObject obj = JsonParser.object().from(json);
		 JsonArray toArray = obj.getArray("to");
		 for(Object object : toArray){
			JsonObject o = (JsonObject)object;
		 	Position p = new Position( o.getInt("row"), o.getInt("column"), o.getInt("damage") );
		 	System.out.println(p);
		 }
		}
		catch(Exception e){
		}
	}
}

class Position{
	private int damage, row, column;
	
	public Position(int r, int c, int d){
		this.damage = d;
		this.row = r;
		this.column = c;
	}
	public int getDamage(){
		return damage;
	}
	public int getRow(){
		return row;
	}
	public int getColumn(){
		return column;
	}
	@Override
	public String toString(){
		return "(" + row + "," + column + "," + damage + ")";
	}
}