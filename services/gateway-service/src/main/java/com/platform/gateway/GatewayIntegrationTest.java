// services/gateway-service/src/test/java/com/platform/gateway/GatewayIntegrationTest.java
package com.platform.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GatewayIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void healthCheckEndpoint_ShouldReturnUp() {
        webTestClient.get()
                .uri("/actuator/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("UP");
    }

    @Test
    void protectedRoute_WithoutAuthHeader_ShouldReturnUnauthorized() {
        webTestClient.get()
                .uri("/api/v1/anomalies/rules")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void publicIngestRoute_ShouldAttachCorrelationIdInResponse() {
        webTestClient.post()
                .uri("/api/v1/events/ingest")
                .exchange()
                .expectHeader().exists("X-Correlation-ID");
    }
}