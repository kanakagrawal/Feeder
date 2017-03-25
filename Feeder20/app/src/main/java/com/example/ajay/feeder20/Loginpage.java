package com.example.ajay.feeder20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStreamReader;

import android.util.Log;

public class Loginpage extends AppCompatActivity {

    ProgressDialog pDialog;
    Button bt;
    EditText user;
    EditText pass;
    TextView txt;
    public final static String ROLLNUMBER="5415";
    public final static Student s = new Student();
    String User,Pass,secretKey;

    String responses;  // to get response from server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SaveSharedPreference.getUserName(Loginpage.this).length() != 0)
        {
            try{
                String responses = SaveSharedPreference.getUserName(Loginpage.this);
                JSONObject jObj = new JSONObject(responses);
                Pass = jObj.getString("password");
                User = jObj.getString("rollnumber");
                secretKey="hello";
                AsyncT asyncT = new AsyncT();
                asyncT.execute();
            }
            catch(org.json.JSONException k){
                Log.d("Error", "Json catch student");
                txt.setText("Invalid Credentials");
                txt.setPadding(0,10,0,0);
                txt.setTextColor(Color.BLACK);
            }
        }


        setContentView(R.layout.activity_loginpage);



        bt = (Button) findViewById(R.id.button);
        user= (EditText) findViewById(R.id.editText);
        pass= (EditText) findViewById(R.id.editText2);
        txt = (TextView) findViewById(R.id.raw);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User= user.getText().toString();
                Pass= pass.getText().toString();
                secretKey="hello";
                AsyncT asyncT = new AsyncT();
                asyncT.execute();
            }
        });
    }

    private class AsyncT extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // do stuff before posting data
            pDialog = new ProgressDialog(Loginpage.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://10.0.2.2:8020/feeder/auth/");

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("rollnumber", User));
                nameValuePairs.add(new BasicNameValuePair("password", Pass));
                nameValuePairs.add(new BasicNameValuePair("secretKey", secretKey));

                Log.e("mainToPost", "mainToPost" + nameValuePairs.toString());

                // Use UrlEncodedFormEntity to send in proper format which we need
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

//                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
//                response.getJSONObject("loggedin");
                Log.e("mainToPost", "mainToPost" + nameValuePairs.toString());
                InputStream inputStream = response.getEntity().getContent();
                Log.e("mainToPost", "mainToPost" + nameValuePairs.toString());
                InputStreamToStringExample str = new InputStreamToStringExample();
                Log.e("mainToPost", "mainToPost" + nameValuePairs.toString());
                responses = str.getStringFromInputStream(inputStream);
                if(responses.contentEquals("")){

                    Log.e("SDSD","dadsadsa");
                }
                Log.e("response", "34"+responses);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try {
                SaveSharedPreference.setUserName(Loginpage.this,responses);
                Student s1 = new Student();
                JSONObject jObj = new JSONObject(responses);
                s1.setId(jObj.getInt("id"));
//                s1.setName(jObj.getString("name"));         // data from JSON format
//                s1.setEmail_id(jObj.getString("email_id"));
                s1.setPassword(jObj.getString("password"));
                s1.setRollnumber(jObj.getString("rollnumber"));
                JSONArray jArr = jObj.getJSONArray("courses");
                List<Course> course = new ArrayList<Course>();
                Log.d("size","a"+jArr.length());
                for (int i=0; i < jArr.length(); i++) {
                    Log.d("json",s1.getRollnumber()+"start");
                    JSONObject jObj1 = jArr.getJSONObject(i);
                    Course c = new Course();
//                    c.setCoursename(jObj1.getString("course_name"));

                    c.setCoursecode(jObj1.getString("course_code"));

//                    c.setSemester(jObj1.getString("semester"));
//                    c.setYear(jObj1.getInt("year"));
//                    c.setId(jObj1.getInt("id"));
                    List<Feedback> feedback = new ArrayList<Feedback>();
                    JSONArray jArr1 = jObj1.getJSONArray("feedback_set");
                    Log.d("json",s1.getRollnumber()+"middle");
                    for(int j=0; j< jArr1.length(); j++){
                        Log.d("json",s1.getRollnumber()+"check"+j+" "+jArr1.length());
                        JSONObject jObj2 = jArr1.getJSONObject(j);
                        Log.d("json",s1.getRollnumber()+"check"+j+" "+jArr1.length());
                        Feedback f = new Feedback();
                        f.setFeed_course(c.getCoursecode());
//                        f.setFeed_id(jObj2.getInt("id"));
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
//                        f.setCourse(jObj2.getInt("course"));
//                        f.setId(jObj2.getInt("id"));
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
                Intent intent = new Intent(Loginpage.this, Calender.class);
                intent.putExtra("s",s1);
                startActivity(intent);
                finish();
            }
            catch(org.json.JSONException k){
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.d("Error", "Json catch student");
                txt.setText("Invalid Credentials");
                txt.setPadding(0,10,0,0);
                txt.setTextColor(Color.BLACK);
            }

        }
    }

    public static class InputStreamToStringExample {

        public static void main(String[] args) throws IOException {

            // intilize an InputStream
            InputStream is =
                    new ByteArrayInputStream("file content..blah blah".getBytes());

            String result = getStringFromInputStream(is);

            System.out.println(result);
            System.out.println("Done");

        }

        // convert InputStream to String
        private static String getStringFromInputStream(InputStream is) {

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sb.toString();
        }

    }

}
