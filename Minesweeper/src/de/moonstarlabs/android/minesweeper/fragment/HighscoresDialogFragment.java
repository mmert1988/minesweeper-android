package de.moonstarlabs.android.minesweeper.fragment;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import de.moonstarlabs.android.minesweeper.R;

/**
 * Dialog zum Anzeigen von Highscores.
 */
public class HighscoresDialogFragment extends DialogFragment {
    
    private static final String ARG_NAMES = "names";
    private static final String ARG_MILLIS = "millis";
    
    /**
     * Implementierung einer statischen newInstance-Methode als eine Best Practice, sowie von Android's Developer-Seite empfohlen wird.
     * @return new {@link HighscoresDialogFragment} intance
     * @param names Namen der Highscorer von easy bis hard
     * @param millis Millis Leistungen der Highscorer von easy bis hard
     */
    public static HighscoresDialogFragment newInstance(final String[] names, final long[] millis) {
        HighscoresDialogFragment instance = new HighscoresDialogFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_NAMES, names);
        args.putLongArray(ARG_MILLIS, millis);
        instance.setArguments(args);
        instance.setRetainInstance(true);
        return instance;
    }
    
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        String[] names = getArguments().getStringArray(ARG_NAMES);
        long[] millis = getArguments().getLongArray(ARG_MILLIS);
        
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.highscores_layout, null);
        TextView textView = (TextView)view.findViewById(R.id.easyHighscoreNameView);
        textView.setText(names[0]);
        textView = (TextView)view.findViewById(R.id.mediumHighscoreNameView);
        textView.setText(names[1]);
        textView = (TextView)view.findViewById(R.id.hardHighscoreNameView);
        textView.setText(names[2]);
        if (millis[0] < Long.MAX_VALUE) {
            textView = (TextView)view.findViewById(R.id.easyHighscoreTimeView);
            textView.setText(String.valueOf(millis[0]));
        }
        if (millis[1] < Long.MAX_VALUE) {
            textView = (TextView)view.findViewById(R.id.mediumHighscoreTimeView);
            textView.setText(String.valueOf(millis[1]));
        }
        if (millis[2] < Long.MAX_VALUE) {
            textView = (TextView)view.findViewById(R.id.hardHighscoreTimeView);
            textView.setText(String.valueOf(millis[2]));
        }
        
        Builder builder = new Builder(getActivity());
        builder.setTitle(R.string.dialog_highsores_title);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, null);
        return builder.create();
    }
    
}

