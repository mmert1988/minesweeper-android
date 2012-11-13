package de.moonstarlabs.android.minesweeper.game;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.util.Pair;
import android.widget.GridView;
import de.moonstarlabs.android.minesweeper.R;
import de.moonstarlabs.android.minesweeper.model.Field;
import de.moonstarlabs.android.minesweeper.model.RectangularField;
import de.moonstarlabs.android.minesweeper.widget.FieldAdapter;
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldView;

public class Game {
	private final Activity context;
	private Field field;
	
	public Game(Activity context, DifficultyLevel level) {
		this.context = context;
		initGame(level);
	}
	
	public Field getField() {
		return field;
	}
	
	private void initGame(DifficultyLevel level) {
		// TODO Auto-generated method stub
		Set<Pair<Integer, Integer>> mineCoords = new HashSet<Pair<Integer, Integer>>();
		mineCoords.add(new Pair<Integer, Integer>(0, 0));
		mineCoords.add(new Pair<Integer, Integer>(1, 1));
		mineCoords.add(new Pair<Integer, Integer>(2, 2));
		mineCoords.add(new Pair<Integer, Integer>(3, 3));
		mineCoords.add(new Pair<Integer, Integer>(4, 4));
		field = new RectangularField(5, 5, mineCoords);
		field.openCell(0);
		field.openCell(1);
		
		FieldAdapter adapter = new FieldAdapter(context, field);
		RectangularFieldView fieldView = (RectangularFieldView) context.findViewById(R.id.fieldView);
		fieldView.setStretchMode(GridView.NO_STRETCH);
		fieldView.setNumColumns(5);
		fieldView.setAdapter(adapter);
	}
	
	public static enum DifficultyLevel {
		EASY,
		MEDIUM,
		HARD
	}
	
}
