package ShowNTell;

import com.grack.nanojson.*;

public class XYDLocation{
	
    int mDamage;
    Location mLoc;
    int mTeam;

    public XYDLocation(int x, int y, int d, int t){
        mLoc = new Location( y , x );
        mDamage = d;
        mTeam = t;
    }
    
    public XYDLocation setTeam(int t){
        mTeam = t;
        return this;
    }
    
    public int getTeam(){
        return mTeam;
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
        setXY(moveLoc.getCol(), moveLoc.getRow());
        return this;
    }
    
    public XYDLocation setXY(int x, int y){
        setX(x);
        setY(y);
        return this;
    }
    
    public void setX(int x){
        int y = getY();
        mLoc = new Location(y, x);
    }
    public void setY(int y){
        int x = getX();
        mLoc = new Location(y, x);
    }
    public int getX(){
            return mLoc.getCol();
    }
    public int getY(){
            return mLoc.getRow();
    }
    public int getD(){
            return mDamage;
    }
    
    public boolean equals( Location other  ){
        return mLoc.equals( other );
    }
    
    public boolean equals(Location other, int otherTeam){
        return mLoc.equals(other) && otherTeam == mTeam;
    }
    
    @Override
    public String toString(){
        return getX() + "," + getY() + "," + getD() + "," + getTeam();
    }
    
	public String toJSONString(){
		return JsonWriter.string()
                        .object()
                        .value("x", getX())
                        .value("y", getY())
                        .value("damage", getD())
                        .value("team", getTeam())	
                        .end()
                        .done(); 
	}
}