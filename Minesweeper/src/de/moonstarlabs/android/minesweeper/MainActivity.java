package de.moonstarlabs.android.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Chronometer;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import de.moonstarlabs.android.minesweeper.game.ClickModeState;
import de.moonstarlabs.android.minesweeper.game.Game;
import de.moonstarlabs.android.minesweeper.game.Game.DifficultyLevel;
import de.moonstarlabs.android.minesweeper.game.Game.Status;
import de.moonstarlabs.android.minesweeper.game.GameListener;
import de.moonstarlabs.android.minesweeper.game.OpenCellModeState;
import de.moonstarlabs.android.minesweeper.game.ToggleMarkModeState;
import de.moonstarlabs.android.minesweeper.model.RectangularField;
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldAdapter;
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldView2;
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldView2.OnItemClickListener;
import de.moonstarlabs.android.minesweeper.widget.RectangularFieldView2.OnItemLongClickListener;

/**
 * Die Haupt-Activity zur Darstellung des Spieldfeldes.
 */
public class MainActivity extends Activity implements OnClickListener, OnItemClickListener,
OnItemLongClickListener, GameListener, OnTouchListener {
    private static final ClickModeState OPEN_CELL_CLICK_MODE_STATE = new OpenCellModeState();
    private static final ClickModeState SET_FLAG_CLICK_MODE_STATE = new ToggleMarkModeState();
    private static final String EXTRA_GAME = "game";
    private static final String EXTRA_LAST_TIMER_BASE = "timerBase";
    
    private Game game;
    private long lastTimerBase;
    private ClickModeState clickModeState;
    
    private RectangularFieldView2 fieldView;
    private ImageButton newGameButton;
    private ImageButton switchClickModeButton;
    private TextView minesLeftView;
    private Chronometer secondsPastView;
    private final Game.DifficultyLevel level = DifficultyLevel.EASY;
    
    private float mx;
    private float my;
    private float curX;
    private float curY;
    
    private ScrollView vScroll;
    private HorizontalScrollView hScroll;
    
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        switchClickModeButton = (ImageButton)findViewById(R.id.switchClickMode);
        newGameButton = (ImageButton)findViewById(R.id.newGameButton);
        minesLeftView = (TextView)findViewById(R.id.minesLeftView);
        secondsPastView = (Chronometer)findViewById(R.id.secondsPastView);
        
        fieldView = (RectangularFieldView2)findViewById(R.id.fieldView);
        fieldView.setOnItemClickListener(this);
        fieldView.setOnItemLongClickListener(this);
        //        fieldView.setStretchMode(GridView.NO_STRETCH);
        //        fieldView.setNumColumns(3);
        
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
        updateViewsOnStatusChange(game.getStatus());
        
        vScroll = (ScrollView)findViewById(R.id.verticalScroll);
        hScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
        vScroll.setOnTouchListener(this);
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
    }
    
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.switchClickMode:
                clickModeState.switchClickMode(this);
                break;
                
            case R.id.newGameButton:
                game = new Game(level);
                initViews(game);
                updateViewsOnStatusChange(game.getStatus());
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
    public void onCellLongClick(final View item, final int position) {
        clickModeState.longClickOn(game, position);
    }
    
    @Override
    public void onCellClick(final View item, final int position) {
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
    
    private void initViews(final Game g) {
        g.addListener(this);
        minesLeftView.setText(String.valueOf(g.getMinesLeft()));
        RectangularFieldAdapter adapter = new RectangularFieldAdapter(this, (RectangularField)g.getField());
        fieldView.setAdapter(adapter);
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
    
    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        Log.i("MainActivity", "onTouch");
        switch (event.getAction()) {
            
            case MotionEvent.ACTION_DOWN:
                mx = event.getX();
                my = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                curX = event.getX();
                curY = event.getY();
                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                mx = curX;
                my = curY;
                break;
            case MotionEvent.ACTION_UP:
                curX = event.getX();
                curY = event.getY();
                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                break;
            default:
                break;
        }
        
        return false;
    }
    
}
