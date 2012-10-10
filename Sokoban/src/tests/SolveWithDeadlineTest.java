package tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import model.Agent;
import model.Cell;
import model.Deadline;
import model.Map;
import model.Position;
import model.SokobanChecker;

import org.junit.Before;
import org.junit.Test;

import exception.DeadlineException;
import exception.IllegalMoveException;
import exception.OffOfMapException;
import exception.PathNotFoundException;

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
	public final void testSolveWithDeadline() throws CloneNotSupportedException, IOException, PathNotFoundException, IllegalMoveException, DeadlineException, OffOfMapException {
		
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
	public final void testSolve2() throws CloneNotSupportedException, PathNotFoundException, IOException, IllegalMoveException, OffOfMapException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/deadline/map2.txt"));
		Map map = new Map(br);
		
		System.out.println(agent.solve2(map, 0));
	}

}
