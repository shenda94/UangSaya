package com.example.mernashenda.uangsaya;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.provider.Settings.Secure;
import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;
import com.andrognito.rxpatternlockview.RxPatternLockView;
import com.andrognito.rxpatternlockview.events.PatternLockCompleteEvent;
import com.andrognito.rxpatternlockview.events.PatternLockCompoundEvent;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.reactivex.functions.Consumer;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import java.util.List;
import java.util.Map;

public class KodeUnikActivity extends AppCompatActivity {
    private PatternLockView mPatternLockView;
    private String nm_lengkap;
    private String no_hp;
    private String email;
    private String NIK;
    private ProgressDialog pDialog;
    private Context context;
    int success;
    String pesan;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_message = "message";
    public static final String TAG_ID       = "id_pengguna";
    private String codepattern;
    private String Pincode;
    NiftyDialogBuilder dialogBuilder;
    String id_user, imeiandroid, no_hp1, email1, nm_lengkap1, NIK1, idgroup;

    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            //Log.d(getClass().getName(), "Pattern complete: " +
            //        PatternLockUtils.patternToString(mPatternLockView, pattern));
            codepattern = PatternLockUtils.patternToString(mPatternLockView, pattern);

            //SharedPreferences sharedPreferences = KodeUnikActivity.this.getSharedPreferences(AppVar.SHARED_PREF_PATTERN, Context.MODE_PRIVATE);

            //Creating editor to store values to shared preferences
            //SharedPreferences.Editor editor = sharedPreferences.edit();

            //Adding values to editor
            //editor.putString(AppVar.PATTTERN_SHARED_PREF, PatternLockUtils.patternToString(mPatternLockView, pattern));
            //.commit();

            //Toast.makeText(KodeUnikActivity.this, PatternLockUtils.patternToString(mPatternLockView, pattern), Toast.LENGTH_LONG).show();
            //startActivity(new Intent(KodeUnikActivity.this,PinActivity.class));

            //new edit
            SharedPreferences sharedPreferences = getSharedPreferences(AppVar.SHARED_PREF_REGISTER, Context.MODE_PRIVATE);
            nm_lengkap = sharedPreferences.getString(AppVar.NMlengkap_SHARED_PREF, "");
            NIK = sharedPreferences.getString(AppVar.NIK_SHARED_PREF, "");
            email = sharedPreferences.getString(AppVar.EMAIL_SHARED_PREF, "");
            no_hp = sharedPreferences.getString(AppVar.NOHP_SHARED_PREF, "");
            String android_id = Secure.getString(KodeUnikActivity.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Pincode = android_id;
            register();
        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kode_unik);
        context = KodeUnikActivity.this;
        pDialog = new ProgressDialog(context);
        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        mPatternLockView = (PatternLockView) findViewById(R.id.patter_lock_view);
        mPatternLockView.setDotCount(3);
        mPatternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        mPatternLockView.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        mPatternLockView.setPathWidth((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        mPatternLockView.setAspectRatioEnabled(true);
        mPatternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS);
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
        mPatternLockView.setDotAnimationDuration(150);
        mPatternLockView.setPathEndAnimationDuration(100);
        mPatternLockView.setCorrectStateColor(ResourceUtils.getColor(this, R.color.white));
        mPatternLockView.setInStealthMode(false);
        mPatternLockView.setTactileFeedbackEnabled(true);
        mPatternLockView.setInputEnabled(true);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);

        RxPatternLockView.patternComplete(mPatternLockView)
                .subscribe(new Consumer<PatternLockCompleteEvent>() {
                    @Override
                    public void accept(PatternLockCompleteEvent patternLockCompleteEvent) throws Exception {
                        Log.d(getClass().getName(), "Complete: " + patternLockCompleteEvent.getPattern().toString());
                    }
                });

