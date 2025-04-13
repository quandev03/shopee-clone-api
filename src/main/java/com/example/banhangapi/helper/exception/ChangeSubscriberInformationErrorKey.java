package com.example.banhangapi.helper.exception;

public enum ChangeSubscriberInformationErrorKey {

    IMAGE_PORTRAIT_NOT_FOUND(1001, "Image portrait not found"),
    EXIT_PRODUCT(1002, "Sản phẩm đã tồn tại");

    private final int code;
    private final String message;

    ChangeSubscriberInformationErrorKey(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}