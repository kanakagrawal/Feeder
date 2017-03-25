package com.example.ajay.feeder20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MainActivity.this,Loginpage.class);
        startActivity(intent);





        sharedpreferences = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.student_default);
        String student = sharedpreferences.getString(getString(R.string.student), defaultValue);
        if(student.contentEquals(getString(R.string.student_default))){

        }
        else{
            try {
                Student s1 = new Student();
                JSONObject jObj = new JSONObject(student);
                s1.setId(jObj.getInt("id"));
                s1.setName(jObj.getString("name"));         // data from JSON format
                s1.setEmail_id(jObj.getString("email_id"));
                s1.setPassword(jObj.getString("password"));
                s1.setRollnumber(jObj.getString("rollnumber"));
                JSONArray jArr = jObj.getJSONArray("courses");
                List<Course> course = new ArrayList<Course>();
                Log.d("size","a"+jArr.length());
                for (int i=0; i < jArr.length(); i++) {
                    Log.d("json",s1.getRollnumber()+"start");
                    JSONObject jObj1 = jArr.getJSONObject(i);
                    Course c = new Course();
                    c.setCoursename(jObj1.getString("course_name"));

                    c.setCoursecode(jObj1.getString("course_code"));

                    c.setSemester(jObj1.getString("semester"));
                    c.setYear(jObj1.getInt("year"));
                    c.setId(jObj1.getInt("id"));
                    List<Feedback> feedback = new ArrayList<Feedback>();
                    JSONArray jArr1 = jObj1.getJSONArray("feedback_set");
                    Log.d("json",s1.getRollnumber()+"middle");
                    for(int j=0; j< jArr1.length(); j++){
                        Log.d("json",s1.getRollnumber()+"check"+j+" "+jArr1.length());
                        JSONObject jObj2 = jArr1.getJSONObject(j);
                        Log.d("json",s1.getRollnumber()+"check"+j+" "+jArr1.length());
                        Feedback f = new Feedback();
                        f.setFeed_course(c.getCoursecode());
                        f.setFeed_id(jObj2.getInt("id"));
                        f.setFeedback_name(jObj2.getString("feedback_name"));
                        f.setFeed_deadline_date(jObj2.getString("deadline_date"));
                        f.setFeed_deadline_time(jObj2.getString("deadline_time"));
                        Log.d("json",s1.getRollnumber()+"check"+j+" "+jArr1.length());
                        List<RatingQuestion> rlist = new ArrayList<>();
                        JSONArray jArr2 = jObj2.getJSONArray("ratingquestion_set");
                        for(int k = 0;k<jArr2.length();k++){
                            JSONObject jObj3 = jArr2.getJSONObject(k);
                            RatingQuestion r = new RatingQuestion();
                            r.setId(jObj3.getInt("id"));
                            r.setFeedback_id(jObj3.getInt("feedback"));
                            r.setQuestion(jObj3.getString("question"));
                            rlist.add(r);
                        }
                        f.setRateque(rlist);

                        List<SubjectiveQuestion> slist = new ArrayList<>();
                        jArr2 = jObj2.getJSONArray("subjectivequestion_set");
                        for(int k = 0;k<jArr2.length();k++){
                            JSONObject jObj3 = jArr2.getJSONObject(k);
                            SubjectiveQuestion s = new SubjectiveQuestion();
                            s.setId(jObj3.getInt("id"));
                            s.setFeedback_id(jObj3.getInt("feedback"));
                            s.setQuestion(jObj3.getString("question"));
                            slist.add(s);
                        }
                        f.setSubque(slist);
                        feedback.add(f);
                    }
                    Log.d("json",s1.getRollnumber()+"middle2");
                    c.setFeed(feedback);
                    List<Deadline> deadline = new ArrayList<>();
                    jArr1 = jObj1.getJSONArray("deadline_set");
                    for(int j = 0 ; j< jArr1.length();j++){
                        JSONObject jObj2 = jArr1.getJSONObject(j);
//                        Log.d("json",s1.getRollnumber()+"check"+j+" "+jArr1.length());
                        Deadline f = new Deadline();
                        f.setCourse(jObj2.getInt("course"));
                        f.setId(jObj2.getInt("id"));
                        f.setName(jObj2.getString("assignment_name"));
                        f.setDate(jObj2.getString("deadline_date"));
                        f.setTime(jObj2.getString("deadline_time"));
                        deadline.add(f);
                    }
                    c.setDeadline(deadline);
                    course.add(c);
                    Log.d("json",s1.getRollnumber()+"end");
                }
                s1.setSubjects(course);
                jArr = jObj.getJSONArray("ratinganswer_set");
                List<RatingAnswer> rating = new ArrayList<>();
                for(int i = 0;i<jArr.length();i++){
                    JSONObject jObj1 = jArr.getJSONObject(i);
                    RatingAnswer r = new RatingAnswer();
                    r.setQuestion_id(jObj1.getInt("question"));
                    rating.add(r);
                }
                s1.setRAnswers(rating);
                jArr = jObj.getJSONArray("subjectiveanswer_set");
                List<SubjectiveAnswer> subjective = new ArrayList<>();
                for(int i = 0;i<jArr.length();i++){
                    JSONObject jObj1 = jArr.getJSONObject(i);
                    SubjectiveAnswer r = new SubjectiveAnswer();
                    r.setQuestion_id(jObj1.getInt("question"));
                    subjective.add(r);
                }
                s1.setSAnswers(subjective);

//                txt.setText(responses);
//                Intent intent = new Intent(MainActivity.this, Calender.class);
//                EditText editText = (EditText) findViewById(R.id.edit_message);
//                String message = editText.getText().toString();
//                intent.putExtra("s",s1);
//                startActivity(intent);
                //}
//            else{
//                txt.setText("Invalid Credentials");
//            }
            }
            catch(org.json.JSONException k){
                Log.d("Error", "Json catch student");
//                txt.setText("Invalid Credentials");
            }
        }
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    public void Clicklog (View view) {
        Log.i("clicks","You Clicked B1");
        Intent i=new Intent(MainActivity.this, Loginpage.class);
        startActivity(i);
    }

}