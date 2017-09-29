package br.com.univel.todolist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private int indexEdit = -1;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Carrega os itens
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();

        lerItens();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapter,
                                               View item, int pos, long id) {
                    //excluir item
                    items.remove(pos);
                    //atualizar adapter
                    itemsAdapter.notifyDataSetChanged();
                    //grava o arquivo novamente
                    gravarItens();
                    return true;
                }

            });

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
                        etNewItem.setText(items.get(position));
                        indexEdit = position;
                    }
                });

    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        //editando
        if(indexEdit >= 0){
            items.set(indexEdit, itemText);
            itemsAdapter.notifyDataSetChanged();
        }else {
            //novo
            itemsAdapter.add(itemText);
        }
        etNewItem.getText().clear();
        gravarItens();
        indexEdit = -1;
    }

    private void lerItens() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void gravarItens() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}