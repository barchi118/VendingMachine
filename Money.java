package VendingMachine;

// お金の種類を表すEnumクラス
public enum Money {
    // 10円、50円、100円、500円、1000円の硬貨・紙幣を表すEnum
    TEN(10),
    FIFTY(50),
    ONE_HUNDRED(100),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000);

    // 金額を保持するフィールド（変更されないので final を付けるのが実務の定石です）
    private final int value;

    // Enumのコンストラクタ
    Money(int value) {
        this.value = value;
    }

    // 金額を取得するGetter
    public int getValue() {
        return value;
    }
}