package spring.miniprojet.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.miniprojet.entity.Note;
import spring.miniprojet.service.NoteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/notes")
@RequiredArgsConstructor
public class NoteRestController {

    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<List<Note>> getAll() {
        return ResponseEntity.ok(noteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getById(@PathVariable Long id) {
        return noteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<Note>> getByEtudiant(@PathVariable Long etudiantId) {
        return ResponseEntity.ok(noteService.findByEtudiantId(etudiantId));
    }

    @GetMapping("/cours/{coursId}")
    public ResponseEntity<List<Note>> getByCours(@PathVariable Long coursId) {
        return ResponseEntity.ok(noteService.findByCoursId(coursId));
    }

    @PostMapping
    public ResponseEntity<?> attribuerNote(@RequestBody Map<String, Object> request) {
        try {
            Long etudiantId = Long.valueOf(request.get("etudiantId").toString());
            Long coursId = Long.valueOf(request.get("coursId").toString());
            Double valeur = Double.valueOf(request.get("valeur").toString());
            String commentaire = (String) request.get("commentaire");

            Note note = noteService.attribuerNote(etudiantId, coursId, valeur, commentaire);
            return ResponseEntity.status(HttpStatus.CREATED).body(note);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            Double valeur = Double.valueOf(request.get("valeur").toString());
            String commentaire = (String) request.get("commentaire");
            return ResponseEntity.ok(noteService.update(id, valeur, commentaire));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/etudiant/{etudiantId}/moyenne")
    public ResponseEntity<Map<String, Object>> getMoyenneEtudiant(@PathVariable Long etudiantId) {
        Map<String, Object> result = new HashMap<>();
        result.put("moyenne", noteService.calculateMoyenneEtudiant(etudiantId));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/cours/{coursId}/stats")
    public ResponseEntity<Map<String, Object>> getStatsCours(@PathVariable Long coursId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("moyenne", noteService.calculateMoyenneCours(coursId));
        stats.put("tauxReussite", noteService.getTauxReussite(coursId, 10.0));
        return ResponseEntity.ok(stats);
    }
}
