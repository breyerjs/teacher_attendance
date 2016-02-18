package assignment2.teacher_attendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        RequestsToolkit tools = new RequestsToolkit();

        //Spinner for changing
        Spinner schoolsSpinner = (Spinner) findViewById(R.id.select_school);
        Spinner teacherSpinner = (Spinner) findViewById(R.id.select_teacher);
        tools.fetchSchoolsAndTeachers(this, schoolsSpinner, teacherSpinner);

        setSpinners();

        //set initially in setSpinners
        //String[] pleaseSelect = {"Select Your Name"};
        //setTeacherSpinner(pleaseSelect);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setSpinners(){
        //Returns the array of teachers for the first item in the spinner
        //this allows initial population of the teacher spinner

        //Set the Schools spinner
        RequestsToolkit tools = new RequestsToolkit();

        Spinner schoolSpinner = (Spinner) findViewById(R.id.select_school);
        //get the keys of Schools and Teachers to get the schools list
        final ArrayList<String> schoolsArray = tools.getJSONKeys(InternalStorage.schoolsAndTeachers);

        //create and set the adapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, schoolsArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner.setAdapter(spinnerArrayAdapter);

        // Populates the teachers spinner when school is selected
        // http://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event
        schoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                try {
                    ArrayAdapter<String> schoolSpinnerAdapter = (ArrayAdapter<String>) parentView.getAdapter();
                    String school = schoolSpinnerAdapter.getItem(position);
                    if (school.equals("Loading..."))
                        return;
                    RequestsToolkit tools = new RequestsToolkit();
                    Log.d("School", school);
                    JSONArray teacherJSONArray = InternalStorage.schoolsAndTeachers.getJSONArray(school);
                    ArrayList<String> teacherArrayList = tools.convertJSONarrayToArrayList(teacherJSONArray);
                    //String [] teacherArray = new String[teacherArrayList.size()];
                    //teacherArrayList.toArray(teacherArray);

                    setTeacherSpinner(teacherArrayList);

                }catch (JSONException e) {Log.d("Json Exception", "In setSchoolSpinner");}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

    }

    public void setTeacherSpinner(ArrayList<String> teacherArray){
        // use this for setting the teacher spinner:
        Spinner teacherSpinner = (Spinner) findViewById(R.id.select_teacher);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teacherArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teacherSpinner.setAdapter(spinnerArrayAdapter);

    }
}
