package org.kata.simulation;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class PaymentSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:9090")
            .acceptHeader("application/json");

    String createPaymentBody = """
            {
                "paymentType": "CREDIT_CARD",
                "paymentStatus": "NEW",
                "items": [
                    {
                        "name": "Item1",
                        "price": 100.0,
                        "quantity": 1
                    },
                    {
                        "name": "Item2",
                        "price": 50.0,
                        "quantity": 2
                    }
                ]
            }
            """;

    ScenarioBuilder createPaymentScenario = scenario("Create Payment Scenario")
            .exec(http("Create Payment")
                    .post("/payments")
                    .body(StringBody(createPaymentBody)).asJson()
                    .check(status().is(200)))
            .pause(Duration.ofMillis(100));

    ScenarioBuilder getPaymentScenario = scenario("Get Payment Scenario")
            .exec(http("Get Payment")
                    .get("/payments")
                    .check(status().is(200)))
            .pause(Duration.ofMillis(100));

    {
        setUp(
                createPaymentScenario.injectOpen(constantUsersPerSec(10).during(Duration.ofMinutes(1))),
                getPaymentScenario.injectOpen(constantUsersPerSec(10).during(Duration.ofMinutes(1)))
        ).protocols(httpProtocol);
    }
}

