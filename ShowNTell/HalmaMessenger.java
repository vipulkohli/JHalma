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
 
import javax.net.ssl.HttpsURLConnection;

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
        ArrayList<String>moves = getRemoteAIMoves(message);
        Iterator<String>jsons = moves.iterator();
        return concat(toSequence(jsons.next()), toSequence(jsons.next()) );
    }
    
    public ArrayList<String> getRemoteAIMoves(String message){
        String [] moveArray = 
		{ 
		  getRemoteData(m_url1, message), 
		  getRemoteData(m_url2, message) 
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

    public static String getRemoteData(String address, String board){
        try {
            URL obj = new URL(address);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            
            //board data
            String urlParameters = "";
            System.out.println(board);
            System.console().writer().print(board);
            
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(board);
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
            
            System.out.println(response.toString());
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(HalmaMessenger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HalmaMessenger.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
    }

}

