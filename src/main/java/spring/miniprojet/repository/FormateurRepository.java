package spring.miniprojet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.miniprojet.entity.Formateur;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormateurRepository extends JpaRepository<Formateur, Long> {

    Optional<Formateur> findByEmail(String email);

    List<Formateur> findBySpecialiteContainingIgnoreCase(String specialite);

    List<Formateur> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

    @Query("SELECT f FROM Formateur f WHERE f.user.id = :userId")
    Optional<Formateur> findByUserId(@Param("userId") Long userId);

    boolean existsByEmail(String email);
}
