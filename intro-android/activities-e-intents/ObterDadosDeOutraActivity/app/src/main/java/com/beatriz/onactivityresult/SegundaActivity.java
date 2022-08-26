package com.beatriz.onactivityresult;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SegundaActivity extends AppCompatActivity {

    private EditText editNome;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        // enable go back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // where these elements will show up on screen
        editNome = findViewById(R.id.edit_nome);
        button = findViewById(R.id.button);

        // on click send the data to another activity
        button.setOnClickListener(view -> {
            // get data from the user input
            String nome = editNome.getText().toString();

            Intent intent = new Intent();
            intent.putExtra("meu_nome", nome);
            // send intent to method OnActivityResult. this method stay on the receiver activity
            setResult(RESULT_OK, intent);
            // finish this activity_segunda
            finish();
        });
    }
}