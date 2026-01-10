package spring.miniprojet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.miniprojet.entity.Groupe;
import spring.miniprojet.entity.SessionPedagogique;
import spring.miniprojet.entity.Specialite;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Long> {

    Optional<Groupe> findByNom(String nom);

    List<Groupe> findBySpecialite(Specialite specialite);

    List<Groupe> findBySession(SessionPedagogique session);

    List<Groupe> findByNiveau(String niveau);

    boolean existsByNom(String nom);
}
