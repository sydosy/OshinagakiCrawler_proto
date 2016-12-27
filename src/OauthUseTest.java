import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import java.io.*;

public class OauthUseTest {
    public static void main(String args[]) throws Exception {
        // このファクトリインスタンスは再利用可能でスレッドセーフです
        TwitterFactory factory = new TwitterFactory();
        AccessToken accessToken = loadAccessToken();
        Twitter twitter = factory.getInstance();
        twitter.setOAuthAccessToken(accessToken);
        Status status = twitter.updateStatus("test_oauth");
        System.out.println("Successfully updated the status to [" + status.getText() + "].");
        System.exit(0);
    }

    private static AccessToken loadAccessToken() {
        try {
            FileReader fileReader = new FileReader(new File("oauth"));
            BufferedReader br = new BufferedReader(fileReader);

            String[] spritToken = br.readLine().split(",");
            String token = spritToken[1];// load from a persistent store
            String tokenSecret = spritToken[2];// load from a persistent store

            br.close();
            fileReader.close();

            return new AccessToken(token, tokenSecret);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
