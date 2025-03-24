package com.example.banhangapi.api.globalEnum;

public enum StatusOrder {
    ORDER_SUCCESS(0),            // thành công (0)
    ORDER_FAIL(1),               // thất bại (1)
    ORDER_CANCEL(2),             // huỷ (2)
    ORDER_REFUSE(3),             // từ chối (3)
    ORDER_PACKING_GOODS(4),      // đóng gói (4)
    ORDER_TRANSPORT_GOODS(5),    // đang vận chuyển (5)
    ORDER_WAITING_FOR_CONFIRMATION(6), // đợi xác nhận (6)
    ORDER_ALL(7);                // all (7)

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
