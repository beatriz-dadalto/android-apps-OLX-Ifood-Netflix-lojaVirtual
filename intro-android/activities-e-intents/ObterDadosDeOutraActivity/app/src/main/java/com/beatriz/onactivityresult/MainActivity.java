package com.beatriz.onactivityresult;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textNome;
    private Button button;

    private final int REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // where these data will show up on screen
        textNome = findViewById(R.id.text_nome);
        button = findViewById(R.id.btn_segundaActivity);

        //on click go to another activity
        button.setOnClickListener(view -> {
            Intent intent = new Intent(this, SegundaActivity.class);

            // receive data from another activity
            startActivityForResult(intent, REQUEST_CODE);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // is valid?
        if (resultCode == RESULT_OK) {
            // is it the right activity that requested data?
            if (requestCode == REQUEST_CODE) {
                // retrieve data from the other activity
                String nome = (String) data.getSerializableExtra("meu_nome");
                // set data to show up on screen
                textNome.setText(nome);
            }
        }
    }
}