package com.example.mernashenda.uangsaya;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapter.OnLoadMoreTransaksi;
import Adapter.TRAdapter;
import Model.PGModel;

public class PengeluaranActivity extends AppCompatActivity {
    String HASIL;
    String Hasil1;
    String Hasil2;
    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private TRAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private static final String TAG = "PengeluaranActivity";
    private List<PGModel> modeldataList;
    String gabung;
    protected Handler handler;
    private ProgressDialog pDialog;
    //Volley Request Queue
    private RequestQueue requestQueue;
    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;
    private int LIMIT = 20;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    String ID_USER, JENIS_TRANSAKSI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        HASIL = bundle.getString("kondisi");
        Hasil1 = "1";
        Hasil2 = "0";

        //Toast.makeText(getApplicationContext(), HASIL,
        //        Toast.LENGTH_SHORT)
        //        .show();

        SharedPreferences sharedPreferences = PengeluaranActivity.this.getSharedPreferences(AppVar.SHARED_PREF_KONDISI_TRANSAKSI, Context.MODE_PRIVATE);
        //Creating editor to store values to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Adding values to editor
        editor.putString(AppVar.SHARED_TRANSAKSI, HASIL);
        editor.commit();
        //SharedPreferences sharedPreferences = getSharedPreferences(AppVar.SHARED_PREF_KONDISI_TRANSAKSI, Context.MODE_PRIVATE);
        //HASIL = sharedPreferences.getString(AppVar.SHARED_TRANSAKSI, "");

        if (HASIL.equals(Hasil1)) {
            setTitle("Pemasukan");
            JENIS_TRANSAKSI = "Pemasukan";
        }
        else if (HASIL.equals(Hasil2)) {
            setTitle("Pengeluaran");
            JENIS_TRANSAKSI = "Pengeluaran";
        }

        SharedPreferences sharedPreferences1 = getSharedPreferences(AppVar.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        ID_USER = sharedPreferences1.getString(AppVar.IDUSER_SHARED_PREF, "");

        try {
            pDialog = new ProgressDialog(this);
            tvEmptyView = (TextView) findViewById(R.id.empty_viewst);
            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_viewst);
            handler = new Handler();
            modeldataList = new ArrayList<PGModel>();

            modeldataList.clear(); //clear list
            mAdapter = new TRAdapter(modeldataList, mRecyclerView);
            mRecyclerView.setAdapter(mAdapter);

            //Toast.makeText(PengeluaranActivity.this, "Error: stu7 " + ID_USER, Toast.LENGTH_LONG).show();
            gabung = AppVar.DATA_URL2 + requestCount + "&id_user=" + ID_USER + "&jenis_transaksi=" + JENIS_TRANSAKSI;

            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Process...");
            pDialog.setIndeterminate(true);
            pDialog.setCanceledOnTouchOutside(false);
            showDialog();

            load_data_first();

            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutst);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // do something
                    mSwipeRefreshLayout.setRefreshing(true);
                    requestCount = 1;
                    gabung = AppVar.DATA_URL2 + requestCount + "&id_user=" + ID_USER + "&jenis_transaksi=" + JENIS_TRANSAKSI;
                    modeldataList = new ArrayList<PGModel>();
                    load_data_first();

