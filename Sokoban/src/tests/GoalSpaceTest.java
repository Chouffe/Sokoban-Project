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
import model.GoalSpace;
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
//        int [] map_num = {94,93,88,87,82,81,79,77,76,70,68,66,64,60,59,58,57};
        int [] map_num = {93};
//        for (int i =1;i<112;i++){
        for (int i: map_num){
            BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/BoxSpace/map"+i+".txt"));
    //        System.out.println(mapas.length);
    //        String [] mapa_lineas = mapas[0].split("\n");
    //        ArrayList<String> mapa = new ArrayList<String>();
    //        for (String s: mapa_lineas)
    //            mapa.add(s);
            System.out.println("MAP"+i);
            Map map = new Map(br);
            System.out.println(map);  
            
            GoalSpaceSearch instance = new GoalSpaceSearch(map.getGoals(),map);
            
            ArrayList<GoalSpace> result = instance.getGoalSpaces();        
            instance.printGS(result);

//        assertEquals(boxes,result);
        }
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
//            BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/BoxSpace/map93.txt"));
//            Map map = new Map(br);
//            System.out.println(map);
//        
//            GoalSpaceSearch instance = new GoalSpaceSearch(map.getGoals(), map);
//            ArrayList<GoalSpace> tests = instance.getGoalSpaces();
////            ArrayList<Box> a = new ArrayList<Box>();
//            GoalSpace a = tests.get(1);
////            for (Box t: tests.get(0))
////            {
////                a.add(t);
////            }
////            for (Box t: tests.get(1))
////            {
////                a.add(t);
////            }
//            GoalSpace b = tests.get(2);
//                    
//            boolean result = instance.isAdjacentV(b.getGoalsAL(), a.getGoalsAL());             
//            System.out.println("A"+result);
//            assertEquals(true,result);
//        }
}
