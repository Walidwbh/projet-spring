package spring.miniprojet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.miniprojet.entity.Cours;
import spring.miniprojet.entity.Etudiant;
import spring.miniprojet.entity.Note;
import spring.miniprojet.repository.CoursRepository;
import spring.miniprojet.repository.EtudiantRepository;
import spring.miniprojet.repository.NoteRepository;
import spring.miniprojet.service.EmailService;
import spring.miniprojet.service.NoteService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final EtudiantRepository etudiantRepository;
    private final CoursRepository coursRepository;
    private final EmailService emailService;

    @Override
    @Transactional(readOnly = true)
    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> findByEtudiantId(Long etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        return noteRepository.findByEtudiant(etudiant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> findByCoursId(Long coursId) {
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        return noteRepository.findByCours(cours);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Note> findByEtudiantAndCours(Long etudiantId, Long coursId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        return noteRepository.findByEtudiantAndCours(etudiant, cours);
    }

    @Override
    public Note save(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public Note attribuerNote(Long etudiantId, Long coursId, Double valeur, String commentaire) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));

        // Vérifier si une note existe déjà
        Optional<Note> existingNote = noteRepository.findByEtudiantAndCours(etudiant, cours);
        if (existingNote.isPresent()) {
            throw new RuntimeException("Une note existe déjà pour cet étudiant dans ce cours");
        }

        Note note = Note.builder()
                .etudiant(etudiant)
                .cours(cours)
                .valeur(valeur)
                .commentaire(commentaire)
                .dateSaisie(LocalDate.now())
                .build();

        Note saved = noteRepository.save(note);

        // Notifier l'étudiant
        if (etudiant.getEmail() != null) {
            emailService.sendNouvelleNote(etudiant.getEmail(), etudiant.getNomComplet(), cours.getTitre(), valeur);
        }

        return saved;
    }

    @Override
    public Note update(Long id, Double valeur, String commentaire) {
        return noteRepository.findById(id)
                .map(note -> {
                    note.setValeur(valeur);
                    note.setCommentaire(commentaire);
                    note.setDateSaisie(LocalDate.now());
                    return noteRepository.save(note);
                })
                .orElseThrow(() -> new RuntimeException("Note non trouvée"));
    }

    @Override
    public void delete(Long id) {
        noteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateMoyenneEtudiant(Long etudiantId) {
        return noteRepository.calculateMoyenneByEtudiantId(etudiantId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateMoyenneCours(Long coursId) {
        return noteRepository.calculateMoyenneByCoursId(coursId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTauxReussite(Long coursId, Double seuilReussite) {
        Long totalNotes = noteRepository.countNotesParCours(coursId);
        if (totalNotes == 0)
            return 0.0;
        Long reussites = noteRepository.countReussiteByCoursId(coursId, seuilReussite);
        return (reussites.doubleValue() / totalNotes.doubleValue()) * 100;
    }
}
