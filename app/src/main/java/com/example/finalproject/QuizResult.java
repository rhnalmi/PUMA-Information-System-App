package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class QuizResult extends AppCompatActivity {
    private FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;

    String names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        final AppCompatButton quitBtn = findViewById(R.id.quitBtn);
        final TextView score = findViewById(R.id.scoreTV);
        final TextView totalScore = findViewById(R.id.totalScoreTV);
        final TextView correct = findViewById(R.id.correctTV);
        final TextView incorrect = findViewById(R.id.incorrectTV);
        final AppCompatButton submitBtn = findViewById(R.id.submitBtn);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        names = document.getString("fName");
                        Toast.makeText(QuizResult.this, names, Toast.LENGTH_SHORT).show();

                        Intent intent = getIntent();
                        long elapsedTimeMillis = intent.getLongExtra("elapsedTime", 0);
                        int result = intent.getIntExtra("score",0);
                        String namess = names;
                        totalScore.setText("/" +"20");
                        score.setText(result+ "");
                        correct.setText(result + "");
                        incorrect.setText(String.valueOf(20-result));
                        submitBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Assuming you have a unique user ID or some identifier
                                //String userId = "user123"; // Replace with the actual user ID

                                // Create a data object to be saved
                                Map<String, Object> resultData = new HashMap<>();
                                resultData.put("email",user.getEmail());
                                resultData.put("fName",namess);
                                resultData.put("score", result);
                                resultData.put("elapsedTime", elapsedTimeMillis);
                                resultData.put("totalQuestions", 20); // Change this to the actual total number of questions

                                db.collection("users").document(user.getUid())
                                        .set(resultData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Handle success
                                                // You can show a success message or perform any other action
                                                Toast.makeText(QuizResult.this, "Result saved successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle failure
                                                // You can show an error message or perform any other action
                                                Toast.makeText(QuizResult.this, "Failed to save result", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    } else{
                        Log.d("Document", "No such document");
                    }
                } else{
                    Log.d("Document", "get failed with", task.getException());
                }
            }
        });





        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}