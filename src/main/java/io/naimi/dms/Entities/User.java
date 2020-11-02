package io.naimi.dms.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@ToString
public class User implements Serializable {
    @Id
    private String username;
    private String cin;
    private String password;
    private Boolean enabled;
    @ManyToMany
    @JoinTable(name = "USERS_ROLES")
    private List<Role> roles;
}


