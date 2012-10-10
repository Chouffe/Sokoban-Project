package tests; 

import static org.junit.Assert.*;
import Agent;
import Box;
import IllegalMoveException;
import Map;
import PathNotFoundException;

import java.io.*;
import java.util.ArrayList;

import model.*;

import org.junit.Before;
import org.junit.Test;


public class BoxPathsTest {
	Agent agent;

	@Before
	public final void Initialize() {
		agent = new Agent();
	}

	@Test
	public final void TestSequentialBoxPath() throws FileNotFoundException, IllegalMoveException{

	BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/boxpaths/map1.txt"));
	Map map = new Map(br);

	String[] paths = new String[map.getNumberOfBoxes()];
	try {
		agent.findSequentialBoxToGoalPaths(map, paths, 0);
	} catch (CloneNotSupportedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (PathNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	assertEquals("ddLdlUruLLulDD", paths[0]);
	}

	@Test
	public final void TestSequentialBoxPaths() throws FileNotFoundException, IllegalMoveException{

	BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/boxpaths/map3.txt"));
	Map map = new Map(br);

	String[] paths = new String[map.getNumberOfBoxes()];
	boolean success = false;
	try {
		success = agent.findSequentialBoxToGoalPaths(map, paths, 0);
	} catch (CloneNotSupportedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (PathNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	assertEquals("llllDD", paths[0]);
	assertEquals("urrrrdLdlUruLLulD", paths[1]);
	assertEquals(true, success);
	}

	@Test
	public final void TestSequentialBoxPathsFailure() throws FileNotFoundException, IllegalMoveException{

	BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/boxpaths/map2.txt"));
	Map map = new Map(br);

	String[] paths = new String[map.getNumberOfBoxes()];
	boolean success = true;
	try {
		success = agent.findSequentialBoxToGoalPaths(map, paths, 0);
	} catch (CloneNotSupportedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (PathNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	assertEquals(null, paths[0]);
	assertEquals(null, paths[1]);
	assertEquals(false, success);
	}

	@Test
	public final void TestFindBoxToGoalPaths() throws FileNotFoundException, IllegalMoveException{

	BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/boxpaths/map2.txt"));
	Map map = new Map(br);

	String[] paths = new String[map.getNumberOfBoxes()];
	ArrayList<Box> ordering = new ArrayList<Box>();
	boolean success = true;
	try {
		success = agent.findBoxToGoalPaths(ordering, map, paths);
	} catch (CloneNotSupportedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (PathNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	assertEquals("RR", paths[0]);
	assertEquals("lllulldRRRR", paths[1]);
	assertEquals(true, success);
	}

//	@Test
//	public final void TestHashPaths() throws FileNotFoundException, IllegalMoveException{
//
//	BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/boxpaths/map6.txt"));
//	Map map = new Map(br);
//
//	String[] paths = new String[map.getNumberOfBoxes()];
//	ArrayList<Box> ordering = new ArrayList<Box>();
//	boolean success = true;
//	try {
//		success = agent.findBoxToGoalPaths(ordering, map, paths);
//	} catch (CloneNotSupportedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (PathNotFoundException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//
//	assertEquals("RR", paths[0]);
//	assertEquals("lllulldRRRR", paths[1]);
//	assertEquals(true, success);
//	}
}
