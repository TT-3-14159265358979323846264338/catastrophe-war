タワーディフェンスゲーム♦作成中♦  
  
**♦概要♦**  
Java Spring boot, JavaScriptを中心に作成したWeb用タワーディフェンスゲーム。  
  
**♦制作背景♦**  
これまでJava単独でゲームを作成してきたが、より実践的な勉強をするため、Spring boot, JavaScriptを導入することにした。  
前回作のタワーディフェンスゲームの良いところを残しつつ、webで動作するゲームの作成を開始した。  
前回作: https://github.com/TT-3-14159265358979323846264338/defend_the_castle  
  
**♦環境♦**  
**言語:**　　　　　　　Java 25, JavaScript ES6+  
**フレームワーク:**　　Spring boot 4.0.3, Spring Security, Spring Session (JDBC)  
**データベース:**　　　MySQL 9.6  
**通信プロトコル:**　　WebSocket (SockJS v1.x, StompJS v7.3.0)  
**フロントエンド:**　　HTML Living Standard, CSS 3  
**テスト:**　　　　　　JUnit 5  
**ビルド:**　　　　　　Maven  
  
**♦MySQL関連設定♦**  
**権限:** ユーザーの権限はSELECT, INSERT, UPDATE, CREATE, DELET, REFERENCES, INDEX  
**other/jdbc設定.txt:** session用テーブル作成コード。初回起動前にDB内にテーブルを作成しておくこと。  
**src/main/resouces/application.properties:** このファイルにデータベース情報を記述する。初回のみ次のように変更することで、指定のデータベース内にテーブルを自動作成する。  
```properties
spring.jpa.hibernate.ddl-auto=update
```

**♦ログイン情報♦**  
**通常ユーザー用**  
**ユーザー名:** user  
**パスワード:** password  
**特記:** テスト用に使用するセーブデータの編集機能以外が使用可能  

**管理者用**  
**ユーザー名:** admin  
**パスワード:** adminpassword  
**特記:** 全部の機能が使用可能  
  
**♦プロジェクト構成♦**  
```text
├─ src/  
│　　├─ main/  
│　　│　　├─ java/com/example/　　　　　　　　:バックエンド制御 (機能やページごとにフォルダ化)  
│　　│　　└─ resources/  
│　　│　　　　　├─ static/  
│　　│　　　　　│　　├ css/　　　　　　　　　　:フロントエンドレイアウト制御 (ページごとにフォルダ化)  
│　　│　　　　　│　　├ images/　　　　　　　　:ゲームで使用する画像ファイル  
│　　│　　　　　│　　├ js/　　　　　　　　　　 :フロントエンド描写制御 (ページごとにフォルダ化)
│　　│　　　　　│　　└ favicon.ico　　　　　　:アイコン画像ファイル  
│　　│　　　　　├─ templates/　　　　　　　　 :HTMLソースコード (ページごとにフォルダ化)  
│　　│　　　　　└─ application.properties　　:アプリケーション設定  
│　　└─ test/　　　　　　　　　　　　　　　　　:テストコード (今後作成予定)  
├─ other/　　　　　　　　　　　　　　　　　　　:設計メモなど (なくてもゲーム影響なし)  
└─ pom.xml　　　　　　　　　　　　　　　　　　 :Mavenの構成
```
  
**♦工夫♦**  
・FindByIndexNameSessionRepositoryやSessionConnectedEvent, sessionDisconnectedのEventListenerなどを用いて、二重ログイン、タブの複製、リロードに対する対策を充実させた。また、これらが発生した時も描写用のExecutorを停止するようにして、リソースリークの防止にも努める。 (../example/catastrophewar/SessionController.java, ../js/auth/index.js)
