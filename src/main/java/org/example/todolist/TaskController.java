package org.example.todolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;

    // wyswietlanie listy zadan
    @GetMapping
    public String showTasks(@AuthenticationPrincipal UserDetails principal, 
                           @RequestParam(required = false) String search,
                           Model model) {
        
        User user = userRepository.findByUsername(principal.getUsername()).get();
        
        List<Task> tasks;
        if (search != null && !search.isEmpty()) {
            tasks = taskRepository.findByUserAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(user, search);
        } else {
            tasks = taskRepository.findByUserOrderByCreatedAtDesc(user);
        }
    
        model.addAttribute("tasks", tasks);
        model.addAttribute("searchTerm", search);
        
        // dodaje statystyki do modelu
        this.dodajStatystyki(model, user);
        
        return "tasks/list";
    }

    // metoda pomocnicza do statystyk
    private void dodajStatystyki(Model model, User user) {
        model.addAttribute("totalTasks", taskRepository.countByUser(user));
        model.addAttribute("completedTasks", taskRepository.countCompletedByUser(user));
        model.addAttribute("pendingTasks", taskRepository.countPendingByUser(user));
        model.addAttribute("overdueTasks", taskRepository.countOverdueByUser(user, LocalDate.now()));
    }

    // formularz nowego zadania
    @GetMapping("/new")
    public String noweZadanie(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/form";
    }

    // dodawanie nowego zadania
    @PostMapping
    public String dodajZadanie(@ModelAttribute Task task,
                             @AuthenticationPrincipal UserDetails principal,
                             RedirectAttributes redirectAttributes) {
        
        User user = userRepository.findByUsername(principal.getUsername()).get();
        task.setUser(user);
        
        taskRepository.save(task);
        redirectAttributes.addFlashAttribute("successMessage", "Zadanie zostalo dodane!");
        return "redirect:/tasks";
    }

    // formularz edycji zadania
    @GetMapping("/{id}/edit")
    public String edytujZadanie(@PathVariable Long id, Model model, 
                               @AuthenticationPrincipal UserDetails principal, 
                               RedirectAttributes redirectAttributes) {
        
        User user = userRepository.findByUsername(principal.getUsername()).get();
        Task task = taskRepository.findById(id).orElse(null);
        
        if (task == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Zadanie nie zostalo znalezione");
            return "redirect:/tasks";
        }
        
        if (!task.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Brak uprawnien do edycji tego zadania");
            return "redirect:/tasks";
        }
        
        model.addAttribute("task", task);
        return "tasks/form";
    }

    // aktualizacja zadania
    @PostMapping("/{id}")
    public String aktualizujZadanie(@PathVariable Long id, @ModelAttribute Task task,
                                   @AuthenticationPrincipal UserDetails principal,
                                   RedirectAttributes redirectAttributes) {
        
        User user = userRepository.findByUsername(principal.getUsername()).get();
        Task existingTask = taskRepository.findById(id).orElse(null);
        
        if (existingTask == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Zadanie nie zostalo znalezione");
            return "redirect:/tasks";
        }
        
        if (!existingTask.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Brak uprawnien do edycji tego zadania");
            return "redirect:/tasks";
        }
        
        // aktualizuje dane zadania
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setDone(task.isDone());
        
        taskRepository.save(existingTask);
        redirectAttributes.addFlashAttribute("successMessage", "Zadanie zostalo zaktualizowane!");
        return "redirect:/tasks";
    }

    // usuwanie zadania
    @PostMapping("/{id}/delete")
    public String usunZadanie(@PathVariable Long id, 
                             @AuthenticationPrincipal UserDetails principal,
                             RedirectAttributes redirectAttributes) {
        
        User user = userRepository.findByUsername(principal.getUsername()).get();
        Task task = taskRepository.findById(id).orElse(null);
        
        if (task == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Zadanie nie zostalo znalezione");
            return "redirect:/tasks";
        }
        
        if (!task.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Brak uprawnien do usuniecia tego zadania");
            return "redirect:/tasks";
        }
        
        taskRepository.delete(task);
        redirectAttributes.addFlashAttribute("successMessage", "Zadanie zostalo usuniete!");
        return "redirect:/tasks";
    }

    // zmiana statusu zadania
    @PostMapping("/{id}/toggle")
    public String zmienStatus(@PathVariable Long id, 
                             @AuthenticationPrincipal UserDetails principal,
                             RedirectAttributes redirectAttributes) {
        
        User user = userRepository.findByUsername(principal.getUsername()).get();
        Task task = taskRepository.findById(id).orElse(null);
        
        if (task == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Zadanie nie zostalo znalezione");
            return "redirect:/tasks";
        }
        
        if (!task.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Brak uprawnien do zmiany statusu tego zadania");
            return "redirect:/tasks";
        }
        
        task.setDone(!task.isDone());
        taskRepository.save(task);
        
        String message = task.isDone() ? "Zadanie zostalo oznaczone jako ukonczone!" : "Zadanie zostalo oznaczone jako nieukonczone!";
        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/tasks";
    }

}
