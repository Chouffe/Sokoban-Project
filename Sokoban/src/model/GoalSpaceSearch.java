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
public class GoalSpaceSearch extends Box implements Cloneable {
    protected ArrayList<Position> goals;
    protected Map board;
    protected LinkedHashSet goalPositions;
    protected int SPACES = 1;
    
    
    public GoalSpaceSearch(ArrayList<Position> goals, Map map) throws CloneNotSupportedException            
    {        
        board = map.clone();
        this.goals = map.getGoals();       
    }
    
    public ArrayList<Position> sort(ArrayList<Position> goals, boolean Vertical)
    {
        ArrayList<Position> sortedGoals = new ArrayList<Position>();
        ArrayList<Position> notSortedGoals = new ArrayList<Position>();
        while (!goals.isEmpty())
        {
            Position b = goals.remove(0);
            if (sortedGoals.isEmpty())
                sortedGoals.add(b);
            else{
                if (Vertical){
                    if (isVAdjacent(b,sortedGoals))
                        sortedGoals.add(b);                                
                    else
                        notSortedGoals.add(b);
                }
                else{
                    if (isHAdjacent(b,sortedGoals))
                        sortedGoals.add(b);                                
                    else
                        notSortedGoals.add(b);                
                }
            }
        }
        if (notSortedGoals.isEmpty())
            return sortedGoals;
        else
        {
            ArrayList<Position> temp = new ArrayList<Position>();
            temp = sort(notSortedGoals, Vertical);
            for (Position b: temp)
            {
                sortedGoals.add(b);
            }
        }
        return sortedGoals;
    }            
    
    public ArrayList<ArrayList<Position>> groupGoals(ArrayList<ArrayList<Position>> Vertical, ArrayList<ArrayList<Position>> Horizontal ) throws CloneNotSupportedException
    {
        LinkedHashSet solutionTest = new LinkedHashSet(); 
        ArrayList<ArrayList<Position>> solution = new ArrayList<ArrayList<Position>>();
        ArrayList<Position> notGrouped = new ArrayList<Position>();
        while (!Vertical.isEmpty())
        {
            ArrayList<Position> goals = Vertical.remove(0);
            if (goals.size()>5){
                for (Position temp: goals){
                    solutionTest.add(""+ temp.getI() +":"+ temp.getJ());
                }
            solution.add(goals);
            }
        }
        while (!Horizontal.isEmpty())
        {
            ArrayList<Position> goals = Horizontal.remove(0);
            ArrayList<Position> goals2 = new ArrayList<Position>();
            if (goals.size()>1){
                for (Position temp: goals){
                    
                    if (solutionTest.add(""+ temp.getI() +":"+ temp.getJ()))
                        goals2.add(temp);
                }
                if (!goals2.isEmpty())
                    solution.add(goals2);
            }
            else
            {
                for (Position temp: goals){                    
                    if (solutionTest.add(""+ temp.getI() +":"+ temp.getJ()))
                        notGrouped.add(temp);                
                }
            }
                
        }
        
        ArrayList<ArrayList<Position>> newGrouped = new ArrayList<ArrayList<Position>>();
        newGrouped = tryToGroup(notGrouped);        
        for (ArrayList<Position> b: newGrouped)
        {
            solution.add(b);
        }
        return solution;
        
    }
    
