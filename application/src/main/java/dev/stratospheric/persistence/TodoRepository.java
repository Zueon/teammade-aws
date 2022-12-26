package dev.stratospheric.persistence;

import dev.stratospheric.entity.Project;
import dev.stratospheric.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByProject(Project project);

}
