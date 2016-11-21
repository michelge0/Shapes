package graphics;
import java.awt.*;

import javax.swing.*;

import environment.Barrier;
import environment.Dungeon;
import agent.Entity;
import agent.Player;
import animation.Animation;

import java.awt.event.*;
import java.util.ArrayList;

import level.Level;

public class GUI extends JPanel
{	
	public static GUI graphics;
	public static TextArea text;
	
	public static Player player;
	public static Dungeon currentDungeon;
	
	public static Entity currentSpeaker;
		
	public static boolean gameInProgress = false;
	public static Animation currentAnimation;
	
	public static void main(String[] args) {
		Level.initializeImages();
		graphics = new GUI();
		
		JFrame window = new JFrame("Shapes");
		window.add(graphics);
		window.setSize(500, 600);
		window.setLocation(15, 15);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setResizable(false);
	}
		
	public void initGUI() {
		player = new Player(130, 130, 18);
		
		addMouseListener(new MouseAdapter() { 
	        public void mousePressed(MouseEvent evt) {
	            requestFocus();
	        }
		});
		
		// all events are defined in terms of this timer
		Timer actionTimer = new Timer(1, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.grow();
				player.move();
				Entity.moveAgents();
				Animation.incrementAll();
				repaint();
			}
		});
		
		actionTimer.start();
		
		text = new TextArea(105, 468, graphics.getWidth() - 105, graphics.getHeight() - 468);
		addKeyListener(player);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (gameInProgress) {
			drawBackground(g);
			drawAgents(g);
			player.draw(g);
			drawBarriers(g);

			drawDialogue(g);
		}
		
		else Level.setScreen(g);
	}

	public void drawDialogue(Graphics g) {		
		// create basic framework:
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, getHeight() - 100, getWidth(), 100);
		g.setColor(new Color(245, 245, 245));
		g.fillRect(5, getHeight() - 95, getWidth() - 10, 90);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(105, getHeight() - 95, 5, 90);
		
		if (currentSpeaker == null) {
			Animation.backgroundDialogue(g);
		}
		else {
			currentSpeaker.displayDialogue(g);
		}
	}
	
	public void drawBackground(Graphics g) {
		ArrayList<Barrier> backgrounds = Dungeon.location.getRoom(currentDungeon).backgrounds;
		
		for (int i = 0; i <= Barrier.MAX_LAYERS; i++) {
			for (int j = 0; j < backgrounds.size(); j++) {
				if (backgrounds.get(j).layer == i) {
					backgrounds.get(j).draw(g);
				}
			}
		}
	}
	
	public void drawBarriers(Graphics g) {
		ArrayList<Barrier> gates = Dungeon.location.getRoom(currentDungeon).gates;
		ArrayList<Barrier> walls = Dungeon.location.getRoom(currentDungeon).walls;
		
		for (int i = 0; i < walls.size(); i++) {
			walls.get(i).draw(g);
		}
		
		for (int i = 0; i < gates.size(); i++) {
			gates.get(i).draw(g);
		}
	}
	
	public void drawAgents(Graphics g) {
		ArrayList<Entity> agents = Dungeon.location.getRoom(currentDungeon).agents;
				
		for (int i = 0; i < agents.size(); i++) {
			agents.get(i).draw(g);
			if (currentSpeaker == agents.get(i)) agents.get(i).speakingHalo(g);
		}
	}
}
