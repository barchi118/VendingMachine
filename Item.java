package VendingMachine;

// 商品クラス
public class Item {
    // 商品名
    private String name;
    // 商品価格
    private double price;
    // 商品在庫
    private int stock;

    // コンストラクタ
    public Item(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // 各種ゲッター
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    // 在庫が1以上あるかを判定
    public boolean isAvailable() {
        return stock > 0;
    }

    // 在庫を1減らす
    public void reduceStock() {
        if (stock > 0) {
            stock--;
        } else {
            throw new IllegalStateException("在庫がありません");
        }
    }

}
