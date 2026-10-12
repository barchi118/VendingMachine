package VendingMachine;

// 販売管理クラス
public class SalesManager {

    // 投入金額
    private int currentAmount;
    // 在庫状況を確認するための管理オブジェクト
    private StockManager stockManager;

    // コンストラクタ
    public SalesManager(StockManager stockManager) {
        this.currentAmount = 0;
        this.stockManager = stockManager;
    }

    // お金を投入するメソッド
    // 改修前
    // public void insertMoney(int amount) {
    // if (amount <= 0) {
    // throw new IllegalArgumentException("投入金額は正の値でなければなりません");
    // }
    // this.currentAmount += amount;
    // }

    // 改修後
    public void insertMoney(Money coin) {
        if (coin == null) {
            throw new IllegalArgumentException("硬貨が認識できません");
        }
        // Enumは正の固定値であることが保証されているので、0円以下のチェックが不要！
        this.currentAmount += coin.getValue();
        System.out.println(coin.getValue() + "円硬貨が投入されました。現在の投入金額: " + this.currentAmount + "円");
    }

    // 商品を購入するメソッド 改修前
    // public void purchaseItem(String itemName) {

    // Item item = stockManager.getItem(itemName);
    // // エラー処理
    // if (item == null) {
    // // 商品が存在しない場合の例外を投げる
    // throw new IllegalArgumentException("指定された商品は存在しません");
    // }
    // if (!item.isAvailable()) {
    // // 商品が在庫切れの場合の例外を投げる
    // throw new IllegalStateException("在庫がありません");
    // }
    // if (currentAmount < item.getPrice()) {
    // // 投入金額が不足している場合の例外を投げる
    // throw new IllegalStateException("投入金額が不足しています");
    // }

    // // 正常処理 在庫を減らす
    // item.reduceStock();
    // // 投入金額から商品価格を引く
    // currentAmount -= item.getPrice();
    // }

    // ★改修2: 購入メソッドに「年齢フラグ(isAdult)」を追加
    public void purchaseItem(String itemName, boolean isAdult) {

        Item item = stockManager.getItem(itemName);

        if (item == null) {
            throw new IllegalArgumentException("指定された商品は存在しません");
        }

        // ★改修3: if文による在庫チェックを削除し、Item自身のチェック機能に任せる（ポリモーフィズム）
        // 相手がただのItemか、AlcoholItemかは気にしない。命令するだけ！
        item.validatePurchase(isAdult);

        if (currentAmount < item.getPrice()) {
            throw new IllegalStateException("投入金額が不足しています");
        }

        // 正常処理: 在庫を減らす（LuckyItemなら、ここで勝手に当たり判定も動く）
        item.reduceStock();
        currentAmount -= item.getPrice();

        System.out.println(itemName + " を購入しました。残り投入金額: " + this.currentAmount + "円");
    }

    // 投入金額を返金するメソッド
    public int refund() {
        int refundAmount = currentAmount;
        currentAmount = 0;
        return refundAmount;
    }

}
