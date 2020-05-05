package com.example.totolist

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("tasks")
class TaskController(private val taskRepository: TaskRepository){
    @GetMapping("")
    fun index(model: Model): String {
        val tasks = taskRepository.findAll()
        model.addAttribute("tasks", tasks)
        return "tasks/index"
    }

    @GetMapping("new")
    fun new(@ModelAttribute("form") form: TaskCreateForm): String {
        return "tasks/new"
    }

    @PostMapping("")
    fun create(@Validated form: TaskCreateForm,
               bindingResult: BindingResult, model: Model): String {
        if(bindingResult.hasErrors()) {
            model.addAttribute("form", form)
            //エラーも渡す必要アリ
            return "tasks/new"
        }

        val content = requireNotNull(form.content)
        taskRepository.create(content)
        return "redirect:/tasks"
    }

    @GetMapping("{id}/edit")
    fun edit(@PathVariable("id") id: Long,
             @ModelAttribute("form") form: TaskUpdateForm): String {
        val task = taskRepository.findById(id) ?: throw NotFoundException()
        form.id = id
        form.content = task.content
        form.done = task.done
        return "tasks/edit"
    }

    @PatchMapping("{id}")
    fun update(@PathVariable("id") id: Long,
               @Validated form: TaskUpdateForm,
               bindingResult: BindingResult,
               model: Model): String {
        if(bindingResult.hasErrors()){
            model.addAttribute("form", form)
            return "tasks/edit"
        }

        val task = taskRepository.findById(id) ?: throw NotFoundException()
        val newTask = task.copy(content = requireNotNull(form.content),
                done = form.done)
        taskRepository.update(newTask)
        return "redirect:/tasks"

    }
}