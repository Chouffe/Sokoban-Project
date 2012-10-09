/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import model.Box;
import model.BoxSpace;
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
public class BoxSpaceTest {
    
    public BoxSpaceTest() {
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
        
        ArrayList<Box> boxes = map.getBoxes();        
        
        BoxSpace instance = new BoxSpace(boxes, map);    
        
        ArrayList<ArrayList<Box>> result = instance.getBoxSpaces(boxes);        
        instance.print(result);

//        assertEquals(boxes,result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}