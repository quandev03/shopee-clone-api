package com.example.banhangapi.api.repository;

import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.api.globalEnum.ROLES;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);
    Boolean deleteByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByPhoneNumber(String phoneNumber);

    @Query("""
    SELECT COUNT(*) FROM User u WHERE u.roles = 1
    """)
    Integer totalUser();

    @Query(value = """
    SELECT u FROM User u 
    WHERE u.admin = false
        AND (:active IS NULL OR u.active = :active)
        AND (:username IS NULL OR u.username = :username)
        AND (:fullname IS NULL OR u.fullName = :fullname)
        AND (:phone IS NULL OR u.phoneNumber = :phone)
    """,
            countQuery = """
        SELECT COUNT(u) FROM User u 
        WHERE u.admin = false
            AND (:active IS NULL OR u.active = :active)
            AND (:username IS NULL OR u.username = :username)
            AND (:fullname IS NULL OR u.fullName = :fullname)
            AND (:phone IS NULL OR u.phoneNumber = :phone)
    """)
    Page<User> findAllForAdmin(Pageable pageable,
                               @Param("active") Boolean active,
                               @Param("username") String username,
                               @Param("fullname") String fullname,
                               @Param("phone") String phone);

    @Transactional
    @Modifying
    @Query("""
        UPDATE User u set u.admin = true, u.roles = :roles
            WHERE u.id = :id AND u.active = true
    """)
    void decentralizationAdmin(@Param("id") String id, @Param("roles") ROLES roles);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u set u.roles = :roles, u.admin = false
            WHERE u.id = :id AND u.active = true
    """)
    void decentralizedCensorship(@Param("id") String id, @Param("roles") ROLES roles);

    @Transactional
    @Modifying
    @Query("""
        UPDATE User u set u.roles = :roles, u.admin = false
            WHERE u.id = :id AND u.active = true
    """)
    void decentralizedUser(@Param("id") String id, @Param("roles") ROLES roles);

    @Transactional
    @Modifying
    @Query("""
        UPDATE User u SET u.active = CASE WHEN u.active = true THEN false ELSE true END
            WHERE u.id = :id AND u.admin = false
    """)
    void toggleAccountActive(@Param("id") String id);
}
