package Model;

/**
 * Created by merna.shenda on 6/22/2018.
 */

public class PGModel {
    public String id_transaksi;
    public String nm_transaksi;
    public String keterangan;
    public String tgl_transaksi;
    public String id_jenis_transaksi;
    public String jenis_transaksi;
    public String id_akun_kas;
    public String id_user_input;
    public String nm_akun;
    public String nm_jenis_transaksi;
    public String nominal;

    public PGModel() {

    }

    public PGModel(String id_transaksi, String nm_transaksi, String keterangan, String tgl_transaksi, String id_jenis_transaksi, String jenis_transaksi, String id_akun_kas, String id_user_input, String nm_akun, String nm_jenis_transaksi, String nominal) {
        this.id_transaksi = id_transaksi;
        this.nm_transaksi = nm_transaksi;
        this.keterangan = keterangan;
        this.tgl_transaksi = tgl_transaksi;
        this.id_jenis_transaksi = id_jenis_transaksi;
        this.jenis_transaksi = jenis_transaksi;
        this.id_akun_kas = id_akun_kas;
        this.id_user_input = id_user_input;
        this.nm_akun = nm_akun;
        this.nm_jenis_transaksi = nm_jenis_transaksi;
        this.nominal = nominal;
    }

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getNm_transaksi() {
        return nm_transaksi;
    }

    public void setNm_transaksi(String nm_transaksi) {
        this.nm_transaksi = nm_transaksi;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTgl_transaksi() {
        return tgl_transaksi;
    }

    public void setTgl_transaksi(String tgl_transaksi) {
        this.tgl_transaksi = tgl_transaksi;
    }

    public String getId_jenis_transaksi() {
        return id_jenis_transaksi;
    }

    public void setId_jenis_transaksi(String id_jenis_transaksi) {
        this.id_jenis_transaksi = id_jenis_transaksi;
    }

    public String getJenis_transaksi() {
        return jenis_transaksi;
    }

    public void setJenis_transaksi(String jenis_transaksi) {
        this.jenis_transaksi = jenis_transaksi;
    }

    public String getId_akun_kas() {
        return id_akun_kas;
    }

    public void setId_akun_kas(String id_akun_kas) {
        this.id_akun_kas = id_akun_kas;
    }

    public String getId_user_input() {
        return id_user_input;
    }

    public void setId_user_input(String id_user_input) {
        this.id_user_input = id_user_input;
    }

    public String getNm_akun() {
        return nm_akun;
    }

    public void setNm_akun(String nm_akun) {
        this.nm_akun = nm_akun;
    }

    public String getNm_jenis_transaksi() {
        return nm_jenis_transaksi;
    }

    public void setNm_jenis_transaksi(String nm_jenis_transaksi) {
        this.nm_jenis_transaksi = nm_jenis_transaksi;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }
}
