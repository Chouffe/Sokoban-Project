package model;

public abstract class Element 
{
	
	protected Position position;
	protected boolean onGoal;
	
	public Element()
	{
		position = null;
		onGoal = false;
	}
	
	public Element(Position position, boolean onGoal)
	{
		this.position = position;
		this.onGoal = onGoal;
	}
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public boolean isOnGoal() {
		return onGoal;
	}

	public void setOnGoal(boolean onGoal) {
		this.onGoal = onGoal;
	}

}
