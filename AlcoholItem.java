package VendingMachine;

// アルコール商品クラス
public class AlcoholItem extends Item {
    // Itemクラスを継承したアルコール商品クラス
    public AlcoholItem(String name, double price, int stock) {
        super(name, price, stock);
    }

    // 親クラスのメソッドをオーバーライド
    @Override
    public void validatePurchase(boolean isAdult) {
        // まず親クラス(Item)の基本ルール（在庫チェック）を実行する
        super.validatePurchase(isAdult);

        // その後、お酒独自のルール（年齢確認）を追加で実行する
        if (!isAdult) {
            throw new IllegalStateException("未成年にはお酒を販売できません");
        }
    }
}
