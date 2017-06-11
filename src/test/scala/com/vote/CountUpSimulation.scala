package com.vote

import java.util.UUID

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class CountUpSimulation extends Simulation {

  val feeder = new Feeder[String] {
    override def hasNext = true

    override def next: Map[String, String] = {
      Map("voter" -> UUID.randomUUID.toString())
    }
  }

  val candidateFeeder = {
    Array(
      Map("candidate" -> s"A"),
      Map("candidate" -> s"B"),
      Map("candidate" -> s"C"),
      Map("candidate" -> s"D")
    )
  }.circular

  val baseUrl = "http://localhost:8080"

  val httpConf = http // 4
    .baseURL(baseUrl) // 5
    .acceptHeader(
      "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // 6
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader(
      "Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("BasicSimulation")
    .feed(feeder)
    .feed(candidateFeeder)
    .repeat(1) {
      exec(
        http("request_1")
          .post("/countMeUp/candidate/${candidate}/voter/${voter}"))
    }

  setUp(
    scn.inject(atOnceUsers(100), constantUsersPerSec(20) during (15 seconds))
  ).protocols(httpConf)

}
