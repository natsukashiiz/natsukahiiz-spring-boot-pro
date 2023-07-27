package com.natsukashiiz.iiserverapi.repository;

import com.natsukashiiz.iiserverapi.entity.SignHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SignHistoryRepository extends JpaRepository<SignHistory, Long> {
    List<SignHistory> findByUid(Long uid, Pageable pageable);

    Long countByUid(Long uid);
}
