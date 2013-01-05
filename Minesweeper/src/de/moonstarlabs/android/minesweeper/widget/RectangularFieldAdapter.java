package de.moonstarlabs.android.minesweeper.widget;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import de.moonstarlabs.android.minesweeper.R;
import de.moonstarlabs.android.minesweeper.model.Cell;
import de.moonstarlabs.android.minesweeper.model.RectangularField;

/**
 * Adapter-Erweiterung, die ein Objekt der Field-Klasse einem AdapterView
 * anpasst.
 */
public class RectangularFieldAdapter extends BaseAdapter implements Observer {
    /**
     * Workaround für einen Bug in GridView, weswegen die Standard-Listener für
     * Click und LongClick nicht funktionieren.
     */
    public interface OnCellClickListener {
        /**
         * Workaround-Implementierung.
         *
         * @param item
         *            Item
         * @param position
         *            Position
         */
        void onItemClick(View item, int position);
    }
    
    /**
     * Workaround für einen Bug in GridView, weswegen die Standard-Listener für
     * Click und LongClick nicht funktionieren.
     */
    public interface OnCellLongClickListener {
        /**
         * Workaround-Implementierung.
         *
         * @param item
         *            Item
         * @param position
         *            Position
         */
        void onItemLongClick(View item, int position);
    }
    
    private final Context mContext;
    private final RectangularField mField;
    private OnCellClickListener onCellClickListener;
    private OnCellLongClickListener onCellLongClickListener;
    private final Handler changeHandler = new Handler();
    
    /**
     * Creates a new instance of {@link RectangularFieldAdapter}.
     * 
     * @param context
     *            Application Context
     * @param field
     *            Objekt für Feld
     */
    public RectangularFieldAdapter(final Context context, final RectangularField field) {
        mContext = context;
        mField = field;
        mField.registerObserver(new ContentObserver(changeHandler) {
            @Override
            public void onChange(final boolean selfChange) {
                super.onChange(selfChange);
                notifyDataSetChanged();
            }
        });
    }
    
    @Override
    public int getCount() {
        return mField.getCellsCount();
    }
    
    @Override
    public Object getItem(final int position) {
        return mField.getCell(position);
    }
    
    @Override
    public long getItemId(final int position) {
        return mField.getCell(position).hashCode();
    }
    
    @Override
    public int getItemViewType(final int position) {
        return IGNORE_ITEM_VIEW_TYPE;
    }
    
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        Cell cell = mField.getCell(position);
        View view = convertView;
        
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cell, null);
        }
        
        View cellView = view.findViewById(R.id.cellView);
        if (!(cellView instanceof ImageView)) {
            throw new AssertionError();
        }
        
        ImageView button = (ImageView)cellView;
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (onCellClickListener != null) {
                    onCellClickListener.onItemClick(v, position);
                }
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                if (onCellLongClickListener != null) {
                    onCellLongClickListener.onItemLongClick(v, position);
                    return true;
                }
                return false;
            }
        });
        
        if (cell.isMarked()) {
            button.setImageResource(R.drawable.flag);
        }
        else if (cell.isOpen()) {
            button.setBackgroundResource(R.drawable.cell_background_pressed);
            if (cell.isMined()) {
                button.setImageResource(R.drawable.mine);
            }
            else {
                int minesInNeighbours = mField.calculateMinedNeighbours(position);
                if (minesInNeighbours > 0) {
                    setNumber(minesInNeighbours, button);
                }
            }
        }
        else if (cell.isSuspect()) {
            button.setImageResource(R.drawable.suspect);
        }
        
        return view;
    }
    
    private void setNumber(final int minesInNeighbours, final ImageView button) {
        switch (minesInNeighbours) {
            case 1:
                button.setImageResource(R.drawable.one);
                break;
            case 2:
                button.setImageResource(R.drawable.two);
                break;
            case 3:
                button.setImageResource(R.drawable.three);
                break;
            case 4:
                button.setImageResource(R.drawable.four);
                break;
            case 5:
                button.setImageResource(R.drawable.five);
                break;
            case 6:
                button.setImageResource(R.drawable.six);
                break;
            case 7:
                button.setImageResource(R.drawable.seven);
                break;
            case 8:
                button.setImageResource(R.drawable.eigth);
                break;
            default:
                return;
        }
    }
    
    @Override
    public int getViewTypeCount() {
        return 1;
    }
    
    @Override
    public boolean hasStableIds() {
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }
    
    @Override
    public boolean isEnabled(final int position) {
        return true;
    }
    
    /**
     * Accessor für die Anzahl der Zeilen.
     * 
     * @return Anzahl der Zeilen
     */
    public int getRows() {
        return mField.getRows();
    }
    
    /**
     * Accessor für die Anzahl der Spalten.
     * 
     * @return Anzahl der Spalten
     */
    public int getColumns() {
        return mField.getColumns();
    }
    
    @Override
    public void update(final Observable observable, final Object data) {
        notifyDataSetChanged();
    }
    
    /**
     * Workaround für Android-Bug: setzt den OnItemClickListener.
     *
     * @param listener
     *            OnItemClickListener
     */
    public void setOnCellClickListener(final OnCellClickListener listener) {
        onCellClickListener = listener;
    }
    
    /**
     * Workaround für Android-Bug: setzt den OnItemLongClickListener.
     *
     * @param listener
     *            OnItemLongClickListener
     */
    public void setOnCellLongClickListener(final OnCellLongClickListener listener) {
        onCellLongClickListener = listener;
    }
    
}
