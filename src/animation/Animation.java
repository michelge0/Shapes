package animation;

import agent.Entity;
import graphics.GUI;

import java.util.ArrayList;
import java.awt.*;

public abstract class Animation
{
	/****************************************
	 * STATIC FIELDS AND METHODS
	 */
	
	// Managing all animations:
	
	static ArrayList<Animation> animations = new ArrayList<Animation>();
	
	public static void incrementAll() {
		for (int i = 0; i < animations.size(); i++) {
			animations.get(i).increment();
		}
	}
	
	public static void endAll() {
		animations.clear();
	}
	
	// Background dialogue:
	
	public static Entity backgroundSpeaker;
	public static String backgroundDialogue;
	public static int backgroundDialogueCounter;
	public static int backgroundDialogueTime;
	
	public static AnimationSequence currentDialogue;
	
	public static void backgroundDialogue(Graphics g) {
		if (backgroundSpeaker != null) {
			backgroundSpeaker.displayIcon(g);
			
			GUI.text.drawText(g, backgroundDialogue);
			
			if (backgroundDialogueCounter >= backgroundDialogueTime) {
				backgroundDialogue = "";
				backgroundSpeaker = null;
				backgroundDialogueCounter = 0;
			}
			
			if (backgroundDialogueCounter == Entity.INACTIVE) return;
			
			backgroundDialogueCounter += 2;
		}
	}
	
	public static void speakBackground(Entity speaker, String text) {
		if (text == "") return;
		
		backgroundDialogue = text;
		
		backgroundSpeaker = speaker;
		
		backgroundDialogueCounter = Entity.INACTIVE;
	}
	
	public static void speakBackground(int timeInSeconds, Entity speaker, String text) {
		speakBackground(speaker, text);
		
		backgroundDialogueTime = 1000 * timeInSeconds;
		backgroundDialogueCounter = Entity.ACTIVE;
	}
	
	public static void enterDialogueMode(AnimationSequence dialogue) {
		currentDialogue = dialogue;
		GUI.player.freeze();
		GUI.graphics.removeKeyListener(GUI.player);
		GUI.graphics.addKeyListener(dialogue);
	}
	
	public static void leaveDialogueMode() {
		GUI.graphics.removeKeyListener(currentDialogue);
		GUI.player.unfreeze();
		GUI.graphics.addKeyListener(GUI.player);
	}
	
	// Time control:
	
	public static void pause(int timeInSeconds) {
		 try {
			 Thread.sleep(timeInSeconds * 500);
		 } catch (Exception e) {}
	}
	
	/******************************************
	 * NON STATIC FIELDS AND METHODS
	 */
	
	int counter = 0;
	int eventTime; // event happens every eventTime milliseconds
	
	public Animation(double eventTimeInSeconds) {
		this.eventTime = (int) (eventTimeInSeconds * 1000);
	}
	
	public void increment() {
		if (counter >= eventTime) {
			event();
			counter = 0;
		}
		// +=2 makes each 1000 counters last 1 second
		else counter += 2;
	}
	
	// starts an animation:
	public void start() {
		animations.add(this);
	}
	
	public void stop() {
		terminationEvent();
		animations.remove(this);
	}
	
	// to be defined by individual objects (is called when the animation ends)
	public void terminationEvent() {}
	
	// each Animation object defines its own event
	public abstract void event();
}
