package de.moonstarlabs.android.minesweeper.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model-Klasse für einzelne Zelle, die ganz einfache Informationen speichert.
 */
public class Cell implements Parcelable {
    
    private final boolean isMined;
    private boolean isMarked;
    private boolean isOpen;
    private boolean isSuspect;
    
    Cell(final boolean isMined) {
        this.isMined = isMined;
        isMarked = false;
        isOpen = false;
        isSuspect = false;
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
    
    void suspect() {
        isSuspect = true;
    }
    
    void unSuspect() {
        isSuspect = false;
    }
    
    /**
     * Gibt zurück, ob die Zelle eine Mine enthält.
     * @return ob die Zelle eine Mine enthält
     */
    public boolean isMined() {
        return isMined;
    }
    
    /**
     * Gibt zurück, ob die Zelle mit einer Fahne markiert ist.
     * @return ob die Zelle mit einer Fahne markiert ist.
     */
    public boolean isMarked() {
        return isMarked;
    }
    
    /**
     * Gibt zurück, ob die Zelle geöffnet ist.
     * @return ob die Zelle geöffnet ist.
     */
    public boolean isOpen() {
        return isOpen;
    }
    
    /**
     * Gibt zurück, ob die Zelle verdächtigt ist.
     * @return ob die Zelle verdächtigt ist
     */
    public boolean isSuspect() {
        return isSuspect;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(final Parcel out, final int flags) {
        out.writeString(String.valueOf(isMined));
        out.writeString(String.valueOf(isMarked));
        out.writeString(String.valueOf(isOpen));
        out.writeString(String.valueOf(isSuspect));
    }
    
    /**
     * Standard-Implementierung von Parcelable-Creator nach der Developer-Doku
     * von Android.
     */
    public static final Parcelable.Creator<Cell> CREATOR = new Creator<Cell>() {
        @Override
        public Cell[] newArray(final int size) {
            return new Cell[size];
        }
        @Override
        public Cell createFromParcel(final Parcel in) {
            return new Cell(in);
        }
    };
    
    private Cell(final Parcel in) {
        isMined = Boolean.valueOf(in.readString());
        isMarked = Boolean.valueOf(in.readString());
        isOpen = Boolean.valueOf(in.readString());
        isSuspect = Boolean.valueOf(in.readString());
    }
    
}
