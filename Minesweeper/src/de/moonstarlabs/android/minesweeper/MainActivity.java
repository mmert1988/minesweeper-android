package de.moonstarlabs.android.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import de.moonstarlabs.android.minesweeper.game.ClickModeState;
import de.moonstarlabs.android.minesweeper.game.Game;
import de.moonstarlabs.android.minesweeper.game.Game.Status;
import de.moonstarlabs.android.minesweeper.game.GameListener;
import de.moonstarlabs.android.minesweeper.game.OpenCellModeState;
import de.moonstarlabs.android.minesweeper.game.ToggleMarkModeState;
import de.moonstarlabs.android.minesweeper.widget.FieldAdapter;
import de.moonstarlabs.android.minesweeper.widget.FieldAdapter.OnItemClickListener;
import de.moonstarlabs.android.minesweeper.widget.FieldAdapter.OnItemLongClickListener;
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldView;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemLongClickListener,
		GameListener {
	private static final ClickModeState openCellClickModeState = new OpenCellModeState();
	private static final ClickModeState setFlagClickModeState = new ToggleMarkModeState();
	private static final String EXTRA_GAME = "game";
	private static final String EXTRA_LAST_TIMER_BASE = "timerBase";

	private Game game;
	private long lastTimerBase;
	private ClickModeState clickModeState;

	private RectangularFieldView fieldView;
	private ImageButton newGameButton;
	private ImageButton switchClickModeButton;
	private TextView minesLeftView;
	private Chronometer secondsPastView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		switchClickModeButton = (ImageButton) findViewById(R.id.switchClickMode);
		newGameButton = (ImageButton) findViewById(R.id.newGameButton);
		minesLeftView = (TextView) findViewById(R.id.minesLeftView);
		secondsPastView = (Chronometer) findViewById(R.id.secondsPastView);
		
		fieldView = (RectangularFieldView) findViewById(R.id.fieldView);
		fieldView.setStretchMode(GridView.NO_STRETCH);
		fieldView.setNumColumns(5);

		if (savedInstanceState != null) {
			game = savedInstanceState.getParcelable(EXTRA_GAME);
			lastTimerBase = savedInstanceState.getLong(EXTRA_LAST_TIMER_BASE);
		} else {
			game = new Game(null);
			lastTimerBase = SystemClock.elapsedRealtime();
		}

		secondsPastView.setBase(lastTimerBase);
		initViews(game);
		updateViewsOnStatusChange(game.getStatus());
	}

	@Override
	protected void onResume() {
		super.onResume();
		setOpenCellMode();
	}

	@Override
	protected void onPause() {
		super.onPause();
		secondsPastView.stop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(EXTRA_GAME, game);
		outState.putLong(EXTRA_LAST_TIMER_BASE, lastTimerBase);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switchClickMode:
			clickModeState.switchClickMode(this);
			break;

		case R.id.newGameButton:
			game = new Game(null);
			initViews(game);
			updateViewsOnStatusChange(game.getStatus());
			break;
		}
	}

	public void setOpenCellMode() {
		clickModeState = openCellClickModeState;
		switchClickModeButton.setImageResource(R.drawable.mine);
	}

	public void setToggleMarkMode() {
		clickModeState = setFlagClickModeState;
		switchClickModeButton.setImageResource(R.drawable.flag);
	}

	@Override
	public void onItemLongClick(View item, int position) {
		clickModeState.longClickOn(game, position);
	}

	@Override
	public void onItemClick(View item, int position) {
		clickModeState.clickOn(game, position);
	}

	@Override
	public void onGameStatusChanged(Status status) {
		updateViewsOnStatusChange(status);
	}

	@Override
	public void onMinesLeftChanged(int minesLeft) {
		minesLeftView.setText(String.valueOf(minesLeft));
	}

	private void initViews(Game game) {
		game.addListener(this);
		minesLeftView.setText(String.valueOf(game.getMinesLeft()));
		FieldAdapter adapter = new FieldAdapter(this, game.getField());
		adapter.setOnItemClickListener(this);
		adapter.setOnItemLongClickListener(this);
		fieldView.setAdapter(adapter);
	}

	private void updateViewsOnStatusChange(Game.Status status) {
		switch (status) {
		case NEW:
			secondsPastView.setBase(SystemClock.elapsedRealtime());
			secondsPastView.stop();
			newGameButton.setImageResource(R.drawable.new_game);
			break;
			
		case RUNNING:
			lastTimerBase = game.getStartMillis();
			secondsPastView.setBase(game.getStartMillis());
			secondsPastView.start();
			newGameButton.setImageResource(R.drawable.new_game);
			break;
			
		case LOST:
			secondsPastView.stop();
			newGameButton.setImageResource(R.drawable.game_over);
			break;
			
		case WON:
			newGameButton.setImageResource(R.drawable.game_over);
			secondsPastView.stop();
			break;
		}
	}

}
