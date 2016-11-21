package util;

public class Random
{	
	/*public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(fluctuation(10, .1));
		}
	}*/
	
	public static int roll(int sides) {
		return (int) (Math.random() * sides + 1);
	}
	
	// returns a decimal greater than PERCENT of the original value but less than original value
	public static double upperProportion(int number, double percent) {
		return bounded((percent * number), number);
	}
	
	// same as upper proportion, but now output is between 0 and PERCENT of input
	public static double lowerProportion(int number, double percent) {
		return bounded(0, percent * number);
	}
	
	// returns + or - PERCENT of number
	public static double fluctuation(int number, double percent) {
		double fluctuation = lowerProportion(number, percent);
		if (Math.random() > 0.5) {
			return number + fluctuation;
		}
		else return number - fluctuation;
	}
	
	public static int boundedInt(double lowerBound, double upperBound) {
		return (int) bounded(lowerBound, upperBound);
	}
	
	// returns a random number between two bounds
	public static double bounded(double lowerBound, double upperBound) {
		return (lowerBound) + (Math.random() * (upperBound - lowerBound));
	}
}
