package environment;
import graphics.GUI;
import agent.Entity;
import animation.Animation;

public class Gate extends Barrier
{	
	protected Room linkedRoom; // room to which it leads
	
	private Animation event;
	private boolean hasEvent;
	
	private boolean inDungeon; // whether it is part of a classic dungeon
	private int direction;	
	
	// general:
	public Gate(Room room, int x, int y, int width, int height) {
		super(room, x, y, width, height);
		
		initialize();
	}
	
	// with linkedRoom:
	public Gate(Room room, int x, int y, int width, int height, Room linkedRoom) {
		super(room, x, y, width, height);
		
		this.linkedRoom = linkedRoom;
		initialize();
	}
	
	// for dungeons:
	public Gate(Room room, int x, int y, int width, int height, int direction) {
		super(room, x, y, width, height);
		
		this.direction = direction;
		try {
			linkedRoom = ((RoomDungeon) room).getAdjacent(direction);
		}
		catch (Exception e) {}
		inDungeon = true;
		
		initialize();
	}
	
	public void initialize() {
		room.addToGates(this);
		open = true;
		formID = "BarrierGate";
	}
	
	/*******************************************************/
	
	public void linkTo(Room room) {
		linkedRoom = room;
	}
	
	public void setEvent(Animation animation) {
		hasEvent = true;
		event = animation;
	}
	
	/******************************************************/
	// CORE METHODS
	
	/**
	 * @collisionEvent
	 * the large if statement tests if the player actually
	 * fits within the doorframe
	 */
	
	public boolean collisionEvent(Entity collider) {	
		if (!open) return true;
		
		if (collider.getClass().getSimpleName().equals("Player")) {
			if (!(collider.getXCenter() >= this.xPos &&
				collider.getXCenter() + collider.getWidth() / 2 <= this.xPos + this.xSize
				||
				collider.getYCenter() >= this.yPos &&
				collider.getYCenter() + collider.getHeight() / 2 <= this.yPos + this.ySize))
				return true;
						
			if (linkedRoom != null) {				
				if (inDungeon) {
					RoomDungeon destination = (RoomDungeon) linkedRoom;
					
					Dungeon.setLocation(linkedRoom);
					destination.createWalls(GUI.graphics.getWidth(), GUI.graphics.getHeight() - 100);
					destination.setSpawn(RoomDungeon.opposite(direction));
					Dungeon.spawn(linkedRoom.spawnLocation);
				}
				
				else {
					if (hasEvent) event.start();
					Dungeon.teleport(linkedRoom);
				}
				
				return false;
			}
		}
		return true;
	}
}
