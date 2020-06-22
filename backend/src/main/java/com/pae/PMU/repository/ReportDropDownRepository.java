package com.pae.PMU.repository;


import com.pae.PMU.entity.ReportDropDownEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDropDownRepository extends JpaRepository<ReportDropDownEntity, String> {

}