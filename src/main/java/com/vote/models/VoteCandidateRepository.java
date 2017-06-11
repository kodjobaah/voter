package com.vote.models;

import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface VoteCandidateRepository extends CrudRepository<VoteCandidate, Long> {

  @Query(
    value =
        "select new com.vote.models.VoteResults(vc.candidate, count(vc)) from VoteCandidate vc "
            + " where vc.createDt >= ?1 and vc.createDt <= ?2  group by vc.candidate"
  )
  List<VoteResults> findVoteCount(LocalDateTime start, LocalDateTime end);
}
