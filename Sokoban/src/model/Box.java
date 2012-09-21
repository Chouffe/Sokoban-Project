package model;

public class Box extends Element implements Cloneable{

	public Box()
	{
		super();
	}
	
	public Box(Position position, boolean onGoal) {
		super(position, onGoal);
	}

	public Box clone() throws CloneNotSupportedException {
		
		Box copy = (Box)super.clone();
		copy.position = position.clone();
		return copy;
	}
}
