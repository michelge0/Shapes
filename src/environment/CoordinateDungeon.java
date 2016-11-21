package environment;


public class CoordinateDungeon extends Coordinate<Integer>
{
	public CoordinateDungeon(int x, int y) {
		super(x, y);
	}
	
	public Room getRoom(Dungeon dungeon) {
		return dungeon.rooms.get(this.x).get(this.y);
	}
		
	public void setCoordinates(Dungeon dungeon, Room room) {
		x = room.getXIndex();
		y = room.getYIndex();
	}
	
	public void incrementX(int amount) {
		x += amount;
	}
	
	public void incrementY(int amount) {
		y += amount;
	}
}
