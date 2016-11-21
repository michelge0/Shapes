package animation;
import agent.Entity;


public class Speech
{	
	public Entity speaker;
	public String dialogue;
	
	// whether the speech is a pause
	public boolean pause;
	public int pauseTime;
		
	public Speech(Entity speaker, String dialogue) {
		this.speaker = speaker;
		this.dialogue = dialogue;
	}
	
	// for creating a pause in a dialogue sequence:
	public Speech(int timeInSeconds) {
		pauseTime = timeInSeconds * 500;
	}
	
	public void speak() {
		if (pause) Animation.pause(pauseTime);
		else speaker.speak(dialogue);
	}
}
