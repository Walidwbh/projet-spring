package spring.miniprojet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import spring.miniprojet.entity.User;
import spring.miniprojet.service.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final EtudiantService etudiantService;
    private final FormateurService formateurService;
    private final CoursService coursService;
    private final UserService userService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElse(null);

        model.addAttribute("username", username);

        if (user != null) {
            switch (user.getRole()) {
                case ADMIN:
                    model.addAttribute("totalEtudiants", etudiantService.findAll().size());
                    model.addAttribute("totalFormateurs", formateurService.findAll().size());
                    model.addAttribute("totalCours", coursService.findAll().size());
                    return "admin/dashboard";
                case FORMATEUR:
                    var formateur = formateurService.findByUserId(user.getId()).orElse(null);
                    if (formateur != null) {
                        model.addAttribute("formateur", formateur);
                        model.addAttribute("mesCours", coursService.findByFormateurId(formateur.getId()));
                    }
                    return "formateur/dashboard";
                case ETUDIANT:
                    var etudiant = etudiantService.findByUserId(user.getId()).orElse(null);
                    if (etudiant != null) {
                        model.addAttribute("etudiant", etudiant);
                        model.addAttribute("mesCours", coursService.findByEtudiantId(etudiant.getId()));
                        model.addAttribute("moyenne", etudiantService.calculateMoyenneGenerale(etudiant.getId()));
                    }
                    return "etudiant/dashboard";
            }
        }

        return "redirect:/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
