package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items;
    Button buttonAdd;
    EditText etItems;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;
    String test;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = findViewById(R.id.buttonAdd);
        etItems = findViewById(R.id.etItems);
        rvItems = findViewById(R.id.rvItems);
        loadItems();



        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                test=items.remove(position);
                itemsAdapter.notifyItemRemoved(position);

                Toast.makeText(getApplicationContext(), "Items was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        itemsAdapter=new ItemsAdapter(items, onLongClickListener);

        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager( this));

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItems.getText().toString();
                //add item to model and notify adapter that an item is interted
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size() -1 );
                etItems.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");

    }

    private void loadItems() {
        try {
            items=new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items =new ArrayList<>();
        }
    }

    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }

    }
}