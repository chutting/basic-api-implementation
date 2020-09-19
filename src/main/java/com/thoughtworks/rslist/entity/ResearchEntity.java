package com.thoughtworks.rslist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Builder
@Table(name = "research")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResearchEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "name")
  private String eventName;

  @OneToMany(mappedBy = "research", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
  private List<VoteEntity> voteList;

  private String keyword;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @JsonIgnore
  public UserEntity getUser() {
    return user;
  }

  @JsonProperty
  public void setUser(UserEntity user) {
    this.user = user;
  }
}
