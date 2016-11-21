package graphics;
import java.awt.*;

public class TextArea
{
	private int xPos;
	private int yPos;
	private int width;
	private int height;
	
	private int marginWidth = 15;
	
	private int displayX = xPos + marginWidth;
	private int displayY = yPos + marginWidth + 10;
	
	public TextArea(int xPos, int yPos, int width, int height) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
	}
	
	/****************************************/
	// SETTING AND GETTING
	
	public void setXPos(int amount) {
		xPos = amount;
	}
	
	public void setYPos(int amount) {
		yPos = amount;
	}
	
	public void setWidth(int newWidth) {
		width = newWidth;
	}
	
	public void setHeight(int newHeight) {

		height = newHeight;
	}
	
	public void setMargin(int newMargin) {
		marginWidth = newMargin;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getMargin() {
		return marginWidth;
	}
	
	/*****************************************/
	// MAIN DRAWING METHODS
	
	public void drawText(Graphics g, String text) {
		text = setFont(g, text);
		drawWords(g, text);
	}
	
			
	public void drawWords(Graphics g, String text) {
		if (g.getFontMetrics().stringWidth(text) > width - (2 * marginWidth)) {
			for (int i = 0; i < text.length(); i++) {
				// if the displayed text is too big for one line
				if (g.getFontMetrics().stringWidth(text.substring(0, i))
						> width - (2 * marginWidth)) {
					
					// display text up to last space
					int lastSpace = findLastSpace(text, i);
					g.drawString(text.substring(0, lastSpace), displayX, displayY);
					
					// next line; break if it goes over the text box area
					displayY += 20;
					if (displayY > yPos + height) break;
					
					// recursively call the drawText method to display the next line
					drawWords(g, text.substring(lastSpace, text.length()));
					break;
				}
			} 
		}
		
		else g.drawString(text, displayX, displayY);
		
		displayX = xPos + marginWidth;
		displayY = yPos + marginWidth + 10;
	}
	
	// checks for font tags tags
	private String setFont(Graphics g, String text) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Default", Font.PLAIN, 18));
				
		if (text.startsWith("<i>")) {
			g.setFont(new Font("Default", Font.ITALIC, 18));
			
			return text.replace("<i>", "");
		}
		
		return text;
	}
	
	// finds index of last space before index 
	public int findLastSpace(String text, int index) {
		for (int i = index; i >= 0; i--) {
			if (text.charAt(i) == ' ') return i;
		}
		return 0;
	}
}
