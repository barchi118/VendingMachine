package VendingMachine;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== 1. システムの初期化 ===");
        StockManager stockManager = new StockManager();
        SalesManager salesManager = new SalesManager(stockManager);

        System.out.println("\n=== 2. 商品の補充 ===");
        Item cola = new Item("コーラ", 160, 2);
        Item water = new Item("水", 110, 0);
        AlcoholItem beer = new AlcoholItem("ビール", 250, 2);
        LuckyItem luckyCola = new LuckyItem("当たり付きコーラ", 160, 5);

        stockManager.addItem(cola);
        stockManager.addItem(water);
        stockManager.addItem(beer);
        stockManager.addItem(luckyCola);
        stockManager.displayItems();

        System.out.println("--- シナリオE（Money型を用いたお金の投入） ---");
        try {
            salesManager.refund(); // 一旦残金をリセット
            // Enumの500円玉を投入する
            salesManager.insertMoney(Money.FIVE_HUNDRED);
        } catch (Exception e) {
            System.out.println("エラー: " + e.getMessage());
        }

        System.out.println("\n--- シナリオF-1（お酒の正常購入: 大人） ---");
        try {
            // isAdult = true でビールを購入
            salesManager.purchaseItem("ビール", true);
        } catch (Exception e) {
            System.out.println("エラー: " + e.getMessage());
        }

        System.out.println("\n--- シナリオF-2（お酒の異常購入: 未成年） ---");
        try {
            // isAdult = false でビールを購入
            salesManager.purchaseItem("ビール", false);
        } catch (Exception e) {
            System.out.println("想定通りのエラーを検知: " + e.getMessage());
        }

        System.out.println("\n--- シナリオG（ラッキー商品の購入） ---");
        try {
            // 運試し！（20%の確率で当たりが出ます）
            salesManager.insertMoney(Money.ONE_THOUSAND);
            salesManager.purchaseItem("当たり付きコーラ", true);
            salesManager.purchaseItem("当たり付きコーラ", false); // 当たり付きコーラは未成年(false)でも買える！
        } catch (Exception e) {
            System.out.println("エラー: " + e.getMessage());
        }

        System.out.println("\n=== 4. お釣りの返却 ===");
        salesManager.refund();

        System.out.println("\n=== 最終状態の確認 ===");
        stockManager.displayItems();
    }

    // 改修前
    // System.out.println("\n=== 3. 購入シミュレーション ===");

    // System.out.println("--- シナリオA（正常購入） ---");
    // try {
    // salesManager.insertMoney(500);
    // salesManager.purchaseItem("コーラ"); // purchaseItemに変更
    // } catch (Exception e) {
    // System.out.println("エラー: " + e.getMessage());
    // }

    // System.out.println("\n--- シナリオB（異常系：金額不足） ---");
    // try {
    // salesManager.purchaseItem("コーラ");
    // salesManager.purchaseItem("コーラ");
    // } catch (Exception e) {
    // System.out.println("想定通りのエラーを検知: " + e.getMessage());
    // }

    // System.out.println("\n--- シナリオC（異常系：在庫切れ） ---");
    // try {
    // salesManager.insertMoney(1000);
    // salesManager.purchaseItem("水");
    // } catch (Exception e) {
    // System.out.println("想定通りのエラーを検知: " + e.getMessage());
    // }

    // System.out.println("\n--- シナリオD（異常系：存在しない商品） ---");
    // try {
    // salesManager.purchaseItem("お茶");
    // } catch (Exception e) {
    // System.out.println("想定通りのエラーを検知: " + e.getMessage());
    // }

    // System.out.println("\n=== 4. お釣りの返却 ===");
    // salesManager.refund();

    // System.out.println("\n=== 最終状態の確認 ===");
    // stockManager.displayItems();
}
