package com.codepath.simpletodo;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;

/**
 * Simple class to display error dialog to user on empty to-do item name
 *
 * Created by vmiha on 9/5/16.
 */
public class InputCheckerUtil {

    /**
     * Displays an error dialog
     * @param activity - the activity for which to display the dialog
     */
    public static void displayEmptyInputErrorMsg(AppCompatActivity activity){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(activity);

        dlgAlert.setMessage("Please provide a name for to-do item");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }
}
