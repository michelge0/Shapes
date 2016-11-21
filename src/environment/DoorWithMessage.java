package environment;
import graphics.GUI;
import agent.Entity;


public class DoorWithMessage extends Door
{
	public String openMessage;
	public String closedMessage;
	
	public DoorWithMessage(Room room, int x, int y, int width, int height,
			String openMessage, String closedMessage) {
		
		super(room, x, y, width, height);
		
		this.openMessage = openMessage;
		this.closedMessage = closedMessage;
	}

	public boolean collisionEvent(Entity collider) {
		if (collider.getClass().getSimpleName().equals("Player")) {
			if (open) GUI.player.speak(1, openMessage);
			else GUI.player.speak(1, closedMessage);
		}
		return super.collisionEvent(collider);
	}
}