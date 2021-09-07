package com.linbankbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ERoles name;

    @JsonIgnore
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    private Set<User> user;

    public Role(ERoles name) {
        this.name = name;
    }

    public Role() {

    }
}
