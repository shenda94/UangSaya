package Adapter;

/**
 * Created by merna.shenda on 5/21/2018.
 */
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;
import Model.Kasmodel;
import com.example.mernashenda.uangsaya.R;

public class kasadapter extends RecyclerView.Adapter<kasadapter.MyViewHolder> {
    private List<Kasmodel> daftarSurahsList;
    private Context contextx;
    View dialogView;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nomorakun1, namaakun1, saldo1, kelompokkas1, statusdefault1, id_akun1;
        public Button button;
        public RelativeLayout viewBackground, viewForeground;
        private AppCompatButton btn_sharex;

        public MyViewHolder(View view) {
            super(view);
            contextx = view.getContext();

            nomorakun1 = (TextView) view.findViewById(R.id.nomorakun); // judul loker
            namaakun1 = (TextView) view.findViewById(R.id.namaakun); // judul loker
            saldo1 = (TextView) view.findViewById(R.id.saldo); // judul loker

            kelompokkas1 = (TextView) view.findViewById(R.id.kelompokkas); // judul loker
            statusdefault1 = (TextView) view.findViewById(R.id.statusdefault); // judul loker
            id_akun1 = (TextView) view.findViewById(R.id.id_akun); // judul loker

            viewBackground = (RelativeLayout) view.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout) view.findViewById(R.id.view_foreground);

        }
    }

    public kasadapter(List<Kasmodel> daftarSurahsList) {
        this.daftarSurahsList = daftarSurahsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lis_kas, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //MyViewHolder
        Kasmodel dftrsurah = daftarSurahsList.get(position);
        holder.nomorakun1.setText(dftrsurah.getNomor_akun());
        holder.namaakun1.setText("Nama Kas/Bank : " + dftrsurah.getNm_akun());
        holder.saldo1.setText("Saldo Rp. " + String.valueOf(dftrsurah.getSaldo()));
        holder.id_akun1.setText(dftrsurah.getId_akun());
        holder.kelompokkas1.setText(dftrsurah.getKelompok_akun());
        holder.statusdefault1.setText(dftrsurah.getStatus_default());
    }

    @Override
    public int getItemCount() {
        return daftarSurahsList.size();
    }

    public void removeItem(int position) {
        daftarSurahsList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Kasmodel item, int position) {
        daftarSurahsList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
