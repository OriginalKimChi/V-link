package kr.co.vlink.Vlink.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@ToString
@Document("HashTag")
public class  HashTag {
    @Id
    private String id;
    private String hashTag;
    private Integer count;

    @Builder
    public HashTag(String id, String hashTag) {
        this.id = id;
        this.hashTag = hashTag;
        this.count = 1;
    }

    public void updateCount() { this.count++; }
}
