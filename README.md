# 時間がない人のためのサークルチェッカー
## 概要
Twitterのフォロー欄からコミケ(コミックマーケット)参加者を割り出す。

割り出したコミケ参加者からお品書き(商品情報)を抽出する。

お品書きはtwitterに画像を上げたり、WebブログURLや、
pixivのURLを上げたりと様々である。

それを一括して取り出すプログラムを目指す。

## 機能
- フォロー欄からユーザーの抽出
- 抽出したユーザーのリストの作成
- リストの更新
- 抽出したユーザーのURL付tweetの検出
- URL付tweetがお品書きかどうかの判別
- 重複URLの削除
- お品書き情報およびサークル情報を保存

## API制限の回避
- 抽出したユーザーをローカル保存
- ObjectをJSON、JSONをObjectに変換する必要がある

### ローカルに保存するべき情報
#### Circleクラス
- サークル名
- サークル配置場所
- お品書きURL
- Authorクラス

#### Authorクラス
- twitterID
- twitter_name
- twitter_screen_name
- twitter_icon

### twitterアカウント抽出正規表現
- `.*\w.*\d\d[ab].*`
- `.*日目.*`
- `.*C91.*`
- `.*[木金土]曜.*`

## tweet抽出単語
- お品書き
- おしながき
- 新刊
- 落としました
- 落ちました
- グッズ



## 参考文献
[Twitter4J](http://twitter4j.org/ja/index.html)
[twitterの検索術 (search/tweets と search/universal)](https://gist.github.com/cucmberium/e687e88565b6a9ca7039)
[Twitter4Jで自分のフォロワーのリストを作成する](http://eiryu.hatenablog.com/entry/20101030/1288456899)
[GSONの基本的な使い方](http://qiita.com/u-chida/items/cbdd040e4199a10936dc)
[TwitterAPIで期間指定してTweetを取得する方法](http://qiita.com/areph/items/0745cb744a12810334c6#%E3%81%9D%E3%81%AE%E4%BB%96)
[Twitter APIの使い方まとめ](https://syncer.jp/twitter-api-matome)
[GET statuses/home_timeline - ホームタイムラインを取得](https://syncer.jp/twitter-api-matome/get/statuses/home_timeline)
