// services/schema-registry/src/main/java/com/platform/schema/model/Schema.java
package com.platform.schema.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(
        name = "schemas",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"subject", "version"})
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schema {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private Integer version;

    @Column(name = "schema_definition", nullable = false, columnDefinition = "TEXT")
    private String schemaDefinition;

    @Builder.Default
    @Column(name = "compatibility_mode", nullable = false)
    private String compatibilityMode = "BACKWARD";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}