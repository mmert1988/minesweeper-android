package de.moonstarlabs.android.minesweeper.game;

import java.util.HashSet;
import java.util.Set;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Pair;
import de.moonstarlabs.android.minesweeper.model.Cell;
import de.moonstarlabs.android.minesweeper.model.Field;
import de.moonstarlabs.android.minesweeper.model.RectangularField;

public class Game implements Parcelable {
	private Field field;
	private Status status = Status.NEW;
	private long startMillis;

	private Set<GameListener> listeners = new HashSet<GameListener>();

	public Game(DifficultyLevel level) {
		initGame(level);
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

	public Field getField() {
		return field;
	}

	public void openCell(int position) {
		if (status == Status.NEW) {
			start();
		}

		if (status != Status.RUNNING
				|| (field.getCell(position).isMarked() && field
						.calculateMinedNeighbours(position) > 0)) {
			return;
		}

		if (field.getCell(position).isMined()) {
			field.openAllMinedCells();
			status = Status.LOST;

			for (GameListener listener : listeners) {
				listener.onGameStatusChanged(status);
			}
			return;
		}

		if (field.getCell(position).isOpen()
				&& field.calculateMinedNeighbours(position) > 0) {
			if (!field.openUnmarkedNeighbours(position)) {
				field.openAllMinedCells();
				status = Status.LOST;
			}
		} else {
			field.openCell(position);
		}

		if (isGameWon()) {
			status = Status.WON;

			for (GameListener listener : listeners) {
				listener.onGameStatusChanged(status);
			}
		}
	}

	public void toggleMarkCell(int position) {
		if (status == Status.NEW) {
			start();
		}

		if (status != Status.RUNNING) {
			return;
		}

		if (field.getCell(position).isOpen() && field.calculateMinedNeighbours(position) > 0) {
			if (!field.openUnmarkedNeighbours(position)) {
				field.openAllMinedCells();
				status = Status.LOST;
			}
		} else {
			if (!field.getCell(position).isMarked()) {
				field.markCell(position);
			} else {
				field.unmarkCell(position);
			}
			
			for (GameListener listener : listeners) {
				listener.onMinesLeftChanged(computeMinesLeft());
			}
		}

		if (isGameWon()) {
			status = Status.WON;

			for (GameListener listener : listeners) {
				listener.onGameStatusChanged(status);
			}
		}
	}

	public void toggleSuspectCell(int position) {
		if (status == Status.NEW) {
			start();
		}

		Cell cell = field.getCell(position);
		if (status != Status.RUNNING || cell.isOpen() || cell.isMarked()) {
			return;
		}

		if (cell.isSuspect()) {
			field.unsuspect(position);
		} else {
			field.suspect(position);
		}
	}

	public void addListener(GameListener listener) {
		listeners.add(listener);
	}

	public void removeListener(GameListener listener) {
		listeners.remove(listener);
	}

	private void initGame(DifficultyLevel level) {
		Set<Pair<Integer, Integer>> mineCoords = new HashSet<Pair<Integer, Integer>>();
		mineCoords.add(new Pair<Integer, Integer>(0, 0));
		mineCoords.add(new Pair<Integer, Integer>(1, 1));
		mineCoords.add(new Pair<Integer, Integer>(2, 2));
		mineCoords.add(new Pair<Integer, Integer>(3, 3));
		mineCoords.add(new Pair<Integer, Integer>(4, 4));
		mineCoords.add(new Pair<Integer, Integer>(5, 5));
		field = new RectangularField(6, 6, mineCoords);
		// field = RectangularField.random(6, 6, 10);
	}

	private boolean isGameWon() {
		return (field.getOpenedCellsCount() == field.getCellsCount()-field.getMinedCellsCount());
	}

	private int computeMinesLeft() {
		return field.getMinedCellsCount() - field.getMarkedCellsCount();
	}

	private void start() {
		startMillis = SystemClock.elapsedRealtime();
		status = Status.RUNNING;
		for (GameListener listener : listeners) {
			listener.onGameStatusChanged(status);
		}
	}

	public static enum DifficultyLevel {
		EASY, MEDIUM, HARD
	}

	public static enum Status {
		NEW, RUNNING, WON, LOST
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeParcelable(field, flags);
		out.writeString(status.toString());
		out.writeLong(startMillis);
	}

	public static final Parcelable.Creator<Game> CREATOR = new Creator<Game>() {
		@Override
		public Game[] newArray(int size) {
			return new Game[size];
		}

		@Override
		public Game createFromParcel(Parcel in) {
			return new Game(in);
		}
	};

	private Game(Parcel in) {
		field = in.readParcelable(Field.class.getClassLoader());
		status = Status.valueOf(in.readString());
		startMillis = in.readLong();
	}

}
