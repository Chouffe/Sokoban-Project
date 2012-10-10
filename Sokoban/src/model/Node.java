package model;

/**
 * A node represents a state in the search
 *
 */
public class Node implements Comparable<Node>
{
	
	protected Position position;
	protected BoxMove boxMove;
	protected Map map;
	protected Node parent;
	protected Node child;
	protected int f, g, h;
	
	
	public Node()
	{
		parent = null;
		child = null;
		position = null;
		boxMove = null;
		map = null;
		
		f = 0;
		g = 0;
		h = 0;
	}
	
	public Node(Position position)
	{
		this();
		setPosition(position);
	}
	
	public Node(Position position, Map map)
	{
		this(position);
		this.map = map;
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
		return boxMove.getNewPosition();
	}

	public void setPosition(Position position) {
		this.boxMove = new BoxMove(position, "");
		this.position = position;
	}


	@Override
	public String toString() {
		return "Node [position=" + position + ", f=" + f + "]";
	}

	@Override
	public int hashCode() {
		return map.hashCode();
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
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		return true;
	}

	public BoxMove getBoxMove() {
		return boxMove;
	}

	public void setBoxMove(BoxMove boxMove) {
		this.boxMove = boxMove;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
}
