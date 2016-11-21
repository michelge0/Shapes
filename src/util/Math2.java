package util;

public class Math2
{
	public static double distance(int x1, int x2, int y1, int y2) {
		return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}
}
