package kr.co.vlink.Vlink.domain;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
public class VideoProduct {
    private int startTime;
    private int endTime;

    private float left;
    private float top;
    private float width;
    private float height;

    private Integer count = 0;

    @DBRef
    private Product product;

    public void updateProduct(Product product) {
        this.product = product;
    }
    public void updateCount(int count) { this.count = count; }
    public void addCount() { this.count++; }
}
