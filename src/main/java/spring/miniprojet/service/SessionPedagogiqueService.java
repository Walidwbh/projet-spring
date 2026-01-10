package spring.miniprojet.service;

import spring.miniprojet.entity.SessionPedagogique;

import java.util.List;
import java.util.Optional;

public interface SessionPedagogiqueService {

    List<SessionPedagogique> findAll();

    Optional<SessionPedagogique> findById(Long id);

    Optional<SessionPedagogique> findByNom(String nom);

    Optional<SessionPedagogique> findActive();

    List<SessionPedagogique> findByType(SessionPedagogique.TypeSession type);

    SessionPedagogique save(SessionPedagogique session);

    SessionPedagogique update(Long id, SessionPedagogique session);

    void delete(Long id);

    void setActive(Long sessionId);

    boolean existsByNom(String nom);
}
