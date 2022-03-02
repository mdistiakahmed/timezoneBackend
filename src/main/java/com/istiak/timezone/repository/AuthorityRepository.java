package com.istiak.timezone.repository;

import com.istiak.timezone.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import javax.persistence.QueryHint;
import java.util.Set;

public interface AuthorityRepository  extends JpaRepository<Authority, String> {

    @Query(value = "SELECT DISTINCT tua.authority_name FROM t_user_authority tua WHERE tua.user_id=:userId", nativeQuery = true)
    @QueryHints(@QueryHint(name="org.hibernate.comment", value="Authority.getAuthoritiesForUser"))
    public Set<String> getAuthoritiesForUser(@Param("userId") long userId);
}
