# 書籍管理システム

## このアプリケーションについて
Spring Bootを使用したデモアプリケーション

Initializer（https://start.spring.io/）で選択した項目は、
 * Spring Boot 2.2.4 Java(JDK11)
 * H2DB (demoなので…)
 * Gradle
 * Mustacheテンプレート
だった気がします

## デモ環境
`master`ブランチの内容がデプロイされています

https://demo-library-2020.herokuapp.com/

## 機能
 * 書籍表示（&検索）
   * テーブルのレコードが0になった後の起動時、デモ用マスタデータが入ります
 * 書籍の登録
 * 書籍情報更新
 * 書籍の削除

上記と同様の機能をもつAPIも実装

## 保留事項
 * 検索が完全一致のみ
 * Viewを整える

## 参考にしたページ

ここからベースをとってきた
https://start.spring.io/

アプリケーションとして起動する
https://spring.io/guides/gs/spring-boot/

JPAをつかってデータベースアクセス
https://spring.io/guides/gs/accessing-data-jpa/

JPAでSQLクエリを書く
https://www.baeldung.com/spring-data-jpa-query

Viewをかえすには
https://spring.io/guides/gs/serving-web-content/

Mustache記法のView
https://www.baeldung.com/spring-boot-mustache

テスト用データソース
https://www.codeflow.site/ja/article/spring-testing-separate-data-source