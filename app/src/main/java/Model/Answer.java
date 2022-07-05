package Model;

import java.io.Serializable;

public class Answer implements Serializable {
    private String dapan;
    private int trangthai;

    public Answer(String dapan, int trangthai) {
        this.dapan = dapan;
        this.trangthai = trangthai;
    }

    public Answer() {
    }

    public String getDapan() {
        return dapan;
    }

    public void setDapan(String dapan) {
        this.dapan = dapan;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

}
