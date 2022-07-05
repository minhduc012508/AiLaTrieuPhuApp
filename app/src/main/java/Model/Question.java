package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    private String cauhoi;
    private int mucdo;
    private ArrayList<Answer> danhsachdapan;

    public Question(String cauhoi, int mucdo, ArrayList<Answer> danhsachdapan) {
        this.cauhoi = cauhoi;
        this.mucdo = mucdo;
        this.danhsachdapan = danhsachdapan;
    }

    public Question() {
    }

    public String getCauhoi() {
        return cauhoi;
    }

    public void setCauhoi(String cauhoi) {
        this.cauhoi = cauhoi;
    }

    public int getMucdo() {
        return mucdo;
    }

    public void setMucdo(int mucdo) {
        this.mucdo = mucdo;
    }

    public ArrayList<Answer> getDanhsachdapan() {
        return danhsachdapan;
    }

    public void setDanhsachdapan(ArrayList<Answer> danhsachdapan) {
        this.danhsachdapan = danhsachdapan;
    }
}
