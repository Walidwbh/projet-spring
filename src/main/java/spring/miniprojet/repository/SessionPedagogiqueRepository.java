package spring.miniprojet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.miniprojet.entity.SessionPedagogique;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionPedagogiqueRepository extends JpaRepository<SessionPedagogique, Long> {

    Optional<SessionPedagogique> findByNom(String nom);

    List<SessionPedagogique> findByType(SessionPedagogique.TypeSession type);

    Optional<SessionPedagogique> findByActiveTrue();

    List<SessionPedagogique> findByActiveFalse();

    boolean existsByNom(String nom);
}
