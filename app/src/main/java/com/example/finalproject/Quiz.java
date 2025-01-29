package com.example.finalproject;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Quiz extends AppCompatActivity {

    private long elapsedTimeMillis;
    private boolean isPlaying;

    private final List<QuestionsList> questionsLists = new ArrayList<>();

    private TextView timer;
    private RelativeLayout LOpt1,LOpt2,LOpt3,LOpt4;
    private ImageView cir1,cir2,cir3,cir4;

    private TextView opt1,opt2,opt3,opt4;
    private TextView questionTV;
    private TextView currentQuestion;

    private int currentQuestionPosition = 0;
    private TextView totalQuestion;

    public int result;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://login-regist-f1a4e-default-rtdb.firebaseio.com/");
    private CountDownTimer countDownTimer;
    private TextView indicator;

    private String answer;
    private MediaPlayer mediaPlayer;
    private String selectedOption = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        indicator = findViewById(R.id.indicator);

        timer = findViewById(R.id.timerTV);
        LOpt1 = findViewById(R.id.layoutopt1);
        LOpt2 = findViewById(R.id.layoutopt2);
        LOpt3 = findViewById(R.id.layoutopt3);
        LOpt4 = findViewById(R.id.layoutopt4);

        opt1 = findViewById(R.id.option1);
        opt2 = findViewById(R.id.option2);
        opt3 = findViewById(R.id.option3);
        opt4 = findViewById(R.id.option4);


        questionTV = findViewById(R.id.questionTV);

        final AppCompatButton songBtn = findViewById(R.id.songBtn);

        cir1 = findViewById(R.id.iconcheck1);
        cir2 = findViewById(R.id.iconcheck2);
        cir3 = findViewById(R.id.iconcheck3);
        cir4 = findViewById(R.id.iconcheck4);

        totalQuestion = findViewById(R.id.totalQuestionTV);
        currentQuestion = findViewById(R.id.currentQuestionTV);

        final AppCompatButton nextBtn = findViewById(R.id.nextBtn);

        InstructionsDialog instructionsDialog= new InstructionsDialog(Quiz.this);
        instructionsDialog.setCancelable(false);
        instructionsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        instructionsDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final int getQuizTime = Integer.parseInt(snapshot.child("time").getValue(String.class));
                for(DataSnapshot questions : snapshot.child("questions").getChildren()){
                    String getQuestion = questions.child("question").getValue(String.class);
                    String getOption1 = questions.child("option1").getValue(String.class);
                    String getOption2 = questions.child("option2").getValue(String.class);
                    String getOption3 = questions.child("option3").getValue(String.class);
                    String getOption4 = questions.child("option4").getValue(String.class);
                    String getAnswer = questions.child("answer").getValue(String.class);


                    QuestionsList questionsList = new QuestionsList(getQuestion,getOption1,getOption2,getOption3, getOption4, getAnswer);
                    questionsLists.add(questionsList);
                }
                totalQuestion.setText("/"+questionsLists.size());
                selectQuestion(currentQuestionPosition);
                startQuizTime(getQuizTime);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Quiz.this, "Failed to get the data",Toast.LENGTH_SHORT).show();
            }
        });

        LOpt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "1";
                selectOption(LOpt1, cir1);
            }
        });
        LOpt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "2";
                selectOption(LOpt2, cir2);
            }
        });
        LOpt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "3";
                selectOption(LOpt3, cir3);
            }
        });
        LOpt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = "4";
                selectOption(LOpt4, cir4);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedOption != "0"){
                    //questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOption);

                    if(currentQuestionPosition < questionsLists.size()-1){
                        selectQuestion(currentQuestionPosition+1);
                        answer = questionsLists.get(currentQuestionPosition).getAnswer();
                        if(selectedOption.equals(answer)){
                            currentQuestionPosition++;
                            indicator.setText("CORRECT");
                            result += 1;
                        }
                        else if (!selectedOption.equals(answer)){
                            currentQuestionPosition++;
                            indicator.setText("FALSE");
                        }
                    }
                    else {
                        Intent intent = new Intent(Quiz.this, QuizResult.class);
                        intent.putExtra("score", result);
                        intent.putExtra("elapsedTime", elapsedTimeMillis);
                        startActivity(intent);
                        finish();
                        countDownTimer.cancel();
                        finishQuiz();
                    }

                }
                else {
                    Toast.makeText(Quiz.this, "Please select an option", Toast.LENGTH_SHORT).show();
                }
            }
        });
        songBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    stopSong();
                } else {
                    playSong();
                }
            }
        });


    }


    private void finishQuiz(){
        Intent intent = new Intent(Quiz.this, QuizResult.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("questions",(Serializable)questionsLists);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    private void startQuizTime(int maxTimeSeconds) {
        countDownTimer = new CountDownTimer(maxTimeSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long getHour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long getMinute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                long getSecond = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;

                String generateTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", getHour, getMinute, getSecond);
                timer.setText(generateTime);
            }

            @Override
            public void onFinish() {
                elapsedTimeMillis = maxTimeSeconds * 1000;
                finishQuiz();
                onDestroy();
            }
        };
        countDownTimer.start();
    }

    private void selectQuestion (int questionListPosition){

        resetOptions();

        questionTV.setText(questionsLists.get(questionListPosition).getQuestion());
        opt1.setText(questionsLists.get(questionListPosition).getOption1());
        opt2.setText(questionsLists.get(questionListPosition).getOption2());
        opt3.setText(questionsLists.get(questionListPosition).getOption3());
        opt4.setText(questionsLists.get(questionListPosition).getOption4());

        currentQuestion.setText("Question "+(questionListPosition+1));
    }
    private void resetOptions(){
        LOpt1.setBackgroundResource(R.drawable.round_back_white);
        LOpt2.setBackgroundResource(R.drawable.round_back_white);
        LOpt3.setBackgroundResource(R.drawable.round_back_white);
        LOpt4.setBackgroundResource(R.drawable.round_back_white);

        cir1.setImageResource(R.drawable.optioncircle);
        cir2.setImageResource(R.drawable.optioncircle);
        cir3.setImageResource(R.drawable.optioncircle);
        cir4.setImageResource(R.drawable.optioncircle);
    }
    private void selectOption(RelativeLayout selectedOptionLayout, ImageView selectedOptionIcon){
        resetOptions();
        selectedOptionIcon.setImageResource(R.drawable.checklist);
        selectedOptionLayout.setBackgroundResource(R.drawable.selectedoptionlayoutbg);

    }

    private void playSong() {
        mediaPlayer = MediaPlayer.create(Quiz.this, R.raw.studysong);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        isPlaying = true;
    }

    private void stopSong() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isPlaying = false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSong();
    }


}