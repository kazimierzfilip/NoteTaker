package com.wordpress.javawrok.notetaker;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ListNotesActivity extends AppCompatActivity {

    private List<Note> notes = new ArrayList<>(); //creating list of notes
    private ListView notesListView;
    private int editingNoteId = -1; //-1 means new note (no note is edited)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);
        notesListView = (ListView)findViewById(R.id.notesListView);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //list's onClick method
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int itemNumber, long id) { //on click: create intent and put extra (selected note)
                Intent editNoteIntent = new Intent(view.getContext(), EditNoteActivity.class);
                editNoteIntent.putExtra("Note", notes.get(itemNumber));
                editingNoteId = itemNumber;
                startActivityForResult(editNoteIntent, 1);
            }
        });

        registerForContextMenu(notesListView);

        notes.add(new Note("first note", "notatakaka aaa",new Date())); //adding sample values to list of notes
        notes.add(new Note("second note", "notatakaka aaa",new Date()));
        notes.add(new Note("third note", "notatakaka aaa",new Date()));
        notes.add(new Note("fourth note", "notatakaka aaa",new Date()));
        notes.add(new Note("fifth note", "notatakaka aaa",new Date()));

        populateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //create menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //on "add note" button click

        Intent editNoteIntent = new Intent(this, EditNoteActivity.class);
        //startActivity(editNoteIntent);
        startActivityForResult(editNoteIntent, 1);
        return true;
       // return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) { //on "delete" click
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        AlertDialog.Builder builder = new AlertDialog.Builder();
        builder.setMessage("Are you sure to delete?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        notes.remove(info.position);
                        populateList();
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED) {
            editingNoteId = -1;
            return; //return if clicked "cancel"
        }
        Serializable extra = data.getSerializableExtra("Note");
        if(extra != null){ //if returned with extra, edit or add new note
            Note newNote = (Note)extra;
            if(editingNoteId > -1){
                notes.set(editingNoteId, newNote);
                editingNoteId = -1;
            }else {
                notes.add(newNote);
            }
            populateList();
        }
    }

    private void populateList() { //display titles of list elements

        List<String> values = new ArrayList<>();

        for(Note note: notes){
            values.add(note.getTitle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, android.R.id.text1, values);

        notesListView.setAdapter(adapter);
    }
}
