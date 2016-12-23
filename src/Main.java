import twitter4j.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws IOException {
        Twitter twitter = new TwitterFactory().getInstance();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input screen_name here");
        String screenName = br.readLine();
        //フォローしているUserからサークルを抽出
        try {
            IDs follow = twitter.getFriendsIDs(screenName, -1);
            System.out.println("フォローしている人数: " + follow.getIDs().length);

            User[] circles = Circle.getCircles(follow.getIDs(), twitter).toArray(new User[0]);
            for (User user : circles
                    ) {
                System.out.println(user.getName() + "(" + user.getScreenName() + ")");
            }
            System.out.println("抽出したサークル数: " + circles.length);

            //抽出したUserをまとめてリストに入れる
            System.out.println("Create List name here");
            String listName = br.readLine();
            UserList userList = twitter.createUserList(listName, false, "課題のテスト");
            sleep(5000);
            long userListID = userList.getId();
            for (User user : circles
                    ) {
                twitter.createUserListMember(userListID, user.getId());
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
