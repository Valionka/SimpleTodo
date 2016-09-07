package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by vmiha on 9/5/16.
 */
public class EditItemActivity extends AppCompatActivity {

    int pos;

    /**
     * Set up the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String editItemText = getIntent().getStringExtra("editItemText");
        pos = getIntent().getIntExtra("pos", -1);

        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(editItemText);
        int length = etEditItem.getText().length();
        etEditItem.setSelection(length);
    }

    /**
     * Handle the save event
     * @param v
     */
    public void onSaveItem(View v){
        EditText etName = (EditText) findViewById(R.id.etEditItem);

        if(etName.getText().toString().length() == 0) {
            InputCheckerUtil.displayEmptyInputErrorMsg(this);
        } else {
            Intent data = new Intent();
            data.putExtra("editItemText", etName.getText().toString());
            data.putExtra("pos", pos);

            setResult(RESULT_OK, data);
            finish();
        }
    }

    /**
     * Handle the cancel event
     * @param v
     */
    public void onCancel(View v){
        finish();
    }
}
