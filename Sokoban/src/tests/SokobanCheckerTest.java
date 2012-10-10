package tests;

import static org.junit.Assert.*;
import Map;
import SokobanChecker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import org.junit.Test;

import exception.IllegalMoveException;

public class SokobanCheckerTest 
{
	@Test
	public final void testGoodPath() throws FileNotFoundException, CloneNotSupportedException
	{
		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/checker/map1.txt"));
		Map map = new Map(br);
		
		assertEquals(true, SokobanChecker.mapIsSolved(map.clone(), "RR"));
		assertEquals(false, SokobanChecker.mapIsSolved(map.clone(), "R"));
		
		br = new BufferedReader(new FileReader("src/tests/maps/checker/map2.txt"));
		map = new Map(br);
		
		assertEquals(true, SokobanChecker.mapIsSolved(map.clone(), "RRRLDR"));
		assertEquals(false, SokobanChecker.mapIsSolved(map.clone(), "RRR"));
		assertEquals(false, SokobanChecker.mapIsSolved(map.clone(), "RRRLD"));
		assertEquals(false, SokobanChecker.mapIsSolved(map.clone(), "R"));
		assertEquals(false, SokobanChecker.mapIsSolved(map.clone(), ""));
		
	}
	
	@Test
	public final void testIllegalMoves() throws FileNotFoundException, CloneNotSupportedException
	{
		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/checker/map3.txt"));
		Map map = new Map(br);
		
		assertEquals(false, SokobanChecker.mapIsSolved(map.clone(), "R"));
		assertEquals(false, SokobanChecker.mapIsSolved(map.clone(), "DD"));
		assertEquals(false, SokobanChecker.mapIsSolved(map.clone(), "U"));
		assertEquals(false, SokobanChecker.mapIsSolved(map.clone(), "DDRRRR"));
		assertEquals(false, SokobanChecker.mapIsSolved(map.clone(), "L"));
		
		br = new BufferedReader(new FileReader("src/tests/maps/checker/map4.txt"));
		map = new Map(br);
		
		assertEquals(false, SokobanChecker.mapIsSolved(map.clone(), "D"));
		assertEquals(false, SokobanChecker.mapIsSolved(map.clone(), "DULLD"));
		assertEquals(true, SokobanChecker.mapIsSolved(map.clone(), "DULLDURRRRD"));
	}
	
	@Test
	public final void testShowSolution() throws IllegalMoveException, CloneNotSupportedException, IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/checker/map5.txt"));
		Map map = new Map(br);
		
		SokobanChecker.showMapSolving(map, "llUUUluRRRRRurDDDDDDDldRRRurDDDDrdLLLLLLdlUruLLLuuullldddrRRRRurDldRRRRRdrUUUUruLLLdlUUUUUUUruLLLLLulDDDD");
	}
}
