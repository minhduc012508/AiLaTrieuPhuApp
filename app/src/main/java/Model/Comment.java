package Model;

public class Comment {
    private int id;
    private String name;
    private String content;
    private int like;
    private String cauhoi;

    public Comment(int id,String name, String content, int like, String cauhoi) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.like = like;
        this.cauhoi = cauhoi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getCauhoi() {
        return cauhoi;
    }

    public void setCauhoi(String cauhoi) {
        this.cauhoi = cauhoi;
    }
}
