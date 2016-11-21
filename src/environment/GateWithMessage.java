package environment;
import graphics.GUI;
import agent.Entity;
import animation.Sound;


public class GateWithMessage extends Gate
{
	public String openMessage;
	public String closedMessage;
	
	public GateWithMessage(Room room,
		int x, int y, int width, int height, Room linkedRoom,
		String openMessage, String closedMessage) {
		
		super(room, x, y, width, height, linkedRoom);
		
		this.openMessage = openMessage;
		this.closedMessage = closedMessage;
	}
	
	public boolean collisionEvent(Entity collider) {
		if (collider.getClass().getSimpleName().equals("Player")) {
			if (!(collider.getXPos() - collider.getWidth() / 2 > this.xPos &&
				collider.getXPos() + collider.getWidth() / 2 < this.xPos + this.xSize
				||
				collider.getYPos() - collider.getHeight() / 2 > this.yPos &&
				collider.getYPos() + collider.getHeight() / 2 < this.yPos + this.ySize)) {
				return true;
			}
			
			if (!open) GUI.player.speak(1, this.closedMessage);
			
			if (open && linkedRoom != null) {
				Dungeon.teleport(linkedRoom);
				GUI.player.speak(1, this.openMessage);
				
				Sound.SCHOOL_DOOR.play();
				return false;
			}			
		}
		return true;
	}
}
