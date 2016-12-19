import java.net.URL;

public class Author {
    private Long id;
    private String name;
    private String screen_name;
    private URL profile_image_url_https;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
