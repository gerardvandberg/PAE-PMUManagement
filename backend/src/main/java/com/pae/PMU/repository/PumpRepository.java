package com.pae.PMU.repository;

import com.pae.PMU.entity.PumpEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;

@Repository
public interface PumpRepository extends JpaRepository<PumpEntity, String> {

}