package de.moonstarlabs.android.minesweeper.test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import android.util.Pair;
import de.moonstarlabs.android.minesweeper.model.Field;
import de.moonstarlabs.android.minesweeper.model.RectangularField;

public class RectangularFieldTest extends TestCase {
	Field field;
	
	public void setUp() {
		Set<Pair<Integer, Integer>> mineCoords = new HashSet<Pair<Integer, Integer>>();
		mineCoords.add(new Pair<Integer, Integer>(1, 1));
		mineCoords.add(new Pair<Integer, Integer>(2, 2));
		field = new RectangularField(3, 3, mineCoords);
	}

	public void testGetCountCells() {
		assertEquals(9, field.getCountCells());
	}
	
	public void testGetCell() {
		assertFalse(field.getCell(0).isMined());
		assertFalse(field.getCell(1).isMined());
		assertFalse(field.getCell(2).isMined());
		assertFalse(field.getCell(3).isMined());
		assertTrue(field.getCell(4).isMined());
		assertFalse(field.getCell(5).isMined());
		assertFalse(field.getCell(6).isMined());
		assertFalse(field.getCell(7).isMined());
		assertTrue(field.getCell(8).isMined());
	}
	
	public void testCalculateMinedNeighbours() {
		assertEquals(1, field.calculateMinedNeighbours(0));
		assertEquals(1, field.calculateMinedNeighbours(1));
		assertEquals(1, field.calculateMinedNeighbours(2));
		assertEquals(1, field.calculateMinedNeighbours(3));
		assertEquals(2, field.calculateMinedNeighbours(5));
		assertEquals(1, field.calculateMinedNeighbours(6));
		assertEquals(2, field.calculateMinedNeighbours(7));
	}
	
}
