package kr.co.vlink.Vlink.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductDTO {
    private String id;
    private String productName;
    private String productId;
    private String productImageURL;
    private String price;
    private String deliveryCharge;
    private String productURL;

    private String userId;

    @Builder
    public ProductDTO(String id, String productName, String productId, String productImageURL, String price, String deliveryCharge, String productURL) {
        this.id = id;
        this.productName = productName;
        this.productId = productId;
        this.productImageURL = productImageURL;
        this.price = price;
        this.deliveryCharge = deliveryCharge;
        this.productURL = productURL;
    }
}
