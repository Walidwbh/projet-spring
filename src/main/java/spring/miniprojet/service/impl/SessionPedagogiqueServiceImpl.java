package spring.miniprojet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.miniprojet.entity.SessionPedagogique;
import spring.miniprojet.repository.SessionPedagogiqueRepository;
import spring.miniprojet.service.SessionPedagogiqueService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionPedagogiqueServiceImpl implements SessionPedagogiqueService {

    private final SessionPedagogiqueRepository sessionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SessionPedagogique> findAll() {
        return sessionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SessionPedagogique> findById(Long id) {
        return sessionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SessionPedagogique> findByNom(String nom) {
        return sessionRepository.findByNom(nom);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SessionPedagogique> findActive() {
        return sessionRepository.findByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionPedagogique> findByType(SessionPedagogique.TypeSession type) {
        return sessionRepository.findByType(type);
    }

    @Override
    public SessionPedagogique save(SessionPedagogique session) {
        return sessionRepository.save(session);
    }

    @Override
    public SessionPedagogique update(Long id, SessionPedagogique session) {
        return sessionRepository.findById(id)
                .map(existing -> {
                    existing.setNom(session.getNom());
                    existing.setType(session.getType());
                    existing.setDateDebut(session.getDateDebut());
                    existing.setDateFin(session.getDateFin());
                    return sessionRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Session non trouvée avec l'id: " + id));
    }

    @Override
    public void delete(Long id) {
        sessionRepository.deleteById(id);
    }

    @Override
    public void setActive(Long sessionId) {
        // Désactiver toutes les sessions
        sessionRepository.findAll().forEach(s -> {
            s.setActive(false);
            sessionRepository.save(s);
        });

        // Activer la session sélectionnée
        sessionRepository.findById(sessionId)
                .ifPresent(session -> {
                    session.setActive(true);
                    sessionRepository.save(session);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNom(String nom) {
        return sessionRepository.existsByNom(nom);
    }
}
