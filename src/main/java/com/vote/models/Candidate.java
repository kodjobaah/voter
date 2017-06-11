package com.vote.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(
  name = "CANDIDATE",
  uniqueConstraints = {@UniqueConstraint(columnNames = "candidate_id")}
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Candidate {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @NotNull
  @Column(unique = true, name = "candidate_id")
  private String candidate;

  @OneToMany(mappedBy = "candidate")
  private List<VoteCandidate> voteCandidates = new ArrayList<VoteCandidate>();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getCandidate() {
    return candidate;
  }

  public void setCandidate(String candidate) {
    this.candidate = candidate;
  }

  public List<VoteCandidate> getVoteCandidates() {

    if (voteCandidates == null) {
      voteCandidates = new ArrayList<VoteCandidate>();
    }

    return voteCandidates;
  }

  public void setVoteCandidates(List<VoteCandidate> voteCandidates) {
    this.voteCandidates = voteCandidates;
  }

  public void addVoteCandidate(VoteCandidate voteCandidate) {
    getVoteCandidates().add(voteCandidate);
  }
}
