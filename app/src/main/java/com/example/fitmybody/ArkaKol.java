package com.example.fitmybody;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ArkaKol extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_arka_kol);

        ImageView arka1 = findViewById(R.id.arkakol1);
        ImageView arka2 = findViewById(R.id.arkakol2);
        ImageView arka3 = findViewById(R.id.arkakol3);
        ImageView arka4 = findViewById(R.id.arkakol4);
        ImageView arka5 = findViewById(R.id.arkakol5);
        ImageView arka6 = findViewById(R.id.arkakol6);


        arka1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(ArkaKol.this)
                        .asGif()
                        .load(R.drawable.pushdown_unscreen)
                        .into(arka1);
            }
        });

        arka2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(ArkaKol.this)
                        .asGif()
                        .load(R.drawable.basust_unscreen)
                        .into(arka2);
            }
        });

        arka3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(ArkaKol.this)
                        .asGif()
                        .load(R.drawable.skull_unscreen)
                        .into(arka3);
            }
        });

        arka4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(ArkaKol.this)
                        .asGif()
                        .load(R.drawable.close_grip_unscreen)
                        .into(arka4);
            }
        });

        arka5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(ArkaKol.this)
                        .asGif()
                        .load(R.drawable.dips_unscreen)
                        .into(arka5);
            }
        });

        arka6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(ArkaKol.this)
                        .asGif()
                        .load(R.drawable.kickbak_unscreen)
                        .into(arka6);
            }
        });

        ImageView checkIcon1=findViewById(R.id.checkIcon1);
        ImageView checkIcon2=findViewById(R.id.checkIcon2);
        ImageView checkIcon3=findViewById(R.id.checkIcon3);
        ImageView checkIcon4=findViewById(R.id.checkIcon4);
        ImageView checkIcon5=findViewById(R.id.checkIcon5);
        ImageView checkIcon6=findViewById(R.id.checkIcon6);

        checkIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIcon1.setImageDrawable(getDrawable(R.drawable.filled_circle));
            }
        });

        checkIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIcon2.setImageDrawable(getDrawable(R.drawable.filled_circle));
            }
        });

        checkIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIcon3.setImageDrawable(getDrawable(R.drawable.filled_circle));
            }
        });

        checkIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIcon3.setImageDrawable(getDrawable(R.drawable.filled_circle));
            }
        });
        checkIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIcon4.setImageDrawable(getDrawable(R.drawable.filled_circle));
            }
        });

        checkIcon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIcon5.setImageDrawable(getDrawable(R.drawable.filled_circle));
            }
        });

        checkIcon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIcon6.setImageDrawable(getDrawable(R.drawable.filled_circle));
            }
        });
    }
}