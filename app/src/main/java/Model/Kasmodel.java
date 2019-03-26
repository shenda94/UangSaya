package Model;

/**
 * Created by merna.shenda on 5/21/2018.
 */

public class Kasmodel {
    private String id_akun, nomor_akun, nm_akun, status_default, kelompok_akun, pinatm, pininternet;
    private double saldo;

    public Kasmodel(String id_akun, String nomor_akun, String nm_akun, double saldo, String status_default, String kelompok_akun, String pinatm, String pininternet) {
        this.id_akun = id_akun;
        this.nomor_akun  = nomor_akun;
        this.nm_akun  = nm_akun;
        this.saldo = saldo;

        this.status_default = status_default;
        this.kelompok_akun  = kelompok_akun;
        this.pinatm  = pinatm;
        this.pininternet = pininternet;
    }

    public Kasmodel() {
    }

    public String getId_akun() {
        return id_akun;
    }

    public void setId_akun(String id_akun) {
        this.id_akun = id_akun;
    }

    public String getNomor_akun() {
        return nomor_akun;
    }

    public void setNomor_akun(String nomor_akun) {
        this.nomor_akun = nomor_akun;
    }

    public String getNm_akun() {
        return nm_akun;
    }

    public void setNm_akun(String nm_akun) {
        this.nm_akun = nm_akun;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getStatus_default() {
        return status_default;
    }

    public void setStatus_default(String status_default) {
        this.status_default = status_default;
    }

    public String getKelompok_akun() {
        return kelompok_akun;
    }

    public void setKelompok_akun(String kelompok_akun) {
        this.kelompok_akun = kelompok_akun;
    }

    public String getPinatm() {
        return pinatm;
    }

    public void setPinatm(String pinatm) {
        this.pinatm = pinatm;
    }

    public String getPininternet() {
        return pininternet;
    }

    public void setPininternet(String pininternet) {
        this.pininternet = pininternet;
    }
}
