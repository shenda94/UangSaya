package com.example.mernashenda.uangsaya;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import Adapter.RecyclerItemTouchHelper;
import Adapter.RecyclerTouchListener;
import Adapter.kasadapter;
import Model.Kasmodel;

public class KasActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private static final String TAG = "KasActivity";
    private List<Kasmodel> daftarSurahsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private kasadapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private String urlJsonArry = "http://markaskerja.000webhostapp.com/uangsaya/datakas.php?id_user=";
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KasActivity.this,TambahAkunActivity.class));
                SharedPreferences sharedPreferences = getSharedPreferences(AppVar.SHARED_PREF_KAS,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(AppVar.LOGGEDIN_SHARED_PREF_KAS, "0");
                editor.putString(AppVar.idakun, "");
                editor.putString(AppVar.nomor_kas, "");
                editor.putString(AppVar.nama_akun, "");
                editor.putString(String.valueOf(AppVar.saldo), String.valueOf(0.0));

                editor.putString(AppVar.kelompok_akun, "");
                editor.putString(AppVar.pin_atm, "");
                editor.putString(AppVar.pin_internet, "");
                editor.putString(AppVar.status_kas, "");
                editor.commit();
                //finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view1);
        mAdapter = new kasadapter(daftarSurahsList);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Kasmodel dftrsurah = daftarSurahsList.get(position);

                SharedPreferences sharedPreferences = getSharedPreferences(AppVar.SHARED_PREF_KAS,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(AppVar.LOGGEDIN_SHARED_PREF_KAS, "1");
                editor.putString(AppVar.idakun, dftrsurah.getId_akun());
                editor.putString(AppVar.nomor_kas, dftrsurah.getNomor_akun());
                editor.putString(AppVar.nama_akun, dftrsurah.getNm_akun());
                editor.putString(String.valueOf(AppVar.saldo), String.valueOf(dftrsurah.getSaldo()));

                editor.putString(AppVar.kelompok_akun, dftrsurah.getKelompok_akun());
                editor.putString(AppVar.pin_atm, dftrsurah.getPinatm());
                editor.putString(AppVar.pin_internet, dftrsurah.getPininternet());
                editor.putString(AppVar.status_kas, dftrsurah.getStatus_default());
                editor.commit();

                Intent it = new Intent(KasActivity.this, TambahAkunActivity.class);
                startActivity(it);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // do something
                mSwipeRefreshLayout.setRefreshing(true);
                daftarSurahsList = new ArrayList<Kasmodel>();
                makeJsonArrayRequest();
                mAdapter = new kasadapter(daftarSurahsList);
                recyclerView.setAdapter(mAdapter);

                // after refresh is done, remember to call the following code
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);  // This hides the spinner
                }
            }
        });

        makeJsonArrayRequest();
    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof kasadapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = daftarSurahsList.get(viewHolder.getAdapterPosition()).getNm_akun();

            // backup of removed item for undo purpose
            final Kasmodel deletedItem = daftarSurahsList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from list!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private void makeJsonArrayRequest() {
        SharedPreferences sharedPreferences = getSharedPreferences(AppVar.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String namaku = sharedPreferences.getString(AppVar.IDUSER_SHARED_PREF, "");

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry + namaku,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                        try {
                            if (response.length() == 0) {
                            }
                            else {
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject person = (JSONObject) response
                                            .get(i);

                                    String id_akun = person.getString("id_akun");
                                    String nomor_akun = person.getString("nomor_akun");
                                    String nm_akun = person.getString("nm_akun");
                                    double saldo = person.getDouble("saldo");

                                    String status_default2 = person.getString("status_default");
                                    String status_default3 = "";
                                    String statusname1 = "1";
                                    String statusname2 = "0";
                                    if (status_default2.equals(statusname1)) {
                                        status_default3 = "Default";
                                    }
                                    else if (status_default2.equals(statusname2)) {
                                        status_default3 = "Other";
                                    }
                                    String kelompok_akun = person.getString("kelompok_akun");
                                    String pinatm = person.getString("pinatm");
                                    String pininternet = person.getString("pininternet");

                                    Kasmodel dftrsurah = new Kasmodel(id_akun,nomor_akun,nm_akun,saldo,status_default3,kelompok_akun,pinatm,pininternet);
                                    daftarSurahsList.add(dftrsurah);
                                    mAdapter.notifyDataSetChanged();
                                }
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
        Volley.newRequestQueue(this).add(req);
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
                        Intent intent = new Intent(KasActivity.this, MainActivity.class);
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

        //try {
        //    int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
        //    TextView tv = (TextView) alertDialog.findViewById(textViewId);
        //    tv.setTextColor(getResources().getColor(R.color.red));
        //}
        //catch (Exception e) {
        //    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        //}


        alertDialog.show();

        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        //nbutton.setBackgroundColor(Color.YELLOW);
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //pbutton.setBackgroundColor(Color.YELLOW);
        pbutton.setTextColor(Color.BLACK);


    }
}
