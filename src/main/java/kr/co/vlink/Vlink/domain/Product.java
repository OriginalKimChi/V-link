package kr.co.vlink.Vlink.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@ToString
@NoArgsConstructor
@Document("Product")
public class Product {
    @Id
    private String id;
    private String productName;
    private String productId;
    private String productImageURL;
    private String price;
    private String deliveryCharge;
    private String productURL;

    private Integer viewCount;

    @Builder
    public Product(String id, String productName, String productId, String productImageURL, String price, String deliveryCharge, String productURL) {
        this.id = id;
        this.productName = productName;
        this.productId = productId;
        this.productImageURL = productImageURL;
        this.price = price;
        this.deliveryCharge = deliveryCharge;
        this.productURL = productURL;
        this.viewCount = 0;
    }

    public void updateData(String productName, String productImageURL, String price) {
        this.productName = productName;
        this.productImageURL = productImageURL;
        this.price = price;
    }

    public void addViewCount() {
        if(this.viewCount == null) {
            this.viewCount = 1;
        } else {
            this.viewCount += 1;
        }
    }

}
