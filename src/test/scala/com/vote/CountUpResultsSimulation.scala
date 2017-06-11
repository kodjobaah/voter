package com.vote

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CountUpResultsSimulation extends Simulation {

  val paramsFeeder = {
    Array(
      Map("start" -> s"2017-01-01 10:30:10"),
      Map("end" -> s"2016-12-09 10:30:10")
    )
  }

  val baseUrl = "http://localhost:8080"

  val httpConf = http
    .baseURL(baseUrl)
    .acceptHeader(
      "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // 6
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader(
      "Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("CountMeUpResultsSimulation")
    .feed(paramsFeeder)
    .repeat(4) {
      exec(
        http("request_1")
          .get("/countMeUp/results/start/${start}/end/${end}"))
    }

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
