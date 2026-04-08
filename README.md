タワーディフェンスゲーム♦作成中♦  

♦環境♦  
Java 25, Spring boot 4.0.3, JUnit 5, MySQL 9.6, JavaScript ES6+, SockJS v1.x, StompJS v7.3.0, HTML Living Standard, CSS 3  
  
♦MySQL関連設定♦  
権限: ユーザーの権限はSELECT, INSERT, UPDATE, CREATE (CREATEは初回起動時のみ必要)  
src/main/resouces/application.properties: このファイルにデータベース情報を記述する。初回のみspring.jpa.hibernate.ddl-auto=updateに変更することで、指定のデータベース内にテーブルを自動作成する。  
  
♦プロジェクト構成♦  
├─ src/  
│　　├─ main/  
│　　│　　├─ java/com/example/　　　　　　　　:バックエンド制御 (Spring boot, 機能やwebページごとにフォルダ化)  
│　　│　　└─ resources/  
│　　│　　　　　├─ static/  
│　　│　　　　　│　　├ css/　　　　　　　　　　:フロントエンドレイアウト制御 (webページごとにフォルダ化)  
│　　│　　　　　│　　├ image/　　　　　　　　　:ゲームで使用する画像ファイル  
│　　│　　　　　│　　└ js/　　　　　　　　　　　:フロントエンド描写制御 (webページごとにフォルダ化)  
│　　│　　　　　├─ templates/　　　　　　　　　:HTMLソースコード (webページごとにフォルダ化)  
│　　│　　　　　└─ application.properties　　　　:アプリケーション設定  
│　　└─ test/　　　　　　　　　　　　　　　　　:テストコード (今後作成予定)  
├─ other/　　　　　　　　　　　　　　　　　　　:設計メモなど (なくてもゲーム影響なし)  
└─ pom.xml　　　　　　　　　　　　　　　　　　:Mavenの構成  
  
♦制作背景♦  
これまでJava単独でゲームを作成してきたが、より実践的な勉強をするため、Spring boot, JavaScriptを導入することにした。  
前回作のタワーディフェンスゲームの良いところを残しつつ、webで動作するゲームの作成を開始した。  
前回作: https://github.com/TT-3-14159265358979323846264338/defend_the_castle  
