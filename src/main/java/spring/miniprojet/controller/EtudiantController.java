package spring.miniprojet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.miniprojet.entity.Etudiant;
import spring.miniprojet.entity.Groupe;
import spring.miniprojet.service.EtudiantService;
import spring.miniprojet.service.GroupeService;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/etudiants")
@RequiredArgsConstructor
public class EtudiantController {

    private final EtudiantService etudiantService;
    private final GroupeService groupeService;

    @GetMapping
    public String list(Model model, @RequestParam(required = false) String search) {
        List<Etudiant> etudiants;
        if (search != null && !search.isEmpty()) {
            etudiants = etudiantService.search(search);
        } else {
            etudiants = etudiantService.findAll();
        }
        model.addAttribute("etudiants", etudiants);
        model.addAttribute("search", search);
        return "admin/etudiants/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        model.addAttribute("groupes", groupeService.findAll());
        return "admin/etudiants/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Etudiant etudiant, BindingResult result,
            @RequestParam(required = false) Long groupeId,
            Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("groupes", groupeService.findAll());
            return "admin/etudiants/form";
        }

        if (groupeId != null) {
            Groupe groupe = groupeService.findById(groupeId).orElse(null);
            etudiant.setGroupe(groupe);
        }

        if (etudiant.getId() == null) {
            etudiantService.save(etudiant);
            redirectAttributes.addFlashAttribute("success", "Étudiant créé avec succès");
        } else {
            etudiantService.update(etudiant.getId(), etudiant);
            redirectAttributes.addFlashAttribute("success", "Étudiant modifié avec succès");
        }
        return "redirect:/admin/etudiants";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Etudiant etudiant = etudiantService.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        model.addAttribute("etudiant", etudiant);
        model.addAttribute("groupes", groupeService.findAll());
        return "admin/etudiants/form";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        Etudiant etudiant = etudiantService.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        model.addAttribute("etudiant", etudiant);
        model.addAttribute("moyenne", etudiantService.calculateMoyenneGenerale(id));
        return "admin/etudiants/view";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        etudiantService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Étudiant supprimé avec succès");
        return "redirect:/admin/etudiants";
    }
}
