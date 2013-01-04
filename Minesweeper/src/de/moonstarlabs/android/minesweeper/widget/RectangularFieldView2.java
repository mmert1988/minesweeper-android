package de.moonstarlabs.android.minesweeper.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import de.moonstarlabs.android.minesweeper.R;

/**
 * TODO: Document type RectangularFieldView2.
 */
public class RectangularFieldView2 extends TableLayout {
    /**
     * Workaround für einen Bug in GridView, weswegen die Standard-Listener für
     * Click und LongClick nicht funktionieren.
     */
    public interface OnItemClickListener {
        /**
         * Workaround-Implementierung.
         * 
         * @param item
         *            Item
         * @param position
         *            Position
         */
        void onCellClick(View item, int position);
    }
    
    /**
     * Workaround für einen Bug in GridView, weswegen die Standard-Listener für
     * Click und LongClick nicht funktionieren.
     */
    public interface OnItemLongClickListener {
        /**
         * Workaround-Implementierung.
         * 
         * @param item
         *            Item
         * @param position
         *            Position
         */
        void onCellLongClick(View item, int position);
    }
    
    private RectangularFieldAdapter adapter;
    private OnItemClickListener cellClickListener;
    private OnItemLongClickListener cellLongClickListener;
    private final Handler changeHandler = new Handler();
    
    /**
     * Creates a new instance of {@link RectangularFieldView2}.
     * 
     * @param context
     *            Context
     * @param attrs
     *            Attributes
     */
    public RectangularFieldView2(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }
    
    /**
     * Setter für Adapter.
     * 
     * @param adapter
     *            Adapter
     */
    public void setAdapter(final RectangularFieldAdapter adapter) {
        this.adapter = adapter;
        if (adapter != null) {
            adapter.registerObserver(new ContentObserver(changeHandler) {
                @Override
                public void onChange(final boolean selfChange) {
                    super.onChange(selfChange);
                    refreshCells();
                }
            });
        }
    }
    
    private void addRows() {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < adapter.getRows(); i++) {
            TableRow row = (TableRow)inflater.inflate(R.layout.cell_row, null);
            addView(row);
        }
    }
    
    private void refreshCells() {
        // TODO
    }
    
    /**
     * Getter für Adapter.
     * 
     * @return Adapter
     */
    public RectangularFieldAdapter getAdapter() {
        return adapter;
    }
    
    /**
     * Workaround für Android-Bug: setzt den OnItemClickListener.
     * 
     * @param listener
     *            OnItemClickListener
     */
    public void setOnItemClickListener(final OnItemClickListener listener) {
        cellClickListener = listener;
    }
    
    /**
     * Workaround für Android-Bug: setzt den OnItemLongClickListener.
     * 
     * @param listener
     *            OnItemLongClickListener
     */
    public void setOnItemLongClickListener(final OnItemLongClickListener listener) {
        cellLongClickListener = listener;
    }
    
    @Override
    protected void onLayout(final boolean changed, final int l, final int t, final int r,
            final int b) {
        super.onLayout(changed, l, t, r, b);
        
        if (adapter == null) {
            return;
        }
        
        addRows();
        int position = 0;
        for (int i = 0; i < adapter.getRows(); i++) {
            TableRow row = (TableRow)getChildAt(i);
            for (int j = 0; j < adapter.getColumns(); j++) {
                View child = adapter.getView(position, null, this);
                row.addView(child);
                child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                assignCellListeners(child.findViewById(R.id.cellView), position);
                position++;
            }
        }
    }
    
    private void assignCellListeners(final View cell, final int position) {
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (cellClickListener != null) {
                    cellClickListener.onCellClick(v, position);
                }
            }
        });
        cell.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                if (cellLongClickListener != null) {
                    cellLongClickListener.onCellLongClick(v, position);
                    return true;
                }
                return false;
            }
        });
    }
    
}
