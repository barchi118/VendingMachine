package VendingMachine;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== 1. システムの初期化 ===");
        StockManager stockManager = new StockManager();
        SalesManager salesManager = new SalesManager(stockManager);

        System.out.println("\n=== 2. 商品の補充 ===");
        Item cola = new Item("コーラ", 160, 2);
        Item water = new Item("水", 110, 0);

        stockManager.addItem(cola);
        stockManager.addItem(water);
        stockManager.displayItems();

        System.out.println("\n=== 3. 購入シミュレーション ===");

        System.out.println("--- シナリオA（正常購入） ---");
        try {
            salesManager.insertMoney(500);
            salesManager.purchaseItem("コーラ"); // purchaseItemに変更
        } catch (Exception e) {
            System.out.println("エラー: " + e.getMessage());
        }

        System.out.println("\n--- シナリオB（異常系：金額不足） ---");
        try {
            salesManager.purchaseItem("コーラ");
            salesManager.purchaseItem("コーラ");
        } catch (Exception e) {
            System.out.println("想定通りのエラーを検知: " + e.getMessage());
        }

        System.out.println("\n--- シナリオC（異常系：在庫切れ） ---");
        try {
            salesManager.insertMoney(1000);
            salesManager.purchaseItem("水");
        } catch (Exception e) {
            System.out.println("想定通りのエラーを検知: " + e.getMessage());
        }

        System.out.println("\n--- シナリオD（異常系：存在しない商品） ---");
        try {
            salesManager.purchaseItem("お茶");
        } catch (Exception e) {
            System.out.println("想定通りのエラーを検知: " + e.getMessage());
        }

        System.out.println("\n=== 4. お釣りの返却 ===");
        salesManager.refund();

        System.out.println("\n=== 最終状態の確認 ===");
        stockManager.displayItems();
    }
}