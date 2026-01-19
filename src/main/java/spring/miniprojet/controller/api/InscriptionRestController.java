package spring.miniprojet.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.miniprojet.entity.Inscription;
import spring.miniprojet.service.InscriptionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/inscriptions")
@RequiredArgsConstructor
public class InscriptionRestController {

    private final InscriptionService inscriptionService;

    @GetMapping
    public ResponseEntity<List<Inscription>> getAll() {
        return ResponseEntity.ok(inscriptionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inscription> getById(@PathVariable Long id) {
        return inscriptionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<Inscription>> getByEtudiant(@PathVariable Long etudiantId) {
        return ResponseEntity.ok(inscriptionService.findByEtudiantId(etudiantId));
    }

    @GetMapping("/cours/{coursId}")
    public ResponseEntity<List<Inscription>> getByCours(@PathVariable Long coursId) {
        return ResponseEntity.ok(inscriptionService.findByCoursId(coursId));
    }

    @PostMapping
    public ResponseEntity<?> inscrire(@RequestBody Map<String, Long> request) {
        try {
            Long etudiantId = request.get("etudiantId");
            Long coursId = request.get("coursId");
            Inscription inscription = inscriptionService.inscrire(etudiantId, coursId);
            return ResponseEntity.status(HttpStatus.CREATED).body(inscription);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/confirmer")
    public ResponseEntity<Inscription> confirmer(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(inscriptionService.confirmer(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/annuler")
    public ResponseEntity<Inscription> annuler(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(inscriptionService.annuler(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkInscription(@RequestParam Long etudiantId, @RequestParam Long coursId) {
        return ResponseEntity.ok(inscriptionService.isEtudiantInscrit(etudiantId, coursId));
    }
}
