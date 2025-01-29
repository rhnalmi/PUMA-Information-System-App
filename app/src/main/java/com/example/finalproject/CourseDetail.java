package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CourseDetail extends AppCompatActivity {
    TextView coudeDesc, coudeTitle;
    ImageView coudeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        coudeDesc = findViewById(R.id.coudeDesc);
        coudeTitle = findViewById(R.id.coudeTitle);
        coudeImage = findViewById(R.id.coudeImage);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            coudeDesc.setText(bundle.getInt("Desc"));
            coudeImage.setImageResource(bundle.getInt("Image"));
            coudeTitle.setText(bundle.getString("Title"));
        }
    }
    }
