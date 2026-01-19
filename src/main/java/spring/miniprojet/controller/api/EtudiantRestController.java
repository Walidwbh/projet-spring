package spring.miniprojet.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.miniprojet.entity.Etudiant;
import spring.miniprojet.service.EtudiantService;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Admin REST API for Etudiant Management
 * Only admins can access these endpoints via JWT Bearer token
 */
@RestController
@RequestMapping("/api/admin/etudiants")
@RequiredArgsConstructor
public class EtudiantRestController {

    private final EtudiantService etudiantService;

    @GetMapping
    public ResponseEntity<List<Etudiant>> getAll() {
        return ResponseEntity.ok(etudiantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Etudiant> getById(@PathVariable Long id) {
        return etudiantService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<Etudiant> getByMatricule(@PathVariable String matricule) {
        return etudiantService.findByMatricule(matricule)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Etudiant>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(etudiantService.search(keyword));
    }

    @PostMapping
    public ResponseEntity<Etudiant> create(@Valid @RequestBody Etudiant etudiant) {
        return ResponseEntity.status(HttpStatus.CREATED).body(etudiantService.save(etudiant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Etudiant> update(@PathVariable Long id, @Valid @RequestBody Etudiant etudiant) {
        try {
            return ResponseEntity.ok(etudiantService.update(id, etudiant));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        etudiantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/moyenne")
    public ResponseEntity<Double> getMoyenne(@PathVariable Long id) {
        Double moyenne = etudiantService.calculateMoyenneGenerale(id);
        return ResponseEntity.ok(moyenne != null ? moyenne : 0.0);
    }
}
