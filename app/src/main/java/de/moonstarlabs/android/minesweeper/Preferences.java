package de.moonstarlabs.android.minesweeper;

/**
 * Klasse für verschiedene Preferences-Konstanten.
 */
public final class Preferences {
    
    private Preferences() {
        throw new AssertionError();
    }
    
    /**
     * Konstante für Spiel-Schwierigkeit.
     */
    public static final String PREF_KEY_LEVEL = "difficulty level";
    
    /**
     * Konstante für highscore Zeit in easy.
     */
    public static final String PREF_EASY_HIGHSORE_MILLIS = "easyHighscoreMillis";
    
    /**
     * Konstante für highscore Zeit in medium.
     */
    public static final String PREF_MEDIUM_HIGHSORE_MILLIS = "mediumHighscoreMillis";
    
    /**
     * Konstante für highscore Zeit in hard.
     */
    public static final String PREF_HARD_HIGHSORE_MILLIS = "hardHighscoreMillis";
    
    /**
     * Konstante für highscore Name in easy.
     */
    public static final String PREF_EASY_HIGHSCORE_NAME = "easyHighscoreName";
    
    /**
     * Konstante für highscore Name in medium.
     */
    public static final String PREF_MEDIUM_HIGHSCORE_NAME = "mediumHighscoreName";
    
    /**
     * Konstante für highscore Name in hard.
     */
    public static final String PREF_HARD_HIGHSCORE_NAME = "hardHighscoreName";
}

