package com.example.hp.spinner;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener,AdapterView.OnItemSelectedListener {
    DatabaseHelper myDB;
    // TextView DisplayStringArray;
    EditText DisplayStringArray;
    TextToSpeech tts;
    Button startRecognizer;
    private static final int RQS_RECOGNITION = 1;

    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startRecognizer= (Button) findViewById(R.id.startrecognizer);
        startRecognizer.setEnabled(false);
        DisplayStringArray= (EditText) findViewById(R.id.print);
        startRecognizer.setOnClickListener(startRecognizerOnClickListener);
        tts= new TextToSpeech(this, this);
        myDB= new DatabaseHelper(this);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // Notify the selected item text
                Toast.makeText
                        (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                        .show();
                language = selectedItemText;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Button.OnClickListener startRecognizerOnClickListener= new Button.OnClickListener() {
        @Override
        public void onClick(View arg0) {

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            //  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-IN");
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "speech to recognize");
            startActivityForResult(intent, RQS_RECOGNITION);


        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == RQS_RECOGNITION) && (resultCode == RESULT_OK)) {

            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//String query = "select " + DatabaseHelper.COL2 + "from " + DatabaseHelper.TABLE_NAME + "where " +DatabaseHelper.COL1+ " = " +result;
            DisplayStringArray.append("translating");

            if (language.equals("English")) {
                DisplayStringArray.append(result.get(0));
            } else if (language.equals("Kannada")) {


                Cursor cursor = myDB.getListContents();
                // Cursor cursor = myDB.translate(result);

                if (cursor.moveToNext()) {
                    DisplayStringArray.append(cursor.getString(0));
                } else {

                    DisplayStringArray.append(result.get(0));

                }
            }
        }
    }

    @Override
    public void onInit(int status) {
        startRecognizer.setEnabled(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
