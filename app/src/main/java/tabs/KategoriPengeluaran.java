package tabs;

/**
 * Created by merna.shenda on 6/22/2018.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import Adapter.OnLoadMoreKP;
import Adapter.PGKatAdapter;
import Model.KPGModel;

public class KategoriPengeluaran extends Fragment {
    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private PGKatAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private static final String TAG = "KategoriActivity";
    private List<KPGModel> modeldataList;
    String gabung;
    protected Handler handler;
    private ProgressDialog pDialog;
    //Volley Request Queue
    private RequestQueue requestQueue;
    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;
    private int LIMIT = 20;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    public String namaku;
    private String urlJsonArry = "http://markaskerja.000webhostapp.com/uangsaya/kategoripengeluaran.php?page=";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ketegoripengeluaran,container,false);
        try {
            pDialog = new ProgressDialog(getActivity());
            tvEmptyView = (TextView) v.findViewById(R.id.empty_view_kPg);
            mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view_kPg);
            handler = new Handler();
            modeldataList = new ArrayList<KPGModel>();

            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(AppVar.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            namaku = sharedPreferences.getString(AppVar.IDUSER_SHARED_PREF, "");
            gabung = urlJsonArry + requestCount + "&id_user=" + namaku;

            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Process...");
            pDialog.setIndeterminate(true);
            pDialog.setCanceledOnTouchOutside(false);
            showDialog();

            load_data_first();

            mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout_kPg);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // do something
                    mSwipeRefreshLayout.setRefreshing(true);
                    requestCount = 1;
                    gabung = urlJsonArry + requestCount + "&id_user=" + namaku;
                    modeldataList = new ArrayList<KPGModel>();
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
        }
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
        JsonArrayRequest reqsubkategori = new JsonArrayRequest(gabung,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {

                            hideDialog();

                            if (response.length() > 0) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject person = (JSONObject) response
                                            .get(i);

                                    String isipesan = person.getString("statuspesan");

                                    String valisi = "0";
                                    String valisi2 = "1";

                                    if (isipesan.equals(valisi)) {
                                    }
                                    else if (isipesan.equals(valisi2)) {
                                        String id_jenis_transaksi = person.getString("id_jenis_transaksi");
                                        String nm_jenis_transaksi = person.getString("nm_jenis_transaksi");
                                        String jenis_transaksi = person.getString("jenis_transaksi");
                                        String keterangan = person.getString("keterangan");
                                        String id_group_transaksi = person.getString("id_group_transaksi");
                                        String status_default = person.getString("status_default");

                                        //Toast.makeText(getActivity(), nm_jenis_transaksi.toString(), Toast.LENGTH_SHORT).show();

                                        modeldataList.add(new KPGModel(id_jenis_transaksi,nm_jenis_transaksi,jenis_transaksi,keterangan,id_group_transaksi, status_default));
                                    }

                                }

                                progresafter();
                            }

                        } catch (JSONException e) {
                            hideDialog();
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error:sd " + e.getMessage(),
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

    public void progresafter() {
        try {
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this.getActivity());
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayoutManager.VERTICAL));
            // create an Object for Adapter
            mAdapter = new PGKatAdapter(modeldataList, mRecyclerView);
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

            mAdapter.setOnLoadMoreListener(new OnLoadMoreKP() {
                @Override
                public void onLoadMoreKP() {
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
    }

    private void onscrolldatax(int start) {
        //Toast.makeText(getActivity(), "test"+kodeunker, Toast.LENGTH_SHORT).show();
        gabung = urlJsonArry + start + "&id_user=" + namaku;

        JsonArrayRequest onscrolldata = new JsonArrayRequest(gabung,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            if (response.length() > 0){
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject person = (JSONObject) response
                                            .get(i);

                                    //Toast.makeText(MainActivity.this, person.getString("statuspesan"), Toast.LENGTH_SHORT).show();
                                    if (person.getInt("statuspesan") == 1){
                                        String id_jenis_transaksi = person.getString("id_jenis_transaksi");
                                        String nm_jenis_transaksi = person.getString("nm_jenis_transaksi");
                                        String jenis_transaksi = person.getString("jenis_transaksi");
                                        String keterangan = person.getString("keterangan");
                                        String id_group_transaksi = person.getString("id_group_transaksi");
                                        String status_default = person.getString("status_default");
                                        modeldataList.add(new KPGModel(id_jenis_transaksi,nm_jenis_transaksi,jenis_transaksi,keterangan,id_group_transaksi, status_default));

                                        //modeldataList.add(new KategoriPMmodel(id_surat,tgl_surat,nomor_surat,jenis_surat,id_penerima,status_penerima,perihal,nm_file,nm_folder,kepada,dari,status_baca));
                                        mAdapter.notifyItemInserted(modeldataList.size());
                                    }
                                    else {
                                        String isipesan = person.getString("isipesan");
                                        //Toast.makeText(getActivity(), isipesan, Toast.LENGTH_SHORT).show();
                                    }
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
    }

}