        RxPatternLockView.patternChanges(mPatternLockView)
                .subscribe(new Consumer<PatternLockCompoundEvent>() {
                    @Override
                    public void accept(PatternLockCompoundEvent event) throws Exception {
                        if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_STARTED) {
                            Log.d(getClass().getName(), "Pattern drawing started");
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_PROGRESS) {
                            Log.d(getClass().getName(), "Pattern progress: " +
                                    PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_COMPLETE) {
                            Log.d(getClass().getName(), "Pattern complete: " +
                                    PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_CLEARED) {
                            Log.d(getClass().getName(), "Pattern has been cleared");
                        }
                    }
                });
    }

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
                        //Toast.makeText(context, "tess " + response, Toast.LENGTH_LONG).show();
                        //cal json
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
                            id_user = jObj.getString("id_user");

                            nm_lengkap1 = jObj.getString("nm_lengkap");
                            email1 = jObj.getString("email");
                            no_hp1 = jObj.getString("no_hp");
                            NIK1= jObj.getString("NIK");
                            imeiandroid = jObj.getString("androidid");
                            idgroup = jObj.getString("id_group");

                            //Toast.makeText(context, "tess " + success, Toast.LENGTH_LONG).show();
                            // Cek error node pada json
                            if (success == 1) {
                                //Toast.makeText(context, "Error: ", Toast.LENGTH_LONG).show();
                                hideDialog();

                                dialogBuilder
                                        .withTitle("Register")                                  //.withTitle(null)  no title
                                        .withTitleColor("#FFFFFF")                                  //def
                                        .withDividerColor("#11000000")                              //def
                                        .withMessage("Pendaftaran Telah Berhasil")                     //.withMessage(null)  no Msg
                                        .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                                        .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)                               //def
                                        .withIcon(getResources().getDrawable(R.mipmap.ic_launcher))
                                        .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                                        .withDuration(700)                                          //def
                                        .withEffect(Effectstype.Slidetop)                                         //def Effectstype.Slidetop
                                        .withButton1Text("Ya")                                      //def gone
                                        .withButton2Text("Tidak")                                  //def gone
                                        .setCustomView(R.layout.custom_view,KodeUnikActivity.this)         //.setCustomView(View or ResId,context)
                                        .setButton1Click(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //Creating a shared preference
                                                SharedPreferences sharedPreferences = KodeUnikActivity.this.getSharedPreferences(AppVar.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                                //Creating editor to store values to shared preferences
                                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                                editor.putBoolean(AppVar.LOGGEDIN_SHARED_PREF, true);

                                                //Adding values to editor
                                                editor.putString(AppVar.USERNAME_SHARED_PREF, nm_lengkap1);
                                                //jObj.getString("id_user")
                                                //Adding values to editor
                                                editor.putString(AppVar.IDUSER_SHARED_PREF, id_user);

                                                //Adding values to editor
                                                editor.putString(AppVar.MAIL_SHARED_PREF, email1);
                                                //jObj.getString("id_user")
                                                //Adding values to editor
                                                editor.putString(AppVar.HPNO_SHARED_PREF, no_hp1);

                                                //Adding values to editor
                                                editor.putString(AppVar.imeihp1_SHARED_PREF, imeiandroid);
                                                //jObj.getString("id_user")
                                                //Adding values to editor
                                                editor.putString(AppVar.nik1_SHARED_PREF, NIK1);

                                                //Adding values to editor
                                                editor.putString(AppVar.IDGROUP_SHARED_PREF, idgroup);

                                                editor.commit();

                                                startActivity(new Intent(KodeUnikActivity.this,HomeActivity.class));
                                                //Toast.makeText(v.getContext(), "i'm btn1", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setButton2Click(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(KodeUnikActivity.this,MainActivity.class));
                                                //Toast.makeText(v.getContext(), "i'm btn2", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .show();

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
                                //NiftyDialogBuilder dialogbuilder = NiftyDialogBuilder.getInstance(this);
                                //Toast.makeText(context, "Register Berhasil", Toast.LENGTH_LONG).show();
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
}
