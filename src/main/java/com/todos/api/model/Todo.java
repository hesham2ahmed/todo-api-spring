package com.todos.api.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.MongoId;
import java.util.Date;

@Data
@Builder
public class Todo {
    @MongoId
    @Generated
    private String id;
    private String personId;
    private String title;
    private String desc;
    private Date creatDate;
    private Date endDate;
    private boolean completed;
}
