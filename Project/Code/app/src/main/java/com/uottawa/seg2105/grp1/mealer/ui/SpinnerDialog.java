package com.uottawa.seg2105.grp1.mealer.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

//From quinestor over at:
//https://stackoverflow.com/questions/12841803/best-way-to-show-a-loading-progress-indicator

public class SpinnerDialog extends DialogFragment {

    public SpinnerDialog() {
        // use empty constructors. If something is needed use onCreate's
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        ProgressDialog _dialog = new ProgressDialog(getActivity());
        this.setStyle(STYLE_NO_TITLE, getTheme()); // You can use styles or inflate a view
        _dialog.setMessage("Loading..."); // set your messages if not inflated from XML

        _dialog.setCancelable(false);

        return _dialog;
    }
}