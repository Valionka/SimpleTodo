package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Main activity functionality
 * Created by vmiha on 9/5/16.
 */
public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 20;

    /**
     * Set up the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        //populateSampleItems();
        // read the saved items from a file
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        setupListViewListener();
        lvItems.setAdapter(itemsAdapter);

        setupEditListViewListener();
    }

    /**
     * Populates a default item list - not used as items read from file
     */
    public void populateSampleItems(){
        items = new ArrayList<String>();
        items.add("Item 1");
        items.add("Item 2");
        items.add("Item 3");
    }

    /**
     * Adds an item to the list
     * @param v
     */
    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        if(itemText.length() == 0) {
            InputCheckerUtil.displayEmptyInputErrorMsg(this);
        } else {
            itemsAdapter.add(itemText);
            etNewItem.setText("");
            writeItems();
        }
    }

    /**
     * Processes result from Edit activity
     *
     * @param requestCode - the request code to edit activitys
     * @param resultCode - result code set by edit activity
     * @param data - data passed from edit activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String editItemText = data.getExtras().getString("editItemText");
            int pos = data.getExtras().getInt("pos", -1);

            if(pos != -1) {
                items.remove(pos);
                items.add(pos, editItemText);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
            }
        }
    }

    /**
     *  Setting up the long click listener to handle delete events
     */
    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,
                                           View item, int pos, long id) {
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    /**
     * Setting up the  click listener to handle edit events
     */
    private void setupEditListViewListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id){

                // get the edited item
                String editItemText = (String) parent.getItemAtPosition(pos);

                // pass the edited item to the edit activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("editItemText", editItemText);
                i.putExtra("pos", pos);

                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    /**
     * Read the saved items from SQL lite
     *
     * uncomment the code to write to SQL lite
     */
    private void readItems() {

        TodoItemDatabase db = TodoItemDatabase.getInstance(this);
        List<Todo> todos = db.getAllTodos();
        items = new ArrayList<>();
        for (Todo todo :
                todos) {
            items.add(todo.getName());
        }

        /*
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e){
            items = new ArrayList<>();
        }
        */
    }

    /**
     * Write the saved items to SQL lite
     *
     * uncomment the code to write to a file
     */
    private void writeItems() {

        TodoItemDatabase db = TodoItemDatabase.getInstance(this);
        db.deleteAllTodos();

        for (String item :
                items) {
            Todo newTodo = new Todo(item, 1);
            db.addTODO(newTodo);

        }

       /* File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

}