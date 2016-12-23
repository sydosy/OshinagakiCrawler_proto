import java.net.URL;

public class Author {
    private long id;
    private String name;
    private String screen_name;
    private String profile_image_url_https;


    public Author(long id, String name, String screen_name, String profile_image_url_https) {
        this.id = id;
        this.name = name;
        this.screen_name = screen_name;
        this.profile_image_url_https = profile_image_url_https;
    }

    public Author(long id) {
        this.id = id;
    }

    public Author(long id, String name, String screen_name) {
        this.id = id;
        this.name = name;
        this.screen_name = screen_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getProfile_image_url_https() {
        return profile_image_url_https;
    }

    public void setProfile_image_url_https(String profile_image_url_https) {
        this.profile_image_url_https = profile_image_url_https;
    }
}
