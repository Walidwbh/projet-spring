package spring.miniprojet.service;

import spring.miniprojet.entity.Specialite;

import java.util.List;
import java.util.Optional;

public interface SpecialiteService {

    List<Specialite> findAll();

    Optional<Specialite> findById(Long id);

    Optional<Specialite> findByNom(String nom);

    Specialite save(Specialite specialite);

    Specialite update(Long id, Specialite specialite);

    void delete(Long id);

    boolean existsByNom(String nom);
}
