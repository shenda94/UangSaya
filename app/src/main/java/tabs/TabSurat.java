package tabs;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mernashenda.uangsaya.AppVar;
import com.example.mernashenda.uangsaya.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import Model.ModelSuratMasuk;
//import Model.OnLoadMoreListener;
//import Model.SecondAdapter;

/**
 * Created by merna.shenda on 4/26/2018.
 */

public class TabSurat extends Fragment {
    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    //private SecondAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private static final String TAG = "MainActivity";
    //private List<ModelSuratMasuk> modeldataList;
    String gabung;
    protected Handler handler;
    private ProgressDialog pDialog;
    //Volley Request Queue
    private RequestQueue requestQueue;
    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;
    private int LIMIT = 20;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    String kodejab;
    String kodeunor;
    String kodeunker;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_surat_masuk,container,false);
        /*try {
            pDialog = new ProgressDialog(getActivity());
            tvEmptyView = (TextView) v.findViewById(R.id.empty_view);
            mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
            handler = new Handler();
            modeldataList = new ArrayList<ModelSuratMasuk>();

            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(AppVar.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            kodejab  = sharedPreferences.getString(AppVar.KODEJAB_SHARED_PREF, "");
            kodeunor = sharedPreferences.getString(AppVar.KODEUNOR_SHARED_PREF, "");
            kodeunker = sharedPreferences.getString(AppVar.KODEUNKER_SHARED_PREF, "");
            //Toast.makeText(getActivity(), "Error: stu7 " + kodejab, Toast.LENGTH_LONG).show();
            gabung = AppVar.DATA_URL + requestCount + "&kode_unker=" + kodeunker + "&kode_unor=" + kodeunor + "&kode_jab=" + kodejab;

            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Process...");
            pDialog.setIndeterminate(true);
            pDialog.setCanceledOnTouchOutside(false);
            showDialog();

            load_data_first();

            mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // do something
                    mSwipeRefreshLayout.setRefreshing(true);
                    requestCount = 1;
                    gabung = AppVar.DATA_URL + requestCount + "&kode_unker=" + kodeunker + "&kode_unor=" + kodeunor + "&kode_jab=" + kodejab;
                    modeldataList = new ArrayList<ModelSuratMasuk>();
                    //Toast.makeText(getActivity(), "Error: stu7 " + gabung, Toast.LENGTH_LONG).show();

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
            Toast.makeText(getActivity(), "Error: stu7 " + e.toString(), Toast.LENGTH_LONG).show();
        }*/
        return v;
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
        //Toast.makeText(getActivity(), gabung, Toast.LENGTH_SHORT).show();

        JsonArrayRequest reqsubkategori = new JsonArrayRequest(gabung,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            //Toast.makeText(MainActivity.this, "response data from server berhasil", Toast.LENGTH_SHORT).show();
                            hideDialog();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject person = (JSONObject) response
                                        .get(i);

                                //String idd = person.getString("id_kota");
                                //String name = person.getString("nm_kota");

                                String id_surat = person.getString("id_surat");
                                String tgl_surat = person.getString("tgl_surat");
                                String jenis_surat = person.getString("jenis_surat");
                                String nomor_surat = person.getString("nomor_surat");
                                String dari = person.getString("nama_jab");
                                String kepada = person.getString("nama_penerima");
                                String perihal = person.getString("perihal");
                                String nm_file = person.getString("nm_file");
                                String nm_folder = person.getString("nm_folder");
                                String status_penerima = person.getString("status_penerima");
                                String id_penerima = person.getString("id_penerima");
                                String status_baca = person.getString("status_baca");

                                //modeldataList.add(new ModelSuratMasuk(id_surat,tgl_surat,nomor_surat,jenis_surat,id_penerima,status_penerima,perihal,nm_file,nm_folder,kepada,dari,status_baca));
                            }

                            //progresafter();

                        } catch (JSONException e) {
                            hideDialog();
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(req);
        Volley.newRequestQueue(this.getActivity()).add(reqsubkategori);
    }

    /*public void progresafter() {
        try {
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this.getActivity());
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            // create an Object for Adapter
            mAdapter = new SecondAdapter(modeldataList, mRecyclerView);
            // set the adapter object to the Recyclerview
            mRecyclerView.setAdapter(mAdapter);

            if (modeldataList.isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
                tvEmptyView.setVisibility(View.VISIBLE);
                //Toast.makeText(MainActivity.this, " row bernilai 0 ", Toast.LENGTH_SHORT).show();
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                tvEmptyView.setVisibility(View.GONE);
            }

            mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
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
            Toast.makeText(getActivity(), "Error: stu" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }*/

    /*private void onscrolldatax(int start) {
        //Toast.makeText(getActivity(), "test"+kodeunker, Toast.LENGTH_SHORT).show();
        gabung = AppVar.DATA_URL + start + "&kode_unker=" + kodeunker + "&kode_unor=" + kodeunor + "&kode_jab=" + kodejab;

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

                                    String id_surat = person.getString("id_surat");
                                    String tgl_surat = person.getString("tgl_surat");
                                    String jenis_surat = person.getString("jenis_surat");
                                    String nomor_surat = person.getString("nomor_surat");
                                    String dari = person.getString("nama_jab");
                                    String kepada = person.getString("nama_penerima");
                                    String perihal = person.getString("perihal");
                                    String nm_file = person.getString("nm_file");
                                    String nm_folder = person.getString("nm_folder");
                                    String status_penerima = person.getString("status_penerima");
                                    String id_penerima = person.getString("id_penerima");
                                    String status_baca = person.getString("status_baca");

                                    modeldataList.add(new ModelSuratMasuk(id_surat,tgl_surat,nomor_surat,jenis_surat,id_penerima,status_penerima,perihal,nm_file,nm_folder,kepada,dari,status_baca));
                                    mAdapter.notifyItemInserted(modeldataList.size());
                                }
                                else {
                                    String isipesan = person.getString("isipesan");
                                    Toast.makeText(getActivity(), isipesan, Toast.LENGTH_SHORT).show();
                                }
                            }

                            mAdapter.setLoaded();

                            //progresafter();

                        } catch (JSONException e) {
                            //hideDialog();
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //hideDialog();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(req);
        Volley.newRequestQueue(this.getActivity()).add(onscrolldata);
    }*/


}