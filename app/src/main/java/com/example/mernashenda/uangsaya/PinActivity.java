package com.example.mernashenda.uangsaya;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PinActivity extends AppCompatActivity {
    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    public static final String TAG = "PinLockView";
    private String nm_lengkap;
    private String no_hp;
    private String email;
    private String NIK;
    private String codepattern;
    private String Pincode;
    private ProgressDialog pDialog;
    private Context context;
    int success;
    String pesan;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_message = "message";
    public static final String TAG_ID       = "id_pengguna";

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.d(TAG, "Pin complete: " + pin);

            Pincode = pin;
            SharedPreferences sharedPreferences = getSharedPreferences(AppVar.SHARED_PREF_REGISTER, Context.MODE_PRIVATE);
            nm_lengkap = sharedPreferences.getString(AppVar.NMlengkap_SHARED_PREF, "");
            NIK = sharedPreferences.getString(AppVar.NIK_SHARED_PREF, "");
            email = sharedPreferences.getString(AppVar.EMAIL_SHARED_PREF, "");
            no_hp = sharedPreferences.getString(AppVar.NOHP_SHARED_PREF, "");

            //pattern
            SharedPreferences sharedPatternPreferences = getSharedPreferences(AppVar.SHARED_PREF_PATTERN, Context.MODE_PRIVATE);
            codepattern = sharedPatternPreferences.getString(AppVar.PATTTERN_SHARED_PREF, "");

            //this last step
            register();

            //Toast.makeText(PinActivity.this, pin, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };

    private void register() {
//Getting values from edit texts
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Register Process...");
        pDialog.setIndeterminate(true);
        pDialog.setCanceledOnTouchOutside(false);
        showDialog();
//Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppVar.DAFTAR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//If we are getting success from server
                        //cal json
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
                            //Toast.makeText(context, "tess " + success, Toast.LENGTH_LONG).show();
                            // Cek error node pada json
                            if (success == 1) {
                                //Toast.makeText(context, "Error: ", Toast.LENGTH_LONG).show();
                                hideDialog();

                                //saving
                                //Creating a shared preference
                                //SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(AppVar.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                //Creating editor to store values to shared preferences
                                //SharedPreferences.Editor editor = sharedPreferences.edit();

                                //Adding values to editor
                                //editor.putBoolean(AppVar.LOGGEDIN_SHARED_PREF, true);
                                //editor.putString(AppVar.EMAIL_SHARED_PREF, GlobalVariabel.GblUsername);

                                //Log.d("get data", jObj.toString());
                                //id_pengguna      = jObj.getString(TAG_ID);
                                //editor.putString(AppVar.ID_SHARED_PENGGUNA, id_pengguna);
                                //Toast.makeText(context, "Error: " + id_pengguna, Toast.LENGTH_LONG).show();
                                //Saving values to editor
                                //editor.commit();

                                //gotoCourseActivity();
                            } else {
                                hideDialog();
                                //Displaying an error message on toast
                                Toast.makeText(context, "Register Gagal", Toast.LENGTH_LONG).show();
                                //Toast.makeText(MainActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            Toast.makeText(context, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                            hideDialog();
                            //e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//You can handle error here if you want
                        hideDialog();
                        Toast.makeText(context, "The server unreachable", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//Adding parameters to request
                params.put(AppVar.Key_nmlengkap, nm_lengkap);
                params.put(AppVar.Key_NIK, NIK);
                params.put(AppVar.Key_email, email);
                params.put(AppVar.Key_no_hp, no_hp);
                params.put(AppVar.Key_kode_pattern, codepattern);
                params.put(AppVar.Key_pin_dompet, Pincode);
//returning parameter
                return params;
            }
        };
//Adding the string request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        context = PinActivity.this;
        pDialog = new ProgressDialog(context);
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        //mPinLockView.enableLayoutShuffling();

        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);
    }

}
