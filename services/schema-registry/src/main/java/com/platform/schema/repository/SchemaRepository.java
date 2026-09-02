// services/schema-registry/src/main/java/com/platform/schema/repository/SchemaRepository.java
package com.platform.schema.repository;

import com.platform.schema.model.Schema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchemaRepository extends JpaRepository<Schema, String> {

    Optional<Schema> findFirstBySubjectOrderByVersionDesc(String subject);

    Optional<Schema> findBySubjectAndVersion(String subject, Integer version);

    @Query("SELECT MAX(s.version) FROM Schema s WHERE s.subject = :subject")
    Optional<Integer> findMaxVersionBySubject(@Param("subject") String subject);

    @Query("SELECT s.version FROM Schema s WHERE s.subject = :subject ORDER BY s.version ASC")
    List<Integer> findAllVersionsBySubject(@Param("subject") String subject);
}