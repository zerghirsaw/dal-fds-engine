package com.digdaya.dal.repository;

import com.digdaya.dal.entity.DalHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DalHistoryRepository extends JpaRepository<DalHistoryEntity, Long> {
    
    // Sihir Spring: Hanya dengan menulis nama fungsi ini, 
    // Spring otomatis membuatkan query "SELECT * FROM ... ORDER BY analysis_date DESC"
    List<DalHistoryEntity> findAllByOrderByAnalysisDateDesc();
    
}
