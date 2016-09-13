package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

/**
 * Main activity functionality
 * Created by vmiha on 9/5/16.
 */
public class MainActivity extends AppCompatActivity {

    //ArrayList<String> items;
    //ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 20;
    List<Todo> todos;
    TodoAdapter todoAdapter;

    /**
     * Set up the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);

        // read the saved items from a data source
        readItems();

        //populateSampleItems();
        //itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        //lvItems.setAdapter(itemsAdapter);

        todoAdapter = new TodoAdapter(this, todos);
        setupListViewListener();
        lvItems.setAdapter(todoAdapter);
        setupEditListViewListener();
    }

    /**
     * Populates a default item list - not used as items read from file

    public void populateSampleItems(){
        items = new ArrayList<String>();
        items.add("Item 1");
        items.add("Item 2");
        items.add("Item 3");
    }
    */

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

            todoAdapter.add(new Todo(itemText, Todo.Priority.LOW, "date") );
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
                todos.remove(pos);
                todos.add(pos, new Todo(editItemText, Todo.Priority.LOW, "date"));
                todoAdapter.notifyDataSetChanged();
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
                todos.remove(pos);
                todoAdapter.notifyDataSetChanged();
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
                Todo todo = (Todo)parent.getItemAtPosition(pos);

                // pass the edited item to the edit activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("editItemText", todo.getName());
                i.putExtra("pos", pos);

                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    /**
     * Read the saved items from SQL lite
     *
     * uncomment the code to write to a file
     */
    private void readItems() {

        TodoItemDatabase db = TodoItemDatabase.getInstance(this);
        todos = db.getAllTodos();

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

        for (Todo todo : todos) {
            db.addTODO(todo);
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