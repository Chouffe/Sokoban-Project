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
    
    
    public BoxSpace(ArrayList<Box> boxes, Map map) throws CloneNotSupportedException            
    {        
        board = map.clone();
        this.boxes = map.getBoxes();
        this.boxCount=this.boxes.size();
    }
    
    public void addBox()
    {
        boxCount++;
    }
    
    public void removeBox()
    {
        boxCount--;
    }
    
    public Box getBox(int index)
    {
        return this.boxes.get(index);
    }
    
    
    
    public ArrayList<ArrayList<Box>> Search(ArrayList<Box> Allboxes) throws CloneNotSupportedException
    {
        // Clone the array of boxes.
        ArrayList<Box> solution = new ArrayList<Box>();
        LinkedHashSet solutionString = new LinkedHashSet(); 
        ArrayList<Box> boxes = new ArrayList<Box>();
        for (Box b: Allboxes){
            boxes.add(b.clone());            
        }
        
        
        // To return the BoxSpaces.
        ArrayList<ArrayList<Box>> BoxGroups = new ArrayList<ArrayList<Box>>();
        
                
        // Check where to look for boxes.
        boolean [] directionsToLook = new boolean[4];
        
        // To split the box groups.
        boolean group = false;
        
        // Continue while there are still boxes to look.
        while (!boxes.isEmpty())
        {
            // Get new box.
            Box b = boxes.remove(0);
            
//            System.out.println("Checking .... "+ b.getPosition());
            // Check for boxes around.
            directionsToLook = LookAround(b);

            group = false;
            
            // Check if there is a box... up/down/left/right
            for (boolean flag: directionsToLook)
            {
                group = group || flag;
            }
//            System.out.println("Split has:"+split);
            
            // If there was .... Form a group.
            if (group)
            {
                // If the boxes are not adjacent any more... split but not add the next.
                if (!solution.isEmpty())
                    if(!isAdjacent(b.getPosition(),solution.get(solution.size()-1).getPosition()))
                        split(solution,BoxGroups,null);                                            
                addBox(b,solutionString,solution);                                
            }                
            // if not ...
            else
            {
                //Split and add the box left alone.
                split(solution,BoxGroups,b);
            }
        }
        // When you finish checking all ... Add the last group.
        split(solution,BoxGroups,null);
        return BoxGroups;
        
    }
    

    
        // Method that checks for boxes around a box.
        // Returns a boolean array with the following format:
        //
        // [ Up , Down, Left, Right ]
        //
        // Recives a Box object.
        //
        // @autor: Luis 
        // @note: unboundMove is not working ... :S
        public boolean [] LookAround (Box boxInSquare) throws CloneNotSupportedException
        {
            
            // Always initialize as he can go no where.
            boolean [] directions = {false, false, false, false};                        
            
            
            Position box = boxInSquare.getPosition();
            
            Position up = box.clone().up(board);//.unboundMove('U');            
            
            Position down = box.clone().down(board);
            
            Position right = box.clone().right(board);//.unboundMove('R');
            
            Position left = box.clone().left(board);//.unboundMove('L');
            
            
            // UP
            if (board.getCellFromPosition(up).getType() == Cell.ECell.BOX )
                directions[0]=true;
            // DOWN
            if (board.getCellFromPosition(down).getType() == Cell.ECell.BOX)
                directions[1]=true;
            // LEFT
            if (board.getCellFromPosition(left).getType() == Cell.ECell.BOX )               
                directions[2]=true;
            // RIGHT
            if (board.getCellFromPosition(right).getType() == Cell.ECell.BOX)
                directions[3]=true;
            
            return directions;
        }              
        
        // Method that checks if a box has already been 
        // checked in that position.
        //
        // Returns false if it has been visited, else true.
        //
        // Recives a position and a lhashSet with all the visiter
        // positions.
        // @author: Luis
        //
        public boolean wasVisited(Position point, LinkedHashSet lhashSet)
        {            
            return lhashSet.add(""+ point.getI() +":"+ point.getJ());
            
        }
        
        /*
         * @author Luis
         * @description checks if two positions are considered adjacent or not.
         * @param receives two positions to compare.
         */
        public boolean isAdjacent(Position one, Position two)
        {            
            if (Math.abs(one.getI()-two.getI())<=2)
                if (Math.abs(one.getJ()-two.getJ())<=2)
                    return true;
            return false;
        }
        
        /*
         * @author: Luis
         * @description adds a box to the HashSet if it is not visited.
         *              Also adds the box to a group.
         * @param   - Box b is added
         *          - solutionsString contains all the positions of the boxes 
         *          added as a group to prevent duplicates.
         *          - solution contains the Boxes.
         */
        public void addBox(Box b,LinkedHashSet solutionString, ArrayList<Box> solution) throws CloneNotSupportedException
        {
            if (wasVisited(b.getPosition(),solutionString)){
//                System.out.println("Adding ..."+b.getPosition());
                solution.add(b);
            }
        }
        
        /*
         * @author Luis
         * @description splits a group of BoxSpace with the next one.
         * @param   -  solution contains the Boxes.
         *          -  BoxGroups contains the BoxSpaces.
         *          -  Box b, contains the box under analysis.
         * **Note**: If b is null, b is not added as a BoxSpace of length 1.
         */
        
        public void split(ArrayList<Box> solution, ArrayList<ArrayList<Box>> BoxGroups, Box b)
        {
            // Split the gruoups
            if (!solution.isEmpty()){                    
                BoxGroups.add((ArrayList<Box>)solution.clone());

                /*
                 * For debugging
                 */
//                System.out.println("Spliting .... ");
//                for (Box t: solution)
//                    System.out.println(t.getPosition());
            }
            if (b!=null){
                // Add the ones that have no Group as singles.
                ArrayList<Box> temp = new ArrayList<Box>();
                temp.add(b);
                BoxGroups.add( (ArrayList<Box>)temp.clone() );                
            }
            solution.clear();
        }        
    
}
