package ShowNTell;

/**
 * CollisionAnalyst
 * Enforces collisions between pieces. Also validates that moves follow the rules.
 */

import java.util.*;

public class CollisionAnalyst extends OfficialObserver{
    
    private final Object VALIDATOR;
    
    public CollisionAnalyst( Object inValidator ){
        super();
        if(inValidator == null)
            VALIDATOR = false;
        else    
            VALIDATOR = inValidator;
    }
    
    @Override
    //called whenever an update is received from the observable
    protected void handleUpdate(){
        if( !super.checkRecipient( MY_EMAIL ) )
            return;
        super.replyToOfficial( MY_EMAIL, getNewBoardPosition( super.getMessage() ) );
    }

    //||||||||||||||MEMBER DATA|||||||||||||
    private static final String
        SPLIT_PHRASE = "SPLITSPLIT",
        MY_EMAIL = "c";
    private static final int
        DAMAGE_START = 5,
        DAMAGE_LITE = 5;
    //||||||||||||||||||||||||||||||||||||||

    public String getNewBoardPosition(String twoPlayerMoveData){
        ArrayList<String> playerMoves = new ArrayList<String>();
        String [] data = toStrArray( twoPlayerMoveData );
        String board = data[0];
        playerMoves.add(data[1]);
        playerMoves.add(data[2]);
        String outBoard = getNewPieceData( board, playerMoves );
        return outBoard;
    }

    private static String [] toStrArray(String multiData){
        return multiData.replace(" ", "").split(SPLIT_PHRASE);
    }

    private static boolean isOwnCollision(Location toLoc0, Location toLoc1, XYDLocation xyd){
        return (xyd.equals(toLoc0) && xyd.getTeam() == 0)
                || (xyd.equals(toLoc1) && xyd.getTeam() == 1);
    }

    private static boolean isEnemyCollision(Location toLoc0, Location toLoc1, XYDLocation xyd){
        return (xyd.equals(toLoc0) && xyd.getTeam() == 1)
                || (xyd.equals(toLoc1) && xyd.getTeam() == 0);
    }

    /*
     * returns new board piece locations
     * WARNING: only supports 2 player game
     */
    public String getNewPieceData( String oldBoard, ArrayList<String> movesList ){
        Location
            fromLoc0 = getFromLocation( movesList.get(0) ),
            fromLoc1 = getFromLocation( movesList.get(1) ),
            toLoc0 = getToLocation( movesList.get(0) ),
            toLoc1 = getToLocation( movesList.get(1) );
        ArrayList<XYDLocation>
            nextBoard = getXYDList(oldBoard);
        ArrayList<Location>
            toLocArray0 = getToLocationArray( movesList.get(0) ),
            toLocArray1 = getToLocationArray( movesList.get(1) );

        //Verify move is valid
        Integer damage0 = toIntArray(movesList.get(0))[2];
        Integer damage1 = toIntArray(movesList.get(1))[2];
        ArrayList<Object> params = new ArrayList<Object> ();
        Object [] test = {  damage0, fromLoc0, 0, toLocArray0, nextBoard, BOARD_SIZE        };
        for(Object param : test)
            params.add(param);
        ArrayList<Object> params2 = new ArrayList<Object> ();
        Object [] test2 = { damage1, fromLoc1, 1, toLocArray1, nextBoard, BOARD_SIZE };
        for(Object param : test2)
            params2.add(param);
        boolean isValid0 = !VALIDATOR.equals( params );
        boolean isValid1 = !VALIDATOR.equals( params2 );
        if (!isValid0 && !isValid1){
            return "2"+nextBoard.toString().replace(" ", "");
        }
        if (!isValid0){
            return "0"+nextBoard.toString().replace(" ", "");
        }
        if (!isValid1){
            return "1"+nextBoard.toString().replace(" ", "");
        }

        //Check if there was a collision and update the board
        boolean isHeadOnCollision = toLoc0.equals(toLoc1);
        boolean movedPiece0 = false, movedPiece1 = false;
        for( XYDLocation xyd : nextBoard ){
            if(!isHeadOnCollision){
                if( !movedPiece0 && xyd.equals( fromLoc0, 0, damage0) ){
                    xyd.setXY( toLoc0 );
                    movedPiece0 = true;
                }
                else if( !movedPiece1 && xyd.equals( fromLoc1, 1, damage1 ) ){
                    xyd.setXY( toLoc1 );
                    movedPiece1 = true;
                }
                else if( isOwnCollision( toLoc0, toLoc1, xyd ) )
                    xyd.setD( DAMAGE_START + 1);
                else if( isEnemyCollision( toLoc0, toLoc1, xyd ) )
                    xyd.setD( DAMAGE_LITE + 1);
            }
            else{
                if( !movedPiece0 && xyd.equals( fromLoc0, 0, damage0 ) ){
                    xyd.setXYD( toLoc0, DAMAGE_START + 1);
                    movedPiece0 = true;
                }
                else if( !movedPiece1 && xyd.equals( fromLoc1, 1, damage1 ) ){
                    xyd.setXYD( toLoc1, DAMAGE_START + 1);
                    movedPiece1 = true;
                }
            }
            xyd.heal(); //heals all pieces
        }
        
        //of superclass
        VICTORY = checkVictory(nextBoard);

        return "a"+nextBoard.toString().replace(" ", "");
    }
    
