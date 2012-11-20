package de.moonstarlabs.android.minesweeper.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Cell implements Parcelable {
	
	private final boolean isMined;
	private boolean isMarked = false;
	private boolean isOpen = false;
	
	Cell(boolean isMined) {
		this.isMined = isMined;
	}
	
	void open() {
		isOpen = true;
	}
	
	void mark() {
		isMarked = true;
	}
	
	void unmark() {
		isMarked = false;
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(String.valueOf(isMined));
		out.writeString(String.valueOf(isMarked));
		out.writeString(String.valueOf(isOpen));
	}
	
	public static final Parcelable.Creator<Cell> CREATOR = new Creator<Cell>() {
		
		@Override
		public Cell[] newArray(int size) {
			return new Cell[size];
		}
		
		@Override
		public Cell createFromParcel(Parcel in) {
			return new Cell(in);
		}
	};
	
	private Cell(Parcel in) {
		this.isMined = Boolean.valueOf(in.readString());
		this.isMarked = Boolean.valueOf(in.readString());
		this.isOpen = Boolean.valueOf(in.readString());
	}

}
