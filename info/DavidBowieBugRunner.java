

/** Name: Parker Moore
 * Class: AP Computer Science
 * Teacher: Mr. Klus
 * Program: 
 * Description: 
 */

/**
 * @author parkermoore
 *
 */
 
public class DavidBowieBugRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Location l = new Location(0, 0);
		Location l2 = new Location(0,1);
		Location l3 = new Location(100,0);
		System.out.println(l.compareTo(l3) );
		boolean [] checklist = {
				l.toString().equals("(0, 0)"),
				l.getRow() == 0,
				l.getCol() == 0,
				l.getAdjacentLocation(Location.EAST).equals( l2 ),
				l.getDirectionToward( l2 ) == 90,
				(Integer) l.hashCode() instanceof Integer,
				l.compareTo(l2) == 1,
				l.compareTo(5) == 0 - 100,
			};
		for(boolean b : checklist)
			System.out.println(l.compareTo(l3));
		try{
				ActorWorld world = new ActorWorld();
				int dir = Location.SOUTHEAST;
				int num = 9;
				for (int y = 0; y <= 2; y++){
					for (int x = 0; x <= 2; x++)
						world.add(new Location(y, x), new Piece( --num , dir));
				}
		/*	for (int b = 10; b <= 40; b+= 5){
				int c = 50;
				int d = c + 1;
				ActorWorld world = new ActorWorld(new BoundedGrid(c,d), b);
								world.show();
				for(int crit = 0; crit < b; crit++)
					world.add(new Location(crit, 0), new Critter());
				for(int k = 1; k < d; k++){
					for(int j = 0; j < c; j++){
							world.add(new Location(j, k), new Flower());
					}
				}
				for(int k = 0; k < 00; k++)
				world.add(new Location(k, 49), new Critter());
			}
			*/
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
