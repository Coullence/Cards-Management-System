package com.google.cms.card_management;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepo extends JpaRepository<Card, Long> {
    List<Card> findByDeletedFlag(Character deletedFlag);
    Page<Card> findByNameAndColorAndStatusOrderByPostedTimeDesc(String name, String color, String status, Pageable pageable);
}
