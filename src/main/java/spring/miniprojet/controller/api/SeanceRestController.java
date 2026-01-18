package spring.miniprojet.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.miniprojet.entity.Seance;
import spring.miniprojet.service.SeanceService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/seances")
@RequiredArgsConstructor
public class SeanceRestController {

    private final SeanceService seanceService;

    @GetMapping
    public ResponseEntity<List<Seance>> getAll() {
        return ResponseEntity.ok(seanceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seance> getById(@PathVariable Long id) {
        return seanceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cours/{coursId}")
    public ResponseEntity<List<Seance>> getByCours(@PathVariable Long coursId) {
        return ResponseEntity.ok(seanceService.findByCoursId(coursId));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Seance>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(seanceService.findByDateSeance(date));
    }

    @GetMapping("/range")
    public ResponseEntity<List<Seance>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(seanceService.findByDateRange(dateDebut, dateFin));
    }

    @GetMapping("/etudiant/{etudiantId}/emploi-du-temps")
    public ResponseEntity<List<Seance>> getEmploiDuTempsEtudiant(
            @PathVariable Long etudiantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(seanceService.findEmploiDuTempsEtudiant(etudiantId, dateDebut, dateFin));
    }

    @GetMapping("/formateur/{formateurId}/emploi-du-temps")
    public ResponseEntity<List<Seance>> getEmploiDuTempsFormateur(
            @PathVariable Long formateurId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(seanceService.findEmploiDuTempsFormateur(formateurId, date));
    }

    @PostMapping
    public ResponseEntity<?> planifier(@RequestBody Map<String, Object> request) {
        try {
            Long coursId = Long.valueOf(request.get("coursId").toString());
            LocalDate date = LocalDate.parse(request.get("date").toString());
            LocalTime heureDebut = LocalTime.parse(request.get("heureDebut").toString());
            LocalTime heureFin = LocalTime.parse(request.get("heureFin").toString());
            String salle = (String) request.get("salle");
            Seance.TypeSeance type = Seance.TypeSeance.valueOf(request.get("type").toString());

            Seance seance = seanceService.planifier(coursId, date, heureDebut, heureFin, salle, type);
            return ResponseEntity.status(HttpStatus.CREATED).body(seance);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Seance seance) {
        try {
            return ResponseEntity.ok(seanceService.update(id, seance));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        seanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
