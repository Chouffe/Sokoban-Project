package model;

/**
 * 
 * Class which provides an optimization for matching goals and boxes
 * 
 * @author arthur
 *
 */
public class BoxToGoalPath implements Cloneable {
	
	protected Position boxPosition;
	protected Position goalPosition;
	
	public BoxToGoalPath(Box box, Position goal) throws CloneNotSupportedException
	{
		this.boxPosition = box.getPosition().clone();
		this.goalPosition = goal.clone();
	}
	
	
	/**
	 * 
	 * Given a String which represents moves, it cuts the firstPlayerMoves
	 * 
	 * @param findPath
	 * @author arthur
	 * @return
	 */
	public static String removeFirstPlayerMoves(String findPath)
	{
		int indexFirstUpperCase = findPath.length();
		int i = 0;
		for(Character c : findPath.toCharArray())
		{
			if(c == 'U' || c == 'D' || c == 'L' || c == 'R')
			{
				indexFirstUpperCase = i;
				break;
			}
			i++;
		}
		return findPath.substring(indexFirstUpperCase);
	}


	public Position getBoxPosition() {
		return boxPosition;
	}


	public void setBoxPosition(Position boxPosition) {
		this.boxPosition = boxPosition;
	}


	public Position getGoalPosition() {
		return goalPosition;
	}


	public void setGoalPosition(Position goalPosition) {
		this.goalPosition = goalPosition;
	}
	
	

	@Override
	public BoxToGoalPath clone() throws CloneNotSupportedException {
		
		//BoxToGoalPath clone = new BoxToGoalPath(null, null);
		BoxToGoalPath clone = (BoxToGoalPath)super.clone();
		
		// Clone the positions
		clone.boxPosition = this.boxPosition.clone();
		clone.goalPosition = this.goalPosition.clone();
		
		return clone;
	}


	@Override
	public int hashCode() 
	{
		final int prime = 223;
		int result = 1;
		
		result = prime * result
				+ ((boxPosition == null) ? 0 : boxPosition.hashCode());
		result = prime * result
				+ ((goalPosition == null) ? 0 : goalPosition.hashCode());
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
		BoxToGoalPath other = (BoxToGoalPath) obj;
		if (boxPosition == null) {
			if (other.boxPosition != null)
				return false;
		} else if (!boxPosition.equals(other.boxPosition))
			return false;
		if (goalPosition == null) {
			if (other.goalPosition != null)
				return false;
		} else if (!goalPosition.equals(other.goalPosition))
			return false;
		return true;
	}
}
