package Model;

public class User {
    private int id;
    private int diemso;
    private String email;
    private String hoten;
    private int checkthoigian;
    private int mucdo;
    private String date;


    public User(){

    }
    public User(int id, int diemso, String email, String hoten, int checkthoigian, int mucdo,String date) {
        this.id = id;
        this.diemso = diemso;
        this.email = email;
        this.hoten = hoten;
        this.checkthoigian = checkthoigian;
        this.mucdo = mucdo;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiemso() {
        return diemso;
    }

    public void setDiemso(int diemso) {
        this.diemso = diemso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public int getCheckthoigian() {
        return checkthoigian;
    }

    public void setCheckthoigian(int checkthoigian) {
        this.checkthoigian = checkthoigian;
    }

    public int getMucdo() {
        return mucdo;
    }

    public void setMucdo(int mucdo) {
        this.mucdo = mucdo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", diemso=" + diemso +
                ", email='" + email + '\'' +
                ", hoten='" + hoten + '\'' +
                ", checkthoigian=" + checkthoigian +
                ", mucdo=" + mucdo +
                ", date='" + date + '\'' +
                '}';
    }
}
