package level;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.Timer;
import javax.imageio.ImageIO;

import animation.Sound;
import environment.CoordinateDungeon;
import environment.Dungeon;
import graphics.GUI;

public abstract class Level
{	
	/*********************************
	 *	STATIC FIELDS AND METHODS
	 *********************************/
	
	public static Image startScreen, endScreen, credits1, credits2, credits3, credits4, credits5, credits6, currentCreditScreen;
	
	public static Level currentLevel;
	public static SpawnPoint currentSpawn;
	
	public static boolean credits;

	public static Timer displayTimer = new Timer(9100, new ActionListener() {
		int screen = 2;
		public void actionPerformed(ActionEvent e) {
			switch (screen) {
			case 2:
				currentCreditScreen = credits2;
				GUI.graphics.repaint(); break;
			case 3: currentCreditScreen = credits3;
				GUI.graphics.repaint(); break;
			case 4: currentCreditScreen = credits4;
				GUI.graphics.repaint(); break;
			case 5: currentCreditScreen = credits5;
				GUI.graphics.repaint(); break;
			case 6: currentCreditScreen = credits6;
				GUI.graphics.repaint(); break;
			default: credits = false;
				GUI.graphics.repaint(); screen = 2; break;
			}
		screen++;
		}
	});
	
	public static void setLevel(Level level) {
		currentLevel = level;
		GUI.currentDungeon = currentLevel.dungeon;
		level.createRooms();
	}
	
	public static boolean gameOver = false;
	
	public static void setScreen(Graphics g) {
		if (credits) g.drawImage(currentCreditScreen, 0, 0, GUI.graphics);
		else if (gameOver) setEndScreen(g);
		else setStartScreen(g);
	}
	
	public static void setEndScreen(Graphics g) {
		g.clearRect(0, 0, GUI.graphics.getWidth(), GUI.graphics.getHeight());
			g.drawImage(endScreen, 0, 0, GUI.graphics);
			GUI.graphics.addKeyListener(new KeyListener() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_SPACE) {
						GUI.player.stopMovements();
						GUI.gameInProgress = true;
						currentSpawn.respawn();
						GUI.graphics.addKeyListener(GUI.player);
						GUI.graphics.removeKeyListener(this);
					}
				}
				public void keyReleased(KeyEvent e) {}
				public void keyTyped(KeyEvent e) {}
			});
	}
	
	public static void credits() {
		credits = true;
		Sound.CREDITS.play();
		currentCreditScreen = credits1;
		GUI.graphics.repaint();
		
		displayTimer.restart();
	}
	
	public static void setStartScreen(Graphics g) {
		g.clearRect(0, 0, GUI.graphics.getWidth(), GUI.graphics.getHeight());
		
		g.drawImage(startScreen, 0, 0, GUI.graphics);
		
		displayTimer.stop();

		GUI.graphics.requestFocus();
		
		GUI.graphics.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					GUI.graphics.initGUI();
					GUI.gameInProgress = true;
					GUI.graphics.removeKeyListener(this);
					Level.setLevel(new LevelOne());	
				}
				else if (e.getKeyCode() == KeyEvent.VK_C) {
					GUI.graphics.removeKeyListener(this);
					credits();
				}
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});
	}
	
	public static void initializeImages() {
		try {
				startScreen = ImageIO.read(Level.class.getResource("/resources/Title Screen.png"));
			  } catch (IOException e) { e.printStackTrace(); }
			try {
				endScreen = ImageIO.read(Level.class.getResource("/resources/End Screen.png"));
			} catch (IOException e) { e.printStackTrace(); }
			try {
				credits1 = ImageIO.read(Level.class.getResource("/resources/Credits 1.png"));
			} catch (IOException e) { e.printStackTrace(); }
			try {
				credits2 = ImageIO.read(Level.class.getResource("/resources/Credits 2.png"));
			} catch (IOException e) { e.printStackTrace(); }
			try {
				credits3 = ImageIO.read(Level.class.getResource("/resources/Credits 3.png"));
			} catch (IOException e) { e.printStackTrace(); }
			try {
				credits4 = ImageIO.read(Level.class.getResource("/resources/Credits 4.png"));
			} catch (IOException e) { e.printStackTrace(); }
			try {
				credits5 = ImageIO.read(Level.class.getResource("/resources/Credits 5.png"));
			} catch (IOException e) { e.printStackTrace(); }
			try {
				credits6 = ImageIO.read(Level.class.getResource("/resources/Credits 6.png"));
			} catch (IOException e) { e.printStackTrace(); }	
	}

	
	/*********************************
	 * NONSTATIC FIELDS AND METHODS
	 *********************************/
	
	public Dungeon dungeon;
	public CoordinateDungeon startingLocation;
	
	public void initializeDungeon(int x, int y, String dungeonType) {
		dungeon = new Dungeon(x, y, dungeonType);
	}
	
	/*********************************/
	// CORE METHODS
	
	// used to create unique content of each room
	public abstract void createRooms();
}
