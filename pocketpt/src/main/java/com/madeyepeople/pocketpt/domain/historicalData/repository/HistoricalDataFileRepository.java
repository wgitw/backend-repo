package com.madeyepeople.pocketpt.domain.historicalData.repository;

import com.madeyepeople.pocketpt.domain.historicalData.constant.Scope;
import com.madeyepeople.pocketpt.domain.historicalData.entity.HistoricalData;
import com.madeyepeople.pocketpt.domain.historicalData.entity.HistoricalDataFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoricalDataFileRepository extends JpaRepository<HistoricalDataFile, Long> {
    List<HistoricalDataFile> findByHistoricalDataAndIsDeletedFalse(HistoricalData historicalData);
    Optional<HistoricalDataFile> findByHistoricalDataAndHistoricalDataFileIdAndIsDeletedFalse(HistoricalData historicalData, Long historicalDataFileId);

    List<HistoricalDataFile> findByHistoricalDataAndScopeAndIsDeletedFalse(HistoricalData historicalData, Scope scope);
}
