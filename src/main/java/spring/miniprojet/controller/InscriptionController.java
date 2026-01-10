package spring.miniprojet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.miniprojet.entity.Inscription;
import spring.miniprojet.service.CoursService;
import spring.miniprojet.service.EtudiantService;
import spring.miniprojet.service.InscriptionService;

@Controller
@RequestMapping("/admin/inscriptions")
@RequiredArgsConstructor
public class InscriptionController {

    private final InscriptionService inscriptionService;
    private final EtudiantService etudiantService;
    private final CoursService coursService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("inscriptions", inscriptionService.findAll());
        return "admin/inscriptions/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("etudiants", etudiantService.findAll());
        model.addAttribute("cours", coursService.findAll());
        return "admin/inscriptions/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam Long etudiantId, @RequestParam Long coursId,
            RedirectAttributes redirectAttributes) {
        try {
            inscriptionService.inscrire(etudiantId, coursId);
            redirectAttributes.addFlashAttribute("success", "Inscription créée avec succès");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/inscriptions";
    }

    @GetMapping("/confirmer/{id}")
    public String confirmer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        inscriptionService.confirmer(id);
        redirectAttributes.addFlashAttribute("success", "Inscription confirmée");
        return "redirect:/admin/inscriptions";
    }

    @GetMapping("/annuler/{id}")
    public String annuler(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        inscriptionService.annuler(id);
        redirectAttributes.addFlashAttribute("success", "Inscription annulée");
        return "redirect:/admin/inscriptions";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        inscriptionService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Inscription supprimée");
        return "redirect:/admin/inscriptions";
    }
}
