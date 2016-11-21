package agent;

import java.awt.Color;

import environment.Room;
import animation.Sound;

public class Animal extends Creature
{
	public Animal(Room room, int x, int y, int width, int height) {
		super(room, x, y, width, height);
		color = new Color(190, 115, 0);
		movesEvery = 6;
	}
	
	public Animal(int x, int y, int width, int height) {
		super(x, y, width, height);
		color = new Color(190, 115, 0);
		movesEvery = 6;
	}
	
	/*************************/
	// CORE METHODS
	
	public void collideIntoPlayer(Player collidee) {
		super.collideIntoPlayer(collidee);
		
		if (Math.random() > .8) Sound.BEAST1.play();
		else if (Math.random() > .6) Sound.BEAST2.play();
		else if (Math.random() > .4) Sound.BEAST3.play();
		else Sound.BEAST4.play();
	}
}
