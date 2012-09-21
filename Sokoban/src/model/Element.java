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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (onGoal ? 1231 : 1237);
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Element other = (Element) obj;
		if (onGoal != other.onGoal)
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}
	
	
}
