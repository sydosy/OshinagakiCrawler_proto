import twitter4j.User;

public class Author {
    private String name;
    private User twitterAccount;
    private Long twitterID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getTwitterAccount() {
        return twitterAccount;
    }

    public void setTwitterAccount(User twitterAccount) {
        this.twitterAccount = twitterAccount;
    }

    public Long getTwitterID() {
        return twitterID;
    }

    public void setTwitterID(Long twitterID) {
        this.twitterID = twitterID;
    }
}
