package environment;
import java.util.ArrayList;

import agent.Entity;
import agent.Creature;
import animation.Animation;

public class Room
{		
	protected int xIndex, yIndex;
	protected Dungeon dungeon;
	
	protected boolean hasEntranceEvent;
	protected Animation entranceEvent;
	
	/*
	 * First array in barriers signifies layer of barrier.
	 * Layer 0 is background stuff; layer 1 is gates; layer
	 * 2 is walls.
	 */
	
	public ArrayList<ArrayList<Barrier>> barriers = new ArrayList<ArrayList<Barrier>>(3);
		public final ArrayList<Barrier> backgrounds = new ArrayList<Barrier>();
		public final ArrayList<Barrier> gates = new ArrayList<Barrier>();
		public final ArrayList<Barrier> walls = new ArrayList<Barrier>();
	public ArrayList<Entity> agents = new ArrayList<Entity>();
	
	protected Coordinate<Integer> spawnLocation = new Coordinate<Integer>(50, 50);
	
	public Room(Dungeon dungeon, int x, int y) {
		xIndex = x;
		yIndex = y;
		initialize(dungeon);
	}
	
	// floating rooms:
	public Room(Dungeon dungeon) {
		xIndex = -1;
		yIndex = -1;
		initialize(dungeon);
	}
	
	protected void initialize(Dungeon dungeon) {
		this.dungeon = dungeon;

		barriers.add(backgrounds);
		barriers.add(gates);
		barriers.add(walls);
	}
	
	/*****************************/
	// MANAGING BARRIERS
	
	public void addToBackground(Barrier background) {
		background.formID = "Background";
		backgrounds.add(background);
	}
	
	public void addToGates(Barrier gate) {
		gate.formID = "Gate";
		gates.add(gate);
	}
	
	public void addToWalls(Barrier wall) {
		wall.formID = "Wall";
		walls.add(wall);
	}
	
	public void remove(Barrier barrier) {
		if (barrier.formID.equals("Background")) backgrounds.remove(barrier);
		else if (barrier.formID.equals("Wall")) walls.remove(barrier);
		else if (barrier.formID.equals("Gate")) gates.remove(barrier);
	}
	
	/*****************************/
	// SETTING AND GETTING
	
	public int getXIndex() {
		return xIndex;
	}
	
	public int getYIndex() {
		return yIndex;
	}
	
	public void setSpawnLocation(int x, int y) {
		spawnLocation.setX(x);
		spawnLocation.setY(y);
	}
	
	public int getXSpawn() {
		return spawnLocation.getX();
	}
	
	public int getYSpawn() {
		return spawnLocation.getY();
	}
	
	// Room objects define this for themselves:
	public void setEntranceEvent(Animation animation) {
		hasEntranceEvent = true;
		
		entranceEvent = animation;
	}
	
	public void entranceEvent() {
		if (entranceEvent != null)
			entranceEvent.start();
	}
	
	public void remove(Creature creature) {
		agents.remove(creature);
	}
	
	public void add(Creature creature) {
		agents.add(creature);
	}
}