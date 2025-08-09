package com.example.fitmybody;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Karin extends AppCompatActivity {

    private ArrayList<Spor>karin = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_karin);

        ImageView karin1 = findViewById(R.id.karin1);
        ImageView karin2 = findViewById(R.id.karin2);
        ImageView karin3 = findViewById(R.id.karin3);
        ImageView karin4 = findViewById(R.id.karin4);
        ImageView karin5 = findViewById(R.id.karin5);
        ImageView karin6 = findViewById(R.id.karin6);
        ImageView karin7 = findViewById(R.id.karin7);
        ImageView karin8 = findViewById(R.id.karin8);

        karin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Karin.this)
                        .asGif()
                        .load(R.drawable.crunch_unscreen)
                        .into(karin1);
            }
        });

        karin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Karin.this)
                        .asGif()
                        .load(R.drawable.lyingleg_unscreen)
                        .into(karin2);
            }
        });

        karin3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Karin.this)
                        .asGif()
                        .load(R.drawable.plank_unscreen)
                        .into(karin3);
            }
        });

        karin4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Karin.this)
                        .asGif()
                        .load(R.drawable.russiantwist_unscreen)
                        .into(karin4);
            }
        });

        karin5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Karin.this)
                        .asGif()
                        .load(R.drawable.bicycle_unscreen)
                        .into(karin5);
            }
        });

        karin6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Karin.this)
                        .asGif()
                        .load(R.drawable.mountain_unscreen)
                        .into(karin6);
            }
        });

        karin7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Karin.this)
                        .asGif()
                        .load(R.drawable.reversecrunch_unscreen)
                        .into(karin7);
            }
        });

        karin8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Karin.this)
                        .asGif()
                        .load(R.drawable.decline_unscreen)
                        .into(karin8);
            }
        });

        ImageView checkIcon1=findViewById(R.id.checkIcon1);
        ImageView checkIcon2=findViewById(R.id.checkIcon2);
        ImageView checkIcon3=findViewById(R.id.checkIcon3);
        ImageView checkIcon4=findViewById(R.id.checkIcon4);
        ImageView checkIcon5=findViewById(R.id.checkIcon5);
        ImageView checkIcon6=findViewById(R.id.checkIcon6);
        ImageView checkIcon7=findViewById(R.id.checkIcon7);
        ImageView checkIcon8=findViewById(R.id.checkIcon8);


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

        checkIcon7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIcon7.setImageDrawable(getDrawable(R.drawable.filled_circle));
            }
        });

        checkIcon8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIcon8      .setImageDrawable(getDrawable(R.drawable.filled_circle));
            }
        });
    }
}