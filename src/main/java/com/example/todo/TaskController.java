package com.example.todo;

import com.example.todo.domain.Task;
import com.example.todo.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Controller
public class TaskController {

    @Autowired
    private TaskRepo taskRepo;


    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Map<String, Object> model
    ) {
        model.put("name", name);
        return "greeting";
    }

    @GetMapping
    public String main(Map<String, Object> model) {
        Iterable<Task> tasks = taskRepo.findAll();

        model.put("tasks", tasks);
        return "main";
    }

    @PostMapping
    public String add(@RequestParam String text,
                      Map<String, Object> model
    ) {
        Task task = new Task(text);

        taskRepo.save(task);

        Iterable<Task> tasks = taskRepo.findAll();

        model.put("tasks", tasks);

        return "main";
    }

    @GetMapping("/filter/{filter}")
    public String filter(@PathVariable String filter,
                         Map<String, Object> model

    ) {
        Iterable<Task> tasks;

        if (filter != null && !filter.isEmpty()) {
            tasks = taskRepo.findTaskByTextContains(filter);
        } else {
            tasks = taskRepo.findAll();
        }

        model.put("tasks", tasks);

        return "main";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        System.out.println("IN DELETE");
        taskRepo.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Map<String, Object> model
    ) {
        Optional<Task> taskOptional = taskRepo.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            model.put("task", task);
        }
        return "taskEdit";
    }

    @GetMapping("/edit/update")
    public String update(
            @PathParam ("id") Long id,
            @PathParam("text") String text,
            Map<String, Object> model
    ) {
        Task task = taskRepo.findById(id).get();
        if (!StringUtils.isEmpty(text) & !task.getText().equals(text)) {
                task.setText(text);

            taskRepo.save(task);

            model.put("task", task);
        }

        return "redirect:/";
        /*
        Не удалось преобразовать значение типа «java.lang.String» в требуемый тип «java.lang.Long»;
        вложенным исключением является java.lang.NumberFormatException: для входной строки: «сохранить»
        */
    }
}