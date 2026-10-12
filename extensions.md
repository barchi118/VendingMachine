# 自販機システム 拡張機能　詳細設計書

## ① `Money` 列挙型（Enum）の導入【オーバーロードの学習】
現実の自販機が「1円玉」や「1万円札」を物理的に弾くように、システムでも不正な硬貨を型レベルで弾くための仕組みを導入する。Javaでは、決まった値しか持たないオブジェクトの集まりを表現するのに **Enum（列挙型）** という特殊なクラスを使用する。

- **`Money` Enum（新規作成）**
  * `TEN(10)`, `FIFTY(50)`, `ONE_HUNDRED(100)`, `FIVE_HUNDRED(500)`, `ONE_THOUSAND(1000)` の5つの定数を定義する。
  * `private int value`: 金額を保持するフィールド。
  * `public int getValue()`: 金額を取得するGetter。

---

## ② `Item` クラスの改修と派生クラス【継承とポリモーフィズムの学習】
商品ごとに異なるビジネスルール（年齢確認など）を、自販機本体の `if` 文を増やすことなく、各商品オブジェクト自身に判断させる（ポリモーフィズム）。

- **1. `Item` クラス（親クラス・改修）**
  * **追加メソッド**: `public void validatePurchase(boolean isAdult)`
    * 購入前の総合チェックを行う。
    * 親クラスの基本機能として、在庫がない場合は `IllegalStateException`（"在庫がありません"）を投げる。

- **2. `AlcoholItem` クラス（子クラス・新規作成）**
  * `Item` を **継承 (`extends`)** する。
  * **オーバーライド**: `validatePurchase(boolean isAdult)` メソッドを上書きする。
    * `super.validatePurchase(isAdult)` を呼び出し、まず親の「在庫チェック」を行わせる。
    * その後、引数 `isAdult` が `false` の場合は `IllegalStateException`（"未成年にはお酒を販売できません"）を投げる独自のビジネスルールを追加する。

- **3. `LuckyItem` クラス（子クラス・新規作成）**
  * `Item` を **継承 (`extends`)** する。当たり付きのジュースを表現する。
  * **オーバーライド**: `reduceStock()` メソッドを上書きする。
    * 乱数（`Math.random()` 等）を使用し、例えば20%の確率で「当たり」のコンソール出力をする。
    * 通常通り親の在庫減少処理（`super.reduceStock()`）を呼ぶ。

---

## ③ `SalesManager` クラスの改修
自販機の表側の機能（インターフェース）を拡張し、新しい通貨仕様と年齢確認に対応する。

- **メソッドのオーバーロード（追加）**
  * `public void insertMoney(Money coin)`
    * **役割**: `Money` Enumを受け取る新しい投入口。
    * **処理**: Enumで定義された硬貨しか受け付けないため、0円以下のバリデーションが不要になる。`currentAmount += coin.getValue();` のみを実行する。
    * *(※既存の `insertMoney(int amount)` も残しておくことで、オーバーロードが成立する)*

- **購入メソッドの改修**
  * `public void purchaseItem(String itemName, boolean isAdult)`
    * 引数に「購入者が成人かどうかのフラグ (`isAdult`)」を追加する。
    * 既存の在庫チェック（`!item.isAvailable()`）の `if` 文を削除し、代わりに `item.validatePurchase(isAdult);` を呼び出す。
    * **ポリモーフィズムの恩恵**: 取得した `item` がジュースなのかお酒なのかを `SalesManager` は一切気にしない。`validatePurchase` と命令するだけで、オブジェクト自身が自分の型に応じたチェックを勝手に実行する。

---

## ④ `Main` クラス（シナリオテスト用・改修）
拡張した機能を検証するためのシナリオを追加する。

- **処理フロー（追加・変更点）**
  1. **商品の補充**
     * `AlcoholItem`（例: "ビール", 250円）と `LuckyItem`（例: "当たり付きコーラ", 160円）をインスタンス化し、`StockManager` に追加する。
  2. **シナリオE（Money型を用いたお金の投入）**
     * `salesManager.insertMoney(Money.FIVE_HUNDRED);` を実行し、正しく500円が加算されるか確認する。
  3. **シナリオF（お酒の正常購入 / 未成年エラー）**
     * 大人の場合: `salesManager.purchaseItem("ビール", true);` 成功を確認。
     * 未成年の場合: `salesManager.purchaseItem("ビール", false);` 未成年エラーで弾かれることを確認。