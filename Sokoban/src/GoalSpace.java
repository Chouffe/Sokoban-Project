/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 *
 * @author lfreina
 */
public class GoalSpace {
    protected int maxX;
    protected int minX;
    protected int maxY;
    protected int minY;
    protected int goalCount;
    protected ArrayList<Goal> goals;    
    protected LinkedHashSet goalPositions;
    
    public GoalSpace()
    {
        this.goalCount=-1;
        this.goalPositions = null;
        this.goals = null;
        maxY=-1;
        maxX=-1;
        minX=Integer.MAX_VALUE;
        minY=Integer.MAX_VALUE;
    }
    
    public GoalSpace(ArrayList<Position> goals) 
    {        
        this.goalCount=goals.size();
        this.goalPositions = new LinkedHashSet();
        this.goals = new ArrayList<Goal>();
        maxY=-1;
        maxX=-1;
        minX=Integer.MAX_VALUE;
        minY=Integer.MAX_VALUE;
        for (Position b: goals)
        {
            Goal g = new Goal(b.getI(),b.getJ());
            addGoal(g);
            if(g.getJ()>maxY)
                maxY=g.getJ();
            if(g.getJ()<minY)
                minY=g.getJ();
            if(g.getI()>maxX)
                maxX = g.getI();
            if(g.getI()<minX)
                minX = g.getI();
        }
    }
    
    // Returns true if was added or false if was already in the BoxSpace.
    public boolean addGoal(Goal new_goal)
    {
        goalCount++;
        boolean add = isIncluded(new_goal);
        if (add)
        {
            goals.add(new_goal);            
            return true;
        }
        else
            return false;
    }
    
    public Goal removeBox(int index)
    {
        goalCount--;
        // Remove from the boxSpace.
        Goal temp = this.goals.remove(index);        
        // remove from the HashSet
        goalPositions.remove(""+ temp.getI() +":"+ temp.getJ());        
        return temp;
    }
    
    public Goal getGoal(int index)
    {
        return this.goals.get(index);
    }
    
    public ArrayList<Goal> getGoals()
    {
        return this.goals;
    }
    
    public ArrayList<Position> getGoalsAL()
    {
        ArrayList<Position> temp = new ArrayList<Position>();
        for (Goal g: goals)
        {
            temp.add(g);
        }
        return temp;
    }
    
    public Position getPosition(Goal goal)
    {
        return goal;
    }    
    
    public int getGoalCount()
    {
        return goals.size();
    }
    
    private boolean isIncluded(Position point)
    {            
        return this.goalPositions.add(""+ point.getI() +":"+ point.getJ());

    }
    
    public GoalSpace clone() throws CloneNotSupportedException 
    {
		
        GoalSpace copy = (GoalSpace)super.clone();
        copy.goals = (ArrayList<Goal>) this.goals.clone();        
        copy.goalPositions = (LinkedHashSet) this.goalPositions.clone();
        return copy;
    }
    
}
