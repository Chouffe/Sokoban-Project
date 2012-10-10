package tests;

import Node;
import Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest 
{
	Node root;
	Node child1;
	Node child2;
	Node child3;
	Node grandChild1;
	
	@Before
	public void setUp()
	{
		root = new Node();
		child1 = new Node();
		child2 = new Node();
		child3 = new Node();
		grandChild1 = new Node();
	}
	
	@Test
	public final void testInit()
	{
		Position pos = new Position(1,2);
		
		root = new Node();
		assertEquals(root.getF(), 0);
		assertEquals(root.getG(), 0);
		assertEquals(root.getH(), 0);
		
		assertEquals(null, root.getChild());
		assertEquals(null, root.getChild());
		assertEquals(null, root.getPosition());
		
		child1 = new Node(pos);
		assertEquals(pos, child1.getPosition());
		
	}
	
	@Test
	public final void testEquals()
	{
		Position pos = new Position(1,2);
		Position pos2 = new Position(2,1);
		
		child1 = new Node(pos);
		child2 = new Node(pos2);
		child3 = new Node(new Position(1,2));
		
		assertFalse(child1 == child2);
		assertFalse(child1.equals(child2));
		assertEquals(child1, child3);
		assertTrue(child1.equals(child3));
		
		Position pos3 = new Position(10,23);
		child1.setPosition(pos3);
		
		assertFalse(child1.equals(child3));
	}
	
	@Test
	public final void testSetTree()
	{
		grandChild1.setParent(child1);
		child1.setParent(root);
		
		assertEquals(root, child1.getParent());
		assertEquals(grandChild1.getParent().getParent(), root);
	}
	
	@Test
	public final void testSortNode()
	{
		root.setF(40);
		child1.setF(30);
		child2.setF(1);
		child3.setF(25);
		
		List<Node> lNodes = new ArrayList<Node>();
		lNodes.add(root);
		lNodes.add(child1);
		lNodes.add(child2);
		lNodes.add(child3);
		
		assertEquals(lNodes.get(0), root);
		assertEquals(lNodes.get(1), child1);
		assertEquals(lNodes.get(2), child2);
		assertEquals(lNodes.get(3), child3);
		
		Collections.sort(lNodes);
		
		assertEquals(lNodes.get(0), child2);
		assertEquals(lNodes.get(1), child3);
		assertEquals(lNodes.get(2), child1);
		assertEquals(lNodes.get(3), root);
		
	}
	
}
