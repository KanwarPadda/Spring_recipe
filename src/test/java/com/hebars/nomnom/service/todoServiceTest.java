/*
 *********************************************************************************

 * Project: < TEAM_HEBARS>
 * Assignment: < assignment 1 >
 * Authors & Student Number: <
    Kanwartejbir Singh Padda(101280244);
    Amandeep Hazra (101286346);
    Sanjana Sharmin (101277205);
 >
 * Date: November 7, 2021,
 * Description: <it test case for todo_service Page;>
 ***********************************************************************************
 */
package com.hebars.nomnom.service;

import com.hebars.nomnom.TestData;
import com.hebars.nomnom.entity.todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class todoServiceTest {

    @Mock
    com.hebars.nomnom.repository.todoRepository todoRepository;

    @InjectMocks
    todoService todoService;

    private todo todo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        todo = TestData.gettodo();
    }

    @Test
    void savetodoTest() {
        when(todoRepository.save(any(todo.class))).thenReturn(todo);
        assertEquals(todo, todoService.savetodo(todo));
    }

    @Test
    void deletetodoTest() {
        when(todoRepository.findById(any(Long.class))).thenReturn(Optional.of(todo));

        todoService.deletetodo(TestData.TEST_ID);
        verify(todoRepository, atLeastOnce()).findById(any(Long.class));
        verify(todoRepository, atLeastOnce()).delete(any(todo.class));
    }

    @Test
    void deletetodoTestFails() {
        assertThrows(EntityNotFoundException.class, () -> {
            todoService.deletetodo(TestData.TEST_ID);
        });
    }

    @Test
    void gettodoByIdTest() {
        when(todoRepository.findById(any(Long.class))).thenReturn(Optional.of(todo));
        assertEquals(todo, todoService.gettodoById(TestData.TEST_ID));
    }

    @Test
    void gettodoByIdTestFails() {
        assertThrows(EntityNotFoundException.class, () -> {
            todoService.gettodoById(TestData.TEST_ID);
        });
    }
}
