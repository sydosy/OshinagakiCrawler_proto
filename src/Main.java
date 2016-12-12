import twitter4j.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        Twitter twitter = new TwitterFactory().getInstance();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input screen_name here");
        String screenName = br.readLine();
        try {
            IDs follow = twitter.getFriendsIDs(screenName, -1);
            System.out.println(follow.getIDs().length);

            User[] circles = Circle.getCircles(follow.getIDs(), twitter).toArray(new User[0]);
            for (User user: circles
                 ) {
                System.out.println(user.getName() + "(" + user.getScreenName() + ")");
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
