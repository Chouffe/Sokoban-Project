package model;

public class Box {
	protected Position position;
	protected boolean onGoal;

	Box(Position pos, boolean onGoal) {
		position = pos;
		this.onGoal = onGoal;
	}

	public boolean isOnGoal() {
		return onGoal;
	}

	public Position getPosition() {
		return position;
	}

	public Box clone() throws CloneNotSupportedException {
		
		Box copy = (Box)super.clone();
		copy.position = position.clone();
	}
}
