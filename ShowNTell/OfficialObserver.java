import java.util.*;

public abstract class OfficialObserver implements Observer{

    private static final String
    SPLIT_PHRASE = "SPLITSPLITSPLIT";
    private Official m_official;
    private String m_message, m_recipient;

    @Override
    public void update(Observable o, Object arg){
            if(arg instanceof String && o instanceof Official){
                    String string = arg.toString();
                    String[] parts = string.split(SPLIT_PHRASE);
                    if(parts.length == 2){
                            m_official = (Official) o;
                            m_recipient = parts[0]; // 004
                            m_message = parts[1]; // 034556
                            handleUpdate();
                    }
            }
    }
    protected void replyToOfficial(String sender, String message){
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
    protected boolean checkRecipient( String inCode ){
            return inCode.equals(m_recipient);
    }

    protected abstract void handleUpdate();
}