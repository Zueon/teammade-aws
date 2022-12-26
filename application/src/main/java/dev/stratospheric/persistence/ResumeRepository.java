package dev.stratospheric.persistence;


import dev.stratospheric.entity.Member;
import dev.stratospheric.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, String> {
    Resume findByCreator(Member creator);
}
