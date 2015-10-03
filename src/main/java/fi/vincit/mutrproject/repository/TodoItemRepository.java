package fi.vincit.mutrproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fi.vincit.mutrproject.service.todo.model.TodoItem;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
}
