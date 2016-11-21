package environment;

public class Coordinate<Type>
{
	protected Type x;
	protected Type y;
	
	public Coordinate(Type x, Type y) {
		this.x = x;
		this.y = y;
	}
	
	public Type getX() {
		return x;
	}
	
	public Type getY() {
		return y;
	}
	
	public void setX(Type value) {
		x = value;
	}
	
	public void setY(Type value) {
		y = value;
	}
}
