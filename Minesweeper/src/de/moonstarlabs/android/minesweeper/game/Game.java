package de.moonstarlabs.android.minesweeper.game;

import java.util.HashSet;
import java.util.Set;

import android.util.Pair;
import android.widget.GridView;
import de.moonstarlabs.android.minesweeper.MainActivity;
import de.moonstarlabs.android.minesweeper.R;
import de.moonstarlabs.android.minesweeper.model.Field;
import de.moonstarlabs.android.minesweeper.model.RectangularField;
import de.moonstarlabs.android.minesweeper.widget.FieldAdapter;
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldView;

public class Game {
	private final MainActivity activity;
	private Field field;
	
	public Game(MainActivity context, DifficultyLevel level) {
		this.activity = context;
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
		
		FieldAdapter adapter = new FieldAdapter(activity, field);
		adapter.setOnItemClickListener(activity);
		adapter.setOnItemLongClickListener(activity);
		RectangularFieldView fieldView = (RectangularFieldView) activity.findViewById(R.id.fieldView);
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
