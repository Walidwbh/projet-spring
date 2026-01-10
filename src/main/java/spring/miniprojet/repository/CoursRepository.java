package spring.miniprojet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.miniprojet.entity.Cours;
import spring.miniprojet.entity.Formateur;
import spring.miniprojet.entity.SessionPedagogique;
import spring.miniprojet.entity.Specialite;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {

    Optional<Cours> findByCode(String code);

    List<Cours> findByFormateur(Formateur formateur);

    List<Cours> findBySpecialite(Specialite specialite);

    List<Cours> findBySession(SessionPedagogique session);

    List<Cours> findByTitreContainingIgnoreCase(String titre);

    @Query("SELECT c FROM Cours c JOIN c.groupes g WHERE g.id = :groupeId")
    List<Cours> findByGroupeId(@Param("groupeId") Long groupeId);

    @Query("SELECT c FROM Cours c JOIN c.inscriptions i WHERE i.etudiant.id = :etudiantId")
    List<Cours> findByEtudiantId(@Param("etudiantId") Long etudiantId);

    @Query("SELECT c FROM Cours c LEFT JOIN FETCH c.inscriptions WHERE c.id = :id")
    Optional<Cours> findByIdWithInscriptions(@Param("id") Long id);

    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.cours.id = :coursId AND i.statut = 'CONFIRMEE'")
    Long countEtudiantsInscrits(@Param("coursId") Long coursId);

    boolean existsByCode(String code);
}
