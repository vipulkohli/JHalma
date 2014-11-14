import com.grack.nanojson.*;
import java.util.*;
import java.net.*;

public class HalmaMessenger extends OfficialObserver{
    
    private String m_url1, m_url2;

    public HalmaMessenger(String inPlayer1addy, String inPlayer2addy){
            m_url1 = inPlayer1addy;
            m_url2 = inPlayer2addy;
    }
	
    private static ArrayList<String> toJSONList(String [] jsons){
        ArrayList<String> list = new ArrayList<String>();
        for(String str : jsons)
            list.add(str);
        return list;
    }
    
    //get and send the AIs' moves when they are requested
    public void handleUpdate(){
        if( "m".equals( super.getMessageRecipient() ) ) 
            super.replyToOfficial( "m" , respondWithAIMoves() );
    }
    
    private static String concat(String a, String b){
        return a + "SPLITSPLIT" + b;
    }
    
    //convert the AIs' moves into the correct format
    private String respondWithAIMoves(){
        ArrayList<String> moves = getRemoteAIMoves();
        Iterator<String> jsons = moves.iterator();
        return concat(toSequence(jsons.next()), toSequence(jsons.next()) );
    }
    
    //get the AIs' moves
    public ArrayList<String> getRemoteAIMoves(){
        String [] moveArray = 
            { 
                getRemoteData(m_url1), 
                getRemoteData(m_url2) 
            };
        return toJSONList(moveArray);
    }
    
    public static String toSequence(String json){
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

    //message an AI and receive a response (move)
    public static String getRemoteData(String address){
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

