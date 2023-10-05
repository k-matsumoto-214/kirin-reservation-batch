# とある病院の予約を取得するバッチ

### 目的
- 子供の病院予約を確実に取得したかった
  - 当日の特定時刻に予約を先着順で受け付けている
  - 早い時は1分経たずに埋まってしまう
  - 朝、忙しいときに忘れたり、取れなかったりするとその日子供を病院へつれていけない
  
### 使用技術  
- [事前予約登録LINEBot](https://github.com/k-matsumoto-214/kirin-linebot)
  - Java
  - SpringBoot
  - LINEBotSDK
  - [Groovy(自動テスト)](https://github.com/k-matsumoto-214/kirin-linebot/tree/master/src/test/groovy/com/kirin/linebot)・・・カバレッジ90%以上(コンフィグクラス含まない)
  - [Spock(自動テスト)](https://github.com/k-matsumoto-214/kirin-linebot/tree/master/src/test/groovy/com/kirin/linebot)・・・カバレッジ90%以上(コンフィグクラス含まない)
- 予約実行バッチ
  - Java
  - SpringBoot
  - LINEBotSDK
  - Selenium
  - [Groovy(自動テスト)](https://github.com/k-matsumoto-214/kirin-reservation-batch/tree/master/src/test/groovy/com/kirin/reservation)・・・カバレッジ90%以上(コンフィグクラス含まない)
  - [Spock(自動テスト)](https://github.com/k-matsumoto-214/kirin-reservation-batch/tree/master/src/test/groovy/com/kirin/reservation)・・・カバレッジ90%以上(コンフィグクラス含まない)
- DB
  - MySQL

### できること
- 予約の事前登録
  - LINEBotを通して予約の事前登録を行う
  - 息子二人 * 午前午後の4パターンの事前予約が可能
  - 先日付を指定しての事前予約が可能
- 予約の実施
  - 毎日特定時刻にDBを参照して事前予約があれば予約を実施する
  - Seleniumにて予約ページを操作して予約する
- 結果の通知
  - 予約実施時に成功・失敗を通知する
  - 予約成功時には、予約順・問診票へのリンクを含んだ通知を送信する

### 動作イメージ
![linebot](https://github.com/k-matsumoto-214/kirin-reservation-batch/assets/91876695/4ee2afc9-42e1-43e7-a1b6-2a711478f77c)
![スクリーンショット 2023-10-05 10 31 51](https://github.com/k-matsumoto-214/kirin-reservation-batch/assets/91876695/9f07e64f-f4e6-47a2-bf76-6bb82ace0622)


