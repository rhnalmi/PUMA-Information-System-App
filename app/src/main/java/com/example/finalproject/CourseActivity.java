package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CourseActivity extends AppCompatActivity {
    TextView tlbname;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, profile, course, competition, isInfo, logout;

    RecyclerView recyclerView;
    List<DataCourseClass> courseList;
    CourseAdapter adapter;
    DataCourseClass courseData;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home =  findViewById(R.id.home);
        profile = findViewById(R.id.profile);
        course = findViewById(R.id.course);
        competition = findViewById(R.id.competition);
        isInfo = findViewById(R.id.isinfo);
        logout = findViewById(R.id.logout);

        tlbname = findViewById(R.id.toolbarName);





        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(CourseActivity.this, MainActivity.class);
                finish();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(CourseActivity.this, ProfileActivity.class);
                finish();
            }
        });
        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        competition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(CourseActivity.this, CompetitionActivity.class);
                finish();
            }
        });
        isInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(CourseActivity.this, ISinfoActivity.class);
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Login.class);
                startActivity(intent);
                finish();
                Toast.makeText(getBaseContext(), "Logout", Toast.LENGTH_LONG).show();
            }
        });


        recyclerView = findViewById(R.id.courseRecycler);
        searchView = findViewById(R.id.courseSearch);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(CourseActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        courseList = new ArrayList<>();
        courseData = new DataCourseClass("Web Design", R.string.WebDesign, "Semester 1", R.drawable.webdesign);
        courseList.add(courseData);
        courseData = new DataCourseClass("Management of Behavior", R.string.MoB, "Semester 1", R.drawable.mob);
        courseList.add(courseData);
        courseData = new DataCourseClass("Programming Concept", R.string.ProgrammingConcepts, "Semester 1", R.drawable.programming);
        courseList.add(courseData);
        adapter = new CourseAdapter(CourseActivity.this, courseList);
        recyclerView.setAdapter(adapter);

        tlbname.setText("Course");

    }

    private void searchList(String text){
        List<DataCourseClass> dataSearchList = new ArrayList<>();
        for (DataCourseClass data : courseList){
            if (data.getCourseTitle().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()){
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setSearchList(dataSearchList);
        }
    }


    public  static void openDrawer (DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer (DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }
    public static void redirectActivity (Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}