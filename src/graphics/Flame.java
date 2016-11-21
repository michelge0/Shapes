package graphics;

import java.awt.*;

import util.Random;
import agent.Entity;
import animation.Animation;

public class Flame extends BackgroundPainting
{
	private double lifetime;
	private Fire fire;
	
	public Flame(Fire fire, int x, int y, int base, int height, double lifetime) {
	    super(fire.room, x, y, base, height, 0);
	    
	    this.fire = fire;
	    fire.flames.add(this);
	    
	    fillColor = new Color(255, (int) (Math.random() * 255), 0, Random.boundedInt(100, 255));
	    this.lifetime = lifetime;
	    
	    new Animation(this.lifetime) {
	    	public void event() {
	    		Flame.this.fire.flames.remove(Flame.this);
	    		room.remove(Flame.this);
	    		
	    		if (Math.random() > .75) {
	    			new Ember(Flame.this.fire, xPos, yPos, xSize, ySize, Flame.this.lifetime * 4);
	    		}
	    		
	    		stop();
	    	}
	    }.start();
    }
	
	private class Ember extends Flame {
		public Ember(Fire fire, int x, int y, int base, int height, double lifetime) {
		    super(fire, x, y, base, height, lifetime);
		    
		    new Animation(1) {
		    	public void event() {
		    	    fillColor = new Color((int) (Math.random() * 255), 0, 0, Random.boundedInt(200, 255));
		    	}
		    }.start();
	    }
	}
	
	/*********************************/
	// CORE METHODS
	
	public boolean collisionEvent(Entity collider) {
		fire.flames.remove(this);
		room.remove(this);
		return false;
	}
	
	public void draw(Graphics g) {
		g.setColor(fillColor);
		Polygon shape = new Polygon(new int[] {xPos, xPos + xSize / 2, xPos + xSize}, new int[] {yPos + ySize, yPos, yPos + ySize}, 3);
		g.fillPolygon(shape);
	}
}
