package spring.miniprojet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.miniprojet.entity.Cours;
import spring.miniprojet.entity.Etudiant;
import spring.miniprojet.entity.Note;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByEtudiant(Etudiant etudiant);

    List<Note> findByCours(Cours cours);

    Optional<Note> findByEtudiantAndCours(Etudiant etudiant, Cours cours);

    @Query("SELECT AVG(n.valeur) FROM Note n WHERE n.etudiant.id = :etudiantId")
    Double calculateMoyenneByEtudiantId(@Param("etudiantId") Long etudiantId);

    @Query("SELECT AVG(n.valeur) FROM Note n WHERE n.cours.id = :coursId")
    Double calculateMoyenneByCoursId(@Param("coursId") Long coursId);

    @Query("SELECT COUNT(n) FROM Note n WHERE n.cours.id = :coursId AND n.valeur >= :seuilReussite")
    Long countReussiteByCoursId(@Param("coursId") Long coursId, @Param("seuilReussite") Double seuilReussite);

    @Query("SELECT COUNT(n) FROM Note n WHERE n.cours.id = :coursId")
    Long countNotesParCours(@Param("coursId") Long coursId);

    @Query("SELECT n FROM Note n WHERE n.etudiant.id = :etudiantId ORDER BY n.dateSaisie DESC")
    List<Note> findByEtudiantIdOrderByDateDesc(@Param("etudiantId") Long etudiantId);

    boolean existsByEtudiantAndCours(Etudiant etudiant, Cours cours);
}
