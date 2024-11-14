package com.wekids.backend.design.repository;

import com.wekids.backend.card.domain.Card;
import com.wekids.backend.design.domain.Design;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignRepository extends JpaRepository<Design, Long> {
    Design findByCard(Card card);
}
