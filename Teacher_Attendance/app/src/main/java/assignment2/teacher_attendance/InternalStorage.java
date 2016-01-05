package assignment2.teacher_attendance;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jackson Breyer on 1/4/2016.
 */
public final class InternalStorage extends AppCompatActivity {
    static JSONObject schoolsAndTeachers = newJSONwithLoading();


    public static JSONObject newJSONwithLoading() {
        try {
            JSONObject obj = new JSONObject().put("Loading...", "Loading...");
            return obj;
        } catch (JSONException e) {

        }
        //shouldn't get here
        return null;
    }


}

