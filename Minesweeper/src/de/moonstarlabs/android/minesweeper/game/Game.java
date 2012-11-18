package de.moonstarlabs.android.minesweeper.game;

import java.util.HashSet;
import java.util.Set;

import android.os.SystemClock;
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
	private Status status = Status.RUNNING;
	private final long startMillis;
	
	private Set<GameListener> listeners = new HashSet<GameListener>();
	
	public Game(MainActivity context, DifficultyLevel level) {
		this.activity = context;
		initGame(level);
		startMillis = SystemClock.elapsedRealtime();
	}
	
	public Status getStatus() {
		return status;
	}
	
	public int getMinesLeft() {
		return computeMinesLeft();
	}
	
	public long getStartMillis() {
		return startMillis;
	}
	
	public void openCell(int position) {
		if (status != Status.RUNNING) {
			return;
		}
		
		if (field.getCell(position).isMined()) {
			field.openAllMinedCells();
			status = Status.LOST;
			
			for (GameListener listener: listeners) {
				listener.onGameStatusChanged(status);
			}
			return;
		}
		
		field.openCell(position);
		
		if (isGameWon()) {
			status = Status.WON;
			
			for (GameListener listener: listeners) {
				listener.onGameStatusChanged(status);
			}
		}
	}
	
	public void toggleMarkCell(int position) {
		if (status != Status.RUNNING) {
			return;
		}
		
		if (!field.getCell(position).isMarked()) {
			field.markCell(position);
		} else {
			field.unmarkCell(position);
		}
		
		for (GameListener listener: listeners) {
			listener.onMinesLeftChanged(computeMinesLeft());
		}
		
		if (isGameWon()) {
			status = Status.WON;
			
			for (GameListener listener: listeners) {
				listener.onGameStatusChanged(status);
			}
		}
	}
	
	public void addListener(GameListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(GameListener listener) {
		listeners.remove(listener);
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
		
		FieldAdapter adapter = new FieldAdapter(activity, field);
		adapter.setOnItemClickListener(activity);
		adapter.setOnItemLongClickListener(activity);
		RectangularFieldView fieldView = (RectangularFieldView) activity.findViewById(R.id.fieldView);
		fieldView.setStretchMode(GridView.NO_STRETCH);
		fieldView.setNumColumns(5);
		fieldView.setAdapter(adapter);
	}
	
	private boolean isGameWon() {
		return field.getOpenedCellsCount() + field.getMarkedCellsCount() == field.getCellsCount() &&
				field.getMarkedCellsCount() == field.getMinedCellsCount();
	}
	
	private int computeMinesLeft() {
		return field.getMinedCellsCount() - field.getMarkedCellsCount();
	}
	
	public static enum DifficultyLevel {
		EASY,
		MEDIUM,
		HARD
	}
	
	public static enum Status {
		RUNNING,
		WON,
		LOST
	}
	
}
