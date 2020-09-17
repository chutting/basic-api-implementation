package com.thoughtworks.rslist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.rslist.api.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@Table(name = "research")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResearchEntity {

  @Id
  @GeneratedValue
  private int id;

  @Column(name = "name")
  private String eventName;

  @Column(name = "vote_num")
  @Builder.Default
  private Integer voteNum = 0;

  private String keyword;

  @JoinColumn(name = "user_id")
  private Integer userId;
}
