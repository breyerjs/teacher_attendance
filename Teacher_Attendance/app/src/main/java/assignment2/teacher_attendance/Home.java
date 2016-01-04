package assignment2.teacher_attendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        String[] initialTeacherArray = setSchoolSpinner();

        //set initially--changes as schoolSpinner changes
        setTeacherSpinner(initialTeacherArray);

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

    public String[] setSchoolSpinner(){
        //Returns the array of teachers for the first item in the spinner
        //this allows initial populaiton of the teacher spinner

        //Set the Schools spinner
        Spinner schoolSpinner = (Spinner) findViewById(R.id.select_school);
        String[] schoolsArray = {"Home", "Brandeis"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, schoolsArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner.setAdapter(spinnerArrayAdapter);

        // Populates the teachers spinner when school is selected
        // http://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event
        schoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        //is this necessary?
        String[] testTeacherArray = {"Jackson Breyer", "Rachelle Sarmiento"};
        return testTeacherArray;
    }

    public void setTeacherSpinner(String[] teacherArray){
        // use this for setting the teacher spinner:
        Spinner teacherSpinner = (Spinner) findViewById(R.id.select_teacher);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teacherArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teacherSpinner.setAdapter(spinnerArrayAdapter);

    }
}
