package dev.stratospheric.persistence;


import dev.stratospheric.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByEmail(String email);
    Member findByEmailAndPassword(String email, String password);
    Optional<Member> findByEmail(String email);
}
