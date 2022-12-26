package dev.stratospheric.dto;

import dev.stratospheric.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDto implements Serializable {
    private  LocalDateTime createdAt;
    private  LocalDateTime updatedAt;
    private  Long tid;
    private  String creator;
    private  String title;
    private  Integer isDone;

    public TodoDto(final Todo entity) {
        this.tid = entity.getTid();
        this.title = entity.getTitle();
        this.isDone = entity.getIsDone();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.creator = entity.getCreator();
    }

    public static Todo toEntity(final TodoDto dto) {
        Todo todo =  Todo.builder()
                .tid(dto.getTid())
                .title(dto.getTitle())
                .isDone(dto.getIsDone())
                .creator(dto.getCreator())
                .build();
        todo.setCreatedAt(dto.getCreatedAt());
        todo.setUpdatedAt(dto.getUpdatedAt());

        return todo;
    }
}
