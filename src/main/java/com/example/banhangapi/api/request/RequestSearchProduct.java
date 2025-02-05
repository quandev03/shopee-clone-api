package com.example.banhangapi.api.request;

import lombok.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Builder
@ToString
public class RequestSearchProduct {
    private String nameProduct;
    private Long minPrice;
    private Long maxPrice;

    public RequestSearchProduct() {
        this.nameProduct = "";
        this.minPrice = 0L;
        this.maxPrice = 999999999999999999L;
    }
    public RequestSearchProduct(String nameProduct, Long minPrice, Long maxPrice) {
        this.nameProduct = nameProduct == null ? "" : nameProduct;
        this.minPrice = minPrice==null ? 0L : minPrice;
        this.maxPrice = maxPrice==null ? 99999999999999999L : maxPrice;
    }
    public static RequestSearchProduct fromString(String input) {
        // Biểu thức chính quy để phân tích chuỗi
        String regex = "RequestSearchProduce\\(nameProduct=(.*?), minPrice=(\\d+), maxPrice=(\\d+)\\)";Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            // Trích xuất giá trị từ chuỗi
            String nameProduct = matcher.group(1);
            Long minPrice = Long.parseLong(matcher.group(2));
            Long maxPrice = Long.parseLong(matcher.group(3));

            // Tạo và trả về đối tượng RequestSearchProduce
            return new RequestSearchProduct(nameProduct, minPrice, maxPrice);
        } else {
            throw new IllegalArgumentException("Chuỗi không hợp lệ");
        }
    }
}
