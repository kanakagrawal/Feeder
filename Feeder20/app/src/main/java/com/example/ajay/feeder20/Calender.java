package com.example.ajay.feeder20;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class Calender extends AppCompatActivity {
    private boolean undo = false;
//    private Spinner coursespinner;
    public Student student=new Student();
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    List<String> setCustomResourceForDates(Student student,List<ColorDrawable> colors) {

        List<String> Coursecodes = new ArrayList<>();
        Coursecodes.add("All");
        List<Course> c = student.getSubjects();
        colors.add(new ColorDrawable(Color.YELLOW));
        colors.add(new ColorDrawable(Color.GREEN));
        colors.add(new ColorDrawable(Color.RED));
        colors.add(new ColorDrawable(Color.CYAN));
        colors.add(new ColorDrawable(Color.MAGENTA));
        colors.add(new ColorDrawable(Color.GRAY));
        colors.add(new ColorDrawable(Color.BLUE));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date();
        for(int i = 0;i<c.size();i++){
            Coursecodes.add(c.get(i).getCoursecode());
            List<Feedback> f = new ArrayList<Feedback>();
            f = c.get(i).getFeed();
            for(int j = 0;j<f.size();j++){
                String d = f.get(j).getFeed_deadline_date();
                String t = f.get(j).getFeed_deadline_time();
                String dtStart = d+"T"+t+"Z";
                try {
                    date = format.parse(dtStart);
                    System.out.println(date);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (caldroidFragment != null) {
                    caldroidFragment.setBackgroundDrawableForDate(colors.get(i), date);
//                    caldroidFragment.setTextColorForDate(R.color.white, date);
                }
            }
            List<Deadline> de = new ArrayList<>();
            de = c.get(i).getDeadline();
            for(int j = 0;j<de.size();j++){
                String d = de.get(j).getDate();
                String t = de.get(j).getTime();
                String dtStart = d+"T"+t+"Z";
                try {
                    date = format.parse(dtStart);
                    System.out.println(date);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (caldroidFragment != null) {
                    caldroidFragment.setBackgroundDrawableForDate(colors.get(i), date);
//                    caldroidFragment.setTextColorForDate(R.color.white, date);
                }
            }
        }
        return Coursecodes;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                SaveSharedPreference.clearUserName(Calender.this);
                Intent intent = new Intent(Calender.this,Loginpage.class);
                startActivity(intent);
                return true;

            case R.id.action_refresh:
                intent = new Intent(Calender.this,Loginpage.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

//    public void addItemsOnCourseSpinner(List<String> list) {
//        coursespinner = (Spinner) findViewById(R.id.course_spinner);
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, list);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        coursespinner.setAdapter(dataAdapter);
//    }
//    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//        Log.d("SA","spinner");
////        Toast.makeText(parent.getContext(),
////                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
////                Toast.LENGTH_SHORT).show();
//    }
//    public void onNothingSelected(AdapterView<?> parent) {
//        Log.d("SA","spinnedadasr");
//        // Another interface callback
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        Intent intent = getIntent();
        List<String> Coursecodes = new ArrayList<>();
        student = (Student) intent.getSerializableExtra("s");
        final List<ColorDrawable> colors = new ArrayList<ColorDrawable>();

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        caldroidFragment = new CaldroidFragment();

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }

        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            caldroidFragment.setArguments(args);
        }

        Coursecodes = setCustomResourceForDates(student,colors);
//        addItemsOnCourseSpinner(Coursecodes);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendarView, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                LinearLayout l = (LinearLayout) findViewById(R.id.display);
                l.removeAllViews();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date date2 = new Date();
                List<Course> c = student.getSubjects();
                for(int i = 0;i<c.size();i++){
                    Course course = c.get(i);
                    List<Feedback> f = course.getFeed();
                    for(int j = 0;j<f.size();j++){
                        final Feedback feedback = f.get(j);
                        String d = feedback.getFeed_deadline_date();
                        String t = feedback.getFeed_deadline_time();
                        String dtStart = d+"T"+t+"Z";
                        try {
                            date2 = format.parse(dtStart);
                            System.out.println(date);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if(removeTime(date2).equals(removeTime(date))){
                            Button b = new Button(getApplicationContext());
                            b.setText(course.getCoursecode()+" : "+feedback.getFeedback_name());
                            b.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                            b.setBackgroundColor(colors.get(i).getColor());
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Calender.this, FeedbackForm.class);
                                    intent.putExtra("feedback",feedback);
                                    intent.putExtra("student",student);
                                    startActivity(intent);
                                }
                            });
                            l.addView(b);
                        }
                    }
                    List<Deadline> de = course.getDeadline();
                    for(int j = 0;j<de.size();j++){
                        String d = de.get(j).getDate();
                        String t = de.get(j).getTime();
                        String dtStart = d+"T"+t+"Z";
                        try {
                            date2 = format.parse(dtStart);
                            System.out.println(date);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if(removeTime(date2).equals(removeTime(date))){
                            Button b = new Button(getApplicationContext());
                            b.setText(course.getCoursecode()+" : "+de.get(j).getName()+"("+de.get(j).getTime()+")");
                            b.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                            b.setBackgroundColor(colors.get(i).getColor());
                            l.addView(b);
                        }
                    }
                }
                if(l.getChildCount()==0){
                    TextView t = new TextView(getApplicationContext());
                    t.setGravity(Gravity.CENTER_HORIZONTAL);
                    t.setText("No Deadlines on this day");
                    t.setPadding(0,40,0,0);
                    t.setTextColor(Color.BLACK);
                    l.addView(t);
                }
            }

            @Override
            public void onChangeMonth(int month, int year) {
//                String text = "month: " + month + " year: " + year;
//                Toast.makeText(getApplicationContext(), text,
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
//                Toast.makeText(getApplicationContext(),
//                        "Long click " + formatter.format(date),
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
//                    Toast.makeText(getApplicationContext(),
//                            "Caldroid view is created", Toast.LENGTH_SHORT)
//                            .show();
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

        caldroidFragment.refreshView();
    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }

    public Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}


