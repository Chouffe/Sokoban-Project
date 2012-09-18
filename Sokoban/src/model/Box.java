package model;

public class Box {
	protected Position position;
	protected boolean onGoal;

	Box(Map m, Position pos, boolean goal) {
		map = m;
		position = pos;
		onGoal = goal;
	}

	public boolean isOnGoal() {
		return onGoal;
	}

	public Position getPosition() {
		return position;
	}
}
