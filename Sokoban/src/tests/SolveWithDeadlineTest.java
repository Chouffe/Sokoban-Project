package tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import Agent;
import Cell;
import Deadline;
import DeadlineException;
import IllegalMoveException;
import Map;
import PathNotFoundException;
import Position;
import SokobanChecker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


import org.junit.Before;
import org.junit.Test;


public class SolveWithDeadlineTest {

	Agent agent;
	BufferedReader br;
	
	@Before
	public final void setUp()
	{
		agent = new Agent();
		br = null;
	}
	
	@Test
	public final void testSolveWithDeadline() throws CloneNotSupportedException, IOException, PathNotFoundException, IllegalMoveException, DeadlineException {
		
		br = new BufferedReader(new FileReader("src/tests/maps/deadline/map1.txt"));
		Map map = new Map(br);
		Date date = new Date();
		date.setTime(date.getTime()+1000);
		Deadline due = new Deadline(date);
		assertTrue(SokobanChecker.mapIsSolved(map, agent.solveWithDeadline(map, due)));
		
		br = new BufferedReader(new FileReader("src/tests/maps/deadline/map2.txt"));
		map = new Map(br);
		date = new Date();
		date.setTime(date.getTime()+300);
		due = new Deadline(date);
		try
		{
			assertFalse(SokobanChecker.mapIsSolved(map, agent.solveWithDeadline(map, due)));
			fail("Should have thrown a deadline exception");
		}
		catch(DeadlineException e)
		{
			date = new Date();
			date.setTime(date.getTime()+5000);
			due = new Deadline(date);
			assertTrue(SokobanChecker.mapIsSolved(map, agent.solveWithDeadline(map, due)));
			System.out.println(due.TimeUntil());
		}
		
		
		
		//assertTrue(SokobanChecker.mapIsSolved(map, "UUU"));
	}
	
	@Test
	public final void testSolve2() throws CloneNotSupportedException, PathNotFoundException, IOException, IllegalMoveException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/deadline/map2.txt"));
		Map map = new Map(br);
		
		System.out.println(agent.solve2(map, 0));
	}

}
