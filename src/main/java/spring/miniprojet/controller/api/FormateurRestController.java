package spring.miniprojet.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.miniprojet.entity.Formateur;
import spring.miniprojet.service.FormateurService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/formateurs")
@RequiredArgsConstructor
public class FormateurRestController {

    private final FormateurService formateurService;

    @GetMapping
    public ResponseEntity<List<Formateur>> getAll() {
        return ResponseEntity.ok(formateurService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Formateur> getById(@PathVariable Long id) {
        return formateurService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Formateur>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(formateurService.search(keyword));
    }

    @GetMapping("/specialite/{specialite}")
    public ResponseEntity<List<Formateur>> getBySpecialite(@PathVariable String specialite) {
        return ResponseEntity.ok(formateurService.findBySpecialite(specialite));
    }

    @PostMapping
    public ResponseEntity<Formateur> create(@Valid @RequestBody Formateur formateur) {
        return ResponseEntity.status(HttpStatus.CREATED).body(formateurService.save(formateur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formateur> update(@PathVariable Long id, @Valid @RequestBody Formateur formateur) {
        try {
            return ResponseEntity.ok(formateurService.update(id, formateur));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        formateurService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
