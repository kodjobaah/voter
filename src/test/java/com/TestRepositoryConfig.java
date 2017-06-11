package com;

import com.vote.models.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
  basePackageClasses = {
    CandidateRepository.class,
    VoteCandidateRepository.class,
    VoteRepository.class
  }
)
@EntityScan(basePackageClasses = {Vote.class, Candidate.class, VoteCandidate.class})
@Import({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class TestRepositoryConfig {}
