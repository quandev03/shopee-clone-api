package com.example.banhangapi.api.globalEnum;

public enum StatusOrder {
    ORDER_WAITING_FOR_CONFIRMATION(0), // đợi xác nhận (6)
    ORDER_PACKING_GOODS(1),      // đóng gói (4)
    ORDER_TRANSPORT_GOODS(2),    // đang vận chuyển (5)     // all (7)
    ORDER_SUCCESS(3),            // thành công (0)
    ORDER_CANCEL(4),
    ORDER_ALL(5);           // huỷ (2)

    private final int value;

    // Constructor
    StatusOrder(int value) {
        this.value = value;
    }

    // Phương thức để lấy giá trị của trạng thái
    public int getValue() {
        return value;
    }

    // Phương thức static để chuyển đổi từ giá trị int thành Enum
    public static StatusOrder fromInt(int i) {
        for (StatusOrder status : StatusOrder.values()) {
            if (status.getValue() == i) {
                return status;
            }
        }
        // Nếu không tìm thấy, trả về null hoặc bạn có thể ném exception
        return null;
    }
}
