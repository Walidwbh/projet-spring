package spring.miniprojet.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.miniprojet.entity.Cours;
import spring.miniprojet.service.CoursService;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cours")
@RequiredArgsConstructor
public class CoursRestController {

    private final CoursService coursService;

    @GetMapping
    public ResponseEntity<List<Cours>> getAll() {
        return ResponseEntity.ok(coursService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cours> getById(@PathVariable Long id) {
        return coursService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Cours> getByCode(@PathVariable String code) {
        return coursService.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Cours>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(coursService.search(keyword));
    }

    @GetMapping("/formateur/{formateurId}")
    public ResponseEntity<List<Cours>> getByFormateur(@PathVariable Long formateurId) {
        return ResponseEntity.ok(coursService.findByFormateurId(formateurId));
    }

    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<Cours>> getByEtudiant(@PathVariable Long etudiantId) {
        return ResponseEntity.ok(coursService.findByEtudiantId(etudiantId));
    }

    @PostMapping
    public ResponseEntity<Cours> create(@Valid @RequestBody Cours cours) {
        return ResponseEntity.status(HttpStatus.CREATED).body(coursService.save(cours));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cours> update(@PathVariable Long id, @Valid @RequestBody Cours cours) {
        try {
            return ResponseEntity.ok(coursService.update(id, cours));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coursService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{coursId}/formateur/{formateurId}")
    public ResponseEntity<Void> assignerFormateur(@PathVariable Long coursId, @PathVariable Long formateurId) {
        coursService.assignerFormateur(coursId, formateurId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{coursId}/groupe/{groupeId}")
    public ResponseEntity<Void> assignerGroupe(@PathVariable Long coursId, @PathVariable Long groupeId) {
        coursService.assignerGroupe(coursId, groupeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{coursId}/groupe/{groupeId}")
    public ResponseEntity<Void> retirerGroupe(@PathVariable Long coursId, @PathVariable Long groupeId) {
        coursService.retirerGroupe(coursId, groupeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable Long id) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("nombreInscrits", coursService.countEtudiantsInscrits(id));
        stats.put("tauxReussite", coursService.getTauxReussite(id));
        return ResponseEntity.ok(stats);
    }
}
