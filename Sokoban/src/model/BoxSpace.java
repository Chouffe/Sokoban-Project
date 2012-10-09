/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 *
 * @author lfreina
 */
public class BoxSpace extends Box implements Cloneable {
    protected int maxX;
    protected int minX;
    protected int maxY;
    protected int minY;
    protected int boxCount;
    protected ArrayList<Box> boxes;
    protected ArrayList<Position> Positions;
    protected LinkedHashSet boxPositions;
    
    
    public BoxSpace(ArrayList<Box> boxes) 
    {        
        this.boxCount=this.boxes.size();
        maxY=-1;
        maxX=-1;
        minX=Integer.MAX_VALUE;
        minY=Integer.MAX_VALUE;
        for (Box b: boxes)
        {
            
            addBox(b);
            if(b.getPosition().getJ()>maxY)
                maxY=b.getPosition().getJ();
            if(b.getPosition().getJ()<minY)
                minY=b.getPosition().getJ();
            if(b.getPosition().getI()>maxX)
                maxX = b.getPosition().getI();
            if(b.getPosition().getI()<minX)
                minX = b.getPosition().getI();
        }
    }
    
    // Returns true if was added or false if was already in the BoxSpace.
    public boolean addBox(Box new_box)
    {
        boxCount++;
        boolean add = isIncluded(new_box.getPosition());
        if (add)
        {
            boxes.add(new_box);
            Positions.add(new_box.getPosition());
            return true;
        }
        else
            return false;
    }
    
    public Box removeBox(int index)
    {
        boxCount--;
        // Remove from the boxSpace.
        Box temp = this.boxes.remove(index);
        this.Positions.remove(index);
        // remove from the HashSet
        boxPositions.remove(""+ temp.getPosition().getI() +":"+ temp.getPosition().getJ());        
        return temp;
    }
    
    public Box getBox(int index)
    {
        return this.boxes.get(index);
    }
    
    public Position getPosition(Box box)
    {
        return this.Positions.get( this.boxes.indexOf(box) );
    }
    
    public int getBoxCount()
    {
        return boxes.size();
    }
    
    private boolean isIncluded(Position point)
        {            
            return this.boxPositions.add(""+ point.getI() +":"+ point.getJ());
            
        }
        
}
    
