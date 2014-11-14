/**
 * @(#)GoGetter.java
 *
 *
 * @author 
 * @version 1.00 2014/4/27
 */
import java.io.*;
import java.util.*;
import java.net.*;
public class Program {
        
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        String player1 = "http://helloworldss.net/halma/jsontest.php";
     	String player2 = "http://helloworldss.net/halma/jsontest.php";
     	new HalmaGame(player1, player2);  
        if(true)
            return;
    try{
        long startTime = System.nanoTime();
        URL url = new URL("http://dallas.craigslist.org/search/moa?zoomToPosting=&catAbb=moa&query=972&minAsk=&maxAsk=&excats=");
        ArrayList<String>nums = new ArrayList<String>();
		Scanner s = new Scanner(url.openStream());
		s.useDelimiter("</html>");
		String page = s.next();
		Scanner k = new Scanner(page);
		k.useDelimiter("data-pid=\"");
		while(k.hasNext())
			nums.add(k.next());
		for(int ind = 1; ind < 100; ind++){
			String id = nums.get(ind);
			id = id.substring(0,id.indexOf("\""));
			//System.out.println(id);
			url = new URL("http://dallas.craigslist.org/fb/" + id);
			Scanner t = new Scanner(url.openStream());
			String buf = "";
			while(t.hasNextLine())
				buf += t.nextLine();
			t.close();
            System.out.println(buf.substring(0,100));
		}
		System.out.println("Mission complete");
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Total execution time to create 1000K objects in Java in millis: "
                + elapsedTime/1000000);
    }
    catch(Exception e){
        e.printStackTrace();
    }
    }
}
