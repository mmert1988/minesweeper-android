package de.moonstarlabs.android.minesweeper.model;

public class Cell {
	
	private boolean isMined;
	private boolean isMarked = false;
	private boolean isOpen = false;
	
	Cell(boolean isMined) {
		this.isMined = isMined;
	}
	
	public boolean isMined() {
		return isMined;
	}
	
	public void mark() {
		isMarked = true;
	}
	
	public void unmark() {
		isMarked = false;
	}
	
	public boolean isMarked() {
		return isMarked;
	}
	
	public void open() {
		isOpen = true;
	}
	
	public boolean isOpen() {
		return isOpen;
	}

}
