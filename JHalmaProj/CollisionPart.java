/**
 * @(#)CollisionPart.java
 *
 *
 * @author 
 * @version 1.00 2014/11/12
 */
import info.gridworld.grid.*;
import java.util.*;
public class CollisionPart {
private static final int DAMAGE_START = 5;
       /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	ArrayList<String>playerMoves = new ArrayList<String>();
        playerMoves.add("[1,2,3,4,5,6]");
        playerMoves.add("[9,8,7,4,5,6]");
        String board = "[0,0,0,9,8,0,1,2,3]";
      	getNewPieces(board, playerMoves );
    }
    
    public static void getNewPieces(String oldBoard, ArrayList<String>movesList ){
    	Location fromLoc0 = getFromLocation( movesList.get(0) );
    	Location toLoc0 = getToLocation( movesList.get(0) );
        Location fromLoc1 = getFromLocation( movesList.get(1) );
        Location toLoc1 = getToLocation( movesList.get(1) );
        boolean isCollision = toLoc0.equals(toLoc1);
    	ArrayList<XYDLocation> nextBoard = getXYDList(oldBoard);
    	for(XYDLocation xyd : nextBoard){
            if(!isCollision){
                if(xyd.equals(fromLoc0))
                    xyd.setXY(toLoc0).heal();
                else if (xyd.equals(fromLoc1))
                    xyd.setXY(toLoc1).heal();
            }
            else{
                if(xyd.equals(fromLoc0)){
                    xyd.setXYD(toLoc0, DAMAGE_START);
                }
                else if (xyd.equals(fromLoc1)){
                    xyd.setXYD(toLoc1, DAMAGE_START);
                }
            }
            System.out.println(xyd);
    	}
    }
    
    private static ArrayList<XYDLocation> getXYDList( String inBoard ){
    	int[]coords = toIntArray(inBoard);
    	ArrayList<Integer>coordList = new ArrayList<Integer>();
    	ArrayList<XYDLocation> xydlist = new ArrayList<XYDLocation>();
    	for(int coordinate : coords){
    		coordList.add( coordinate );
    	}
    	Iterator<Integer> itr = coordList.iterator();
    	while( itr.hasNext() )
    		xydlist.add( new XYDLocation( itr.next(), itr.next(), itr.next() ) );
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
    	return new Location( moveArray[moveArray.length - 3] , moveArray[moveArray.length - 2]);
    }
    
    private static class XYDLocation{
    	
    	int mDamage;
    	Location mLoc;
    	
    	public XYDLocation(int x, int y, int d){
    		mLoc = new Location( x , y );
    		mDamage = d;
    	}
        
        public XYDLocation heal(){
            if (mDamage > 0)
                mDamage--;
            return this;
        }
        
        public XYDLocation setD(int d){
            mDamage = d;
            return this;
        }
        public XYDLocation setXYD(Location moveLoc, int d){
            setD(d);
            setXY(moveLoc);
            return this;
        }
        public XYDLocation setXY(Location moveLoc){
            setXY(moveLoc.getRow(), moveLoc.getCol());
            return this;
        }
        
        public XYDLocation setXY(int x, int y){
            setX(x);
            setY(y);
            return this;
        }
        public void setX(int x){
            int y = getY();
            mLoc = new Location(x, y);
        }
        public void setY(int y){
            int x = getX();
            mLoc = new Location(x, y);
        }
    	public int getX(){
    		return mLoc.getRow();
    	}
    	public int getY(){
    		return mLoc.getCol();
    	}
    	public int getD(){
    		return mDamage;
    	}
    	public boolean equals(Location other){
    		return mLoc.equals(other);
    	}
    	public String toString(){
    		return "(" + getX() + "," + getY() + "," + getD() + ")";  
    	}
    }

}
