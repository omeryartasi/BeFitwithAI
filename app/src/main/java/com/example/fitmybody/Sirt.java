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

public class Sirt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sirt);

        ImageView imageSirt1 = findViewById(R.id.sirt1);
        ImageView imageSirt2 = findViewById(R.id.sirt2);
        ImageView imageSirt3 = findViewById(R.id.sirt3);
        ImageView imageSirt4 = findViewById(R.id.sirt4);
        ImageView imageSirt5 = findViewById(R.id.sirt5);
        ImageView imageSirt6 = findViewById(R.id.sirt6);
        ImageView imageSirt7 = findViewById(R.id.sirt7);
        ImageView imageSirt8 = findViewById(R.id.sirt8);


        imageSirt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Sirt.this)
                        .asGif()
                        .load(R.drawable.pull_up_unscreen)
                        .into(imageSirt1);
            }
        });

        imageSirt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Sirt.this)
                        .asGif()
                        .load(R.drawable.lat_pull_down_unscreen)
                        .into(imageSirt2);
            }
        });

        imageSirt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Sirt.this)
                        .asGif()
                        .load(R.drawable.barbell_row_unscreen)
                        .into(imageSirt3);
            }
        });

        imageSirt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Sirt.this)
                        .asGif()
                        .load(R.drawable.seated_cable_unscreen)
                        .into(imageSirt4);
            }
        });


        imageSirt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Sirt.this)
                        .asGif()
                        .load(R.drawable.t_bar_row_unscreen)
                        .into(imageSirt5);
            }
        });


        imageSirt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Sirt.this)
                        .asGif()
                        .load(R.drawable.dumbell_row_unscreen)
                        .into(imageSirt6);
            }
        });


        imageSirt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Sirt.this)
                        .asGif()
                        .load(R.drawable.face_pull_unscreen)
                        .into(imageSirt7);
            }
        });


        imageSirt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Sirt.this)
                        .asGif()
                        .load(R.drawable.deadlift_unscreen)
                        .into(imageSirt8);
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
                checkIcon8.setImageDrawable(getDrawable(R.drawable.filled_circle));
            }
        });
    }
}