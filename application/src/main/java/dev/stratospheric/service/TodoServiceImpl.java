package dev.stratospheric.service;

import dev.stratospheric.entity.Project;
import dev.stratospheric.entity.Todo;
import dev.stratospheric.persistence.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    private void validate(final Todo entity) {
        if (entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if (entity.getProject() == null) {
            log.warn("Unknown Project.");
            throw new RuntimeException("Unknown Project.");
        }
    }

    @Override
    public List<Todo> create(Todo todo) {
        validate(todo);
        Todo saved = todoRepository.save(todo);
        log.info("Entity ID : {} is saved", saved.getTid());


        return todoRepository.findByProject(saved.getProject());
    }

    @Override
    public List<Todo> retrieve(Project project) {
        return todoRepository.findByProject(project);
    }

    @Override
    public List<Todo> update(Todo todo) {
        validate(todo);

        final Optional<Todo> original = todoRepository.findById(todo.getTid());

        original.ifPresent(prev -> {
            prev.setTitle(todo.getTitle());
            prev.setIsDone(todo.getIsDone());
            prev.setUpdatedAt(todo.getCreatedAt());

            todoRepository.save(prev);
        });
        return retrieve(todo.getProject());
    }

    @Override
    public List<Todo> delete(Todo todo) {
        validate(todo);

        try {
            todoRepository.delete(todo);
        } catch (Exception e){
            log.error("Todo Entity 삭제에 문제가 발생하였습니다. " + todo.getTid(), e);
            throw new RuntimeException("Error deleting entity " + todo.getTid());
        }
        return retrieve(todo.getProject());
    }
}
