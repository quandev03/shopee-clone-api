package com.example.banhangapi.api.entity;

import com.example.banhangapi.api.globalEnum.ROLES;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Getter

public class User  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createBy", nullable = true)
    @CreatedBy
    private User createBy;
    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;


    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9_]{5,20}$", message = "Username is incorrect!")
    @Size(min = 5, max = 20 ,message = "Invalid length")
    @NotEmpty(message = "Username is not emtpy")
    private String username;

    @Column(nullable = false)
    private String password;

    @Pattern(regexp = "^(03|05|07|08|09)[0-9]{8}$", message = "Phone number is incorrect")
    @Size(min = 10, max = 10, message = "Phone number is incorrect")
    @Column(unique = true)
    private String phoneNumber;

    private String address;

    @Column(name = "roles")
    private ROLES roles = ROLES.ROLE_USER;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "createBy"
    )
    private List<Comment> comments = new ArrayList<>();

//    @OneToOne
//    Address addressUser;

    @Column(name = "birthday")
    private String birthday = "01/01/1990";

    private String avatar;
    private String fullName;
}

