package spring.miniprojet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.miniprojet.entity.Specialite;

import java.util.Optional;

@Repository
public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {

    Optional<Specialite> findByNom(String nom);

    boolean existsByNom(String nom);
}
