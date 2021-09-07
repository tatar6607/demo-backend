package com.linbankbackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "Users")

@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ssn;


    private String firstname;
    private String lastname;
    private String address;

    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

    private String email;
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;


    @Column(name = "last_logged_in")
    private LocalDateTime lastLoggedIn;

    @ManyToOne
    @JoinColumn(name = "role")
    private Role role;

    public User(String ssn, String firstname, String lastname, String address, String mobilePhoneNumber, String email, String password) {
        this.ssn = ssn;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.email = email;
        this.password = password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.ssn;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
