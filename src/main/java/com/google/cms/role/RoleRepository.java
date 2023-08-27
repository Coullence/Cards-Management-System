package com.google.cms.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    @Query(value = "SELECT * FROM roles WHERE deleted_flag = :deletedFlag", nativeQuery = true)
    List<Role> findByDeletedFlag(Character deletedFlag);
}
