package com.example.fitmybody;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class bacak extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bacak);

        ImageView image1 = findViewById(R.id.bacak1);
        ImageView image2 = findViewById(R.id.bacak2);
        ImageView image3 = findViewById(R.id.bacak3);
        ImageView image4 = findViewById(R.id.bacak4);
        ImageView image5 = findViewById(R.id.bacak5);
        ImageView image6 = findViewById(R.id.bacak6);
        ImageView image7 = findViewById(R.id.bacak7);
        ImageView image8 = findViewById(R.id.bacak8);


        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Glide.with(bacak.this)
                           .asGif()
                           .load(R.drawable.squats_unscreen)
                           .into(image1);
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(bacak.this)
                        .asGif()
                        .load(R.drawable.lunge_unscreen)
                        .into(image2);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(bacak.this)
                        .asGif()
                        .load(R.drawable.leg_press_unscreen)
                        .into(image3);
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(bacak.this)
                        .asGif()
                        .load(R.drawable.leg_curl_unscreen)
                        .into(image4);
            }
        });

        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(bacak.this)
                        .asGif()
                        .load(R.drawable.leg_extension_unscreen)
                        .into(image5);
            }
        });

        image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(bacak.this)
                        .asGif()
                        .load(R.drawable.hip_thrust_unscreen)
                        .into(image6);
            }
        });

        image7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(bacak.this)
                        .asGif()
                        .load(R.drawable.calf_raise_unscreen)
                        .into(image7);
            }
        });

        image8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(bacak.this)
                        .asGif()
                        .load(R.drawable.glute_kickback_unscreen)
                        .into(image8);
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