package com.example.fitmybody;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Omuz extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_omuz);

        ImageView omuz1 = findViewById(R.id.omuz1);
        ImageView omuz2 = findViewById(R.id.omuz2);
        ImageView omuz3 = findViewById(R.id.omuz3);
        ImageView omuz4 = findViewById(R.id.omuz4);
        ImageView omuz5= findViewById(R.id.omuz5);
        ImageView omuz6 = findViewById(R.id.omuz6);
        ImageView omuz7 = findViewById(R.id.omuz7);
        ImageView omuz8 = findViewById(R.id.omuz8);

        omuz1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Omuz.this)
                        .asGif()
                        .load(R.drawable.overhead_unscreen)
                        .into(omuz1);
            }
        });

        omuz2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Omuz.this)
                        .asGif()
                        .load(R.drawable.lateral_raise_unscreen)
                        .into(omuz2);
            }
        });

        omuz3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Omuz.this)
                        .asGif()
                        .load(R.drawable.front_raise_unscreen)
                        .into(omuz3);
            }
        });

        omuz4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Omuz.this)
                        .asGif()
                        .load(R.drawable.reverse_unscreen)
                        .into(omuz4);
            }
        });

        omuz5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Omuz.this)
                        .asGif()
                        .load(R.drawable.arnold_unscreen)
                        .into(omuz5);
            }
        });

        omuz6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Omuz.this)
                        .asGif()
                        .load(R.drawable.facep_omuz_unscreen)
                        .into(omuz6);
            }
        });

        omuz7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Omuz.this)
                        .asGif()
                        .load(R.drawable.upright_unscreen)
                        .into(omuz7);
            }
        });

        omuz8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(Omuz.this)
                        .asGif()
                        .load(R.drawable.leaning_unscreen)
                        .into(omuz8);
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