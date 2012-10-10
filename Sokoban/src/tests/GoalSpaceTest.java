/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import model.Box;
import model.BoxSpace;
import model.BoxSpaceSearch;
import model.Goal;
import model.GoalSpaceSearch;
import model.Map;
import model.Position;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lfreina
 */
public class GoalSpaceTest {
    
    public GoalSpaceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
//    @Test
//    public void Look_around (Position square) throws FileNotFoundException, CloneNotSupportedException 
//    {                
//        System.out.println("Look_around");
//        BoxSpace instance = null;
//        boolean[] expResult = null;
//        boolean[] result = instance.Look_around(square);
//        assertEquals(expResult[0], result[0]);
//        assertEquals(expResult[1], result[1]);
//        assertEquals(expResult[2], result[2]);
//        assertEquals(expResult[3], result[3]);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
    /**
     * Test of Search method, of class BoxSpace.
     */
    
    
    @Test
    public void testSearch() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/map13.txt"));
        Map map = new Map(br);
	System.out.println(map);
        
        ArrayList<Position> goals = map.getGoals();  
        
        GoalSpaceSearch instance = new GoalSpaceSearch(goals, map);    
        
        ArrayList<ArrayList<Position>> result = instance.getGoalSpaces();        
        instance.print(result);

//        assertEquals(boxes,result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
//    @Test
//    public void isAdjacentV() throws CloneNotSupportedException, FileNotFoundException 
//        {            
//            BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/map13.txt"));
//            Map map = new Map(br);
//            System.out.println(map);
//        
//            ArrayList<Box> boxes = map.getBoxes(); 
//            
//            Box a = boxes.get(4);
//            Box b = boxes.get(6);
//            System.out.println(a.getPosition());
//            System.out.println(b.getPosition());
//            BoxSpace instance = new BoxSpace(boxes, map);
//            boolean result = instance.isAdjacentV(b, a);
//            result = result && instance.isAdjacentV(b, a);
//            System.out.println("A"+result);
//            assertEquals(false,result);
//        }
    
//    @Test
//    public void isAdjacentV2() throws CloneNotSupportedException, FileNotFoundException 
//        {            
//            BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/map13.txt"));
//            Map map = new Map(br);
//            System.out.println(map);
//        
//            ArrayList<Box> boxes = map.getBoxes(); 
//            
//            BoxSpace instance = new BoxSpace(boxes, map);
//            ArrayList<ArrayList<Box>> tests = instance.getBoxSpaces();
////            ArrayList<Box> a = new ArrayList<Box>();
//            ArrayList<Box> a = tests.get(2);
////            for (Box t: tests.get(0))
////            {
////                a.add(t);
////            }
////            for (Box t: tests.get(1))
////            {
////                a.add(t);
////            }
//            ArrayList<Box> b = tests.get(3);
//                    
//            boolean result = instance.isAdjacentV(b, a);             
//            System.out.println("A"+result);
//            assertEquals(false,result);
//        }
}
