package agent;

import environment.Room;
import environment.Tree;
import environment.TreeLeaf;
import graphics.GUI;

import java.awt.Color;
import java.util.ArrayList;

import animation.Animation;
import animation.Sound;

public class Assassin extends Creature
{
	TreeLeaf hidingSpot;
	ArrayList<Tree> hidingSpots = new ArrayList<Tree>();
	boolean chasingPlayer;
	boolean soundPlaying;
	
	public Assassin(Room room, int x, int y, int width, int height) {
		super(room, x, y, width, height);
		
		initialize();
	}
	
	public Assassin(Room room, int x, int y, int width, int height, int sides) {
		super(room, x, y, width, height);
		
		initialize();
	}
	
	private void initialize() {
		Sound.BUG_WALKING.play();

		color = Color.BLACK;
		movesEvery = 4;
		
		for (int i = 0; i < room.walls.size(); i++) {
			if (room.walls.get(i).getClass().getSimpleName().equals("Tree"))
				hidingSpots.add((Tree) room.walls.get(i));
		}
		
		findHidingSpot();
	}
	
	/**************************************/
	// MOVEMENT
	
	private void playSound() {
		Sound.BUG_WALKING.play();
		soundPlaying = true;
		new Animation(7) {
			public void event() {
				soundPlaying = false;
				stop();
			}
		}.start();
	}
	
	public void calculateMovements() {
		if (near(GUI.player, 100)) {
			playSound();
			chasingPlayer = true;
			track(GUI.player);
			return;
		}
		
		else {
			if (chasingPlayer) {
				chasingPlayer = false;
				findHidingSpot();
			}
			track(hidingSpot);
		}
	}
	
	public void findHidingSpot() {		
		for (int i = 0; i < hidingSpots.size(); i++) {
			for (int j = 0; j < hidingSpots.get(i).leaves.size(); j++) {
				TreeLeaf thisLeaf = hidingSpots.get(i).leaves.get(j);
				if (isLargeEnough(thisLeaf) && near(thisLeaf, 100)) {
					hidingSpot = thisLeaf;
					shuffleHidingSpots();
					return;
				}
			}
		}
	}
	
	/**********************************************/
	// UTILITIES
	
	public boolean isLargeEnough(TreeLeaf leaf) {
		if (leaf.diameter >= this.xSize &&
			leaf.diameter >= this.ySize) return true;
		else return false;
	}
	
	public void shuffleHidingSpots() {
		for(int i = 0; i < hidingSpots.size(); i++) {
			int randomSwitch = (int) (Math.random() * hidingSpots.size());
			Tree placeholder = hidingSpots.get(i);	// some variable manipulating is required
			hidingSpots.set(i, hidingSpots.get(randomSwitch));
			hidingSpots.set(randomSwitch, placeholder);
		}
	}
	
	public void collideIntoPlayer(Player collidee) {
		collidee.takeRecoil(this);
		collidee.takeDamage(1);
		collidee.speak(1, "Get it off me! Get it off!");
		
		if (Math.random() > .75) Sound.BUG1.play();
		else if (Math.random() > .5) Sound.BUG2.play();
		else Sound.BUG3.play();
		
		
		if (Math.random() > .6) {
			room.agents.remove(this);
		}
	}
}
