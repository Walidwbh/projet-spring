package spring.miniprojet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.miniprojet.entity.Specialite;
import spring.miniprojet.repository.SpecialiteRepository;
import spring.miniprojet.service.SpecialiteService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SpecialiteServiceImpl implements SpecialiteService {

    private final SpecialiteRepository specialiteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Specialite> findAll() {
        return specialiteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Specialite> findById(Long id) {
        return specialiteRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Specialite> findByNom(String nom) {
        return specialiteRepository.findByNom(nom);
    }

    @Override
    public Specialite save(Specialite specialite) {
        return specialiteRepository.save(specialite);
    }

    @Override
    public Specialite update(Long id, Specialite specialite) {
        return specialiteRepository.findById(id)
                .map(existing -> {
                    existing.setNom(specialite.getNom());
                    existing.setDescription(specialite.getDescription());
                    return specialiteRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Spécialité non trouvée avec l'id: " + id));
    }

    @Override
    public void delete(Long id) {
        specialiteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNom(String nom) {
        return specialiteRepository.existsByNom(nom);
    }
}
