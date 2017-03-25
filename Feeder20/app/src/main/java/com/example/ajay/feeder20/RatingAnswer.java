package com.example.ajay.feeder20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanak on 5/11/16.
 */
public class RatingAnswer implements Serializable{
    private int id;
    private int answer_rated;
    private int question_id;
    private int student_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnswer_rated() {
        return answer_rated;
    }

    public void setAnswer_rated(int answer_rated) {
        this.answer_rated = answer_rated;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }
    public void submit(){
        AsyncT2 asyncT = new AsyncT2();
        asyncT.execute();
    }


    private class AsyncT2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://10.0.2.2:8020/feeder/auth/rating/");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("answer", Integer.toString(answer_rated)));
                nameValuePairs.add(new BasicNameValuePair("student_id", Integer.toString(student_id)));
                nameValuePairs.add(new BasicNameValuePair("question_id", Integer.toString(question_id)));

                Log.e("mainToPost", "mainToPost" + nameValuePairs.toString());

                // Use UrlEncodedFormEntity to send in proper format which we need
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

//                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