    public ArrayList<ArrayList<Position>> getGoalSpaces() throws CloneNotSupportedException
    {
        
//        ArrayList<Box> sortH = sort(boxes,false);
        ArrayList<ArrayList<Position>> searchH = Search(goals);        
        ArrayList<Position> sortV = sort(goals,true);
        ArrayList<ArrayList<Position>> searchV = Search(sortV); 
        ArrayList<ArrayList<Position>> solution = groupGoals(searchV,searchH);
//        print(solution);
//        System.out.println(solution.size()+"*********SIZE**********");
//        if (boxCount<20)
        
        
        int oldsolutionSize = 0;      
        int count=0;

        while (oldsolutionSize!=solution.size()){
            oldsolutionSize = solution.size();
//            System.out.println(solution.size()+"********* OLD SIZE**********"); 
            ArrayList<ArrayList<Position>> singles = getSingles(solution);                
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
        return solution;          
    }
    
    public ArrayList<ArrayList<Position>> getSingles(ArrayList<ArrayList<Position>> solution)
    {
        ArrayList<ArrayList<Position>> newS = new ArrayList<ArrayList<Position>>();
        ArrayList<Position> s = new ArrayList<Position>();
        for (ArrayList<Position> b: solution)
        {
            if (b.size()==1)
            {
                s.add(b.get(0));
            }
            else
                newS.add(b);
                
        }
        s = sort(s,true);
        for (Position b:s)
        {
            ArrayList<Position> temp = new ArrayList<Position> ();
            temp.add(b); 
            newS.add(temp);
        }
        
        return newS;
    }
    
    public void print(ArrayList<ArrayList<Position>> result)
    {
        System.out.println("Return:");
        if (result.isEmpty())
            System.out.print("FAIL");
        else{
            System.out.println("Size"+result.size());
            for (int t=0;t<result.size();t++){
                
                ArrayList<Position> temp = result.get(t);
                System.out.println("Group:"+(t+1) + " size of group:"+ temp.size());
                for (int r=0; r<temp.size();r++){                    
                    System.out.println(temp.get(r));
                }
            }
        }
    }
    
    
    // TODO ¿Implement GoalSpace?
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
    
    
    public ArrayList<ArrayList<Position>> Search(ArrayList<Position> AllGoals) throws CloneNotSupportedException
    {
//        if (Allboxes.size()>20)
//            SPACES = 3;
//        else
//            SPACES = 1;
        // Clone the array of boxes.
        ArrayList<Position> solution = new ArrayList<Position>();
        LinkedHashSet solutionString = new LinkedHashSet(); 
        ArrayList<Position> goals = new ArrayList<Position>();
        for (Position b: AllGoals){
            goals.add(b.clone());
//            System.out.println(b.clone().getPosition());
        }
        
        
        // To return the GoalSpaces.
        ArrayList<ArrayList<Position>> GoalGroups = new ArrayList<ArrayList<Position>>();
        
                
        // Check where to look for boxes.
        boolean [] directionsToLook = new boolean[4];
        
        // To split the box groups.
        boolean group = false;
        
        // Continue while there are still boxes to look.
        while (!goals.isEmpty())
        {
            // Get new box.
            Position b = goals.remove(0);
            
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
                        split(solution,GoalGroups,null);
                        if (!goals.isEmpty())
                            if(!isAdjacent(b,goals)){
//                                System.out.println("No more adjacent boxes in boxes.");
                                split(solution,GoalGroups,null);
                                
                            }
                    }
                addGoal(b,solutionString,solution);                                
            }                
            // if not ...
            else
            {
                //Split and add the box left alone.
//                System.out.println("Normal split!");
                split(solution,GoalGroups,b);
            }
        }
        // When you finish checking all ... Add the last group.
        split(solution,GoalGroups,null);        
        return GoalGroups;
        
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
        public boolean [] LookAround (Position GoalSquare) throws CloneNotSupportedException
        {
            
            // Always initialize as he can go no where.
            boolean [] directions = {false, false, false, false};                        
            // UP
            directions[0]=LookUp(GoalSquare);
            // DOWN
            directions[1]=LookDown(GoalSquare);
            // LEFT
            directions[2]=LookLeft(GoalSquare);
            // RIGHT
            directions[3]=LookRight(GoalSquare);
            
            return directions;
        }  
        
        public boolean LookRight (Position goal) throws CloneNotSupportedException
        {
                                 
            
            Position right = goal.clone().right(board);
            
            //RIGHT
            Cell.ECell rightType = board.getCellFromPosition(right).getType();
            if (rightType == Cell.ECell.EMPTY_FLOOR ||
                    rightType == Cell.ECell.VISITED ||
                    rightType == Cell.ECell.PLAYER ||
                    rightType == Cell.ECell.GOAL_SQUARE||
                    rightType == Cell.ECell.PLAYER_ON_GOAL_SQUARE) 
                    {
                
                return true;
            }
            
            return false;
        }
        
        public boolean LookLeft (Position goal) throws CloneNotSupportedException
        {                                              
            Position left = goal.clone().left(board);
                        
            // LEFT
            Cell.ECell leftType = board.getCellFromPosition(left).getType();
            if (leftType == Cell.ECell.EMPTY_FLOOR ||
                    leftType == Cell.ECell.VISITED ||
                    leftType == Cell.ECell.PLAYER ||
                    leftType == Cell.ECell.GOAL_SQUARE ||
                    leftType == Cell.ECell.PLAYER_ON_GOAL_SQUARE)  {             
                
//                System.out.println("Left "+ left +" is free.");
                return true;
            }
            
            return false;
        }
        
