package Adapter;

/**
 * Created by merna.shenda on 6/26/2018.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import com.example.mernashenda.uangsaya.TambahPengeluaranActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Model.KPGModel;
import Model.PGModel;
import cn.pedant.SweetAlert.SweetAlertDialog;
import customfonts.MyTextView;
import static com.example.mernashenda.uangsaya.R.color.message;

public class TRAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<PGModel> modeldataList;

    // The minimum amount of items to have below your current scroll position
// before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreTransaksi onLoadMoreListener;

    public TRAdapter(List<PGModel> modeldata, RecyclerView recyclerView) {
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
                                        onLoadMoreListener.onLoadMoreTransaksi();
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
                    R.layout.list_transaksi, parent, false);

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
            PGModel singleStudent= (PGModel) modeldataList.get(position);
            ((StudentViewHolder) holder).contextx.getResources().getColor(R.color.colorText);
            ((StudentViewHolder) holder).namatr.setText(singleStudent.getNm_transaksi());
            ((StudentViewHolder) holder).jenistr.setText(String.valueOf(singleStudent.getJenis_transaksi()));
            ((StudentViewHolder) holder).keterangan.setText(String.valueOf(singleStudent.getKeterangan()));
            ((StudentViewHolder) holder).nm_jenis_transaksi.setText(String.valueOf(singleStudent.getNm_jenis_transaksi()));
            ((StudentViewHolder) holder).nominal.setText(String.valueOf(singleStudent.getNominal()));

            String hasil1 = singleStudent.getJenis_transaksi();
            String hasil2 = "Pemasukan";
            String hasil3 = "Pengeluaran";

            if (hasil1.equals(hasil2)) {
                ((StudentViewHolder) holder).akun.setText("Ke Akun " + String.valueOf(singleStudent.getNm_akun()));
            }
            else if (hasil1.equals(hasil3)) {
                ((StudentViewHolder) holder).akun.setText("Dari Akun " + String.valueOf(singleStudent.getNm_akun()));
            }


            ((StudentViewHolder) holder).tgltransaksi.setText(String.valueOf(singleStudent.getTgl_transaksi()));

            //Glide.with(((StudentViewHolder) holder).contextx).load(R.drawable.ic_money)
            //        .thumbnail(0.5f)
            //        .crossFade()
            //        .transform(new CircleTransform(((StudentViewHolder) holder).contextx))
            //        .diskCacheStrategy(DiskCacheStrategy.ALL)
             //       .into(((StudentViewHolder) holder).imgProfile);
            //((StudentViewHolder) holder).imgProfile.setColorFilter(null);

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

    public void setOnLoadMoreListener(OnLoadMoreTransaksi onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView namatr;
        public TextView jenistr;
        public TextView keterangan;
        public TextView nm_jenis_transaksi;
        public TextView nominal;
        public TextView akun;
        public TextView tgltransaksi;
        public PGModel modeldata;
        public Context contextx;
        public ImageView iconImp, imgProfile;

        public StudentViewHolder(View v) {
            super(v);
            contextx = v.getContext();
            namatr = (TextView) v.findViewById(R.id.nama_tr);
            jenistr = (TextView) v.findViewById(R.id.jenis_transaksi_tr);
            nm_jenis_transaksi = (TextView) v.findViewById(R.id.nmjn_transaksi_tr);
            keterangan = (TextView) v.findViewById(R.id.txtketerangan_tr);
            nominal = (TextView) v.findViewById(R.id.nom_tr);
            akun = (TextView) v.findViewById(R.id.akun_tr);
            tgltransaksi = (TextView) v.findViewById(R.id.tgl_transaksi_tr);
            //imgProfile = (ImageView) v.findViewById(R.id.icon_profile_tr);

            LinearLayout navButton4 = (LinearLayout) v.findViewById(R.id.message_container_tr);
            navButton4.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //set parameter
                    SharedPreferences sharedPreferences = contextx.getSharedPreferences(AppVar.SHARED_PREF_TR,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(AppVar.SHARED_id_transaksi, modeldata.getId_transaksi());
                    editor.putString(AppVar.SHARED_nm_transaksi, modeldata.getNm_transaksi());
                    editor.putString(AppVar.SHARED_jenis_transaksi, modeldata.getJenis_transaksi());
                    editor.putString(AppVar.SHARED_tgl_transaksi, modeldata.getTgl_transaksi());
                    editor.putString(AppVar.SHARED_keterangan, modeldata.getKeterangan());
                    editor.putString(AppVar.SHARED_nominal, modeldata.getNominal());
                    editor.putString(AppVar.SHARED_id_jenis_transaksi, modeldata.getId_jenis_transaksi());
                    editor.putString(AppVar.SHARED_id_akun_kas, modeldata.getId_akun_kas());
                    editor.putString(AppVar.SHARED_nm_akun, modeldata.getNm_akun());
                    editor.putString(AppVar.SHARED_nm_jenis_transaksi, modeldata.getNm_jenis_transaksi());
                    editor.commit();

                    Intent intent = new Intent(contextx, TambahPengeluaranActivity.class);
                    contextx.startActivity(intent);
                }
            });
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
