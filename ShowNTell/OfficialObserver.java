package ShowNTell;

/**
 * OfficialObserver
 * Abstract class, representing a component of the game.
 * Implements communication between the Official and OfficialObservers
 */

import java.util.*;

public abstract class OfficialObserver implements Observer{

    protected abstract void handleUpdate();

    protected static final int BOARD_SIZE = Official.BOARD_SIZE;

    protected static boolean VICTORY = false;

    private static final String
    	SPLIT_PHRASE = "SPLITSPLITSPLIT";
    private Official m_official;
    private String m_message, m_recipient;

    @Override
    public void update(Observable o, Object arg){
        if(arg instanceof String && o instanceof Official)
        	processOfficialUpdate( (Official) o, arg.toString() );
    }

    private void processOfficialUpdate( Official o, String arg ){
        String[] parts = arg.split(SPLIT_PHRASE);
        if(parts.length != 2) return;
        m_official = (Official) o;
        m_recipient = parts[0];
        m_message = parts[1];
        handleUpdate();
        o.setVictory(VICTORY);
    }

    public static void print(String message){
    	System.out.println(message);
    }

    protected void replyToOfficial(String sender, String message){
        m_official.reply(sender, message);
    }

    protected Official getOfficial(){
        return m_official;
    }

    protected String getMessageRecipient(){
        return m_recipient;
    }

    protected String getMessage(){
        return m_message;
    }

    protected boolean checkRecipient( String inCode ){
        return inCode.equals(m_recipient);
    }
}