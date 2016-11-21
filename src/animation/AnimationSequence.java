package animation;
import graphics.GUI;

import java.util.ArrayList;
import java.awt.event.*;

public class AnimationSequence implements KeyListener
{
	ArrayList<Object> animations = new ArrayList<Object>();
	public int currentSequenceIndex = 0;
	
	public AnimationSequence(ArrayList<Object> animations) {
		this.animations = animations;
	}
	
	public AnimationSequence(Object[] animations) {
		for (Object i: animations) {
			this.animations.add(i);
		}
	}
	
	public void nextAnimation() {
		try {
			Object currentAnimation = animations.get(currentSequenceIndex);

				try {
					((Speech) currentAnimation).speak();
				} catch (Exception e) {}
				
				try {
					((Animation) currentAnimation).start();
				} catch (Exception e) {}
				
			currentSequenceIndex++;
		}
		catch (IndexOutOfBoundsException e) {
			Animation.leaveDialogueMode();
			GUI.currentSpeaker = null;
			finishAnimationEvent();
		}
	}
	
	// This can be redefined at will:
	public void finishAnimationEvent() {}
	
	/**************************************/
	// CORE METHODS
	
	public void keyPressed(KeyEvent e) {
		try {
			Animation lastAnimation = (Animation) animations.get(currentSequenceIndex - 1);
			// checks if the last animation still is running (exists in Animation.animations)
			if (Animation.animations.indexOf(lastAnimation) != -1) {
				return;
			}
		}
		// if last animation is of type Speech, an exception is thrown and the try block is skipped
		catch (Exception evt) {}
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			nextAnimation();
		}
	}
	
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
