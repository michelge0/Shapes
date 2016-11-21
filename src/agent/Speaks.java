package agent;
import graphics.GUI;

import java.awt.*;

public interface Speaks
{
	public static final int ICON_CENTER_X = 55;
	public static final int ICON_CENTER_Y = GUI.graphics.getHeight() - 50;
	
	public static final int TEXTBOX_X = 120;
	public static final int TEXTBOX_Y = 490;
	
	/**
	 * @displayIcon
	 * The icon that is displayed in the dialogue
	 * window when this character is speaking.
	 */
	
	public abstract void displayIcon(Graphics g);
	
	/**
	 * @speakingHalo
	 * The halo that appears around a character,
	 * indicating that they are speaking.
	 */
	
	public abstract void speakingHalo(Graphics g);
}
