package com.example.mernashenda.uangsaya;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import SlidingTabs.SlidingTabAdapter;
import cn.pedant.SweetAlert.SweetAlertDialog;
import tabs.KategoriPemasukan;
import tabs.KategoriPengeluaran;
import tabs.TabSurat;

public class KategoriActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    public String namakat, jeniskat, ketkat, iduser, IDGROUP, status_def, idjenis;
    public String nilaival;
    public EditText namakategori1;
    public Spinner cmjenistransaksi1;
    public EditText txtidjenistransaksi1;
    public EditText keterangan1;
    public TextView txtidd;
    int success;
    private static final String TAG_SUCCESS = "success";
    private Context context;
    private ProgressDialog pDialog;
    String IDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = KategoriActivity.this;

        pDialog = new ProgressDialog(context);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //txtidd = (TextView) findViewById(R.id.id_jenis_tr_2);

        //SharedPreferences sharedPreferences = this.getSharedPreferences(AppVar.SHARED_PREF_KAT, Context.MODE_PRIVATE);
        //IDD  = sharedPreferences.getString(AppVar.idjenis, "");
        //txtidd.setText(IDD);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addkategori();
            }
        });
    }

    public void addkategori() {
        dialog = new AlertDialog.Builder(KategoriActivity.this);
        inflater = KategoriActivity.this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        //dialog.setIcon(R.drawable.ic_shapes);
        dialog.setTitle("Add Kategori");

        namakategori1 = (EditText) dialogView.findViewById(R.id.namakategori);
        cmjenistransaksi1 = (Spinner) dialogView.findViewById(R.id.cmjenistransaksi);
        txtidjenistransaksi1 = (EditText) dialogView.findViewById(R.id.txtidjenistransaksi);
        keterangan1 = (EditText) dialogView.findViewById(R.id.keterangan);

        SharedPreferences sharedPreferences = getSharedPreferences(AppVar.SHARED_PREF_KAT,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppVar.idjenis, "");
        editor.putString(AppVar.namajenis, "");
        editor.putString(AppVar.jenistr, "");
        editor.putString(AppVar.idgroupjns, "");
        editor.putString(AppVar.keterangantr, "");
        editor.putString(AppVar.statusdef, "");
        editor.commit();

        dialog.setPositiveButton(R.string.simpankategori, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                simpan_update();
            }
        });

        dialog.setNegativeButton(R.string.keluarkategori, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //dialog.show();
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        //nbutton.setBackgroundColor(Color.YELLOW);
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //pbutton.setBackgroundColor(Color.YELLOW);
        pbutton.setTextColor(Color.BLACK);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void simpan_update() {
//Getting values from edit texts
        namakat = namakategori1.getText().toString();
        jeniskat = cmjenistransaksi1.getSelectedItem().toString();
        ketkat = keterangan1.getText().toString();
        idjenis = txtidjenistransaksi1.getText().toString();

        String statusname = cmjenistransaksi1.getSelectedItem().toString();
        String statusname1 = "Pemasukan";
        String statusname2 = "Pengeluaran";

        if (statusname.equals(statusname1)) {
            nilaival = "1";
        }
        else if (statusname.equals(statusname2)) {
            nilaival = "0";
        }

        //Toast.makeText(context, "tess " + kelompokakun1, Toast.LENGTH_LONG).show();

        SharedPreferences sharedPreferences = getSharedPreferences(AppVar.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        iduser = sharedPreferences.getString(AppVar.IDUSER_SHARED_PREF, "");
        IDGROUP = sharedPreferences.getString(AppVar.IDGROUP_SHARED_PREF, "");
        String nilai = "1"; //admin
        String nilai1 = "0";

        if (IDGROUP.equals(nilai)) {
            status_def = "1";
        }
        else if (IDGROUP.equals(nilai1)) {
            status_def = "0";
        }

        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Saving Process...");
        pDialog.setIndeterminate(true);
        pDialog.setCanceledOnTouchOutside(false);
        showDialog();
//Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppVar.KATEGORI_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//If we are getting success from server
                        //Toast.makeText(context, "tess " + response, Toast.LENGTH_LONG).show();
                        //cal json
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            //Toast.makeText(context, "tess " + jObj.getString("message"), Toast.LENGTH_LONG).show();
                            // Cek error node pada json
                            if (success == 1) {
                                //Toast.makeText(context, "Error: ", Toast.LENGTH_LONG).show();
                                hideDialog();
                                SweetAlertDialog pDialog = new SweetAlertDialog(KategoriActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.setTitleText("Informasi");
                                pDialog.setContentText("Simpan Data Kas/Bank Berhasil!");
                                pDialog.setCancelable(false);
                                pDialog.setConfirmText("OK");
                                pDialog.setConfirmClickListener(null);
                                pDialog.show();


                            } else {
                                hideDialog();
                                SweetAlertDialog pDialog1 = new SweetAlertDialog(KategoriActivity.this, SweetAlertDialog.ERROR_TYPE);
                                pDialog1.setTitleText("Informasi");
                                pDialog1.setContentText("Simpan Kas/Bank Gagal");
                                pDialog1.setCancelable(false);
                                pDialog1.setConfirmText("OK");
                                pDialog1.setConfirmClickListener(null);
                                pDialog1.show();
                                //Displaying an error message on toast
                                //Toast.makeText(context, "Register Gagal", Toast.LENGTH_LONG).show();
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
                params.put(AppVar.nama_jenis_transaksi, namakat);
                params.put(AppVar.id_jenis_transaksi, idjenis);
                params.put(AppVar.jenis_transaksi, jeniskat);
                params.put(AppVar.keterangan, ketkat);
                params.put(AppVar.id_group_transaksi, nilaival);
                params.put(AppVar.status_default, status_def);
                params.put(AppVar.id_user, iduser);
//returning parameter
                return params;
            }
        };
//Adding the string request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void setupViewPager(ViewPager viewPager) {
        KategoriActivity.ViewPagerAdapter adapter = new KategoriActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new KategoriPemasukan(), "Pemasukan");
        adapter.addFragment(new KategoriPengeluaran(), "Pengeluaran");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Toast.makeText(tentangkamiActivity.this,
        //        "Your Message", Toast.LENGTH_LONG).show();
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            super.onBackPressed();
            return true;
        }

        if (id == R.id.action_exit) {
            logout();
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
                        Intent intent = new Intent(KategoriActivity.this, MainActivity.class);
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
