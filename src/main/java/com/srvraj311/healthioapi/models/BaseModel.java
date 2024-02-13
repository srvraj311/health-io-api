package com.srvraj311.healthioapi.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public abstract class BaseModel {
    @Field(name = "created_at")
    @CreatedDate
    LocalDateTime createdAt;

    @Field(name = "updated_at")
    @LastModifiedDate
    LocalDateTime updatedAt;

    @Field(name = "deleted_at")
    LocalDateTime deletedAt;
}