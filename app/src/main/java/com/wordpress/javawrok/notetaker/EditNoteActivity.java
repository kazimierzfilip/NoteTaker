package com.wordpress.javawrok.notetaker;

import android.content.Intent;
import java.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class EditNoteActivity extends AppCompatActivity {

    private boolean isInEditMode = true; // true - note fields are in edit mode, false - opposite
    private Button saveButton;
    private Button cancelButton;
    private EditText titleText;
    private EditText noteText;
    private TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        //obtaining elements from UI
        saveButton = (Button)findViewById(R.id.saveButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);
        titleText = (EditText)findViewById(R.id.titleText);
        noteText = (EditText)findViewById(R.id.noteText);
        dateText = (TextView)findViewById(R.id.dateText);

        Serializable extra = getIntent().getSerializableExtra("Note");
        if(extra != null){ //if there are extras, then load note from extra
            Note note = (Note)extra;
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String date = dateFormat.format(note.getDate());

            titleText.setText(note.getTitle());
            noteText.setText(note.getNote());
            dateText.setText(date);

            noteEnabler(false); //disabling editing for beginning
        }

        cancelButton.setOnClickListener(new OnClickListener() { //if cancelButton clicked - sends "canceled" intent
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }
        });

        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(titleText.getText().length()==0){ //if title is blank
                    titleText.setHint("Enter Title");
                }
                else if(isInEditMode){ // Saving procedure. Sending note to list

                    Intent returnIntent = new Intent();
                    Note note = new Note(titleText.getText().toString(), noteText.getText().toString(),Calendar.getInstance().getTime());
                    returnIntent.putExtra("Note",note);
                    setResult(RESULT_OK, returnIntent);
                    finish();

                }else{ // Editing procedure. Change "Edit" to "Save" and enable text fields
                    noteEnabler(true);
                }

            }
        });
    }

    private void noteEnabler(boolean state) { // disable/enable text fields and change text "Save" to "Edit" or "Edit" to "Save"
        isInEditMode = state;
        titleText.setEnabled(state);
        noteText.setEnabled(state);
        if(state) saveButton.setText("Save");
        else saveButton.setText("Edit");
    }
}


