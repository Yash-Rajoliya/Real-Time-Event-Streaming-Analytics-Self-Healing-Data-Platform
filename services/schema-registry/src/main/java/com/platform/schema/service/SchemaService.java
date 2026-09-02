// services/schema-registry/src/main/java/com/platform/schema/service/SchemaService.java
package com.platform.schema.service;

import com.platform.schema.model.Schema;
import com.platform.schema.repository.SchemaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.SchemaValidationException;
import org.apache.avro.SchemaValidatorBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchemaService {

    private final SchemaRepository schemaRepository;

    @Transactional
    public Schema registerSchema(String subject, String schemaDefinitionJson) {
        // Parse schema to validate syntax
        org.apache.avro.Schema parsedSchema = parseAvroSchema(schemaDefinitionJson);

        if (!isCompatible(subject, schemaDefinitionJson)) {
            throw new IllegalArgumentException("Schema is incompatible with the existing schema version for subject: " + subject);
        }

        Integer latestVersion = schemaRepository.findMaxVersionBySubject(subject).orElse(0);
        int nextVersion = latestVersion + 1;

        Schema schemaEntity = Schema.builder()
                .subject(subject)
                .version(nextVersion)
                .schemaDefinition(parsedSchema.toString())
                .compatibilityMode("BACKWARD")
                .build();

        log.info("Registering new schema for subject [{}] version [{}]", subject, nextVersion);
        return schemaRepository.save(schemaEntity);
    }

    @Transactional(readOnly = true)
    public Schema getLatestSchema(String subject) {
        return schemaRepository.findFirstBySubjectOrderByVersionDesc(subject)
                .orElseThrow(() -> new IllegalArgumentException("No schemas found for subject: " + subject));
    }

    @Transactional(readOnly = true)
    public Schema getSchemaBySubjectAndVersion(String subject, Integer version) {
        return schemaRepository.findBySubjectAndVersion(subject, version)
                .orElseThrow(() -> new IllegalArgumentException("Schema not found for subject: " + subject + " version: " + version));
    }

    @Transactional(readOnly = true)
    public List<Integer> getVersionsForSubject(String subject) {
        return schemaRepository.findAllVersionsBySubject(subject);
    }

    public boolean isCompatible(String subject, String newSchemaJson) {
        return schemaRepository.findFirstBySubjectOrderByVersionDesc(subject)
                .map(latest -> {
                    try {
                        org.apache.avro.Schema newSchema = parseAvroSchema(newSchemaJson);
                        org.apache.avro.Schema existingSchema = parseAvroSchema(latest.getSchemaDefinition());

                        new SchemaValidatorBuilder()
                                .canReadStrategy()
                                .validateLatest()
                                .validate(newSchema, Collections.singleton(existingSchema));
                        return true;
                    } catch (SchemaValidationException ex) {
                        log.warn("Schema compatibility validation failed for subject [{}]: {}", subject, ex.getMessage());
                        return false;
                    }
                })
                .orElse(true); // If subject doesn't exist, it is compatible by default
    }

    private org.apache.avro.Schema parseAvroSchema(String json) {
        try {
            return new org.apache.avro.Schema.Parser().parse(json);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Avro schema format: " + e.getMessage(), e);
        }
    }
}