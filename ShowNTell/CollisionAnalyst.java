import info.gridworld.grid.*;
import java.util.*;

public class CollisionAnalyst extends OfficialObserver{
    
    private static final String 
        SPLIT_PHRASE = "SPLITSPLIT",
        MY_EMAIL = "c";
    private static final int 
        DAMAGE_START = 5,
        DAMAGE_LITE = 1;
    
    @Override
    //called whenever an update is received from the observable
    protected void handleUpdate(){
        if( !super.checkRecipient( MY_EMAIL ) )
            return;
        super.replyToOfficial(
            MY_EMAIL,
            getNewBoardPosition( super.getMessage() )
        );
    }
    
    private static String [] toStrArray(String multiData){
        return multiData.replace(" ", "").split(SPLIT_PHRASE);
    }
    
    public static String getNewBoardPosition(String multiData){
        ArrayList<String>playerMoves = new ArrayList<String>();
        String [] data = toStrArray( multiData );
        String board = data[0];
        playerMoves.add(data[1]);
        playerMoves.add(data[2]);
      	String outBoard = getNewPieceData( board, playerMoves );
        return outBoard;
    }
    
    private static boolean isOwnCollision(Location toLoc0, Location toLoc1, XYDLocation xyd){
        return (xyd.equals(toLoc0) && xyd.getTeam() == 0)
                || (xyd.equals(toLoc1) && xyd.getTeam() == 1);
    }
    
    private static boolean isEnemyCollision(Location toLoc0, Location toLoc1, XYDLocation xyd){
        return (xyd.equals(toLoc0) && xyd.getTeam() == 1)
                || (xyd.equals(toLoc1) && xyd.getTeam() == 0);
    }
    
    public static String getNewPieceData(String oldBoard, ArrayList<String> movesList ){
    	Location fromLoc0 = getFromLocation( movesList.get(0) );
    	Location toLoc0 = getToLocation( movesList.get(0) );
        Location fromLoc1 = getFromLocation( movesList.get(1) );
        Location toLoc1 = getToLocation( movesList.get(1) );
        boolean isHeadOnCollision = toLoc0.equals(toLoc1);
    	ArrayList<XYDLocation> nextBoard = getXYDList(oldBoard);
    	for(XYDLocation xyd : nextBoard){
            if(!isHeadOnCollision){
                xyd.heal(); //heals all pieces
                if( isOwnCollision(toLoc0, toLoc1, xyd) )
                    xyd.setD( DAMAGE_START );
                if( isEnemyCollision(toLoc0, toLoc1, xyd) ){
                    xyd.setD( DAMAGE_LITE );
                    isHeadOnCollision = true;
                }
                if( xyd.equals(fromLoc0, 0) )
                    xyd.setXY(toLoc0);
                else if( xyd.equals(fromLoc1, 1) )
                    xyd.setXY(toLoc1);
            }
            else{
                if(xyd.equals(fromLoc0, 0)){
                    xyd.setXYD(toLoc0, DAMAGE_START);
                }
                else if (xyd.equals(fromLoc1, 1)){
                    xyd.setXYD(toLoc1, DAMAGE_START);
                }
            }
    	}
        return nextBoard.toString().replace(" ", "");
    }
    private static ArrayList<Integer> toIntList(int [] coords){
        ArrayList<Integer>coordList = new ArrayList<Integer>();
        for(int coordinate : coords)
    		coordList.add( coordinate );
    	return coordList;
    }
    public static ArrayList<XYDLocation> getXYDList( String inBoard ){
    	ArrayList<Integer>coordList = toIntList( toIntArray(inBoard) );
    	ArrayList<XYDLocation> xydlist = new ArrayList<XYDLocation>();
    	Iterator<Integer> itr = coordList.iterator();
    	while( itr.hasNext() )
    		xydlist.add( new XYDLocation( itr.next(), itr.next(), itr.next(), itr.next() ) );
    	return xydlist;
    }
    
    public static int [] toIntArray(String inStr){
    	String [] nums 
    		= inStr.replace("[", "").replace("]", "")
    			.replace(" ", "").split(",");
    	ArrayList<Integer>coords = new ArrayList<Integer>();
    	for(String num : nums){
    		coords.add(Integer.parseInt(num, 10));
    	}
    	int [] outArray = new int[coords.size()];
    	Iterator <Integer> itr = coords.iterator();
    	for(int k = 0; k < outArray.length; k++)
    		outArray[k] = itr.next();
    	return outArray;
    }
    
    public static Location getFromLocation(String move){
    	int [] moveArray = toIntArray( move );
    	return new Location( moveArray[0] , moveArray[1]);
    }
    
    public static Location getToLocation(String move){
    	int [] moveArray = toIntArray( move );
    	return new Location( moveArray[moveArray.length - 2] , moveArray[moveArray.length - 1]);
    }

}
