package dev.stratospheric.persistence;

import dev.stratospheric.entity.File;
import dev.stratospheric.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, String> {
    List<File> findByProject(Project project);
}
