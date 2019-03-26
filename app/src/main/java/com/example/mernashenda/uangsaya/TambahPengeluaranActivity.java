package com.example.mernashenda.uangsaya;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import multispinnerfilter.KeyPairBoolData;
import multispinnerfilter.SingleSpinner;
import multispinnerfilter.SpinnerListener;

public class TambahPengeluaranActivity extends AppCompatActivity {
    private DatePicker datePicker;
    private Calendar calendar;
    TextView dateView, txt8;
    EditText nama, ket, nom, idtrxtx;
    private int year, month, day;
    private String pendidikanulr = "http://markaskerja.000webhostapp.com/uangsaya/combokat.php?id_user=";
    List<KeyPairBoolData> pendidikanarr = new ArrayList<KeyPairBoolData>();
    private SingleSpinner singlependidikan; //kota
    private static final String TAG = "TambahPengeluaranActivity";
    String jsonResponse, HASIL, Hasil1, Hasil2, iduser, jenis_transaksi, namatr, nominal1, ket1, idakun, idjenispengeluaran, tgltr, idtr;
    private String akunulr = "http://markaskerja.000webhostapp.com/uangsaya/comboakun.php?id_user=";
    List<KeyPairBoolData> pakunarr = new ArrayList<KeyPairBoolData>();
    private SingleSpinner singleakun; //kota
    Button btsimpan;

