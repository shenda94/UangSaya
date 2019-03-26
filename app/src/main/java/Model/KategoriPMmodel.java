package Model;

/**
 * Created by merna.shenda on 5/22/2018.
 */

public class KategoriPMmodel {
    public int id;
    public String id_jenis_transaksi;
    public String nm_jenis_transaksi;
    public String jenis_transaksi;
    public String keterangan;
    public String id_group_transaksi;
    public String status_default;

    public KategoriPMmodel() {
    }

    public KategoriPMmodel(String id_jenis_transaksi, String nm_jenis_transaksi, String jenis_transaksi, String keterangan, String id_group_transaksi, String status_default) {
        this.id_jenis_transaksi = id_jenis_transaksi;
        this.nm_jenis_transaksi = nm_jenis_transaksi;
        this.jenis_transaksi = jenis_transaksi;
        this.keterangan = keterangan;
        this.id_group_transaksi = id_group_transaksi;
        this.status_default = status_default;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_jenis_transaksi() {
        return id_jenis_transaksi;
    }

    public void setId_jenis_transaksi(String id_jenis_transaksi) {
        this.id_jenis_transaksi = id_jenis_transaksi;
    }

    public String getNm_jenis_transaksi() {
        return nm_jenis_transaksi;
    }

    public void setNm_jenis_transaksi(String nm_jenis_transaksi) {
        this.nm_jenis_transaksi = nm_jenis_transaksi;
    }

    public String getJenis_transaksi() {
        return jenis_transaksi;
    }

    public void setJenis_transaksi(String jenis_transaksi) {
        this.jenis_transaksi = jenis_transaksi;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getId_group_transaksi() {
        return id_group_transaksi;
    }

    public void setId_group_transaksi(String id_group_transaksi) {
        this.id_group_transaksi = id_group_transaksi;
    }

    public String getStatus_default() {
        return status_default;
    }

    public void setStatus_default(String status_default) {
        this.status_default = status_default;
    }
}
