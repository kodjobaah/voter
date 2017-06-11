package com.vote.models;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface VoteRepository extends CrudRepository<Vote, Long> {
  public Optional<Vote> findByVoterId(String voterId);
}