    private static boolean checkVictory(ArrayList<XYDLocation> nextBoard){
        boolean isAtFinish;
        boolean redVict = true, blueVict = true;

        //create lists of where pieces will be if someone has won
        ArrayList<XYDLocation> redVictory = new ArrayList<>();
        for (int x = BOARD_SIZE-3; x < BOARD_SIZE; x++){
            for (int y = 0; y < 3; y++){
                redVictory.add(new XYDLocation(x, y, 0, 0));
            }
        }
        ArrayList<XYDLocation> blueVictory = new ArrayList<>();
        for (int x = 0; x < 3; x++){
            for (int y = 0; y < 3; y++){
                blueVictory.add(new XYDLocation(x, y, 0, 0));
            }
        }

        //compare those lists to the current board
        for ( XYDLocation victory : redVictory ){
            isAtFinish = false;
            for ( XYDLocation xyd : nextBoard ){
                if (xyd.getX() == victory.getX() && xyd.getY() == victory.getY()){
                    isAtFinish = true;
                    break;
                }
            }
            if (isAtFinish == false){
                redVict = false;
                break;
            }
        }
        for ( XYDLocation victory : blueVictory ){
            isAtFinish = false;
            for ( XYDLocation xyd : nextBoard ){
                if (xyd.getX() == victory.getX() && xyd.getY() == victory.getY()){
                    isAtFinish = true;
                    break;
                }
            }
            if (isAtFinish == false){
                blueVict = false;
                break;
            }
        }
        return (redVict || blueVict);
    }

    private static ArrayList<Integer> toIntList(int [] coords){
        ArrayList<Integer> coordList = new ArrayList<Integer>();
        for(int coordinate : coords)
            coordList.add( coordinate );
        return coordList;
    }

    public static ArrayList<XYDLocation> getXYDList( String inBoard ){
        ArrayList<Integer> coordList = toIntList( toIntArray(inBoard) );
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
        for(String num : nums)
            coords.add(Integer.parseInt(num, 10));
        int [] outArray = new int[coords.size()];
        Iterator <Integer> itr = coords.iterator();
        for(int k = 0; k < outArray.length; k++)
            outArray[k] = itr.next();
        return outArray;
    }

    public static Location getFromLocation(String move){
        int [] moveArray = toIntArray( move );
        return new Location( moveArray[1] , moveArray[0]);
    }

    public static Location getToLocation(String move){
        int [] moveArray = toIntArray( move );
        return new Location( moveArray[moveArray.length - 1] , moveArray[moveArray.length - 2]);
    }

    public static ArrayList<Location> getToLocationArray(String move){
        int [] moveArray = toIntArray( move );
        ArrayList<Location> moveArrayList = new ArrayList<>();
        for (int i = 3; i < moveArray.length; i+=2){
            moveArrayList.add(new Location(moveArray[i+1], moveArray[i]));
        }
        return moveArrayList;
    }
}

