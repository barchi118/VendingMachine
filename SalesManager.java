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
    public void insertMoney(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("投入金額は正の値でなければなりません");
        }
        this.currentAmount += amount;
    }

    // 商品を購入するメソッド
    public void purchaseItem(String itemName) {

        Item item = stockManager.getItem(itemName);
        // エラー処理
        if (item == null) {
            // 商品が存在しない場合の例外を投げる
            throw new IllegalArgumentException("指定された商品は存在しません");
        }
        if (!item.isAvailable()) {
            // 商品が在庫切れの場合の例外を投げる
            throw new IllegalStateException("在庫がありません");
        }
        if (currentAmount < item.getPrice()) {
            // 投入金額が不足している場合の例外を投げる
            throw new IllegalStateException("投入金額が不足しています");
        }

        // 正常処理 在庫を減らす
        item.reduceStock();
        // 投入金額から商品価格を引く
        currentAmount -= item.getPrice();
    }

    // 投入金額を返金するメソッド
    public int refund() {
        int refundAmount = currentAmount;
        currentAmount = 0;
        return refundAmount;
    }

}
