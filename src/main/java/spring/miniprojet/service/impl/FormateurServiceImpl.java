package spring.miniprojet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.miniprojet.entity.Formateur;
import spring.miniprojet.repository.FormateurRepository;
import spring.miniprojet.service.FormateurService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FormateurServiceImpl implements FormateurService {

    private final FormateurRepository formateurRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Formateur> findAll() {
        return formateurRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Formateur> findById(Long id) {
        return formateurRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Formateur> findByEmail(String email) {
        return formateurRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Formateur> findByUserId(Long userId) {
        return formateurRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Formateur> findBySpecialite(String specialite) {
        return formateurRepository.findBySpecialiteContainingIgnoreCase(specialite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Formateur> search(String keyword) {
        return formateurRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public Formateur save(Formateur formateur) {
        return formateurRepository.save(formateur);
    }

    @Override
    public Formateur update(Long id, Formateur formateur) {
        return formateurRepository.findById(id)
                .map(existing -> {
                    existing.setNom(formateur.getNom());
                    existing.setPrenom(formateur.getPrenom());
                    existing.setEmail(formateur.getEmail());
                    existing.setSpecialite(formateur.getSpecialite());
                    return formateurRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Formateur non trouvé avec l'id: " + id));
    }

    @Override
    public void delete(Long id) {
        formateurRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return formateurRepository.existsByEmail(email);
    }
}
