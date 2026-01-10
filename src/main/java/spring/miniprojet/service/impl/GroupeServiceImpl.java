package spring.miniprojet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.miniprojet.entity.Groupe;
import spring.miniprojet.entity.SessionPedagogique;
import spring.miniprojet.entity.Specialite;
import spring.miniprojet.repository.GroupeRepository;
import spring.miniprojet.repository.SessionPedagogiqueRepository;
import spring.miniprojet.repository.SpecialiteRepository;
import spring.miniprojet.service.GroupeService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupeServiceImpl implements GroupeService {

    private final GroupeRepository groupeRepository;
    private final SpecialiteRepository specialiteRepository;
    private final SessionPedagogiqueRepository sessionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Groupe> findAll() {
        return groupeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Groupe> findById(Long id) {
        return groupeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Groupe> findByNom(String nom) {
        return groupeRepository.findByNom(nom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Groupe> findBySpecialiteId(Long specialiteId) {
        Specialite specialite = specialiteRepository.findById(specialiteId)
                .orElseThrow(() -> new RuntimeException("Spécialité non trouvée"));
        return groupeRepository.findBySpecialite(specialite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Groupe> findBySessionId(Long sessionId) {
        SessionPedagogique session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        return groupeRepository.findBySession(session);
    }

    @Override
    public Groupe save(Groupe groupe) {
        return groupeRepository.save(groupe);
    }

    @Override
    public Groupe update(Long id, Groupe groupe) {
        return groupeRepository.findById(id)
                .map(existing -> {
                    existing.setNom(groupe.getNom());
                    existing.setNiveau(groupe.getNiveau());
                    existing.setSpecialite(groupe.getSpecialite());
                    existing.setSession(groupe.getSession());
                    return groupeRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé avec l'id: " + id));
    }

    @Override
    public void delete(Long id) {
        groupeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNom(String nom) {
        return groupeRepository.existsByNom(nom);
    }
}
