package de.moonstarlabs.android.minesweeper.widget;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import de.moonstarlabs.android.minesweeper.R;
import de.moonstarlabs.android.minesweeper.model.Cell;
import de.moonstarlabs.android.minesweeper.model.Field;

public class FieldAdapter implements ListAdapter {
	private Context mContext;
	private final Field mField;
	private Set<DataSetObserver> dataSetObserver = new HashSet<DataSetObserver>();

	public FieldAdapter(Context context, Field field) {
		mContext = context;
		mField = field;
	}

	@Override
	public int getCount() {
		return mField.getCountCells();
	}

	@Override
	public Object getItem(int position) {
		return mField.getCell(position);
	}

	@Override
	public long getItemId(int position) {
		return mField.getCell(position).hashCode();
	}

	@Override
	public int getItemViewType(int position) {
		return IGNORE_ITEM_VIEW_TYPE;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Cell cell = mField.getCell(position);
		View view =  convertView;
		
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.cell, null);
		}
		
		ImageView button = (ImageView) view.findViewById(R.id.cellView);
		
		if (cell.isMarked()) {
			button.setImageResource(R.drawable.flag);
		} else if (cell.isOpen()) {
			button.setBackgroundResource(R.drawable.cell_background_pressed);
			if (cell.isMined()) {
				button.setImageResource(R.drawable.mine);
			} else {
				int minesInNeighbours = mField.calculateMinedNeighbours(position);
				if (minesInNeighbours > 0) {
					// TODO
				}
			}
		}

		return view;
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
	public void registerDataSetObserver(DataSetObserver observer) {
		dataSetObserver.add(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		dataSetObserver.remove(observer);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}
	
	public Field getField() {
		return mField;
	}

}
