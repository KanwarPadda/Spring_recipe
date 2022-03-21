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
 * Description: <it provides business logic to application;>
 ***********************************************************************************
 */
package com.hebars.nomnom.service;

import com.hebars.nomnom.entity.todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class todoService {

    @Autowired
    private com.hebars.nomnom.repository.todoRepository todoRepository;

    public todo savetodo(todo todo) {
        todoRepository.save(todo);
        return todo;
    }

    public void deletetodo(Long id) {
        todo todoToDelete = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        todoRepository.delete(todoToDelete);
    }

    public todo gettodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
    }

}
