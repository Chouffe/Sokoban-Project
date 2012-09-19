package model;

/**
 * A node represents a state in the search
 *
 */
public class Node implements Comparable<Node>
{
	
	protected Position position;
	protected Node parent;
	protected Node child;
	protected int f, g, h;
	
	
	public Node()
	{
		parent = null;
		child = null;
		position = null;
		
		f = 0;
		g = 0;
		h = 0;
	}
	
	public Node(Position position)
	{
		this();
		setPosition(position);
	}
	
	/**
	 * Heuristic
	 * @param goal
	 * @return
	 */
	public int goalDistanceEstimate(Node goal)
	{
		return distance(this.position, goal.getPosition());
	}
	
	protected int distance(Position position1, Position position2)
	{
		return (position1.getI() - position2.getI())*(position1.getI() - position2.getI()) + (position1.getJ() - position2.getJ())*(position1.getJ() - position2.getJ());
	}

	
	public boolean isGoal(Node goal)
	{
		return position.equals(goal.getPosition());
	}

	@Override
	public int compareTo(Node o) 
	{
		return (this.f - o.f);
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getChild() {
		return child;
	}

	public void setChild(Node child) {
		this.child = child;
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}


	@Override
	public String toString() {
		return "Node [position=" + position + ", f=" + f + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Node other = (Node) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	
	
	
}