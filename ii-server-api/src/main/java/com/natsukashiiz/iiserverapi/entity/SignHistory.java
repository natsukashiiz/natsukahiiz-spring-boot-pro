package com.natsukashiiz.iiserverapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.natsukashiiz.iicommon.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

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

    @Column(name = "uid", nullable = false, updatable = false, insertable = false)
    @Comment("id of user")
    private Long uid;
}
