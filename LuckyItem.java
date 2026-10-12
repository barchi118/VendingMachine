package VendingMachine;

// ラッキー商品クラス
public class LuckyItem extends Item {
    // Itemクラスを継承したラッキー商品クラス
    public LuckyItem(String name, double price, int stock) {
        super(name, price, stock);
    }

    // 親クラスのメソッドをオーバーライド
    @Override
    public void validatePurchase(boolean isAdult) {
        // まず親クラス(Item)の基本ルール（在庫チェック）を実行する
        super.validatePurchase(isAdult);

        // ラッキー商品独自のルール（当たり判定）を追加で実行する
        double probability = Math.random();

        // 0.2未満なら当たり（つまり20%の確率）
        if (probability < 0.2) {
            System.out.println("🎉 ピピピッ！当たりです！もう1本プレゼント！ 🎉");
        }

    }

}
