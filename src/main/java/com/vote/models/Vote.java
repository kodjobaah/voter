package com.vote.models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(
  name = "VOTE",
  uniqueConstraints = {@UniqueConstraint(columnNames = "voter_id")}
)
public class Vote {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToMany(mappedBy = "vote")
  private List<VoteCandidate> voteCandidates = new ArrayList<VoteCandidate>();

  @Column(name = "voter_id")
  private String voterId;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public String getVoterId() {
    return voterId;
  }

  public void setVoterId(String voterId) {
    this.voterId = voterId;
  }
}
