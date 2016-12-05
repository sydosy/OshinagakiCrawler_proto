import java.net.URL;

public class Circle {
    private String name;
    private String space;
    private URL webSite;
    private URL menuURL;
    private Author author;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public URL getWebSite() {
        return webSite;
    }

    public void setWebSite(URL webSite) {
        this.webSite = webSite;
    }

    public URL getMenuURL() {
        return menuURL;
    }

    public void setMenuURL(URL menuURL) {
        this.menuURL = menuURL;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
