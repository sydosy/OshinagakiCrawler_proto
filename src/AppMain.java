import com.google.gson.Gson;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.*;
import java.util.*;

import static java.lang.Thread.sleep;

public class AppMain {
    public static void main(String[] args) throws TwitterException, IOException {
        //Twitter使用準備
        TwitterFactory factory = new TwitterFactory();
        AccessToken accessToken = loadAccessToken();
        if (accessToken == null) {
            // このファクトリインスタンスは再利用可能でスレッドセーフです
            Twitter twitter = TwitterFactory.getSingleton();
            RequestToken requestToken = twitter.getOAuthRequestToken();
            accessToken = null;
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
            br.close();
            //将来の参照用に accessToken を永続化する
            storeAccessToken(twitter.verifyCredentials().getId(), accessToken);
        }
        Twitter twitter = factory.getInstance();
        twitter.setOAuthAccessToken(accessToken);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("新しいリストを作成しますか？yse[y]/No[other]");
        String input = br.readLine();
        if (input.equals("y")) {
            //フォローしているUserからサークルを抽出
            try {
                IDs follow = twitter.getFriendsIDs(twitter.getScreenName(), -1);
                System.out.println("フォローしている人数: " + follow.getIDs().length);

                User[] circles = Circle.getCircles(follow.getIDs(), twitter).toArray(new User[0]);
                for (User user : circles
                        ) {
                    System.out.println(user.getName() + "(" + user.getScreenName() + ")");
                }
                System.out.println("抽出したサークル数: " + circles.length);

                //抽出したUserをまとめてリストに入れる
                System.out.println("作成するリスト名を入力して下さい");
                String listName = br.readLine();
                System.out.println("リストの説明を入力して下さい");
                String listDescription = br.readLine();
                UserList userList = twitter.createUserList(listName, false, listDescription);
                //サーバーにリスト作成時間を与える
                sleep(5000);

                long userListId = userList.getId();
                for (User user : circles) {
                    twitter.createUserListMember(userListId, user.getId());
                }
            } catch (TwitterException | InterruptedException e) {
                e.printStackTrace();
            }
        }//リストの作成終了

        //リスト一覧表示準備
        List<Circle> circleList = new ArrayList<>();

        try {
            //全リスト取得
            ResponseList<UserList> lists = twitter.getUserLists(twitter.getScreenName());

            long[] userListIDs = new long[lists.size()];

            //リスト一覧の表示とリストIDの格納
            int count = 0;
            for (UserList userList :
                    lists) {
                System.out.println(count + ": " + userList.getFullName());
                userListIDs[count] = userList.getId();
                count++;
            }

            System.out.println("select list number");
            int selectNum = Integer.parseInt(br.readLine());
            //選択したユーザーリストを抽出
            UserList selectList = twitter.showUserList(userListIDs[selectNum]);
            System.out.println(selectList.getName());
            System.out.println(selectList.getFullName());
            System.out.println(selectList.getId());
            System.out.println(selectList.getMemberCount());
            System.out.println();

            //選択したユーザーリストからユーザーを抽出。5000人まで。
            //cursor = -1 にすることで最初のユーザーを取得
            int cursor = -1;
            PagableResponseList<User> users = twitter.getUserListMembers(userListIDs[selectNum], 5000, cursor);
            Iterator<User> iterator = users.iterator();
            URLEntity[] urlEntities;
            while (iterator.hasNext()) {
                Set<String> urls = new HashSet<>();
                User user = iterator.next();
                //ユーザーからお品書きTweetを検索
                for (Status menuTweet : searchMenuTweet(twitter, user).getTweets()) {
                    //tweetからURLを抽出
                    urlEntities = menuTweet.getURLEntities();
                    //URLがあれば
                    if (urlEntities != null && urlEntities.length > 0) {
                        for (URLEntity urlEntity : urlEntities) {
                            //短縮URLをセットに登録
                            urls.add(urlEntity.getExpandedURL());
                        }
                    }
                }
                Circle currentCircle = new Circle(new Author(user.getId(), user.getName(), user.getScreenName(), user.getProfileImageURLHttps()), urls);
                circleList.add(currentCircle);
                System.out.println(currentCircle);

                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        //json書き出し準備
        Gson gson = new Gson();
        FileWriter lastFileWriter = new FileWriter(new File("CircleList.json"));
        PrintWriter lastPw = new PrintWriter(new BufferedWriter(lastFileWriter));
        //json形式で出力
        lastPw.println(gson.toJson(circleList));
        //close処理
        br.close();
        lastPw.close();
        lastFileWriter.close();
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

    public static QueryResult searchMenuTweet(Twitter twitter, User user) throws TwitterException {
        Query query = new Query();
        query.setLang("ja");
        query.setCount(100);
        query.setQuery("from:" + user.getScreenName() + " exclude:retweets お品書き OR おしながき OR 新刊 OR 落としました OR 落ちました OR グッズ");
        query.setSince("2016-12-01");
        query.setUntil("2016-12-31");
        return twitter.search(query);
    }
}
