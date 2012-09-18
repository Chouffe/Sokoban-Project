package model;

public class Box {
	
	protected Map map;
	protected Position position;
	protected boolean onGoal;

	Box(Map m, Position pos) {
		map = m;
		position = pos;
		if (m.getGoals().contains(pos)) {
			onGoal = true;
		}
	}

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
