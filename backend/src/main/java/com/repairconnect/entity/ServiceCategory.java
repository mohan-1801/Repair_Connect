package com.repairconnect.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="service_categories") public class ServiceCategory {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @Column(nullable=false,unique=true) private String name; private String description; private boolean active=true; private LocalDateTime createdAt=LocalDateTime.now();
 public ServiceCategory(){} public ServiceCategory(String n,String d){name=n;description=d;} public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;} public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}