package com.vote.controllers;

import com.vote.models.VoteCandidateRepository;
import com.vote.services.VoteService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/countMeUp")
public class VoteController {
  private static final Logger logger = LoggerFactory.getLogger(VoteController.class);
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  List<String> listOfCandidates;

  {
    listOfCandidates = new ArrayList<String>();
    listOfCandidates.add("A");
    listOfCandidates.add("B");
    listOfCandidates.add("C");
    listOfCandidates.add("D");
  }

  @Autowired private VoteService voteService;

  @Autowired private VoteCandidateRepository voteCandidateRepository;

  @PostMapping("/candidate/{candidate}/voter/{voter}")
  public ResponseEntity<?> countMeUp(@PathVariable String candidate, @PathVariable String voter) {

    List<String> allowable =
        listOfCandidates
            .stream()
            .filter(c -> c.equalsIgnoreCase(candidate))
            .collect(Collectors.toList());

    if (allowable.isEmpty()) {
      return ResponseEntity.badRequest().build();
    } else {
      return ResponseEntity.status(voteService.registerAndVote(voter, candidate)).build();
    }
  }

  @GetMapping("/results/start/{start}/end/{end}")
  public ResponseEntity<?> countMeUpResults(@PathVariable String start, @PathVariable String end) {

    try {
      LocalDateTime st = LocalDateTime.parse(start, formatter);
      LocalDateTime ed = LocalDateTime.parse(end, formatter);
      return ResponseEntity.ok(voteCandidateRepository.findVoteCount(st, ed));
    } catch (DateTimeParseException dte) {
      logger.error("Invalid Date [{}]", dte.getParsedString());
      return ResponseEntity.badRequest().build();
    }
  }
}
