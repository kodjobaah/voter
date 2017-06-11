package com.vote.models;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface CandidateRepository extends CrudRepository<Candidate, Long> {

  public Optional<Candidate> findByCandidate(String candidate);
}
