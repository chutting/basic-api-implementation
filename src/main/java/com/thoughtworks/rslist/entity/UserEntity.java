package com.thoughtworks.rslist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.rslist.api.Research;
import com.thoughtworks.rslist.api.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
  public UserEntity(String userName, Integer age, String gender, String email, String phoneNumber) {
    this.userName = userName;
    this.age = age;
    this.gender = gender;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

  @Id
  @GeneratedValue
  private Integer id;

  @Column(name = "name")
  private String userName;

  private Integer age;

  private String gender;

  private String email;

  private String phoneNumber;

  @Builder.Default
  private Integer voteNum = 10;

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private List<ResearchEntity> researchList;
}
