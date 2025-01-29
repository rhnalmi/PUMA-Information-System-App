package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.Document;

import org.checkerframework.checker.nullness.qual.NonNull;


public class ProfileActivity extends AppCompatActivity {

    TextView tittlename, signedup, name, email1,tlbname;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, profile, course, competition, isInfo, logout;

    FirebaseAuth auth;
    FirebaseUser user;



    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        DocumentReference docRef = db.collection("users").document("fName");



        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        tittlename = findViewById(R.id.name1);
        signedup =findViewById(R.id.signed);
        name = findViewById(R.id.profileName);
        email1 = findViewById(R.id.profileEmail);
        tlbname = findViewById(R.id.toolbarName);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home =  findViewById(R.id.home);
        profile = findViewById(R.id.profile);
        course = findViewById(R.id.course);
        competition = findViewById(R.id.competition);
        isInfo = findViewById(R.id.isinfo);
        logout = findViewById(R.id.logout);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // fetching from document
                        String names = document.getString("fName");

                        tittlename.setText(names);
                        name.setText(names);

                    } else {
                        Log.d("Document", "No such document");
                    }
                } else {
                    Log.d("Document", "get failed with ", task.getException());
                }
            }
        });







        if(user != null){
            if (user.isEmailVerified()){
                email1.setText(user.getEmail());
            } else{
                name.setText("Ga ada isinya :( huhuhu");
            }
        }

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ProfileActivity.this, MainActivity.class);
                finish();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ProfileActivity.this, CourseActivity.class);
                finish();
            }
        });
        competition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ProfileActivity.this, CompetitionActivity.class);
                finish();
            }
        });
        isInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ProfileActivity.this, ISinfoActivity.class);
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

        tlbname.setText("Profile");
    }
//    public class Profile {
//
//        private String fName;
//
//        public Profile(String fName) {
//            this.fName = fName;
//        }
//
//        public String getFName() {
//            return fName;
//        }
//
//        public void setFName(String fName) {
//            this.fName = fName;
//        }
//    }

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
