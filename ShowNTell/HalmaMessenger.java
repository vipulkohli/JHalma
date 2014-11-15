import com.grack.nanojson.*;
import java.net.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
 

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
    
    @Override
    //called whenever an update is received from the observable
    public void handleUpdate(){
        if( "m".equals( super.getMessageRecipient() ) ) 
            super.replyToOfficial( "m" , respondWithAIMoves( super.getMessage() ) );
    }
    
    private static String concat(String a, String b){
        return a + "SPLITSPLIT" + b;
    }
    
    private String respondWithAIMoves(String message){
        ArrayList<String> moves = getRemoteAIMoves(message);
        Iterator<String> jsons = moves.iterator();
        return concat(toSequence(jsons.next()), toSequence(jsons.next()) );
    }
    
    public ArrayList<String> getRemoteAIMoves(String message){
        String [] moveArray = 
            { 
                getRemoteData(m_url1, message, 0), 
                getRemoteData(m_url2, message, 1)
            };
        return toJSONList(moveArray);
    }
    
    public static String toSequence(String json){
            ArrayList<Integer>sequence = new ArrayList<Integer>();
            JsonObject obj;
            try{ obj = JsonParser.object().from(json); }
            catch(JsonParserException e){ e.printStackTrace(); return "";}
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

    //send JSON as a POST request to an AI and receive a JSON response
    public static String getRemoteData(String address, String board, int playerNum){
        try {
            URL obj = new URL(address);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            
            //board data
            ArrayList<CollisionAnalyst.XYDLocation> boardList = CollisionAnalyst.getXYDList(board);
            String urlParameters = convertBoardToJSON(boardList, playerNum);
            
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            System.out.println("AI Response: " + response.toString());
            return response.toString();
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(HalmaMessenger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HalmaMessenger.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
    }
    
    private static String convertBoardToJSON(ArrayList<CollisionAnalyst.XYDLocation> boardList, int playerNum){
        String JSON = "{\"boardSize\":18,\"pieces\":[";
        for (CollisionAnalyst.XYDLocation piece : boardList){
            if (piece.getTeam() == playerNum){
                JSON += "{" + piece.toJSONString() + "},";
            }
        }
        JSON = JSON.substring(0, JSON.length()-1);
        
        JSON += "],\"enemy\":[";
        for (CollisionAnalyst.XYDLocation piece : boardList){
            if (piece.getTeam() != playerNum){
                JSON += "{" + piece.toJSONString() + "},";
            }
        }
        JSON = JSON.substring(0, JSON.length()-1);
        JSON += "],\"destinations\":[";
        
        if (playerNum == 0){
            JSON += "{\"x\":0,\"y\":0},{\"x\":1,\"y\":1}"; //TODO: SET DESTINATIONS
            JSON += "],\"enemydestinations\":[";
            JSON += "{\"x\":17,\"y\":17},{\"x\":16,\"y\":16}";
        }
        else{
            JSON += "{\"x\":17,\"y\":17},{\"x\":16,\"y\":16}";
            JSON += "],\"enemydestinations\":[";
            JSON += "{\"x\":0,\"y\":0},{\"x\":1,\"y\":1}";
        }
        
        JSON += "]}";
        
        return JSON;
    }
}

