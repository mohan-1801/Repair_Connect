package com.repairconnect.entity;

import jakarta.persistence.*; import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Entity @Table(name="users")
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="full_name", nullable=false) private String fullName;
    @Column(nullable=false, unique=true) private String email;
    @JsonIgnore @Column(nullable=false) private String password;
    private String phone;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role;
    private boolean enabled = true;
    private LocalDateTime createdAt = LocalDateTime.now();
    public User() {}
    public User(String fullName,String email,String password,String phone,Role role){this.fullName=fullName;this.email=email;this.password=password;this.phone=phone;this.role=role;}
    public Long getId(){return id;} public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
    public String getEmail(){return email;} public String getPassword(){return password;} public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public Role getRole(){return role;} public boolean isEnabled(){return enabled;} public void setEnabled(boolean v){enabled=v;} public LocalDateTime getCreatedAt(){return createdAt;}
}