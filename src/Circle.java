import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Circle {
    private String name;
    private String space;
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

    //フォローからサークルさんを見つける
    public static ArrayList<User> getCircles(long[] twitterIDs, Twitter twitter) throws TwitterException {
        ArrayList<User> targetUsers = new ArrayList<>();
        int loopMax = twitterIDs.length / 100;
        int surplusUsers = twitterIDs.length % 100;

        for (int i = 0; i < loopMax; i++) {
            //lookupUsersの仕様が100人までの情報しか取得できないため100人ずつ処理
            ResponseList<User> users = twitter.lookupUsers(Arrays.copyOfRange(twitterIDs, i * 100, (i + 1) * 100));
            for (User user : users) {
                if (user.getName().matches(".*日目.*")) {
                    targetUsers.add(user);
                }
            }
        }
        //100人ずつやったあとの最後の端数を処理
        //555人のユーザ名を調べる場合500人は上で処理し,55人をこちらで処理する
        if (surplusUsers != 0) {
            ResponseList<User> users = twitter.lookupUsers(Arrays.copyOfRange(twitterIDs, loopMax * 100, loopMax * 100 + surplusUsers));
            for (User user : users) {
                if (user.getName().matches(".*日目.*")) {
                    targetUsers.add(user);
                }
            }
        }
        return targetUsers;
    }

    public URL searchMenuTweet(Author author){
        return null;
    }

}
