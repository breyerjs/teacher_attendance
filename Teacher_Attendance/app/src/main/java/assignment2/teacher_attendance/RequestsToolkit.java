package assignment2.teacher_attendance;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jackson Breyer on 1/4/2016.
 */
public class RequestsToolkit {
    public void fetchSchoolsAndTeachers(final Context context, final Spinner schoolSpinner, final Spinner teacherSpinner){
        //populates InternalStorage:: schoolsAndTeachers
        //called on starting Home

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://teacher-attendance.herokuapp.com/tabackend/get_all_schools_and_teachers";


        /*
            Add the password to the request
         */

        //make the json request

        try{
            //leaving this for future use in post requests.
            JSONObject requestBody = new JSONObject();
            requestBody.put("password", "stayinschool");

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, requestBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                InternalStorage.schoolsAndTeachers = response;
                                ArrayAdapter<String> schoolSpinnerAdapter = (ArrayAdapter<String>) schoolSpinner.getAdapter();

                                //clear and reset the schools
                                schoolSpinnerAdapter.clear();
                                for (String school : getJSONKeys(InternalStorage.schoolsAndTeachers)) {
                                    schoolSpinnerAdapter.add(school);
                                }

                                //set teachers
                                JSONArray teacherJSONArray = InternalStorage.schoolsAndTeachers.getJSONArray(schoolSpinnerAdapter.getItem(0));
                                ArrayList<String> teacherArray = convertJSONarrayToArrayList(teacherJSONArray);
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, teacherArray);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                teacherSpinner.setAdapter(spinnerArrayAdapter);


                                //teachers recreates the adapter, but schools CHANGES the adapter, so we
                                // notify it that it's been chaged
                                schoolSpinnerAdapter.notifyDataSetChanged();

                            } catch (JSONException e){Log.d("JSON Exception", "jsonnnnn");}
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub

                        }
                    });


            // Add the request to the RequestQueue.
            queue.add(jsonRequest);
        }catch(JSONException e) {

        }
    }

    public ArrayList<String> getJSONKeys(JSONObject obj){
        /*
            Returns a string array of all of the keys in a JSONObject
         */
        Iterator keysToCopyIterator = obj.keys();
        ArrayList<String> keysList = new ArrayList<String>();
        while(keysToCopyIterator.hasNext()) {
            String key = (String) keysToCopyIterator.next();
            keysList.add(key);
        }

        return keysList;
    }

    public ArrayList<String> convertJSONarrayToArrayList(JSONArray jsarr){
        try {
            ArrayList<String> ret = new ArrayList<String>();
            if (jsarr != null) {
                for (int i = 0; i < jsarr.length(); i++) {
                    ret.add(jsarr.get(i).toString());
                }
            }
            return ret;
        } catch (JSONException e ){}
        //should never get here
        return null;
    }

    public void submitAttendance(final Context context, String schoolName, String enteredPassword,
                                 String f_name, String l_name){
        checkPasswordCorrect(context, schoolName, enteredPassword,f_name, l_name);
    }

    public boolean checkPasswordCorrect(final Context context,
                                        final String schoolName,
                                        final String enteredPassword,
                                        final String f_name,
                                        final String l_name){
        // Calls server and asks if the password is correct. If it is
        // makes a POST request to log the attendance

        //call from Home

        try {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "https://teacher-attendance.herokuapp.com/tabackend/password_correct";

            JSONObject requestBody = new JSONObject();
            requestBody.put("password", "stayinschool");
            requestBody.put("entered_password", enteredPassword);
            requestBody.put("school_name", schoolName);
            requestBody.put("f_name", f_name);
            requestBody.put("l_name", l_name);

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //if correct, post data
                                //if not correct, TOAST that PW is incorrect
                                Log.d("Pw Verified? ", Boolean.toString(response.getBoolean("password_correct")));
                                if (! response.getBoolean("password_correct"))
                                    Toast.makeText(context, "The password is incorrect", Toast.LENGTH_LONG).show();

                                //since the password was correct, proceed to submitting attendance.
                                else{
                                    postSubmission(context, schoolName, enteredPassword, f_name, l_name);
                                }


                                //Now,



                            } catch (JSONException  e){Log.d("Json issues", e.toString());}

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.d("error", error.toString());
                        }
                    });


            // Add the request to the RequestQueue.
            queue.add(jsonRequest);
        }catch(JSONException e) {Log.d("JsonIssue", e.toString());}

    return true;
    }

    public boolean postSubmission(final Context context,
                                        final String schoolName,
                                        final String enteredPassword,
                                        final String f_name,
                                        final String l_name){
        // Once the password is verified, this is called to send a POST request to the
        //  server.


        try {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "https://teacher-attendance.herokuapp.com/tabackend/submit_attendance";

            JSONObject requestBody = new JSONObject();
            requestBody.put("password", "stayinschool");
            requestBody.put("entered_password", enteredPassword);
            requestBody.put("school_name", schoolName);
            requestBody.put("phone_number", "7035551891");
            //todo FIXME! Add GPS stuff
            requestBody.put("near_school", true);
            requestBody.put("f_name", f_name);
            requestBody.put("l_name", l_name);

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //if correct, post data
                                //if not correct, TOAST that PW is incorrect
                                if (! response.getBoolean("submission_successful"))
                                    Toast.makeText(context, "There was an error", Toast.LENGTH_LONG).show();
                                Log.d("Submission? ", Boolean.toString(response.getBoolean("submission_successful")));
                            } catch (JSONException  e){Log.d("Json issues", e.toString());}

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.d("error", error.toString());
                        }
                    });


            // Add the request to the RequestQueue.
            queue.add(jsonRequest);
        }catch(JSONException e) {Log.d("JsonIssue", e.toString());}

        return true;
    }


}
