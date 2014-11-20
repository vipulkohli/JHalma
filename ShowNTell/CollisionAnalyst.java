package ShowNTell;

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
        ArrayList<String> playerMoves = new ArrayList<String>();
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
        
        ArrayList<XYDLocation> nextBoard = getXYDList(oldBoard);
        
        ArrayList<Location> toLocArray0 = getToLocationArray( movesList.get(0) );
        ArrayList<Location> toLocArray1 = getToLocationArray( movesList.get(1) );
        
        //Verify move is valid
        int damage0 = toIntArray(movesList.get(0))[2];
        int damage1 = toIntArray(movesList.get(1))[2];
        
        boolean isValid0 = isValidMoveRequest(damage0, fromLoc0, toLocArray0, nextBoard);
        boolean isValid1 = isValidMoveRequest(damage1, fromLoc1, toLocArray1, nextBoard);
        if (!isValid0 || !isValid1){
            OfficialObserver.print("INVALID MOVE!"+isValid0+isValid1);
        }
        
        //Check if there was a collision and update the board
        boolean isHeadOnCollision = toLoc0.equals(toLoc1);
    	for(XYDLocation xyd : nextBoard){
            xyd.heal(); //heals all pieces
            if(!isHeadOnCollision){
                if( isOwnCollision(toLoc0, toLoc1, xyd) )
                    xyd.setD( DAMAGE_START );
                if( isEnemyCollision(toLoc0, toLoc1, xyd) ){
                    xyd.setD( DAMAGE_LITE );
                    //isHeadOnCollision = true;
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
    
    //---------------FUNCTIONS FOR VALIDATING MOVES---------------
    //Based on the JavaScript version by Dr. Coyle
    private static boolean isFreeCell(Location c1, ArrayList<XYDLocation> gPiecesArr) {
        for(int i=0; i<gPiecesArr.size(); i++) {
            if(c1.getCol() == gPiecesArr.get(i).getX() &&
               c1.getRow() == gPiecesArr.get(i).getY())
            return false;
        }
        return true;
    }
    
    private static boolean isThereAPieceBetween(Location cell1, Location cell2, ArrayList<XYDLocation> gPieces) {
        /* note: assumes cell1 and cell2 are 2 squares away
         either vertically, horizontally, or diagonally */
        int rowBetween = (cell1.getRow() + cell2.getRow()) / 2;
        int columnBetween = (cell1.getCol() + cell2.getCol()) / 2;
        for (int i = 0; i < gPieces.size(); i++) {
            if ((gPieces.get(i).getY() == rowBetween) &&
                    (gPieces.get(i).getX() == columnBetween)) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isOneSpaceAway(Location c1, Location c2) {
        int diffx = Math.abs(c1.getCol() - c2.getCol());
        int diffy = Math.abs(c1.getRow() - c2.getRow());
        int diffxy = diffx + diffy;
        if (diffxy == 1) return true;  // x y axis
        if (diffx==1 && diffy==1) return true; // diagonal
        return false;  // not linear or diagonal
    }

    private static boolean isTwoSpacesAway(Location c1, Location c2) {
        int diffx = Math.abs(c1.getCol() - c2.getCol());
        int diffy = Math.abs(c1.getRow() - c2.getRow());
        // check x and y
        if  ((diffx == 2 && diffy == 0) ||
             (diffx == 0 && diffy == 2)  ) return true;  // x y axis
        // check diagonal
        if (diffx==2 && diffy==2) return true;
        return false;  // not linear or diagonal
    }

    // checks that src & dest are one cell apart and dest is free
    private static boolean isLegalOneSquareMove(Location src, Location dest, ArrayList<XYDLocation> gPiecesArr) {
        return (isOneSpaceAway(src,dest) && isFreeCell(dest, gPiecesArr)); 
    }

    // checks that 1) src & dest are two cells apart 2) dest is free
    //             3) there exists a piece between src and dest
    private static boolean isLegalTwoSquareJump(int damage, Location src, Location dest, ArrayList<XYDLocation> gPiecesArr) {
        return (isTwoSpacesAway(src,dest) && isFreeCell(dest, gPiecesArr) && 
                isThereAPieceBetween(src, dest, gPiecesArr) &&
                damage == 0); 
    }

    // jumpArr will have original source piece followed by jump locations
    private static boolean isArrayOfValidJumps(int damage, Location src, ArrayList<Location> jumpArr, ArrayList<XYDLocation> gPiecesArr) {
        // add src cell to array
        jumpArr.add(0, src);

        while (jumpArr.size() > 1) {
            // check first two cells for jump
            if ( !isLegalTwoSquareJump(damage, jumpArr.get(0), jumpArr.get(1), gPiecesArr) ) {
                print("Illegal jump from " + jumpArr.get(0).toString() +
                        " to: " + jumpArr.get(1).toString());
                return false;  
            }
            // remove first jump
            jumpArr.remove(0);
        }

        // all valid jumps
        return true;
    }

    // checks if piece is holding its position
    private static boolean isPieceHoldingPosition(Location src, Location dest) {
        return (src.getCol() == dest.getCol() && src.getRow() == dest.getRow());
    }


    // checks that array of requested moves is valid.
    // if only one move in array, check either non-jump or one jump
    // else check if all move pairs are jumping over some piece
    private static boolean isValidMoveRequest(int damage, Location src, ArrayList<Location> moveArr, ArrayList<XYDLocation> gPieces) {
        if(moveArr.isEmpty()) return false;

        if(moveArr.size() == 1) {
            Location dest = moveArr.get(0);  // only one
            return (isLegalOneSquareMove(src, dest, gPieces)  ||
                    isLegalTwoSquareJump(damage, src, dest, gPieces)  ||
                    isPieceHoldingPosition(src,dest) ); 
        } 

        // we have a multi jump request
        return isArrayOfValidJumps(damage, src, moveArr, gPieces);
    }

}
