package spring.miniprojet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.miniprojet.entity.Etudiant;
import spring.miniprojet.entity.Groupe;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    Optional<Etudiant> findByMatricule(String matricule);

    Optional<Etudiant> findByEmail(String email);

    List<Etudiant> findByGroupe(Groupe groupe);

    List<Etudiant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

    @Query("SELECT e FROM Etudiant e WHERE e.user.id = :userId")
    Optional<Etudiant> findByUserId(@Param("userId") Long userId);

    @Query("SELECT e FROM Etudiant e JOIN e.inscriptions i WHERE i.cours.id = :coursId")
    List<Etudiant> findByCoursId(@Param("coursId") Long coursId);

    boolean existsByMatricule(String matricule);

    boolean existsByEmail(String email);
}
