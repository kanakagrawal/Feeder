package com.example.ajay.feeder20;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FeedbackForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);
        Intent intent = getIntent();
        final Student student = (Student) intent.getSerializableExtra("student");
        final Feedback feedback = (Feedback) intent.getSerializableExtra("feedback");
        final LinearLayout l = (LinearLayout) findViewById(R.id.questions);
        TextView name = new TextView(this);
        name.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        name.setTextColor(Color.RED);
        name.setTypeface(name.getTypeface().DEFAULT_BOLD);
        name.setText(feedback.getFeed_course() +" : " + feedback.getFeedback_name());
        name.setGravity(Gravity.CENTER_HORIZONTAL);
        l.addView(name);
        final List<RatingQuestion> rlist = feedback.getRateque();
        Boolean check = true;
        if(rlist.size()!=0){
            int x = rlist.get(0).getId();
            List<RatingAnswer> test = student.getRAnswers();
            for(int i = 0;i<test.size();i++){
                if(x==test.get(i).getQuestion_id()){
                    check = false;
                    break;
                }
            }
        }
        else{
            List<SubjectiveAnswer> test= student.getSAnswers();
            int x = feedback.getSubque().get(0).getId();
            for(int i = 0;i<test.size();i++){
                if(x==test.get(i).getQuestion_id()){
                    check = false;
                    break;
                }
            }
        }
        if(check){
            List<TextView> que_r = new ArrayList<>();
            List<RatingAnswer> ranswer = new ArrayList<>();
            for(int i = 0;i<rlist.size();i++){
                TextView que = new TextView(this);
                que.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                que.setText("Q"+(i+1)+". "+rlist.get(i).getQuestion());
                que.setPadding(0,50,0,0);
                que.setTextColor(Color.BLACK);
//            que_r.add(que);
                l.addView(que);
                RatingBar ans = new RatingBar(this);
                ans.setNumStars(5);
                ans.setMax(5);
                ans.setStepSize(1);
                ans.setRating(3);
                ans.setLayoutParams(new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT));
                l.addView(ans);
            }
            List<SubjectiveQuestion> qlist = feedback.getSubque();
            for(int i = 0;i<qlist.size();i++){
                TextView que = new TextView(this);
                que.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                que.setText("Q"+(i+1+rlist.size())+". "+qlist.get(i).getQuestion());
                que.setPadding(0,40,0,0);
                que.setTextColor(Color.BLACK);
//            que_r.add(que);
                l.addView(que);
                EditText ans = new EditText(this);
                ans.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                ans.setPadding(0,40,0,0);
                ans.setTextColor(Color.BLACK);
                l.addView(ans);
            }
            Button b = new Button(getApplicationContext());
            b.setText("Submit");
            b.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            b.setGravity(Gravity.CENTER_HORIZONTAL);
            b.setPadding(0,10,0,0);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("count","c"+l.getChildCount());
                    for(int i = 1;i<l.getChildCount()-1;i=i+2){
                        View que = l.getChildAt(i);
                        if(que instanceof TextView){
                            if((i-1)/2<rlist.size()){
                                RatingAnswer r = new RatingAnswer();
                                View ans = l.getChildAt(i+1);
                                if(ans instanceof RatingBar){
                                    r.setAnswer_rated(((int) ((RatingBar) ans).getRating()));
                                    r.setQuestion_id(feedback.getRateque().get((i-1)/2).getId());
                                    r.setStudent_id(student.getId());
                                    student.getRAnswers().add(r);
                                }
                                r.submit();
                            }
                            else{
                                SubjectiveAnswer r = new SubjectiveAnswer();
                                View ans = l.getChildAt(i+1);
                                if(ans instanceof EditText){
                                    r.setAnswer(((EditText) ans).getText().toString());
                                    r.setQuestion_id(feedback.getSubque().get((i-1)/2).getId());
                                    r.setStudent_id(student.getId());
                                    student.getSAnswers().add(r);
                                }
                                r.submit();
                            }
                        }
                    }

                    Intent intent = new Intent(FeedbackForm.this, Calender.class);
                    intent.putExtra("s",student);
                    startActivity(intent);
                }
            });
            l.addView(b);
        }
        else{
            TextView nam = new TextView(this);
            nam.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            nam.setText("You have already filled the form");
            nam.setPadding(0,40,0,0);
            nam.setTextColor(Color.BLACK);
            nam.setGravity(Gravity.CENTER);
//            nam.setGravity(Gravity.CENTER_HORIZONTAL);
            l.addView(nam);
        }
    }
}