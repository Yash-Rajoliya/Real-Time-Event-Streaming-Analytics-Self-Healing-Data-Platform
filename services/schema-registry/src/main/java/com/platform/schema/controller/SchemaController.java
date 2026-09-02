// services/schema-registry/src/main/java/com/platform/schema/controller/SchemaController.java
package com.platform.schema.controller;

import com.platform.schema.model.Schema;
import com.platform.schema.service.SchemaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schemas")
@RequiredArgsConstructor
public class SchemaController {

    private final SchemaService schemaService;

    @PostMapping("/register")
    public ResponseEntity<SchemaResponse> registerSchema(@Valid @RequestBody RegisterSchemaRequest request) {
        Schema schema = schemaService.registerSchema(request.getSubject(), request.getSchemaDefinition());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(schema));
    }

    @GetMapping("/subjects/{subject}/versions/latest")
    public ResponseEntity<SchemaResponse> getLatestSchema(@PathVariable String subject) {
        Schema schema = schemaService.getLatestSchema(subject);
        return ResponseEntity.ok(mapToResponse(schema));
    }

    @GetMapping("/subjects/{subject}/versions/{version}")
    public ResponseEntity<SchemaResponse> getSchemaByVersion(
            @PathVariable String subject, 
            @PathVariable Integer version) {
        Schema schema = schemaService.getSchemaBySubjectAndVersion(subject, version);
        return ResponseEntity.ok(mapToResponse(schema));
    }

    @GetMapping("/subjects/{subject}/versions")
    public ResponseEntity<List<Integer>> listSubjectVersions(@PathVariable String subject) {
        return ResponseEntity.ok(schemaService.getVersionsForSubject(subject));
    }

    @PostMapping("/subjects/{subject}/compatibility")
    public ResponseEntity<CompatibilityResponse> checkCompatibility(
            @PathVariable String subject, 
            @Valid @RequestBody RegisterSchemaRequest request) {
        boolean compatible = schemaService.isCompatible(subject, request.getSchemaDefinition());
        return ResponseEntity.ok(new CompatibilityResponse(compatible));
    }

    private SchemaResponse mapToResponse(Schema schema) {
        return SchemaResponse.builder()
                .id(schema.getId())
                .subject(schema.getSubject())
                .version(schema.getVersion())
                .schemaDefinition(schema.getSchemaDefinition())
                .compatibilityMode(schema.getCompatibilityMode())
                .createdAt(schema.getCreatedAt().toString())
                .build();
    }

    // --- DTO Models ---

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterSchemaRequest {
        @NotBlank(message = "Subject is required")
        private String subject;

        @NotBlank(message = "Schema definition JSON string is required")
        private String schemaDefinition;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SchemaResponse {
        private String id;
        private String subject;
        private Integer version;
        private String schemaDefinition;
        private String compatibilityMode;
        private String createdAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompatibilityResponse {
        private boolean isCompatible;
    }
}