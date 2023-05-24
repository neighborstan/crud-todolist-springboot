package com.neighborstan.todo.controller;

import com.neighborstan.todo.domain.Status;
import com.neighborstan.todo.domain.Task;
import com.neighborstan.todo.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @ModelAttribute
    public void supplyModel(Model model,
                            @ModelAttribute("task") Task task,
                            @RequestParam("page") Optional<Integer> page,
                            @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Task> tasks = service.showTasks(PageRequest.of(currentPage - 1, pageSize, Sort.by("id").descending()));
        model.addAttribute("tasks", tasks);

        int totalPages = tasks.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @PostMapping
    public String saveTask(@Valid @ModelAttribute("task") Task task,
                           BindingResult bindingResult,
                           @RequestParam("id") Optional<Integer> id,
                           @RequestParam("description") String description,
                           @RequestParam("status") String status) {

        if (bindingResult.hasErrors()) {
            return "index";
        }

        if (id.isPresent()) {
            task = service.findTaskById(id.get()).orElseThrow();
            task.setId(id.get());
            task.setDescription(description);
            task.setStatus(Status.valueOf(status));
        } else {
            task = Task.builder()
                    .description(description)
                    .status(Status.valueOf(status))
                    .build();
        }
        service.saveTask(task);

        return "redirect:/";
    }

    @GetMapping("/{id}/delete")
    public String deleteTask(@PathVariable Integer id) {
        service.deleteTask(id);

        return "redirect:/";
    }
}
