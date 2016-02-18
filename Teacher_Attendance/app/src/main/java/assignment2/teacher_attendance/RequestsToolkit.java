package assignment2.teacher_attendance;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://teacher-attendance.herokuapp.com/tabackend/get_all_schools_and_teachers";


        /*
            Add the password to the request
            create a textview to pass in, for testing

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

}
