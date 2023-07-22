package com.natsukashiiz.iiserverapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.natsukashiiz.iicommon.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "tb_users")
@Data
public class User extends BaseEntity {
    @Column(unique = true, nullable = false, length = 30, updatable = false)
    @Comment("username")
    private String username;
    @Column(unique = true, nullable = false)
    @Comment("email")
    private String email;
    @JsonIgnore
    @Column(nullable = false)
    @Comment("password")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<SignHistory> signHistories;
}
