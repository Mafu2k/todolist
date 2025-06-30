package org.example.todolist;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // strona glowna przekierowuje na logowanie
    @GetMapping("/")
    public String stronaGlowna() {
        return "redirect:/login";
    }

    // formularz rejestracji
    @GetMapping("/register")
    public String formularzRejestracji(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "register";
    }

    // obsluga rejestracji
    @PostMapping("/register")
    public String zarejestrujUzytkownika(@ModelAttribute("form") @Valid RegisterForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        
        // sprawdzam czy hasla sa identyczne
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("error", "Hasla nie sa identyczne");
            return "register";
        }
        
        // sprawdzam czy uzytkownik juz istnieje
        if (userService.userExists(form.getUsername())) {
            model.addAttribute("error", "Uzytkownik o tej nazwie juz istnieje");
            return "register";
        }
        
        // rejestruje nowego uzytkownika
        userService.register(form.getUsername(), form.getPassword());
        model.addAttribute("success", "Konto zostalo utworzone! Mozesz sie teraz zalogowac.");
        return "redirect:/login?registered=true";
    }

    // strona logowania
    @GetMapping("/login")
    public String stronaLogowania(@RequestParam(value = "error", required = false) String error,
                                 @RequestParam(value = "logout", required = false) String logout,
                                 @RequestParam(value = "registered", required = false) String registered,
                                 Model model) {
        if (error != null) {
            model.addAttribute("error", "Bledne dane logowania");
        }
        if (logout != null) {
            model.addAttribute("logout", "Zostales wylogowany");
        }
        if (registered != null) {
            model.addAttribute("success", "Konto zostalo utworzone! Mozesz sie teraz zalogowac.");
        }
        return "login";
    }

    // klasa pomocnicza do formularza rejestracji
    public static class RegisterForm {
        @NotBlank(message = "Nazwa uzytkownika jest wymagana")
        @Size(min = 3, max = 50, message = "Nazwa uzytkownika musi miec od 3 do 50 znakow")
        private String username;
        
        @NotBlank(message = "Haslo jest wymagane")
        @Size(min = 6, message = "Haslo musi miec co najmniej 6 znakow")
        private String password;
        
        @NotBlank(message = "Potwierdzenie hasla jest wymagane")
        private String confirmPassword;

        // gettery i settery
        public String getUsername() { 
            return username; 
        }
        public void setUsername(String username) { 
            this.username = username; 
        }
        public String getPassword() { 
            return password; 
        }
        public void setPassword(String password) { 
            this.password = password; 
        }
        public String getConfirmPassword() { 
            return confirmPassword; 
        }
        public void setConfirmPassword(String confirmPassword) { 
            this.confirmPassword = confirmPassword; 
        }
    }
}