    int success;
    private ProgressDialog pDialog;
    private Context context;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_message = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pengeluaran);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = TambahPengeluaranActivity.this;
        pDialog = new ProgressDialog(context);
        Hasil1 = "1";
        Hasil2 = "0";

        txt8 = (TextView) findViewById(R.id.textview8);
        nama = (EditText) findViewById(R.id.namatrtxt);
        nom = (EditText) findViewById(R.id.txtnominal);
        ket = (EditText) findViewById(R.id.txtket);
        idtrxtx = (EditText) findViewById(R.id.txtidtr);
        btsimpan = (Button) findViewById(R.id.button9);

        SharedPreferences sharedPreferences = getSharedPreferences(AppVar.SHARED_PREF_KONDISI_TRANSAKSI, Context.MODE_PRIVATE);
        HASIL = sharedPreferences.getString(AppVar.SHARED_TRANSAKSI, "");

        SharedPreferences sharedPreferences1 = getSharedPreferences(AppVar.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        iduser = sharedPreferences1.getString(AppVar.IDUSER_SHARED_PREF, "");

        if (HASIL.equals(Hasil1)) {
            pendidikanulr = pendidikanulr + iduser + "&jenis_transaksi=Pemasukan";
            jenis_transaksi = "Pemasukan";
            setTitle("Add Pemasukan");
            txt8.setText("Ke Akun");
        }
        else if (HASIL.equals(Hasil2)){
            pendidikanulr = pendidikanulr + iduser + "&jenis_transaksi=Pengeluaran";
            jenis_transaksi = "Pengeluaran";
            setTitle("Add Pengeluaran");
            txt8.setText("Dari Akun");
        }

        //Toast.makeText(getApplicationContext(), iduser,
        //        Toast.LENGTH_SHORT)
        //       .show();

        akunulr = akunulr +  iduser;
        dateView = (TextView) findViewById(R.id.txtviepicker);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });

        SharedPreferences sharedPreferences2 = getSharedPreferences(AppVar.SHARED_PREF_TR, Context.MODE_PRIVATE);

        final String hasiltr = sharedPreferences2.getString(AppVar.SHARED_id_jenis_transaksi,"");
        if (hasiltr.equals("")){
            pendidikan(0);
        }
        else {
            pendidikan(Integer.parseInt(hasiltr));
        }

        singlependidikan = (SingleSpinner) findViewById(R.id.cmbkategorip);
        singlependidikan.setItems(pendidikanarr, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                //Toast.makeText(getApplicationContext(), String.valueOf(items.size()),
                //        Toast.LENGTH_SHORT)
                //        .show();

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        //Toast.makeText(getApplicationContext(), String.valueOf(i),
                        //        Toast.LENGTH_SHORT)
                        //        .show();
                        //Log.i(TAG, i + ":" + items.get(i).getName() + ":" + items.get(i).isSelected());
                    }
                }
            }
        });

        String hasilakun = sharedPreferences2.getString(AppVar.SHARED_id_akun_kas,"");
        if (hasilakun.equals("")){
            akun(0);
        }
        else {
            akun(Integer.parseInt(hasilakun));
        }

        singleakun = (SingleSpinner) findViewById(R.id.cmbakunp);
        //singleakun.setSelectedIndex(pakunarr,Integer.parseInt(hasilakun));
        singleakun.setItems(pakunarr, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        //Log.i(TAG, i + ":" + items.get(i).getName() + ":" + items.get(i).isSelected());
                    }
                }
            }
        });


        //show get data

        idtrxtx.setText(sharedPreferences2.getString(AppVar.SHARED_id_transaksi,""));
        nama.setText(sharedPreferences2.getString(AppVar.SHARED_nm_transaksi,""));
        ket.setText(sharedPreferences2.getString(AppVar.SHARED_keterangan,""));
        nom.setText(sharedPreferences2.getString(AppVar.SHARED_nominal,""));
        dateView.setText(sharedPreferences2.getString(AppVar.SHARED_tgl_transaksi,""));


        if (hasilakun.equals("")){
            //singleakun.setSelection(0);
        }
        else {

            //singleakun.on
            //singleakun.setSelection(0);
            //singleakun.setSelected(true);
            //singleakun.performClick();

            //singleakun.

            //singleakun.set;
            //singleakun.getSelectedIds().get(0);
            //singleakun.setSelected(true);
            //singleakun.setSelection(new int[]{0, 0});

            //singleakun.getChildAt(Integer.parseInt(hasilakun));
            //singleakun.setSelection(1);
            //singleakun.setId(Integer.parseInt(sharedPreferences2.getString(AppVar.SHARED_id_akun_kas,"")));
            /*for (int i = 0; i < singleakun.getCount(); i++) {
                //if (pakunarr.get(i).getId().toString() == hasilakun ){
                //   singleakun.setSelection(i);
                //}
            }*/
        }


        if (hasiltr.equals("")){
        }
        else {
            //singlependidikan.setSelected(true);
            //singlependidikan.setSelection(0);
            //Toast.makeText(getApplicationContext(), "tr"+hasiltr,
             //       Toast.LENGTH_SHORT)
             //       .show();

            //singlependidikan.getSelectedIds().get(0);


            //singlependidikan.setSelection(0);

            //singlependidikan.
            //singlependidikan.setId(Integer.parseInt(sharedPreferences2.getString(AppVar.SHARED_id_jenis_transaksi,"")));
            //for (int i = 0; i < pendidikanarr.size(); i++) {
            //    if (pendidikanarr.get(i).getId().toString() == hasiltr ){
            //        singlependidikan.setSelection(i);
            //    }
           // }
        }

        btsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nama.getText())) {
                    final SweetAlertDialog pDialog = new SweetAlertDialog(TambahPengeluaranActivity.this, SweetAlertDialog.WARNING_TYPE);
                    pDialog.setTitleText("Informasi");
                    pDialog.setContentText("Nama Transaksi Harus Diisi!");
                    pDialog.setCancelable(false);
                    pDialog.setConfirmText("OK");
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance
                            //startActivity(new Intent(TambahAkunActivity.this,KasActivity.class));
                            pDialog.hide();
                            nama.requestFocus();
                        }
                    });
                    pDialog.show();

                }
                else if (TextUtils.isEmpty(nom.getText())) {
                    final SweetAlertDialog pDialog = new SweetAlertDialog(TambahPengeluaranActivity.this, SweetAlertDialog.WARNING_TYPE);
                    pDialog.setTitleText("Informasi");
                    pDialog.setContentText("Nominal Harus Diisi!");
                    pDialog.setCancelable(false);
                    pDialog.setConfirmText("OK");
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance
                            //startActivity(new Intent(TambahAkunActivity.this,KasActivity.class));
                            pDialog.hide();
                            nom.requestFocus();
                        }
                    });
                    pDialog.show();

                }
                else if (TextUtils.isEmpty(ket.getText())) {
                    final SweetAlertDialog pDialog = new SweetAlertDialog(TambahPengeluaranActivity.this, SweetAlertDialog.WARNING_TYPE);
                    pDialog.setTitleText("Informasi");
                    pDialog.setContentText("Keterangan Harus Diisi!");
                    pDialog.setCancelable(false);
                    pDialog.setConfirmText("OK");
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance
                            //startActivity(new Intent(TambahAkunActivity.this,KasActivity.class));
                            pDialog.hide();
                            ket.requestFocus();
                        }
                    });
                    pDialog.show();
                }
                else {
                    namatr = nama.getText().toString();
                    ket1 = ket.getText().toString();
                    nominal1 = nom.getText().toString();
                    String replaceString1 = singlependidikan.getSelectedIds().toString();
                    String replaceString = replaceString1.replace("[","");//replaces all occurrences of "is" to "was"
                    idjenispengeluaran = replaceString.replace("]","");//replaces all occurrences of "is" to "was"
                    idjenispengeluaran = idjenispengeluaran;

                    String replaceString2 = singleakun.getSelectedIds().toString();
                    //Toast.makeText(getApplicationContext(), replaceString2,
                     //       Toast.LENGTH_SHORT)
                     //       .show();
                    String replaceString3 = replaceString2.replace("[","");//replaces all occurrences of "is" to "was"
                    idakun = replaceString3.replace("]","");//replaces all occurrences of "is" to "was"
                    idakun = idakun;

                    tgltr = dateView.getText().toString();
                    idtr = idtrxtx.getText().toString();

                    tambahtindakan();
                }
            }
        });

    }

    private void tambahtindakan() {
//Getting values from edit texts
        //int replaceString4 = singlependidikan.getId();
       // Toast.makeText(getApplicationContext(), String.valueOf(replaceString4),
        //       Toast.LENGTH_SHORT)
         //    .show();

        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Saving Process...");
        pDialog.setIndeterminate(true);
        pDialog.setCanceledOnTouchOutside(false);
        showDialog();
//Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppVar.DATA_URL3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//If we are getting success from server
                        //cal json
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            Toast.makeText(getApplicationContext(), response,
                                    Toast.LENGTH_SHORT)
                                    .show();
                            //Toast.makeText(context, "tess " + response, Toast.LENGTH_LONG).show();
                            // Cek error node pada json
                            if (success == 1) {
                                //Toast.makeText(context, "Error: ", Toast.LENGTH_LONG).show();
                                hideDialog();
                                final SweetAlertDialog pDialog = new SweetAlertDialog(TambahPengeluaranActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.setTitleText("Informasi");
                                pDialog.setContentText("Simpan Data Transaksi Berhasil");
                                pDialog.setCancelable(false);
                                pDialog.setConfirmText("OK");
                                //pDialog.setConfirmClickListener(null);
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        // reuse previous dialog instance
                                        //startActivity(new Intent(TambahAkunActivity.this,KasActivity.class));
                                        pDialog.hide();
                                        finish();
                                    }
                                });
                                pDialog.show();
                            } else {
                                hideDialog();
                                final SweetAlertDialog pDialog1 = new SweetAlertDialog(TambahPengeluaranActivity.this, SweetAlertDialog.ERROR_TYPE);
                                pDialog1.setTitleText("Informasi");
                                pDialog1.setContentText("Simpan Data Transaksi Gagal");
                                pDialog1.setCancelable(false);
                                pDialog1.setConfirmText("OK");
                                pDialog1.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        // reuse previous dialog instance
                                        //startActivity(new Intent(TambahAkunActivity.this,KasActivity.class));
                                        pDialog1.hide();
                                        finish();
                                    }
                                });
                                pDialog1.show();
                                //Displaying an error message on toast
                                //Toast.makeText(context, "Invalid username or password", Toast.LENGTH_LONG).show();
                                //Toast.makeText(MainActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            hideDialog();
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
                params.put(AppVar.nama_transaksi, namatr);
                params.put(AppVar.id_jenis_transaksi1, idjenispengeluaran);
                params.put(AppVar.jenis_transaksi1, jenis_transaksi);
                params.put(AppVar.keterangan1, ket1);
                params.put(AppVar.id_akun1, idakun);
                params.put(AppVar.id_transaksi, idtr);
                params.put(AppVar.nominal, nominal1);
                params.put(AppVar.tgl_transaksi, tgltr);
                params.put(AppVar.id_user1, iduser);
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

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        //Toast.makeText(getApplicationContext(), "ca",
        //        Toast.LENGTH_SHORT)
        //        .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(year).append("-").append(month).append("-")
                .append(day));
    }

    private void pendidikan(final int ID) {
        JsonArrayRequest reqpendidikan = new JsonArrayRequest(pendidikanulr,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d(TAG, response.toString());
                        pendidikanarr.clear();
                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                String idd = person.getString("id_jenis_transaksi");
                                String name = person.getString("nm_jenis_transaksi");
                                KeyPairBoolData a = new KeyPairBoolData();
                                a.setId(idd);
                                a.setName(name);
                                a.setSelected(false);

                                if (String.valueOf(ID).equals(idd)) {
                                    //Toast.makeText(context, "Invalid username or password", Toast.LENGTH_LONG).show();
                                    a.setSelected(true);
                                }

                                //maxpendidikanarr.add(a);
                                pendidikanarr.add(a);

                            }

                            if (String.valueOf(ID).equals("0")) {
                            }
                            else {
                                singlependidikan.setSelectedIndex(pendidikanarr,ID);
                            }

                            //txtResponse.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(req);
        Volley.newRequestQueue(this).add(reqpendidikan);
    }

    private void akun(final int ID) {
        JsonArrayRequest reqpendidikan = new JsonArrayRequest(akunulr,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d(TAG, response.toString());
                        pakunarr.clear();
                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                String idd = person.getString("id_akun");
                                String name = person.getString("nm_akun");
                                KeyPairBoolData a = new KeyPairBoolData();
                                a.setId(idd);
                                a.setName(name);
                                a.setSelected(false);

                                if (String.valueOf(ID).equals(idd)){
                                    a.setSelected(true);
                                }

                                pakunarr.add(a);

                            }

                            if (String.valueOf(ID).equals("0")) {
                            }
                            else {
                                singleakun.setSelectedIndex(pakunarr,ID);
                            }
                            //txtResponse.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(req);
        Volley.newRequestQueue(this).add(reqpendidikan);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Toast.makeText(TentangActivity.this, id, Toast.LENGTH_LONG).show();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        if (id == R.id.action_exit) {
            logout();
            //return true;
        }

        if (id == R.id.action_bookmark) {
            //logout();
            startActivity(new Intent(TambahPengeluaranActivity.this,TambahPengeluaranActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(AppVar.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(AppVar.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(AppVar.EMAIL_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();

                        //Starting login activity
                        Intent intent = new Intent(TambahPengeluaranActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        //nbutton.setBackgroundColor(Color.YELLOW);
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //pbutton.setBackgroundColor(Color.YELLOW);
        pbutton.setTextColor(Color.BLACK);


    }

}
