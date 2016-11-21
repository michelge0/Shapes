package graphics;

import java.util.ArrayList;

import util.Random;
import animation.Animation;
import environment.Room;
import environment.Zone;

public class Fire extends Zone
{
	ArrayList<Flame> flames = new ArrayList<Flame>();
	Animation flameAnimation;
	
	public Fire(Room room, int x, int y, int width, int height) {
	    super(room, x, y, width, height);
	    
	    createFlames();
    }
	
	private void createFlames() {
		flameAnimation = new Animation(.25) {
			public void event() {
				int numberFlames = (int) (Math.random() * 5);
				
				for (int i = 0; i < numberFlames; i++) {
					int randomX = (int) (Math.random() * (xSize - 10) + xPos);
					int randomY = (int) (Math.random() * (ySize - 10) + yPos);
					int randomBase = Random.boundedInt(10, 20);
					int randomHeight = Random.boundedInt(10, 20);
					
					if (Math.random() > .5) {
						new Flame(Fire.this, randomX, randomY, randomBase, randomHeight, Random.bounded(.5, 1.5));
					}
				}
			}
		};
		flameAnimation.start();
	}
	
	public void extinguish() {
		flameAnimation.stop();
		flames.clear();
	}
}
