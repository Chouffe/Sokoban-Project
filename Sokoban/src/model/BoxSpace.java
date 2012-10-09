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
    protected ArrayList<Position> Exits;
    protected Map board;
    protected LinkedHashSet boxPositions;
    
    
    public BoxSpace(ArrayList<Box> boxes, Map map) throws CloneNotSupportedException            
    {        
        board = map.clone();
        this.boxes = map.getBoxes();
        this.boxCount=this.boxes.size();        
    }
    
    // Returns true if was added or false if was already in the BoxSpace.
    public boolean addBox(Box new_box)
    {
        boxCount++;
        boolean add = isIncluded(new_box.getPosition());
        if (add)
        {
            boxes.add(new_box);
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
        // remove from the HashSet
        boxPositions.remove(""+ temp.getPosition().getI() +":"+ temp.getPosition().getJ());        
        return temp;
    }
    
    public Box getBox(int index)
    {
        return this.boxes.get(index);
    }
    
    private boolean isIncluded(Position point)
        {            
            return this.boxPositions.add(""+ point.getI() +":"+ point.getJ());
            
        }
        
}
    
