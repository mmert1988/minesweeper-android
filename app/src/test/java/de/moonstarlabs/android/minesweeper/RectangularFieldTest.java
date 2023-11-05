package de.moonstarlabs.android.minesweeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import de.moonstarlabs.android.minesweeper.model.Field;
import de.moonstarlabs.android.minesweeper.model.RectangularField;

public class RectangularFieldTest {
	Field field;

	@Before
	public void setUp() {
		Set<Pair<Integer, Integer>> mineCoords = new HashSet<>();
		mineCoords.add(new Pair<>(1, 1));
		mineCoords.add(new Pair<>(2, 2));
		field = new RectangularField(3, 3, mineCoords);
	}

	@Test
	public void testGetCountCells() {
		assertEquals(9, field.getCellsCount());
	}

	@Test
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

	@Test
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