        public boolean LookUp (Position goal) throws CloneNotSupportedException
        {           
            Position up = goal.clone().up(board);
                        
            // LEFT
            Cell.ECell upType = board.getCellFromPosition(up).getType();
            if (upType == Cell.ECell.EMPTY_FLOOR ||
                    upType == Cell.ECell.VISITED ||
                    upType == Cell.ECell.PLAYER ||
                    upType == Cell.ECell.GOAL_SQUARE ||
                    upType == Cell.ECell.PLAYER_ON_GOAL_SQUARE)
            {             
                
//                System.out.println("Up "+ up +" is free.");
                return true;
            }
            
            return false;
        }
        
        public boolean LookDown (Position goal) throws CloneNotSupportedException
        {                                  
            Position down = goal.clone().down(board);
                        
            // LEFT
            Cell.ECell downType = board.getCellFromPosition(down).getType();
            if (downType == Cell.ECell.EMPTY_FLOOR ||
                    downType == Cell.ECell.VISITED ||
                    downType == Cell.ECell.PLAYER ||
                    downType == Cell.ECell.GOAL_SQUARE ||
                    downType == Cell.ECell.PLAYER_ON_GOAL_SQUARE)  {             
                
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
                
        public boolean isAdjacent(Position start, ArrayList<Position> solution) throws CloneNotSupportedException 
        {
            Position one = start.clone();
            boolean group = true;
            for (Position b: solution)
            {
                group = group && isAdjacent(start,b);
            }
            return group;
        }
        public boolean isAdjacent(Position start, Position two) throws CloneNotSupportedException
        {
            Position one = start.clone();
            boolean group = true;
            if (two.equals(one))
                return true;
                
                if (Math.abs(one.getI()-two.getI())==0) 
                {
                                      
                        if (one.getJ()-two.getJ()>=0){
                            group = LookLeft(one);
                            if (group){
//                                System.out.println("moving ..."+ one.clone().getPosition().left(board));
                                one.left(board);
                                group = isAdjacent(one, two);
                            }
                        }
                        else{
                            group = LookRight(one);
                            if (group){
//                                System.out.println("moving ..."+ one.clone().getPosition().right(board));
                                one.right(board);
                                group = isAdjacent(one,two);
                            }
                        }
                     
//                    System.out.println("Group:"+group);
                    return group;
                }
                            
            return false;
        } 
        
        public ArrayList<ArrayList<Position>> TryToMergeV(ArrayList<ArrayList<Position>> Allboxes) throws CloneNotSupportedException
        {            
            // To save the not Grouped/Merged ones.
            ArrayList<ArrayList<Position>> solution = new ArrayList<ArrayList<Position>>();
            // To save the cloned <Arraylistboxes
            ArrayList<ArrayList<Position>> boxes = new ArrayList<ArrayList<Position>>();
            
            LinkedHashSet solutionKeepOut = new LinkedHashSet();
            
            // Clone Allboxes
            for (ArrayList<Position> b: Allboxes){
                boxes.add((ArrayList<Position>)b.clone());
            }                 
            boolean merged = false;
            // To return the BoxSpaces.
            ArrayList<ArrayList<Position>> BoxGroups = new ArrayList<ArrayList<Position>>();            

            // Continue while there are still boxes to look.
            if (!boxes.isEmpty())
            {
                // Get new box.
                ArrayList<Position> b = boxes.remove(0);
                ArrayList<Position> a = new ArrayList<Position>();
                // Take b and compare it with every single ArrayList of Boxes
                for (int i=0;i<boxes.size();i++){
                    ArrayList<Position> other = new ArrayList<Position>();
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
                        
                        for (Position one: b)
                        {
                            addGoal(one,solutionKeepOut,a);
                        }
//                        System.out.println("Merging: "+b.get(0).getPosition()+"S"+b.size());
                        for (Position two: other)
                        {
                            addGoal(two,solutionKeepOut,a);
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
        
        public boolean isAdjacentV(ArrayList<Position> one, ArrayList<Position> two) throws CloneNotSupportedException 
        {            
            boolean group = true;            
            for (int i =0; i<one.size();i++){
                for (int j=0; j<two.size();j++)
                {       
//                    System.out.println("Inside");
                    if (group){
                            if (one.get(i).getI()!=two.get(j).getI())
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
                        if (one.get(i).getI()!=two.get(j).getI())
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
        
        public boolean isAdjacentV(Position start, Position two) throws CloneNotSupportedException 
        {
            Position one = start.clone();
            boolean group = true;
//            System.out.println("Just getting here!!");
            if (two.getI()==one.getI())
                return true;
                        
//            System.out.println("Inside"+one.getPosition());

// ******************************************************************************************************            
// ******************************************************************************************************            
                    if (one.getI()-two.getI()>=0 && one.getI()-two.getI()<=1){
// ******************************************************************************************************
// ******************************************************************************************************
                        // Possible Bug.
// ******************************************************************************************************                        
                        group = LookUp(one);
                        if (group){
//                                System.out.println("moving up..."+ one.clone().getPosition().up(board));
                            one.up(board);
                            group = isAdjacentV(one, two);
                        }
                    }
                    else{
                        group = LookDown(one);
                        if (group){
//                                System.out.println("moving down..."+ one.clone().getPosition().down(board));
                            one.down(board);
                            group = isAdjacentV(one,two);
                        }
                    }

//                    System.out.println("Group:"+group);
                return group;                           
        }
        
        private ArrayList<ArrayList<Position>> tryToGroup (ArrayList<Position> AllGoals) throws CloneNotSupportedException
        {
            ArrayList<Position> solution = new ArrayList<Position>();
            ArrayList<Position> notGroup = new ArrayList<Position>();
            LinkedHashSet solutionKeepOut = new LinkedHashSet(); 
            ArrayList<Position> goals = new ArrayList<Position>();
            for (Position b: AllGoals){
                goals.add(b.clone());
            }        
        
            // To return the BoxSpaces.
            ArrayList<ArrayList<Position>> GoalGroups = new ArrayList<ArrayList<Position>>();            

            // Continue while there are still boxes to look.
            while (!goals.isEmpty())
            {
                // Get new box.
                Position b = goals.remove(0);

                if (solution.isEmpty())
                    addGoal(b,solutionKeepOut,solution);
                else
                {
                    ///**************************************
                    ///**************************************
                    ///**************************************
                    ///**************************************
                    if (isAdjacent(b,solution.get(0)))
                        addGoal(b,solutionKeepOut,solution);
                    else
                        addGoal(b,solutionKeepOut,notGroup);
                    ///**************************************
                    ///**************************************
                    ///**************************************
                    ///**************************************
                }                                
            }
            
            // When you finish checking all ... Add the last group.
            if (!solution.isEmpty())
                GoalGroups.add(solution);
            if (!solution.isEmpty()){            
                ArrayList<ArrayList<Position>> temp = tryToGroup(notGroup);
                for (ArrayList<Position> b: temp)
                {
                    GoalGroups.add(b);
                }
            }            
            for (Position b: notGroup)
            {
                ArrayList<Position> temp = new ArrayList<Position>();
                if (wasVisited(b,solutionKeepOut)){
                    temp.add(b);
                    GoalGroups.add(temp);
                }
            }
            return GoalGroups;
        }
        
        
        private boolean isVAdjacent(Position one, ArrayList<Position> goals)
        {
            for (Position two: goals){                
//                System.out.println("Comparing....:"+one + " and " + two);
                if (Math.abs(one.getJ()-two.getJ())<=1)
                    return true;
            }
            return false;
        }
        
        private boolean isHAdjacent(Position one, ArrayList<Position> goals)
        {
            for (Position two: goals){                
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
        private void addGoal(Position b,LinkedHashSet solutionString, ArrayList<Position> solution) throws CloneNotSupportedException
        {
            if (wasVisited(b,solutionString)){
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
        
        private void split(ArrayList<Position> solution, ArrayList<ArrayList<Position>> GoalGroups, Position b)
        {
            // Split the gruoups
            if (!solution.isEmpty()){                    
                GoalGroups.add((ArrayList<Position>)solution.clone());

                /*
                 * For debugging
                 */
//                System.out.println("Spliting .... ");
//                for (Box t: solution)
//                    System.out.println(t.getPosition());
            }
            if (b!=null){
                // Add the ones that have no Group as singles.
                ArrayList<Position> temp = new ArrayList<Position>();
                temp.add(b);
                GoalGroups.add( (ArrayList<Position>)temp.clone() );                
            }
            solution.clear();
        }        
    
}
