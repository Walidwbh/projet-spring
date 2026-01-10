package spring.miniprojet.service;

import spring.miniprojet.entity.Note;

import java.util.List;
import java.util.Optional;

public interface NoteService {

    List<Note> findAll();

    Optional<Note> findById(Long id);

    List<Note> findByEtudiantId(Long etudiantId);

    List<Note> findByCoursId(Long coursId);

    Optional<Note> findByEtudiantAndCours(Long etudiantId, Long coursId);

    Note save(Note note);

    Note attribuerNote(Long etudiantId, Long coursId, Double valeur, String commentaire);

    Note update(Long id, Double valeur, String commentaire);

    void delete(Long id);

    Double calculateMoyenneEtudiant(Long etudiantId);

    Double calculateMoyenneCours(Long coursId);

    Double getTauxReussite(Long coursId, Double seuilReussite);
}
