package de.moonstarlabs.android.minesweeper.model;

import java.util.Observable;

public class Cell extends Observable {
	
	private boolean isMined;
	private boolean isMarked = false;
	private boolean isOpen = false;
	
	Cell(boolean isMined) {
		this.isMined = isMined;
	}
	
	void open() {
		isOpen = true;
		notifyObservers();
	}
	
	void mark() {
		isMarked = true;
		notifyObservers();
	}
	
	void unmark() {
		isMarked = false;
		notifyObservers();
	}
	
	public boolean isMined() {
		return isMined;
	}
	
	public boolean isMarked() {
		return isMarked;
	}
	
	public boolean isOpen() {
		return isOpen;
	}

}
