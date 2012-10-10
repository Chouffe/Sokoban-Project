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
public class BoxSpaceSearch extends Box implements Cloneable {

    protected ArrayList<Box> boxes;
    protected Map board;
    protected LinkedHashSet boxPositions;
    protected int SPACES = 1;
    
    
    public BoxSpaceSearch(ArrayList<Box> boxes, Map map) throws CloneNotSupportedException            
    {        
        board = map.clone();
        this.boxes = map.getBoxes();       
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
    
    public ArrayList<BoxSpace> getBoxSpaces() throws CloneNotSupportedException
    {
        
//        ArrayList<Box> sortH = sort(boxes,false);
        ArrayList<ArrayList<Box>> searchH = Search(boxes);        
        ArrayList<Box> sortV = sort(boxes,true);
        ArrayList<ArrayList<Box>> searchV = Search(sortV); 
        ArrayList<ArrayList<Box>> solution = groupBoxes(searchV,searchH);
//        print(solution);
//        System.out.println(solution.size()+"*********SIZE**********");
//        if (boxCount<20)
        
        
        int oldsolutionSize = 0;      
        int count=0;

        while (oldsolutionSize!=solution.size()){
            oldsolutionSize = solution.size();
//            System.out.println(solution.size()+"********* OLD SIZE**********"); 
            ArrayList<ArrayList<Box>> singles = getSingles(solution);                
            solution = TryToMergeV(singles);
//            System.out.println(solution.size()+"*********NEW SIZE**********");   
        }
//        System.out.println(solution.size()+"*********STOP!!!!!!!!!!**********");
//        ArrayList<ArrayList<Box>> singles = getSingles(solution);                
//        solution = TryToMergeV(solution);
//        System.out.println(solution.size()+"*********SIZE**********");
//        singles = getSingles(solution);                
//        solution = TryToMergeV(singles);
//        System.out.println(solution.size()+"*********SIZE**********");
//        singles = getSingles(solution);                
//        solution = TryToMergeV(singles);
//        System.out.println(solution.size()+"*********SIZE**********");
        
        ArrayList<BoxSpace> bS = new ArrayList<BoxSpace>();
        for (ArrayList<Box> s: solution)
        {
            BoxSpace temp = new BoxSpace(s);
            bS.add(temp);
        }
        return bS;          
    }
    
    public ArrayList<ArrayList<Box>> getSingles(ArrayList<ArrayList<Box>> solution)
    {
        ArrayList<ArrayList<Box>> newS = new ArrayList<ArrayList<Box>>();
        ArrayList<Box> s = new ArrayList<Box>();
        for (ArrayList<Box> b: solution)
        {
            if (b.size()==1)
            {
                s.add(b.get(0));
            }
            else
                newS.add(b);
                
        }
        s = sort(s,true);
        for (Box b:s)
        {
            ArrayList<Box> temp = new ArrayList<Box> ();
            temp.add(b); 
            newS.add(temp);
        }
        
        return newS;
    }
    
    public void print(ArrayList<ArrayList<Box>> result)
    {
        System.out.println("Return:");
        if (result.isEmpty())
            System.out.print("FAIL");
        else{
            System.out.println("Size"+result.size());
            for (int t=0;t<result.size();t++){
                
                ArrayList<Box> temp = result.get(t);
                System.out.println("Group:"+(t+1) + " size of group:"+ temp.size());
                for (int r=0; r<temp.size();r++){                    
                    System.out.println(temp.get(r).getPosition());
                }
            }
        }
    }
    
    public void printBS(ArrayList<BoxSpace> result)
    {
        System.out.println("Return:");
        if (result.isEmpty())
            System.out.print("FAIL");
        else{
            System.out.println("Size"+result.size());
            for (int t=0;t<result.size();t++){
                
                BoxSpace temp = result.get(t);
                System.out.println("Group:"+(t+1) + " size of group:"+ temp.getBoxCount());
                for (int r=0; r<temp.getBoxCount();r++){                    
                    System.out.println(temp.getPosition(r));
                }
            }
        }
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
                    rightType == Cell.ECell.BOX_ON_GOAL ||
                    rightType == Cell.ECell.PLAYER_ON_GOAL_SQUARE){
                
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
                    leftType == Cell.ECell.BOX_ON_GOAL ||
                    leftType == Cell.ECell.PLAYER_ON_GOAL_SQUARE)  {             
                
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
                    leftType == Cell.ECell.BOX_ON_GOAL ||
                    leftType == Cell.ECell.PLAYER_ON_GOAL_SQUARE)  {             
                
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
                    leftType == Cell.ECell.BOX_ON_GOAL ||
                    leftType == Cell.ECell.PLAYER_ON_GOAL_SQUARE)  {             
                
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
            // To save the not Grouped/Merged ones.
            ArrayList<ArrayList<Box>> solution = new ArrayList<ArrayList<Box>>();
            // To save the cloned <Arraylistboxes
            ArrayList<ArrayList<Box>> boxes = new ArrayList<ArrayList<Box>>();
            
            LinkedHashSet solutionKeepOut = new LinkedHashSet();
            
            // Clone Allboxes
            for (ArrayList<Box> b: Allboxes){
                boxes.add((ArrayList<Box>)b.clone());
            }                 
            boolean merged = false;
            // To return the BoxSpaces.
            ArrayList<ArrayList<Box>> BoxGroups = new ArrayList<ArrayList<Box>>();            

            // Continue while there are still boxes to look.
            if (!boxes.isEmpty())
            {
                // Get new box.
                ArrayList<Box> b = boxes.remove(0);
                ArrayList<Box> a = new ArrayList<Box>();
                // Take b and compare it with every single ArrayList of Boxes
                for (int i=0;i<boxes.size();i++){
                    ArrayList<Box> other = new ArrayList<Box>();
                    if (!boxes.isEmpty())
                        other= boxes.get(i);
                    else
                        other = solution.get(0);
                    if (isAdjacentV(b,other))
                    {
//                        System.out.println("Can be merged.");
                        // Remove other from boxes.
                        //boxes.remove(other);
                        // Merge both ArrayLists
                        
                        for (Box one: b)
                        {
                            addBox(one,solutionKeepOut,a);
                        }
//                        System.out.println("Merging: "+b.get(0).getPosition()+"S"+b.size());
                        for (Box two: other)
                        {
                            addBox(two,solutionKeepOut,a);
                        }
                        // Add so it can be merged again.
//                        System.out.println("AND "+other.get(0).getPosition()+"S"+other.size() + "="+a.size());
                        
                        
                        merged=true;       
//                        break;
                    }
                    else
                    {
                        // Add the non merged ones to another ArrayList
                        solution.add(other);
                    }                    
                }
                
                if (!merged)
                    solution.add(b);
                else{
                    solution.add(a);
//                        System.out.println("New size:"+ solution.size());
                }
            }
            
            // If you merged a group. Check if it can be merged with the next ones.
            if (merged && solution.size()>=2)
                solution = TryToMergeV(solution);            
            return solution;
        }
        
        public boolean isAdjacentV(ArrayList<Box> one, ArrayList<Box> two) throws CloneNotSupportedException 
        {            
            boolean group = true;            
            for (int i =0; i<one.size();i++){
                for (int j=0; j<two.size();j++)
                {       
//                    System.out.println("Inside");
                    if (group){
                            if (one.get(i).getPosition().getI()!=two.get(j).getPosition().getI())
                                group = group && isAdjacentV(one.get(i),two.get(j));
                            else{
                                
//                                System.out.println("Inside for "+ one.get(i).getPosition() + "and"+two.get(j).getPosition());                                
                               return false;
                            }
                    }
                    else
                        return false;
                }
//                System.out.println("Group"+group);    
                
            }
//            System.out.println("Trying other side");
            for (int j =0; j<two.size();j++){
                for (int i=0; i<one.size();i++)
                {       
//                    System.out.println("Inside2");
                    if (group){
                        if (one.get(i).getPosition().getI()!=two.get(j).getPosition().getI())
                            group = group && isAdjacentV(two.get(j),one.get(i));
                        else{
//                            System.out.println("Inside for "+ one.get(i).getPosition() + "and"+two.get(j).getPosition());                                
                            return false;
                        }
                    }
                    else
                        return false;
                }
//                System.out.println("Group"+group);    
                
            }
            return group;
//            else{
////                int j=0;
//                for (int i=0; i<length;i++)
//                {
//
//                    if (group){
////                        while (one.get(j).getPosition().getJ()!=two.get(0).getPosition().getJ())
////                            if (j<one.size()-1)
////                                j++; 
//                        group = group && isAdjacentV(one.get(i),two.get(i));
//                    }
//                    else
//                        return false;
//                }
//                return group;
//            }
        }
        
        public boolean isAdjacentV(Box start, Box stop) throws CloneNotSupportedException 
        {
            Box one = start.clone();
            boolean group = true;
//            System.out.println("Just getting here!!");
            if (stop.getPosition().getI()==one.getPosition().getI())
                return true;
            
            Position two = stop.getPosition();
//            System.out.println("Inside"+one.getPosition());

// ******************************************************************************************************            
// ******************************************************************************************************            
                    if (one.getPosition().getI()-two.getI()>=0 ){//&& one.getPosition().getI()-two.getI()<=1){
// ******************************************************************************************************
// ******************************************************************************************************
                        // Possible Bug.
// ******************************************************************************************************                        
                        group = LookUp(one);
                        if (group){
//                                System.out.println("moving up..."+ one.clone().getPosition().up(board));
                            one.getPosition().up(board);
                            group = isAdjacentV(one, stop);
                        }
                    }
                    else{
                        group = LookDown(one);
                        if (group){
//                                System.out.println("moving down..."+ one.clone().getPosition().down(board));
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