                    // after refresh is done, remember to call the following code
                    if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                        //Toast.makeText(MainActivity.this, "tes0", Toast.LENGTH_SHORT).show();
                        mAdapter.notifyDataSetChanged();
                        //mSwipeRefreshLayout.setEnabled(mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0);
                        mSwipeRefreshLayout.setRefreshing(false);  // This hides the spinner
                    }
                }
            });
        }

        catch (Exception e) {
            Toast.makeText(PengeluaranActivity.this, "Error: stu7 " + e.toString(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        //requestCount = 1;
        //gabung = AppVar.DATA_URL + requestCount + "&kode_unker=" + kodeunker + "&kode_unor=" + kodeunor + "&kode_jab=" + kodejab;
        //modeldataList = new ArrayList<ModelSuratMasuk>();
        //load_data_first();
        //mAdapter = new SecondAdapter(modeldataList, mRecyclerView);

        mAdapter.notifyDataSetChanged();
        //mRecyclerView.invalidate();
        //Toast.makeText(getActivity(), "Error: stu7 ", Toast.LENGTH_LONG).show();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void load_data_first() {
        //Toast.makeText(getActivity(), kodejab, Toast.LENGTH_SHORT).show();
        try {
            //Toast.makeText(getActivity(), gabung, Toast.LENGTH_SHORT).show();

            JsonArrayRequest reqsubkategori = new JsonArrayRequest(gabung,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());

                            try {
                                //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                                hideDialog();

                                if (response.length() > 0) {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject person = (JSONObject) response
                                                .get(i);

                                        if (person.getInt("statuspesan") == 1){
                                            String id_transaksi = person.getString("id_transaksi");
                                            String nm_transaksi = person.getString("nm_transaksi");
                                            String keterangan = person.getString("keterangan");
                                            String id_user_input = person.getString("id_user_input");
                                            String tgl_transaksi = person.getString("tgl_transaksi");
                                            String id_jenis_transaksi = person.getString("id_jenis_transaksi");
                                            String id_akun_kas = person.getString("id_akun_kas");
                                            String nm_akun = person.getString("nm_akun");
                                            String nm_jenis_transaksi = person.getString("nm_jenis_transaksi");
                                            String jenis_transaksi = person.getString("jenis_transaksi");
                                            String nominal = person.getString("nominal");

                                            modeldataList.add(new PGModel(id_transaksi, nm_transaksi, keterangan, tgl_transaksi, id_jenis_transaksi, jenis_transaksi, id_akun_kas, id_user_input, nm_akun, nm_jenis_transaksi, nominal));
                                        }
                                        /*"id_transaksi"=>$row['id_transaksi'],
        	    "nm_transaksi"=>$row['nm_transaksi'],
        	    "jenis_transaksi"=>$row['jenis_transaksi'],
        	    "keterangan"=>$row['keterangan'],
        	    "tgl_transaksi"=>$row['tgl_transaksi'],
        	    "id_jenis_transaksi"=>$row['id_jenis_transaksi'],
        	    "id_akun_kas"=>$row['id_akun_kas'],
        	    "tgl_entry"=>$row['tgl_entry'],
        	    "id_user_input"=>$row['id_user_input'],
        	    "nm_akun"=>$row['nm_akun'],
        	    "nm_jenis_transaksi"=>$row['nm_jenis_transaksi'],*/

                                        //modeldataList.add(new SuratTerkirimModel(id_surat,tgl_surat,nomor_surat,jenis_surat,id_penerima,status_penerima,perihal,nm_file,nm_folder,kepada,dari,status_baca));
                                    }

                                    progresafter();
                                }

                            } catch (JSONException e) {
                                hideDialog();
                                e.printStackTrace();
                                Toast.makeText(PengeluaranActivity.this,
                                        "Error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(PengeluaranActivity.this,"Error: stu1 " +
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Adding request to request queue
            //AppController.getInstance().addToRequestQueue(req);
            Volley.newRequestQueue(PengeluaranActivity.this).add(reqsubkategori);
        }
        catch (Exception e) {
            Toast.makeText(PengeluaranActivity.this, "Error: stu0 " + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void progresafter() {
        try {
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            // create an Object for Adapter
            mAdapter = new TRAdapter(modeldataList, mRecyclerView);
            // set the adapter object to the Recyclerview
            mRecyclerView.setAdapter(mAdapter);

            if (modeldataList.isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
                tvEmptyView.setVisibility(View.VISIBLE);
                //Toast.makeText(MainActivity.this, " row bernilai 0 ", Toast.LENGTH_SHORT).show();
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                tvEmptyView.setVisibility(View.GONE);
                //Toast.makeText(getActivity(), " row bernilai 0 ", Toast.LENGTH_SHORT).show();
            }

            mAdapter.setOnLoadMoreListener(new OnLoadMoreTransaksi() {
                @Override
                public void onLoadMoreTransaksi() {
                    //add null , so the adapter will check view_type and show progress bar at bottom
                    modeldataList.add(null);
                    mAdapter.notifyItemInserted(modeldataList.size() - 1);
                    //Toast.makeText(MainActivity.this, " set onload more ", Toast.LENGTH_SHORT).show();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //   remove progress item
                            modeldataList.remove(modeldataList.size() - 1);
                            mAdapter.notifyItemRemoved(modeldataList.size());
                            requestCount = requestCount + 1;
                            onscrolldatax(requestCount);
                        }
                    }, 2000);

                }
            });
        }
        catch (Exception e) {
            Toast.makeText(PengeluaranActivity.this, "Error: stu" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void onscrolldatax(int start) {
        gabung = AppVar.DATA_URL2 + requestCount + "&id_user=" + ID_USER + "&jenis_transaksi=" + JENIS_TRANSAKSI;

        JsonArrayRequest onscrolldata = new JsonArrayRequest(gabung,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject person = (JSONObject) response
                                        .get(i);

                                //Toast.makeText(MainActivity.this, person.getString("statuspesan"), Toast.LENGTH_SHORT).show();
                                if (person.getInt("statuspesan") == 1){
                                    String id_transaksi = person.getString("id_transaksi");
                                    String nm_transaksi = person.getString("nm_transaksi");
                                    String keterangan = person.getString("keterangan");
                                    String id_user_input = person.getString("id_user_input");
                                    String tgl_transaksi = person.getString("tgl_transaksi");
                                    String id_jenis_transaksi = person.getString("id_jenis_transaksi");
                                    String id_akun_kas = person.getString("id_akun_kas");
                                    String nm_akun = person.getString("nm_akun");
                                    String nm_jenis_transaksi = person.getString("nm_jenis_transaksi");
                                    String jenis_transaksi = person.getString("jenis_transaksi");
                                    String nominal = person.getString("nominal");

                                    modeldataList.add(new PGModel(id_transaksi, nm_transaksi, keterangan, tgl_transaksi, id_jenis_transaksi, jenis_transaksi, id_akun_kas, id_user_input, nm_akun, nm_jenis_transaksi, nominal));

                                    //modeldataList.add(new SuratTerkirimModel(id_surat,tgl_surat,nomor_surat,jenis_surat,id_penerima,status_penerima,perihal,nm_file,nm_folder,kepada,dari,status_baca));
                                    mAdapter.notifyItemInserted(modeldataList.size());
                                }
                                else {
                                    String isipesan = person.getString("isipesan");
                                    //Toast.makeText(getActivity(), isipesan, Toast.LENGTH_SHORT).show();
                                }
                            }

                            mAdapter.setLoaded();

                        } catch (JSONException e) {
                            //hideDialog();
                            e.printStackTrace();
                            Toast.makeText(PengeluaranActivity.this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //hideDialog();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(PengeluaranActivity.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(req);
        Volley.newRequestQueue(this).add(onscrolldata);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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

            SharedPreferences sharedPreferences = getSharedPreferences(AppVar.SHARED_PREF_TR,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(AppVar.SHARED_id_transaksi, "");
            editor.putString(AppVar.SHARED_nm_transaksi, "");
            editor.putString(AppVar.SHARED_jenis_transaksi,"");
            editor.putString(AppVar.SHARED_tgl_transaksi, "");
            editor.putString(AppVar.SHARED_keterangan, "");
            editor.putString(AppVar.SHARED_nominal, "");
            editor.putString(AppVar.SHARED_id_jenis_transaksi, "");
            editor.putString(AppVar.SHARED_id_akun_kas, "");
            editor.putString(AppVar.SHARED_nm_akun, "");
            editor.putString(AppVar.SHARED_nm_jenis_transaksi, "");
            editor.commit();

            startActivity(new Intent(PengeluaranActivity.this,TambahPengeluaranActivity.class));
            /*SharedPreferences sharedPreferences = getSharedPreferences(AppVar.SHARED_PREF_GROUP, Context.MODE_PRIVATE);

            //Creating editor to store values to shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();

            //Adding values to editor
            editor.putString(AppVar.ID_GROUP_SHARED_PREF2, "");
            editor.putString(AppVar.NAMA_GROUP_SHARED_PREF2, "");
            editor.putString(AppVar.KODEBARCODE_GROUP_SHARED_PREF2, "");
            editor.commit();

            startActivity(new Intent(ListGroupActivity.this,TambahGroupActivity.class));*/
            //return true;
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
                        Intent intent = new Intent(PengeluaranActivity.this, MainActivity.class);
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
