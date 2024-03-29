import com.google.gson.Gson;
import twitter4j.*;

import java.io.*;
import java.util.*;

import static java.lang.Thread.sleep;

public class SearchMenuTest {
    public static void main(String[] args) throws IOException {
        Twitter twitter = new TwitterFactory().getInstance();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int cursor = -1;
        List<Circle> circleList = new ArrayList<>();
        Gson gson = new Gson();
        FileWriter lastFileWriter = new FileWriter(new File("CircleList.json"));
        PrintWriter lastPw = new PrintWriter(new BufferedWriter(lastFileWriter));

        System.out.println("input your twitter screen_name");
        String screenName = br.readLine();

        try {
            ResponseList<UserList> lists = twitter.getUserLists(screenName);
            long[] userListIDs = new long[lists.size()];
            int count = 0;

            Query query = new Query();
            query.setLang("ja");

            //リスト一覧の表示とリストIDの格納
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
            PagableResponseList<User> users = twitter.getUserListMembers(userListIDs[selectNum], 5000, cursor);
            Iterator<User> iterator = users.iterator();
            URLEntity[] urlEntities;
            while (iterator.hasNext()) {
                Set<String> urls = new HashSet<>();
                User user = iterator.next();
                System.out.println(user.getName());
                //ユーザーからお品書きTweetを検索
                for (Status menuTweet : searchMenuTweet(twitter, query, user).getTweets()) {
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
                circleList.add(new Circle(new Author(user.getId(), user.getName(), user.getScreenName(), user.getProfileImageURLHttps()), urls));

                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (TwitterException e) {
            e.printStackTrace();
        }
        //json形式で出力
        lastPw.println(gson.toJson(circleList));

        //close処理
        br.close();
        lastPw.close();
        lastFileWriter.close();
    }

    public static QueryResult searchMenuTweet(Twitter twitter, Query query, User user) throws TwitterException {
        query.setCount(100);
        query.setQuery("from:" + user.getScreenName() + " お品書き OR おしながき OR 新刊 OR 落としました OR 落ちました OR グッズ");
        query.setSince("2016-12-01");
        query.setUntil("2016-12-31");
        QueryResult result = twitter.search(query);
        return result;
    }
}
