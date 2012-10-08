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
    protected int SPACES = 1;
    
    
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
        return isIncluded(new_box.getPosition());
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
    
    public ArrayList<Box> sort(ArrayList<Box> boxes, boolean Vertical)
    {
        ArrayList<Box> sortedBoxes = new ArrayList<Box>();
        ArrayList<Box> notSortedBoxes = new ArrayList<Box>();
        while (!boxes.isEmpty())
        {
            Box b = boxes.remove(0);
            if (sortedBoxes.isEmpty())
                sortedBoxes.add(b);
            else{
                if (Vertical){
                    if (isVAdjacent(b.getPosition(),sortedBoxes))
                        sortedBoxes.add(b);                                
                    else
                        notSortedBoxes.add(b);
                }
                else{
                    if (isHAdjacent(b.getPosition(),sortedBoxes))
                        sortedBoxes.add(b);                                
                    else
                        notSortedBoxes.add(b);                
                }
            }
        }
        if (notSortedBoxes.isEmpty())
            return sortedBoxes;
        else
        {
            ArrayList<Box> temp = new ArrayList<Box>();
            temp = sort(notSortedBoxes, Vertical);
            for (Box b: temp)
            {
                sortedBoxes.add(b);
            }
        }
        return sortedBoxes;
    }            
    
    public ArrayList<ArrayList<Box>> groupBoxes(ArrayList<ArrayList<Box>> Vertical, ArrayList<ArrayList<Box>> Horizontal ) throws CloneNotSupportedException
    {
        LinkedHashSet solutionTest = new LinkedHashSet(); 
        ArrayList<ArrayList<Box>> solution = new ArrayList<ArrayList<Box>>();
        ArrayList<Box> notGrouped = new ArrayList<Box>();
        while (!Vertical.isEmpty())
        {
            ArrayList<Box> boxes = Vertical.remove(0);
            if (boxes.size()>5){
                for (Box b: boxes){
                    Position temp = b.getPosition();
                    solutionTest.add(""+ temp.getI() +":"+ temp.getJ());
                }
            solution.add(boxes);
            }
        }
        while (!Horizontal.isEmpty())
        {
            ArrayList<Box> boxes = Horizontal.remove(0);
            ArrayList<Box> boxes2 = new ArrayList<Box>();
            if (boxes.size()>1){
                for (Box b: boxes){
                    Position temp = b.getPosition();
                    if (solutionTest.add(""+ temp.getI() +":"+ temp.getJ()))
                        boxes2.add(b);
                }
                if (!boxes2.isEmpty())
                    solution.add(boxes2);
            }
            else
            {
                for (Box b: boxes){
                    Position temp = b.getPosition();
                    if (solutionTest.add(""+ temp.getI() +":"+ temp.getJ()))
                        notGrouped.add(b);                
                }
            }
                
        }
        
        ArrayList<ArrayList<Box>> newGrouped = new ArrayList<ArrayList<Box>>();
        newGrouped = tryToGroup(notGrouped);        
        for (ArrayList<Box> b: newGrouped)
        {
            solution.add(b);
        }
        return solution;
        
    }
    
    
    public ArrayList<ArrayList<Box>> Search(ArrayList<Box> Allboxes) throws CloneNotSupportedException
    {
//        if (Allboxes.size()>20)
//            SPACES = 3;
//        else
//            SPACES = 1;
        // Clone the array of boxes.
        ArrayList<Box> solution = new ArrayList<Box>();
        LinkedHashSet solutionString = new LinkedHashSet(); 
        ArrayList<Box> boxes = new ArrayList<Box>();
        for (Box b: Allboxes){
            boxes.add(b.clone());
//            System.out.println(b.clone().getPosition());
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
                // If the boxes are not adjacent on the solution any more... 
                // split but do not add the next box.
                if (!solution.isEmpty())
                    if(!isAdjacent(b,solution)){
                        // Check if the box has other adjacent boxes to come.
                        split(solution,BoxGroups,null);
                        if (!boxes.isEmpty())
                            if(!isAdjacent(b,boxes)){
//                                System.out.println("No more adjacent boxes in boxes.");
                                split(solution,BoxGroups,null);
                                
                            }
                    }
                addBox(b,solutionString,solution);                                
            }                
            // if not ...
            else
            {
                //Split and add the box left alone.
//                System.out.println("Normal split!");
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
            // UP
            directions[0]=LookUp(boxInSquare);
            // DOWN
            directions[1]=LookDown(boxInSquare);
            // LEFT
            directions[2]=LookLeft(boxInSquare);
            // RIGHT
            directions[3]=LookRight(boxInSquare);
            
            return directions;
        }  
        
        public boolean LookRight (Box boxInSquare) throws CloneNotSupportedException
        {
            
            Position box = boxInSquare.getPosition();            
            
            Position right = box.clone().right(board);
            
            //RIGHT
            Cell.ECell rightType = board.getCellFromPosition(right).getType();
            if (rightType == Cell.ECell.EMPTY_FLOOR ||
                    rightType == Cell.ECell.VISITED ||
                    rightType == Cell.ECell.PLAYER ||
                    rightType == Cell.ECell.GOAL_SQUARE ||
                    rightType == Cell.ECell.BOX ||
                    rightType == Cell.ECell.BOX_ON_GOAL){
                
                return true;
            }
            
            return false;
        }
        
        public boolean LookLeft (Box boxInSquare) throws CloneNotSupportedException
        {           
            
            Position box = boxInSquare.getPosition();            
            
            Position left = box.clone().left(board);
                        
            // LEFT
            Cell.ECell leftType = board.getCellFromPosition(left).getType();
            if (leftType == Cell.ECell.EMPTY_FLOOR ||
                    leftType == Cell.ECell.VISITED ||
                    leftType == Cell.ECell.PLAYER ||
                    leftType == Cell.ECell.GOAL_SQUARE ||
                    leftType == Cell.ECell.BOX ||
                    leftType == Cell.ECell.BOX_ON_GOAL)  {             
                
//                System.out.println("Left "+ left +" is free.");
                return true;
            }
            
            return false;
        }
        
        public boolean LookUp (Box boxInSquare) throws CloneNotSupportedException
        {           
            
            Position box = boxInSquare.getPosition();            
            
            Position up = box.clone().up(board);
                        
            // LEFT
            Cell.ECell leftType = board.getCellFromPosition(up).getType();
            if (leftType == Cell.ECell.EMPTY_FLOOR ||
                    leftType == Cell.ECell.VISITED ||
                    leftType == Cell.ECell.PLAYER ||
                    leftType == Cell.ECell.GOAL_SQUARE ||
                    leftType == Cell.ECell.BOX ||
                    leftType == Cell.ECell.BOX_ON_GOAL)  {             
                
//                System.out.println("Up "+ up +" is free.");
                return true;
            }
            
            return false;
        }
        
        public boolean LookDown (Box boxInSquare) throws CloneNotSupportedException
        {           
            
            Position box = boxInSquare.getPosition();            
            
            Position down = box.clone().down(board);
                        
            // LEFT
            Cell.ECell leftType = board.getCellFromPosition(down).getType();
            if (leftType == Cell.ECell.EMPTY_FLOOR ||
                    leftType == Cell.ECell.VISITED ||
                    leftType == Cell.ECell.PLAYER ||
                    leftType == Cell.ECell.GOAL_SQUARE ||
                    leftType == Cell.ECell.BOX ||
                    leftType == Cell.ECell.BOX_ON_GOAL)  {             
                
//                System.out.println("Down "+ down +" is free.");
                return true;
            }
            
            return false;
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
//        public boolean isAdjacent(Position one, ArrayList<Box> solution)
//        {   
//            for (Box box: solution){
//                Position two = box.getPosition();
////                System.out.println("Comparing....:"+one + " and " + two);
//                if (Math.abs(one.getI()-two.getI())<=SPACES)
//                    if (Math.abs(one.getJ()-two.getJ())<=SPACES)
//                        return true;
//                if (Math.abs(one.getJ()-two.getJ())<=SPACES)
//                    if (Math.abs(one.getI()-two.getI())<=SPACES)
//                        return true;
//            }
//            return false;
//        }
        
        public boolean isAdjacent(Box start, ArrayList<Box> solution) throws CloneNotSupportedException 
        {
            Box one = start.clone();
            boolean group = true;
            for (Box b: solution)
            {
                group = group && isAdjacent(start,b);
            }
            return group;
        }
        public boolean isAdjacent(Box start, Box solution) throws CloneNotSupportedException
        {
            Box one = start.clone();
            boolean group = true;
            if (solution.getPosition().equals(one.getPosition()))
                return true;

                Position two = solution.getPosition();
                if (Math.abs(one.getPosition().getI()-two.getI())==0) 
                {
                                      
                        if (one.getPosition().getJ()-two.getJ()>=0){
                            group = LookLeft(one);
                            if (group){
//                                System.out.println("moving ..."+ one.clone().getPosition().left(board));
                                one.getPosition().left(board);
                                group = isAdjacent(one, solution);
                            }
                        }
                        else{
                            group = LookRight(one);
                            if (group){
//                                System.out.println("moving ..."+ one.clone().getPosition().right(board));
                                one.getPosition().right(board);
                                group = isAdjacent(one,solution);
                            }
                        }
                     
//                    System.out.println("Group:"+group);
                    return group;
                }
                            
            return false;
        } 
        
        public ArrayList<ArrayList<Box>> TryToMergeV(ArrayList<ArrayList<Box>> Allboxes) throws CloneNotSupportedException
        {
            ArrayList<ArrayList<Box>> solution = new ArrayList<ArrayList<Box>>();
            ArrayList<ArrayList<Box>> notGroup = new ArrayList<ArrayList<Box>>();
            ArrayList<Box> solutionTest = new ArrayList<Box>();
            ArrayList<Box> notGroupTest = new ArrayList<Box>();
            LinkedHashSet solutionKeepOut = new LinkedHashSet(); 
            ArrayList<ArrayList<Box>> boxes = new ArrayList<ArrayList<Box>>();
            for (ArrayList<Box> b: Allboxes){
                boxes.add((ArrayList<Box>)b.clone());
            }        
        
            // To return the BoxSpaces.
            ArrayList<ArrayList<Box>> BoxGroups = new ArrayList<ArrayList<Box>>();            

            // Continue while there are still boxes to look.
            while (!boxes.isEmpty())
            {
                // Get new box.
                ArrayList<Box> b = boxes.remove(0);

                if (solution.isEmpty()){
                    for (Box i: b)
                    {
                        addBox(i,solutionKeepOut,solutionTest);
                    }
                    solution.add(b);
                }
                else
                {
                    ///**************************************
                    ///**************************************
                    ///**************************************
                    ///**************************************
                    if (isAdjacentV(b,solution.get(0)))
                    {
                        for (Box i: b)
                        {
                            addBox(i,solutionKeepOut,solutionTest);
                        }
                        solution.add(b);
                    }
                    else
                    {
                        for (Box i: b)
                        {
                            addBox(i,solutionKeepOut,notGroupTest);
                        }
                        notGroup.add(b);
                    }
                    ///**************************************
                    ///**************************************
                    ///**************************************
                    ///**************************************
                }                                
            }
            
            // When you finish checking all ... Add the last group.
            if (!solution.isEmpty())
                BoxGroups.add(solutionTest);
            if (!solution.isEmpty()){            
                ArrayList<ArrayList<Box>> temp = TryToMergeV(notGroup);
                for (ArrayList<Box> b: temp)
                {
                    BoxGroups.add(b);
                }
            }            
            for (Box b: notGroupTest)
            {
                ArrayList<Box> temp = new ArrayList<Box>();
                if (wasVisited(b.getPosition(),solutionKeepOut)){
                    temp.add(b);
                    BoxGroups.add(temp);
                }
            }
            return BoxGroups;
        }
        
        public boolean isAdjacentV(ArrayList<Box> one, ArrayList<Box> two) throws CloneNotSupportedException 
        {
            int length = Math.min(one.size(), two.size());
            boolean group = true;
            if (one.size()==length){
                int j =0;
//                for (int i=0; i<length;i++)
//                {                    
                    if (group){
//                        while (one.get(0).getPosition().getJ()!=two.get(j).getPosition().getJ()){
//                            if (j<two.size()-1)
//                                j++; 
//                        }
                        group = group && isAdjacentV(one.get(0),two.get(j));
                    }
                    else
                        return false;
//                }
//                System.out.println("Group"+group);    
                return group;
            }
            else{
                int j=0;
//                for (int i=0; i<length;i++)
//                {

                    if (group){
//                        while (one.get(j).getPosition().getJ()!=two.get(0).getPosition().getJ())
//                            if (j<one.size()-1)
//                                j++; 
                        group = group || isAdjacentV(one.get(j),two.get(0));
                    }
                    else
                        return false;
//                }
                return group;
            }
        }
        
        public boolean isAdjacentV(Box start, Box stop) throws CloneNotSupportedException 
        {
            Box one = start.clone();
            boolean group = true;
            if (stop.getPosition().equals(one.getPosition()))
                return true;

            Position two = stop.getPosition();
            

                    if (one.getPosition().getI()-two.getI()<=0){
                        group = LookUp(one);
                        if (group){
//                                System.out.println("moving ..."+ one.clone().getPosition().up(board));
                            one.getPosition().up(board);
                            group = isAdjacentV(one, stop);
                        }
                    }
                    else{
                        group = LookDown(one);
                        if (group){
//                                System.out.println("moving ..."+ one.clone().getPosition().down(board));
                            one.getPosition().down(board);
                            group = isAdjacentV(one,stop);
                        }
                    }

//                    System.out.println("Group:"+group);
                return group;                           
        }
        
        private ArrayList<ArrayList<Box>> tryToGroup (ArrayList<Box> Allboxes) throws CloneNotSupportedException
        {
            ArrayList<Box> solution = new ArrayList<Box>();
            ArrayList<Box> notGroup = new ArrayList<Box>();
            LinkedHashSet solutionKeepOut = new LinkedHashSet(); 
            ArrayList<Box> boxes = new ArrayList<Box>();
            for (Box b: Allboxes){
                boxes.add(b.clone());
            }        
        
            // To return the BoxSpaces.
            ArrayList<ArrayList<Box>> BoxGroups = new ArrayList<ArrayList<Box>>();            

            // Continue while there are still boxes to look.
            while (!boxes.isEmpty())
            {
                // Get new box.
                Box b = boxes.remove(0);

                if (solution.isEmpty())
                    addBox(b,solutionKeepOut,solution);
                else
                {
                    ///**************************************
                    ///**************************************
                    ///**************************************
                    ///**************************************
                    if (isAdjacent(b,solution.get(0)))
                        addBox(b,solutionKeepOut,solution);
                    else
                        addBox(b,solutionKeepOut,notGroup);
                    ///**************************************
                    ///**************************************
                    ///**************************************
                    ///**************************************
                }                                
            }
            
            // When you finish checking all ... Add the last group.
            if (!solution.isEmpty())
                BoxGroups.add(solution);
            if (!solution.isEmpty()){            
                ArrayList<ArrayList<Box>> temp = tryToGroup(notGroup);
                for (ArrayList<Box> b: temp)
                {
                    BoxGroups.add(b);
                }
            }            
            for (Box b: notGroup)
            {
                ArrayList<Box> temp = new ArrayList<Box>();
                if (wasVisited(b.getPosition(),solutionKeepOut)){
                    temp.add(b);
                    BoxGroups.add(temp);
                }
            }
            return BoxGroups;
        }
        
        
        private boolean isVAdjacent(Position one, ArrayList<Box> boxes)
        {
            for (Box box: boxes){
                Position two = box.getPosition();
//                System.out.println("Comparing....:"+one + " and " + two);
                if (Math.abs(one.getJ()-two.getJ())<=1)
                    return true;
            }
            return false;
        }
        
        private boolean isHAdjacent(Position one, ArrayList<Box> boxes)
        {
            for (Box box: boxes){
                Position two = box.getPosition();
//                System.out.println("Comparing....:"+one + " and " + two);
                if (Math.abs(one.getI()-two.getI())<=1)
                    return true;
            }
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
        private void addBox(Box b,LinkedHashSet solutionString, ArrayList<Box> solution) throws CloneNotSupportedException
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
        
        private void split(ArrayList<Box> solution, ArrayList<ArrayList<Box>> BoxGroups, Box b)
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
