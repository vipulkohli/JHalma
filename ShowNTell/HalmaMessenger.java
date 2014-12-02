/**
 * HalmaMessenger
 * Sends and receives messages to/from the AIs that are playing the game.
 */
package ShowNTell;
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

    @Override
    //called whenever an update is received from the observable
    public void handleUpdate(){
        if( super.checkRecipient( MY_EMAIL ) )
            super.replyToOfficial( MY_EMAIL , respondWithAIMoves( super.getMessage() ) );
    }

    private static final String
        MY_EMAIL = "m",
        ROW_INDEX = "y",
        COLUMN_INDEX = "x",
        DAMAGE_INDEX = "damage",
        FROM_KEY = "from",
        TO_KEY = "to";

    private final String m_url1, m_url2;

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

    private static String concat(String a, String b){
        return a + "SPLITSPLIT" + b;
    }

    private String respondWithAIMoves(String message){
        ArrayList<String> moves = getRemoteAIMoves(message);
        Iterator<String> jsons = moves.iterator();
        try{
            return concat(toSequence(jsons.next()), toSequence(jsons.next()) );
        }
        catch( NullPointerException n){
            return n.toString();
        }
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
            ArrayList<Integer> sequence = new ArrayList<Integer>();
            JsonObject obj;
            try{ obj = JsonParser.object().from(json); }
            catch(JsonParserException e){ return ""; }
            JsonObject fromObj = obj.getObject( FROM_KEY );
            JsonArray toArray = obj.getArray( TO_KEY );

            if (fromObj == null){
                sequence.add(-1);
                sequence.add(-1);
                sequence.add(-1);
            }
            else{
                int fromRow = fromObj.getInt( COLUMN_INDEX );
                int fromColumn = fromObj.getInt( ROW_INDEX );
                int fromDamage = fromObj.getInt( DAMAGE_INDEX );
                sequence.add(fromRow);
                sequence.add(fromColumn);
                sequence.add(fromDamage);
            }

            if (toArray == null){
                sequence.add(-1);
                sequence.add(-1);
            }
            else{
                for(Object o : toArray){
                    obj = (JsonObject) o;
                    if (obj == null){
                        sequence.add(-1);
                        sequence.add(-1);
                    }
                    else{
                        sequence.add( obj.getInt( COLUMN_INDEX ) );
                        sequence.add( obj.getInt( ROW_INDEX ) );
                    }
                }
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
            ArrayList<XYDLocation> boardList = CollisionAnalyst.getXYDList(board);
            String urlParameters = convertBoardToJSON(boardList, playerNum);
            print("From Messenger to AI: " + urlParameters);

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

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();
            print("AI Response: " + response.toString());

            return response.toString();

        } catch (MalformedURLException ex) {
            Logger.getLogger(HalmaMessenger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HalmaMessenger.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private static JsonObject toJSONObj(XYDLocation piece){
        try{
            return JsonParser.object().from( piece.toJSONString() );
        } catch(JsonParserException ex){
            Logger.getLogger(HalmaMessenger.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static JsonObject toJSONObj(int x, int y){
        String json = JsonWriter.string()
            .object()
            .value("x", x)
            .value("y", y)
            .end()
            .done();
        try{
            return JsonParser.object().from(json);
        }
        catch(JsonParserException ex){
            Logger.getLogger(HalmaMessenger.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static JsonStringWriter range(JsonStringWriter writer, int xMin, int xMax, int yMin, int yMax){
        for(int x = xMin; x <= xMax; x++)
            for(int y = yMin; y <= yMax; y++)
                writer = writer.value( toJSONObj(x, y) );
        return writer;
    }

    private static String convertBoardToJSON(ArrayList<XYDLocation> boardList, int playerNum){
        JsonStringWriter writer = JsonWriter.string().object()
            .value("boardSize", 18)
            .array("pieces");
        for (XYDLocation piece : boardList)
            if (piece.getTeam() == playerNum)
                writer = writer.value( toJSONObj( piece ) );

        writer = writer.end()
            .array("enemy");
        for (XYDLocation piece : boardList)
            if (piece.getTeam() != playerNum)
                writer = writer.value( toJSONObj( piece ) );

        writer = writer.end()
            .array("destinations");
        switch(playerNum){
            case 0:
            writer = range(writer, 17, 17, 0, 2);
            writer = range(writer, 16, 16, 0, 2);
            writer = range(writer, 15, 15, 0, 2);
            writer = writer.end().array("enemydestinations");
            writer = range(writer, 0, 2, 0, 2);
            writer = writer.end();
            break;
            default:
            writer = range(writer, 0, 2, 0, 2);
            writer = writer.end().array("enemydestinations");
            writer = range(writer, 17, 17, 0, 2);
            writer = range(writer, 16, 16, 0, 2);
            writer = range(writer, 15, 15, 0, 2);
            writer = writer.end();
        }//end playerNum switch
        return writer.end().done();
    }
}

