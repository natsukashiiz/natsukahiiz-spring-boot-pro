package com.natsukashiiz.iiserverapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.natsukashiiz.iiboot.configuration.jwt.AuthPrincipal;
import com.natsukashiiz.iicommon.entity.BaseEntity;
import com.natsukashiiz.iicommon.utils.MapperUtil;
import com.natsukashiiz.iiserverapi.model.response.UserResponse;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "tb_users")
@Data
public class User extends BaseEntity {
    @Column(unique = true, nullable = false, length = 30, updatable = false)
    @Comment("username")
    private String username;
    @Column(nullable = false, length = 50)
    @Comment("email")
    private String email;
    @JsonIgnore
    @Column(nullable = false)
    @Comment("password")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<SignHistory> signHistory;

    public static User from(AuthPrincipal auth) {
        return MapperUtil.mapOne(auth, User.class);
    }

    public static UserResponse toResponse(User user) {
        return MapperUtil.mapOne(user, UserResponse.class);
    }
}
