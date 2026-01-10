package spring.miniprojet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.miniprojet.entity.Formateur;
import spring.miniprojet.service.FormateurService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/formateurs")
@RequiredArgsConstructor
public class FormateurController {

    private final FormateurService formateurService;

    @GetMapping
    public String list(Model model, @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("formateurs", formateurService.search(search));
        } else {
            model.addAttribute("formateurs", formateurService.findAll());
        }
        model.addAttribute("search", search);
        return "admin/formateurs/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("formateur", new Formateur());
        return "admin/formateurs/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Formateur formateur, BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/formateurs/form";
        }

        if (formateur.getId() == null) {
            formateurService.save(formateur);
            redirectAttributes.addFlashAttribute("success", "Formateur créé avec succès");
        } else {
            formateurService.update(formateur.getId(), formateur);
            redirectAttributes.addFlashAttribute("success", "Formateur modifié avec succès");
        }
        return "redirect:/admin/formateurs";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Formateur formateur = formateurService.findById(id)
                .orElseThrow(() -> new RuntimeException("Formateur non trouvé"));
        model.addAttribute("formateur", formateur);
        return "admin/formateurs/form";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        Formateur formateur = formateurService.findById(id)
                .orElseThrow(() -> new RuntimeException("Formateur non trouvé"));
        model.addAttribute("formateur", formateur);
        return "admin/formateurs/view";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        formateurService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Formateur supprimé avec succès");
        return "redirect:/admin/formateurs";
    }
}
