package com.example.banhangapi.api.dto;

import com.example.banhangapi.api.entity.Comment;
import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.api.globalEnum.ROLES;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAdminDTO {
    private String id;
    private String username;
    private String phoneNumber;
    private ROLES roles;
    private String birthday = "01/01/1990";
    private String avatar;
    private String fullName;
    private  Boolean admin;
    private Boolean active;
    private String lastLogin;
}
