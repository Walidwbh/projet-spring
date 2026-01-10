package spring.miniprojet.service;

import spring.miniprojet.entity.Groupe;

import java.util.List;
import java.util.Optional;

public interface GroupeService {

    List<Groupe> findAll();

    Optional<Groupe> findById(Long id);

    Optional<Groupe> findByNom(String nom);

    List<Groupe> findBySpecialiteId(Long specialiteId);

    List<Groupe> findBySessionId(Long sessionId);

    Groupe save(Groupe groupe);

    Groupe update(Long id, Groupe groupe);

    void delete(Long id);

    boolean existsByNom(String nom);
}
