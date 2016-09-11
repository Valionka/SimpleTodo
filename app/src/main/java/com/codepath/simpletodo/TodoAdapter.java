package com.codepath.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vmiha on 9/10/16.
 */
public class TodoAdapter extends ArrayAdapter<Todo> {
    public TodoAdapter(Context context, List<Todo> todos) {
        super(context, 0, todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Todo todo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        // Populate the data into the template view using the data object
        tvName.setText(todo.name);
        tvPriority.setText(todo.priority.toString());
        tvDate.setText(todo.date);
        // Return the completed view to render on screen
        return convertView;
    }
}