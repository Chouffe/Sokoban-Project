

public class Player extends Element implements Cloneable{

	public Player()
	{
		super();
	}
	
	public Player(Position position, boolean onGoal) {
		super(position, onGoal);
		// TODO Auto-generated constructor stub
	}
	
	public Player clone() throws CloneNotSupportedException {
		
		if(position != null)
		{
			Player copy = (Player)super.clone();
			copy.position = position.clone();
			return copy;
		}
		else
		{
			return null;
		}
	}

}
