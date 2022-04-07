package com.security.pki.repository;

import com.security.pki.model.SubjectData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectDataRepository extends JpaRepository<SubjectData, Integer> {
}
