package com.natsukashiiz.iicommon.entity;

import java.time.LocalDateTime;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("primary key")
    private Long id;
    @CreationTimestamp
    @Comment("created datetime")
    private LocalDateTime cdt;
    @UpdateTimestamp
    @Comment("updated datetime")
    private LocalDateTime udt;
}
