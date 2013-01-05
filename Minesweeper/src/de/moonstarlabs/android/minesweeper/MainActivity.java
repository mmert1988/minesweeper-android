package de.moonstarlabs.android.minesweeper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import de.moonstarlabs.android.minesweeper.game.ClickModeState;
import de.moonstarlabs.android.minesweeper.game.Game;
import de.moonstarlabs.android.minesweeper.game.Game.DifficultyLevel;
import de.moonstarlabs.android.minesweeper.game.Game.Status;
import de.moonstarlabs.android.minesweeper.game.GameListener;
import de.moonstarlabs.android.minesweeper.game.OpenCellModeState;
import de.moonstarlabs.android.minesweeper.game.ToggleMarkModeState;
import de.moonstarlabs.android.minesweeper.model.FieldListener;
import de.moonstarlabs.android.minesweeper.model.RectangularField;
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldAdapter;
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldAdapter.OnCellClickListener;
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldAdapter.OnCellLongClickListener;

/**
 * Die Haupt-Activity zur Darstellung des Spieldfeldes.
 */
public class MainActivity extends Activity implements OnClickListener, OnCellClickListener,
OnCellLongClickListener, GameListener, FieldListener {
    private static final ClickModeState OPEN_CELL_CLICK_MODE_STATE = new OpenCellModeState();
    private static final ClickModeState SET_FLAG_CLICK_MODE_STATE = new ToggleMarkModeState();
    private static final String EXTRA_GAME = "game";
    private static final String EXTRA_LAST_TIMER_BASE = "timerBase";
    private static final String PREF_KEY_LEVEL = "difficulty level";
    
    private Game game;
    private long lastTimerBase;
    private ClickModeState clickModeState = OPEN_CELL_CLICK_MODE_STATE;
    
    private TableLayout fieldView;
    private ImageButton newGameButton;
    private ImageButton switchClickModeButton;
    private TextView minesLeftView;
    private Chronometer secondsPastView;
    private Game.DifficultyLevel level = DifficultyLevel.EASY;
    
    private HorizontalScrollView hScroll;
    
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViews();
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        level = DifficultyLevel.valueOf(prefs.getString(PREF_KEY_LEVEL, DifficultyLevel.EASY.toString()));
        
        if (savedInstanceState != null) {
            game = savedInstanceState.getParcelable(EXTRA_GAME);
            lastTimerBase = savedInstanceState.getLong(EXTRA_LAST_TIMER_BASE);
        }
        else {
            game = new Game(level);
            lastTimerBase = SystemClock.elapsedRealtime();
        }
        
        secondsPastView.setBase(lastTimerBase);
        initViews(game);
        refreshFieldView(game);
        updateViewsOnStatusChange(game.getStatus());
    }
    
    private void setViews() {
        setContentView(R.layout.activity_main);
        
        switchClickModeButton = (ImageButton)findViewById(R.id.switchClickMode);
        newGameButton = (ImageButton)findViewById(R.id.newGameButton);
        minesLeftView = (TextView)findViewById(R.id.minesLeftView);
        secondsPastView = (Chronometer)findViewById(R.id.secondsPastView);
        
        fieldView = (TableLayout)findViewById(R.id.fieldView);
        hScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
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
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_GAME, game);
        outState.putLong(EXTRA_LAST_TIMER_BASE, lastTimerBase);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putString(PREF_KEY_LEVEL, level.toString());
        prefEdit.commit();
    }
    
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                return true;
            case R.id.menu_difficulty_easy:
                level = DifficultyLevel.EASY;
                initNewGame(level);
                return true;
            case R.id.menu_difficulty_medium:
                level = DifficultyLevel.MEDIUM;
                initNewGame(level);
                return true;
            case R.id.menu_difficulty_hard:
                level = DifficultyLevel.HARD;
                initNewGame(level);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.switchClickMode:
                clickModeState.switchClickMode(this);
                break;
                
            case R.id.newGameButton:
                initNewGame(level);
                break;
            default:
                break;
        }
    }
    
    /**
     * setzt Klick-Modus zum Öffnen.
     */
    public void setOpenCellMode() {
        clickModeState = OPEN_CELL_CLICK_MODE_STATE;
        switchClickModeButton.setImageResource(R.drawable.mine);
    }
    
    /**
     * setzt Klick-Modus zum setzen von Fahnen.
     */
    public void setToggleMarkMode() {
        clickModeState = SET_FLAG_CLICK_MODE_STATE;
        switchClickModeButton.setImageResource(R.drawable.flag);
    }
    
    @Override
    public void onItemLongClick(final View item, final int position) {
        clickModeState.longClickOn(game, position);
    }
    
    @Override
    public void onItemClick(final View item, final int position) {
        clickModeState.clickOn(game, position);
    }
    
    @Override
    public void onGameStatusChanged(final Status status) {
        updateViewsOnStatusChange(status);
    }
    
    @Override
    public void onMinesLeftChanged(final int minesLeft) {
        minesLeftView.setText(String.valueOf(minesLeft));
    }
    
    @Override
    public void onFieldChanged() {
        refreshFieldView(game);
    }
    
    private void initNewGame(final DifficultyLevel level) {
        game = new Game(level);
        initViews(game);
        refreshFieldView(game);
        updateViewsOnStatusChange(game.getStatus());
    }
    
    private void initViews(final Game g) {
        g.addListener(this);
        g.getField().addListener(this);
        minesLeftView.setText(String.valueOf(g.getMinesLeft()));
    }
    
    private void refreshFieldView(final Game g) {
        hScroll.post(new Runnable() {
            @Override
            public void run() {
                RectangularFieldAdapter adapter = new RectangularFieldAdapter(MainActivity.this, (RectangularField)g.getField());
                adapter.setOnCellClickListener(MainActivity.this);
                adapter.setOnCellLongClickListener(MainActivity.this);
                hScroll.removeAllViews();
                fieldView = createView(adapter);
                hScroll.addView(fieldView);
            }
        });
    }
    
    private TableLayout createView(final RectangularFieldAdapter adapter) {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableLayout table = (TableLayout)inflater.inflate(R.layout.rectangular_field_view, null);
        
        for (int i = 0; i < adapter.getRows(); i++) {
            TableRow row = (TableRow)inflater.inflate(R.layout.cell_row, null);
            row.setGravity(Gravity.CENTER_HORIZONTAL);
            table.addView(row);
        }
        int position = 0;
        for (int i = 0; i < adapter.getRows(); i++) {
            TableRow row = (TableRow)table.getChildAt(i);
            for (int j = 0; j < adapter.getColumns(); j++) {
                View child = adapter.getView(position, null, null);
                child.measure(MeasureSpec.EXACTLY, MeasureSpec.EXACTLY);
                row.addView(child);
                position++;
            }
        }
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        table.setLayoutParams(params);
        return table;
    }
    
    private void updateViewsOnStatusChange(final Game.Status status) {
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
                newGameButton.setImageResource(R.drawable.game_won);
                secondsPastView.stop();
                break;
            default:
                break;
        }
    }
    
}
