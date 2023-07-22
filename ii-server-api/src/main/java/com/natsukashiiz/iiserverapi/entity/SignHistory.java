package com.natsukashiiz.iiserverapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.natsukashiiz.iicommon.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "tb_sign_history")
@Data
public class SignHistory extends BaseEntity {
    @Column(nullable = false, length = 32)
    @Comment("ip v4 address")
    private String ipv4;

    @JsonIgnore
    @Column(nullable = false)
    @Comment("user agent")
    private String ua;

    @Column(nullable = false)
    @Comment("device type")
    private Integer device;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false, updatable = false)
    @Comment("user")
    @JsonIgnore
    private User user;

    @Column(name = "uid", updatable = false, insertable = false)
    @Comment("id of user")
    private Long uid;
}
