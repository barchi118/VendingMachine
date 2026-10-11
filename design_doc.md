# 自動販売機システム MVP設計書

## 1. MVPで達成する要件（スコープ）
* **商品管理**: 商品名、価格、在庫数を持つ。
* **お金の投入**: お金を投入し、現在の投入金額を保持・加算する。
* **商品の購入**:
  * 投入金額が価格以上、かつ在庫が1個以上ある場合のみ購入可能。
  * 購入成功時、在庫を1つ減らし、投入金額から代金を差し引く。
* **お釣りの返却**: 残った投入金額をすべて返却し、投入金額を0円にリセットする。

---

## 2. クラス構成と責務

| クラス名 | 役割 / 責務 |
| :--- | :--- |
| **`Item`** | 商品エンティティ。商品名、価格、在庫数をカプセル化して保持し、在庫の増減ルールを管理する。 |
| **`SalesManager`** | 自販機本体。商品一覧（リスト）、投入金額の状態を管理し、購入・返金ロジックを制御する。 |
| **`Main`** | 実行・検証用。自販機の生成、お金の投入、購入、お釣り排出の一連の流れをテストする。 |

---

## 3. 各クラスの詳細設計

### ① `Item` クラス（商品）
* **フィールド**
  * `private String name`（商品名）
  * `private int price`（価格）
  * `private int stock`（在庫数）
* **メソッド**
  * コンストラクタ: `Item(String name, int price, int stock)`
  * 各フィールドの Getter
  * `public boolean isAvailable()`: 在庫が1以上あるかを判定 (`stock > 0`)
  * `public void decreaseStock()`: 在庫を1減らす（在庫が0以下の場合は `IllegalArgumentException` 等を投げる）

### ② `StockManager` クラス（在庫・商品管理）
自販機の「裏側（管理者）」の役割を担い、商品ラインナップと在庫を管理する。

- **フィールド**
  * `private Map<String, Item> inventory`: 商品名(String)をキー、商品オブジェクト(Item)を値として管理する在庫Map。
- **メソッド**
  * `public StockManager()`: コンストラクタ。空のMap（`new HashMap<>()`）を初期化する。
  * `public void addItem(Item item)`: 引数で受け取ったオブジェクトから商品名（`item.getName()`）を取り出し、それをキーとして `inventory` に登録（`put`）する。
  * `public Item getItem(String itemName)`: 商品名をキーにしてMapから直接取得（`get`）する。見つからなければエラー（例外）を投げる。※SalesManagerから商品を検索される際に使用する。
  * `public void displayItems()`: Mapの値（`inventory.values()`）を拡張for文でループし、現在の商品名、価格、在庫状況（在庫あり / 売り切れ）を一覧表示する。


### ③ `SalesManager` クラス（販売・お金管理）
自販機の「表側（顧客）」の役割を担い、お金の投入と購入処理を制御する。

- **フィールド**
  * `private int currentAmount`: 現在投入されている金額。
  * `private StockManager stockManager`: 在庫状況を確認するための管理オブジェクト。
- **メソッド**
  * `public SalesManager(StockManager stockManager)`: コンストラクタ。投入金額を0にし、引数で渡された `stockManager` を自身のフィールドにセットする（連携の準備）。
  * `public void insertMoney(int amount)`:
    * バリデーション: 0円以下の金額は不正入力として例外を投げる。
    * 正常系: `currentAmount` に金額を加算する。
  * `public Item purchaseItem(String itemName)`:
    * 対象商品を `stockManager.getItem(itemName)` を呼び出して取得する。
    * 判定1: 在庫があるか？（`item.isAvailable()`）
    * 判定2: 投入金額が足りているか？（`currentAmount >= item.getPrice()`）
    * 購入処理: 代金を `currentAmount` から減算し、`item.decreaseStock()` を実行。
    * 返却値: 購入できた `Item` オブジェクト。
  * `public int refund()`:
    * 現在の投入金額を返却値として返し、`currentAmount` を0にリセットする。
### ④ `Main` クラス（実行・シナリオテスト用）
自販機システム全体の立ち上げ（初期化）と、ユーザーの操作シナリオをシミュレーションして各クラスの連携を検証する。

- **処理フロー（`public static void main(String[] args)` 内で実行）**
  1. **システムの初期化（セットアップ・DI）**
     * `StockManager` のインスタンスを生成する。
     * その `StockManager` を引数にして、`SalesManager` のインスタンスを生成する。
  2. **商品の補充（管理者業務のシミュレーション）**
     * `Item` オブジェクトを複数作成する（例: "コーラ" 160円 2個、"水" 110円 0個など）。
     * `stockManager.addItem()` を呼び出し、自販機に商品をセットする。
     * `stockManager.displayItems()` を実行し、初期状態のラインナップを確認する。
  3. **購入シミュレーション（顧客業務のシミュレーション）**
     * **シナリオA（正常購入）**:
       * `salesManager.insertMoney(500)` で 500円を投入する。
       * `salesManager.purchaseItem("コーラ")` を実行し、購入成功と残り金額の減算を確認する。
     * **シナリオB（異常系：金額不足）**:
       * 残金が足りない状態でさらに `salesManager.purchaseItem("コーラ")` を実行し、金額不足のエラーで弾かれることを確認する。
     * **シナリオC（異常系：在庫切れ）**:
       * 在庫0の "水" に対して `salesManager.purchaseItem("水")` を実行し、売り切れのエラーで弾かれることを確認する。
     * **シナリオD（異常系：存在しない商品）**:
       * `salesManager.purchaseItem("お茶")` を実行し、商品が存在しないエラーで弾かれることを確認する。
  4. **お釣りの返却**
     * 最後に `salesManager.refund()` を実行し、残金が正しくお釣りとして返却されることを確認する。
---

## 4. 今後の拡張アイデア（MVP完了後）
* **ポリモーフィズムの導入**: 通常商品、ホット/コールド商品、当たり付き商品、酒類（年齢確認が必要）などの派生クラス（継承）の実装。
* **Enumの活用**: 受け付ける硬貨・紙幣（10円、50円、100円、500円、1000円）の限定とバリデーション。