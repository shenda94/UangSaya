package Adapter;

/**
 * Created by merna.shenda on 5/22/2018.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mernashenda.uangsaya.AppVar;
import com.example.mernashenda.uangsaya.CircleTransform;
import com.example.mernashenda.uangsaya.KategoriActivity;
import com.example.mernashenda.uangsaya.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Model.KategoriPMmodel;
import cn.pedant.SweetAlert.SweetAlertDialog;
import customfonts.MyTextView;
import static com.example.mernashenda.uangsaya.R.color.message;

public class PMKatAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<KategoriPMmodel> modeldataList;

    // The minimum amount of items to have below your current scroll position
// before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public PMKatAdapter(List<KategoriPMmodel> modeldata, RecyclerView recyclerView) {
        modeldataList = modeldata;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (modeldataList.size() <= 10) {
                                    loading = false;
                                }
                                else {
                                    if (onLoadMoreListener != null) {
                                        onLoadMoreListener.onLoadMore();
                                    }
                                    loading = true;
                                }

                            }
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return modeldataList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.message_list_row1, parent, false);

            vh = new StudentViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.lay_progress, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StudentViewHolder) {
            KategoriPMmodel singleStudent= (KategoriPMmodel) modeldataList.get(position);
            ((StudentViewHolder) holder).contextx.getResources().getColor(R.color.colorText);
            ((StudentViewHolder) holder).namakategori.setText(singleStudent.getNm_jenis_transaksi());
            ((StudentViewHolder) holder).idjenis.setText(String.valueOf(singleStudent.getId_jenis_transaksi()));
            ((StudentViewHolder) holder).keterangankategori.setText(String.valueOf(singleStudent.getKeterangan()));
            ((StudentViewHolder) holder).jeniskategori1.setText(String.valueOf(singleStudent.getJenis_transaksi()));
            String jenikas = singleStudent.getId_group_transaksi();
            String pemasukan ="Pemasukan";
            String pengeluaran ="Pengeluaran";
            String jenis_transaksii = "";

            if (jenikas.equals(pemasukan)) {
                jenis_transaksii = "1";
            }
            else if (jenikas.equals(pemasukan)) {
                jenis_transaksii = "0";
            }

            Glide.with(((StudentViewHolder) holder).contextx).load(R.drawable.ic_variety)
                    .thumbnail(0.5f)
                    .crossFade()
                    .transform(new CircleTransform(((StudentViewHolder) holder).contextx))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((StudentViewHolder) holder).imgProfile);
            ((StudentViewHolder) holder).imgProfile.setColorFilter(null);

            ((StudentViewHolder) holder).modeldata= singleStudent;

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return modeldataList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView namakategori;
        public TextView jeniskategori1;
        public TextView keterangankategori;
        public TextView idjenis;
        public KategoriPMmodel modeldata;
        public Context contextx;
        public ImageView iconImp, imgProfile;
        AlertDialog.Builder dialog;
        LayoutInflater inflater;
        View dialogView;
        public String namakat, jeniskat, ketkat, iduser, IDGROUP, status_def, idjenis1;
        public String nilaival;
        public EditText namakategori1;
        public Spinner cmjenistransaksi1;
        public EditText txtidjenistransaksi1;
        public EditText keterangan1;
        int success;
        private static final String TAG_SUCCESS = "success";
        public ProgressDialog pDialog;

        public StudentViewHolder(View v) {
            super(v);
            contextx = v.getContext();
            pDialog = new ProgressDialog(contextx);

            namakategori = (TextView) v.findViewById(R.id.nama_kategori);
            keterangankategori = (TextView) v.findViewById(R.id.txtketerangan);
            jeniskategori1 = (TextView) v.findViewById(R.id.jenis_kategori);
            idjenis = (TextView) v.findViewById(R.id.id_jenis_tr);
            imgProfile = (ImageView) v.findViewById(R.id.icon_profile);

            LinearLayout navButton4 = (LinearLayout) v.findViewById(R.id.message_container);
            navButton4.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = contextx.getSharedPreferences(AppVar.SHARED_PREF_KAT,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(AppVar.idjenis, modeldata.getId_jenis_transaksi());
                    editor.putString(AppVar.namajenis, modeldata.getNm_jenis_transaksi());
                    editor.putString(AppVar.jenistr, modeldata.getJenis_transaksi());
                    editor.putString(AppVar.idgroupjns, modeldata.getId_group_transaksi());
                    editor.putString(AppVar.keterangantr, modeldata.getKeterangan());
                    editor.putString(AppVar.statusdef, modeldata.getStatus_default());
                    editor.commit();

                    dialog = new AlertDialog.Builder(contextx);
                    inflater = LayoutInflater.from(contextx);
                    dialogView = inflater.inflate(R.layout.custom_dialog, null);
                    dialog.setView(dialogView);
                    dialog.setCancelable(false);
                    dialog.setTitle("Edit Kategori");
                    namakategori1 = (EditText) dialogView.findViewById(R.id.namakategori);
                    cmjenistransaksi1 = (Spinner) dialogView.findViewById(R.id.cmjenistransaksi);
                    txtidjenistransaksi1 = (EditText) dialogView.findViewById(R.id.txtidjenistransaksi);
                    keterangan1 = (EditText) dialogView.findViewById(R.id.keterangan);
                    SharedPreferences sharedPreferences1 = contextx.getSharedPreferences(AppVar.SHARED_PREF_KAT, Context.MODE_PRIVATE);
                    txtidjenistransaksi1.setText(sharedPreferences1.getString(AppVar.idjenis, ""));
                    namakategori1.setText(sharedPreferences1.getString(AppVar.namajenis, ""));
                    String statusname3 = sharedPreferences1.getString(AppVar.jenistr, "");
                    String statusname1 = "Pemasukan";
                    String statusname2 = "Pengeluaran";

                    if (statusname3.equals(statusname1)) {
                        cmjenistransaksi1.setSelection(0);
                    }
                    else if (statusname3.equals(statusname2)) {
                        cmjenistransaksi1.setSelection(1);
                    }
                    keterangan1.setText(sharedPreferences1.getString(AppVar.keterangantr, ""));

                    dialog.setPositiveButton(R.string.simpankategori, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            simpanupdate();
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
            });

        }

        public void showDialog() {
            if (!pDialog.isShowing())
                pDialog.show();
        }

        public void hideDialog() {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }

        public void simpanupdate() {
            namakat = namakategori1.getText().toString();
            jeniskat = cmjenistransaksi1.getSelectedItem().toString();
            ketkat = keterangan1.getText().toString();
            idjenis1 = txtidjenistransaksi1.getText().toString();

            String statusname = cmjenistransaksi1.getSelectedItem().toString();
            String statusname1 = "Pemasukan";
            String statusname2 = "Pengeluaran";

            if (statusname.equals(statusname1)) {
                nilaival = "1";
            }
            else if (statusname.equals(statusname2)) {
                nilaival = "0";
            }

            SharedPreferences sharedPreferences = contextx.getSharedPreferences(AppVar.SHARED_PREF_NAME, Context.MODE_PRIVATE);
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
                            try {
                                JSONObject jObj = new JSONObject(response);
                                success = jObj.getInt(TAG_SUCCESS);

                                if (success == 1) {
                                    hideDialog();
                                    SweetAlertDialog pDialog = new SweetAlertDialog(contextx, SweetAlertDialog.SUCCESS_TYPE);
                                    pDialog.setTitleText("Informasi");
                                    pDialog.setContentText("Simpan Data Kas/Bank Berhasil!");
                                    pDialog.setCancelable(false);
                                    pDialog.setConfirmText("OK");
                                    pDialog.setConfirmClickListener(null);
                                    pDialog.show();

                                } else {
                                    hideDialog();
                                    SweetAlertDialog pDialog1 = new SweetAlertDialog(contextx, SweetAlertDialog.ERROR_TYPE);
                                    pDialog1.setTitleText("Informasi");
                                    pDialog1.setContentText("Simpan Kas/Bank Gagal");
                                    pDialog1.setCancelable(false);
                                    pDialog1.setConfirmText("OK");
                                    pDialog1.setConfirmClickListener(null);
                                    pDialog1.show();
                                }
                            } catch (JSONException e) {
                                // JSON error
                                Toast.makeText(contextx, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//You can handle error here if you want
                            hideDialog();
                            Toast.makeText(contextx, "The server unreachable", Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
//Adding parameters to request
                    params.put(AppVar.nama_jenis_transaksi, namakat);
                    params.put(AppVar.id_jenis_transaksi, idjenis1);
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
            Volley.newRequestQueue(contextx).add(stringRequest);
        }

    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
