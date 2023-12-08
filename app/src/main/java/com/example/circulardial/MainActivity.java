package com.example.circulardial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CircularDial m_dial;
    private TextView m_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_dial = findViewById(R.id.circularDial);
        m_text = findViewById(R.id.textView);

        m_dial.setSliderChangeListener((d) -> {
            m_text.setText(String.valueOf(d));
        });
    }
}