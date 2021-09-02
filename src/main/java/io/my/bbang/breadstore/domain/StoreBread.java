package io.my.bbang.breadstore.domain;

import io.my.bbang.commons.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "storeBread")
public class StoreBread extends BaseTimeEntity {

    @Id
    private String id;
    private String storeId;
    private String name;
    private int price;
    private List<ModifyUserList> userList;
    private List<LocalDateTime> bakeTimeList;

    @Getter @Setter
    public static class ModifyUserList {
        private String userId;
        private LocalDateTime modifyDate;
    }
}
