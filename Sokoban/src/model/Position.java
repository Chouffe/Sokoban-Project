package model;

/**
 * Represents the position on a map
 */
public class Position implements Cloneable
{
	protected int i;
	protected int j;
	
	//protected Map map;
	
	public Position() {
		i = 0;
		j = 0;
	}
	
	public Position(int i, int j)
	{
		this();
		this.i = i;
		this.j = j;
	}
	
	public int getI()
	{
		return i;
	}
	public int getJ()
	{
		return j;
	}
	public void setI(int newI)
	{
		i = newI;
	}
	public void setJ(int newJ)
	{
		j = newJ;
	}
	
	public void set(int i, int j)
	{
		setI(i);
		setJ(j);
	}
	
	public Position up(Map map)
	{
		if(map != null && map.getMap() != null && map.getMap().size() > 0)
		{
			if(i > 0)
			{
				i -= 1;
			}
		}
		
		return this;
	}
	public Position down(Map map)
	{
		if(map != null && map.getMap() != null && map.getMap().size() > 0)
		{
			if(i + 1 < map.getMap().size())
			{
				i += 1;
			}
		}
		
		return this;
	}
	
	public Position left(Map map)
	{
		if(map != null && map.getMap() != null && map.getMap().size() > 0)
		{
			if(j > 0)
			{
				j -= 1;
			}
		}
		
		return this;
	}
	
	public Position right(Map map)
	{
		if(map != null && map.getMap() != null && map.getMap().size() > 0)
		{
			if(j + 1 < map.getMap().get(1).size())
			{
				j += 1;
			}
		}
		
		return this;
	}

	@Override
	public Position clone() throws CloneNotSupportedException {

		return (Position)super.clone();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + i;
		result = prime * result + j;
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
		Position other = (Position) obj;
		if (i != other.i)
			return false;
		if (j != other.j)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Position [i=" + i + ", j=" + j + "]";
	}
	
	
	
	
	
}
