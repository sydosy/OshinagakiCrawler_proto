import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.*;

public class OauthTest {
    public static void main(String args[]) throws Exception {
        // このファクトリインスタンスは再利用可能でスレッドセーフです
        Twitter twitter = TwitterFactory.getSingleton();
        RequestToken requestToken = twitter.getOAuthRequestToken();
        AccessToken accessToken = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
            String pin = br.readLine();
            try {
                if (pin.length() > 0) {
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                } else {
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if (401 == te.getStatusCode()) {
                    System.out.println("Unable to get the access token.");
                } else {
                    te.printStackTrace();
                }
            }
        }
        //将来の参照用に accessToken を永続化する
        storeAccessToken(twitter.verifyCredentials().getId(), accessToken);
        Status status = twitter.updateStatus(args[0]);
        System.out.println("Successfully updated the status to [" + status.getText() + "].");
        System.exit(0);
    }

    private static void storeAccessToken(long useId, AccessToken accessToken) throws IOException {
        //accessToken.getToken() を保存
        //accessToken.getTokenSecret() を保存
        String saveToken = accessToken.getToken();
        String saveTokenSecret = accessToken.getTokenSecret();
        FileWriter fileWriter = new FileWriter(new File("oauth"));
        PrintWriter printWriter = new PrintWriter(new BufferedWriter(fileWriter));
        printWriter.println(useId + "," + saveToken + "," + saveTokenSecret);

        printWriter.close();
        fileWriter.close();
    }
}
