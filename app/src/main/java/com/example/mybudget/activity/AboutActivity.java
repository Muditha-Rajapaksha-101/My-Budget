package com.example.mybudget.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mybudget.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    ImageView ivfb;
    ImageView ivInsta;
    TextView txtAboutDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //References
        ivfb    = findViewById(R.id.ivfb);
        ivInsta    = findViewById(R.id.ivInsa);
        txtAboutDes    = findViewById(R.id.txtAboutDes);

        //init
        this.setTitle("About Us");
        txtAboutDes.setMovementMethod(new ScrollingMovementMethod());


        //Events
        ivfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
                i.putExtra(Intent.EXTRA_TEXT, "https://www.facebook.com/hasith.sanuja");
                startActivity(Intent.createChooser(i, "Share URL"));
            }
        });

        ivInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
                i.putExtra(Intent.EXTRA_TEXT, "https://www.instagram.com/the_travel_monkey__/");
                startActivity(Intent.createChooser(i, "Share URL"));

            }
        });
    }
}