package VendingMachine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 在庫管理クラス
public class StockManager {

    // 商品在庫を管理するマップ（商品名をキーとして商品オブジェクトを保持）
    private Map<String, Item> inventory;

    // コンストラクタ
    public StockManager() {
        // 初期化時は空の在庫マップを用意する（商品は後から補充する想定）
        this.inventory = new HashMap<>();
    }

    // 商品を補充するメソッド
    public void addItem(Item item) {
        // 商品が null の場合は例外を投げる
        if (item == null) {
            throw new IllegalArgumentException("商品が指定されていません");
        }
        // 商品名をキーとして Map に登録
        this.inventory.put(item.getName(), item);

    }

    // 商品一覧ｓを取得するメソッド
    public void displayItems() {
        System.out.println("商品一覧:");
        for (Item item : inventory.values()) {
            System.out.println(item.getName() + " - 価格: " + item.getPrice() + "円, 在庫: " + item.getStock());
        }
    }

    // 商品名で検索して取得する
    public Item getItem(String itemName) {
        // Mapから商品名(キー)で検索
        Item item = this.inventory.get(itemName);

        // 見つからなかったらエラーを投げる
        if (item == null) {
            throw new IllegalArgumentException("指定された商品は存在しません");
        }
        return item;
    }

}
