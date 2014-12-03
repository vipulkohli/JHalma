package ShowNTell;

/**
 * Enforces halma game rules
 * Based on the JavaScript version by Dr. Coyle
 */
import java.util.*;

public class MoveValidator{
    
    public static boolean isThereAPieceBetween(Location cell1, Location cell2, ArrayList<XYDLocation> gPieces) {
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

    public static boolean isOneSpaceAway(Location c1, Location c2) {
        int diffx = Math.abs(c1.getCol() - c2.getCol());
        int diffy = Math.abs(c1.getRow() - c2.getRow());
        int diffxy = diffx + diffy;
        if (diffxy == 1) return true;  // x y axis
        if (diffx==1 && diffy==1) return true; // diagonal
        return false;  // not linear or diagonal
    }

    public static boolean isTwoSpacesAway(Location c1, Location c2) {
        int diffx = Math.abs(c1.getCol() - c2.getCol());
        int diffy = Math.abs(c1.getRow() - c2.getRow());
        // check x and y
        if  ((diffx == 2 && diffy == 0) || (diffx == 0 && diffy == 2)  ) 
             	return true;  // x y axis
        // check diagonal
        if (diffx==2 && diffy==2) 
        	return true;
        return false;  // not linear or diagonal
    }

    // checks that src & dest are one cell apart and dest is free
    public static boolean isLegalOneSquareMove(Location src, Location dest, ArrayList<XYDLocation> gPiecesArr) {
        return isOneSpaceAway(src,dest);
    }

    // checks that 1) src & dest are two cells apart 2) dest is free
    //             3) there exists a piece between src and dest
    public static boolean isLegalTwoSquareJump(int damage, Location src, Location dest, ArrayList<XYDLocation> gPiecesArr) {
        return (isTwoSpacesAway(src,dest) &&
                isThereAPieceBetween(src, dest, gPiecesArr) &&
                damage == 0);
    }

    // jumpArr will have original source piece followed by jump locations
    public static boolean isArrayOfValidJumps(int damage, Location src, ArrayList<Location> jumpArr, ArrayList<XYDLocation> gPiecesArr) {
        // add src cell to array
        jumpArr.add(0, src);
        while (jumpArr.size() > 1) {
            // check first two cells for jump
            if ( !isLegalTwoSquareJump(damage, jumpArr.get(0), jumpArr.get(1), gPiecesArr) ) {
                System.out.print("Illegal jump from " + jumpArr.get(0).toString() +
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
    public static boolean isPieceHoldingPosition(Location src, Location dest) {
        return (src.getCol() == dest.getCol() && src.getRow() == dest.getRow());
    }
	@Override
	public boolean equals(Object o){
		if(o instanceof List){
			ArrayList<Object> list = (ArrayList<Object>) o;
			if(list.size() != 5)
				return super.equals(o);
			Iterator<Object>itr = list.iterator();
			return !isValidMoveRequest((int) itr.next(), (Location) itr.next(), (ArrayList<Location>) itr.next(), (ArrayList<XYDLocation>) itr.next(), (int) itr.next() );
		}
		return super.equals(o);
	}
	
    // checks that array of requested moves is valid.
    // if only one move in array, check either non-jump or one jump
    // else check if all move pairs are jumping over some piece
    public static boolean isValidMoveRequest(int damage, Location src, ArrayList<Location> moveArr, ArrayList<XYDLocation> gPieces, int BOARD_SIZE) {
        if(moveArr.isEmpty())
            return false;
        
        //check if the AI tries to move outside the board
        Location finalMove = moveArr.get(moveArr.size()-1);
        if (finalMove.getCol() >= BOARD_SIZE || finalMove.getCol() < 0 ||
                finalMove.getRow() >= BOARD_SIZE || finalMove.getRow() < 0)
            return false;
        
        //check that a single move is valid
        if(moveArr.size() == 1) {
            Location dest = moveArr.get(0);  // only one
            return (isLegalOneSquareMove(src, dest, gPieces)  ||
                isLegalTwoSquareJump(damage, src, dest, gPieces)  ||
                isPieceHoldingPosition(src,dest) );
        }
        
        //check that a jump-chain is valid
        return isArrayOfValidJumps(damage, src, moveArr, gPieces);
    }
}

