package spring.miniprojet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.miniprojet.entity.Cours;
import spring.miniprojet.entity.Formateur;
import spring.miniprojet.entity.SessionPedagogique;
import spring.miniprojet.entity.Specialite;
import spring.miniprojet.service.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/cours")
@RequiredArgsConstructor
public class CoursController {

    private final CoursService coursService;
    private final FormateurService formateurService;
    private final SpecialiteService specialiteService;
    private final SessionPedagogiqueService sessionService;
    private final GroupeService groupeService;

    @GetMapping
    public String list(Model model, @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("coursList", coursService.search(search));
        } else {
            model.addAttribute("coursList", coursService.findAll());
        }
        model.addAttribute("search", search);
        return "admin/cours/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("cours", new Cours());
        model.addAttribute("formateurs", formateurService.findAll());
        model.addAttribute("specialites", specialiteService.findAll());
        model.addAttribute("sessions", sessionService.findAll());
        return "admin/cours/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Cours cours, BindingResult result,
            @RequestParam(required = false) Long formateurId,
            @RequestParam(required = false) Long specialiteId,
            @RequestParam(required = false) Long sessionId,
            Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("formateurs", formateurService.findAll());
            model.addAttribute("specialites", specialiteService.findAll());
            model.addAttribute("sessions", sessionService.findAll());
            return "admin/cours/form";
        }

        if (formateurId != null) {
            Formateur formateur = formateurService.findById(formateurId).orElse(null);
            cours.setFormateur(formateur);
        }
        if (specialiteId != null) {
            Specialite specialite = specialiteService.findById(specialiteId).orElse(null);
            cours.setSpecialite(specialite);
        }
        if (sessionId != null) {
            SessionPedagogique session = sessionService.findById(sessionId).orElse(null);
            cours.setSession(session);
        }

        if (cours.getId() == null) {
            coursService.save(cours);
            redirectAttributes.addFlashAttribute("success", "Cours créé avec succès");
        } else {
            coursService.update(cours.getId(), cours);
            redirectAttributes.addFlashAttribute("success", "Cours modifié avec succès");
        }
        return "redirect:/admin/cours";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Cours cours = coursService.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        model.addAttribute("cours", cours);
        model.addAttribute("formateurs", formateurService.findAll());
        model.addAttribute("specialites", specialiteService.findAll());
        model.addAttribute("sessions", sessionService.findAll());
        return "admin/cours/form";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        Cours cours = coursService.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        model.addAttribute("cours", cours);
        model.addAttribute("nombreInscrits", coursService.countEtudiantsInscrits(id));
        model.addAttribute("tauxReussite", coursService.getTauxReussite(id));
        model.addAttribute("groupes", groupeService.findAll());
        return "admin/cours/view";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        coursService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Cours supprimé avec succès");
        return "redirect:/admin/cours";
    }

    @PostMapping("/{coursId}/groupes/add")
    public String addGroupe(@PathVariable Long coursId, @RequestParam Long groupeId,
            RedirectAttributes redirectAttributes) {
        coursService.assignerGroupe(coursId, groupeId);
        redirectAttributes.addFlashAttribute("success", "Groupe ajouté au cours");
        return "redirect:/admin/cours/view/" + coursId;
    }

    @GetMapping("/{coursId}/groupes/remove/{groupeId}")
    public String removeGroupe(@PathVariable Long coursId, @PathVariable Long groupeId,
            RedirectAttributes redirectAttributes) {
        coursService.retirerGroupe(coursId, groupeId);
        redirectAttributes.addFlashAttribute("success", "Groupe retiré du cours");
        return "redirect:/admin/cours/view/" + coursId;
    }
}
