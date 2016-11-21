package animation;
import agent.Entity;


public class AnimationMovement extends Animation
{
	public static final int UP = 90;
	public static final int DOWN = 270;
	public static final int RIGHT = 0;
	public static final int LEFT = 180;
	
	private Entity mover;
	private int direction;
	private double movesPerSecond;
	private int distance;
	private int travelledDistance;
	
	private AnimationMovement linkedTo; // for movements in sync
	private boolean started; // for diagonals
	
	public AnimationMovement(Entity mover, int direction, int distance) {
		super(.003);
		this.mover = mover;
		this.direction = direction;
		this.distance = distance;
		this.movesPerSecond = .003;
	}
	
	public AnimationMovement(Entity mover, int direction, int distance, double movesPerSecond) {
		super(movesPerSecond);
		
		this.mover = mover;
		this.direction = direction;
		this.distance = distance;
		this.movesPerSecond = movesPerSecond;
	}
	
	public void event() {
		if (!started) {
			if (travelledDistance >= distance) {
				travelledDistance = 0;
				stop();
			}
			
			if (linkedTo != null && !Animation.animations.contains(linkedTo)) {
				travelledDistance = 0;
				stop();
			}
	
			switch (direction) {
			case UP:
				mover.moveVertically(-1);
				travelledDistance++;
				break;
			case DOWN:
				mover.moveVertically(1);
				travelledDistance++;
				break;
			case RIGHT:
				mover.moveHorizontally(1);
				travelledDistance++;
				break;
			case LEFT:
				mover.moveHorizontally(-1);
				travelledDistance++;
				break;
			default:
				//some polar equations: x = rcos(theta), etc. 
				double angle = Math.toRadians(direction);
				int x = (int) (distance * Math.cos(angle));
				int y = (int) (distance * Math.sin(angle));
				AnimationMovement xMove, yMove;
				
				/*
				 * The larger one is the "primary" movement. The smaller
				 * one gets a timer slightly larger than the primary movement,
				 * so that they will end at a similar time.
				 */
				
				double xTime, yTime;
				
				if (Math.abs(x) > Math.abs(y)) {
					xTime = Math.abs(movesPerSecond);
					yTime = Math.abs((double) (x / y) * movesPerSecond);
				}
				else {
					xTime = Math.abs(((double) y / x) * movesPerSecond);
					yTime = Math.abs(movesPerSecond);
				}
				
				// when one ends, so does the other, so we only need to define
				// termination events for one of them (x)
				if (x > 0) xMove = new AnimationMovement(mover, RIGHT, x, xTime) {
					public void terminationEvent() {
						terminateParentEvent();
					}
				};
				else xMove = new AnimationMovement(mover, LEFT, Math.abs(x), xTime) {
					public void terminationEvent() {
						terminateParentEvent();
					}
				};
				if (y > 0) yMove = new AnimationMovement(mover, UP, y, yTime);
				else yMove = new AnimationMovement(mover, DOWN, Math.abs(y), yTime);
				
				xMove.linkedTo = yMove;
				yMove.linkedTo = xMove;
				
				yMove.start();
				xMove.start();
				
				/*
				 * For diagonal movements, original animation continues
				 * running after the sub-animations stop. 
				 */
				started = true;
				break;
			}
		}		
	}
	
	private void terminateParentEvent() {
		stop();
	}
}
