package ShowNTell;

import java.util.*;
import java.lang.Thread;

public class HalmaGame extends Thread{
	
	private final Official o;
    
	public HalmaGame(String url1, String url2, String name1, String name2){
		o = new Official();
		OfficialObserver [] array = 
		{
			new HalmaMessenger( url1, url2 ),
			new CollisionAnalyst(),
			new GameBoard( name1, name2 )
		};
		for( Observer keeper : array )
			o.addObserver(keeper);
	}
	
	@Override
	public void run(){
		o.startGame();
	}
	
}