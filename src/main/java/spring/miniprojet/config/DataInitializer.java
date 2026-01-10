package spring.miniprojet.config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import spring.miniprojet.entity.*;
import spring.miniprojet.repository.*;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SpecialiteRepository specialiteRepository;
    private final SessionPedagogiqueRepository sessionRepository;
    private final GroupeRepository groupeRepository;
    private final EtudiantRepository etudiantRepository;
    private final FormateurRepository formateurRepository;
    private final CoursRepository coursRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Créer un admin par défaut si aucun utilisateur n'existe
        if (userRepository.count() == 0) {
            log.info("Initialisation des données de démonstration...");

            // Créer l'utilisateur admin
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@formation.com")
                    .role(User.Role.ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            log.info("Utilisateur admin créé (username: admin, password: admin123)");

            // Créer quelques spécialités
            Specialite informatique = specialiteRepository.save(
                    Specialite.builder().nom("Informatique").description("Développement logiciel et systèmes").build());
            Specialite reseaux = specialiteRepository.save(
                    Specialite.builder().nom("Réseaux").description("Administration réseau et sécurité").build());
            Specialite ia = specialiteRepository.save(
                    Specialite.builder().nom("Intelligence Artificielle")
                            .description("Machine Learning et Deep Learning").build());

            // Créer une session pédagogique
            SessionPedagogique session = sessionRepository.save(
                    SessionPedagogique.builder()
                            .nom("Année Scolaire 2025-2026")
                            .type(SessionPedagogique.TypeSession.ANNEE_SCOLAIRE)
                            .dateDebut(LocalDate.of(2025, 9, 1))
                            .dateFin(LocalDate.of(2026, 6, 30))
                            .active(true)
                            .build());

            // Créer quelques groupes
            Groupe groupe1 = groupeRepository.save(
                    Groupe.builder().nom("TP1-INFO").niveau("1ère année").specialite(informatique).session(session)
                            .build());
            Groupe groupe2 = groupeRepository.save(
                    Groupe.builder().nom("TP2-INFO").niveau("1ère année").specialite(informatique).session(session)
                            .build());

            // Créer un formateur de démonstration
            User formateurUser = userRepository.save(
                    User.builder()
                            .username("formateur")
                            .password(passwordEncoder.encode("formateur123"))
                            .email("formateur@formation.com")
                            .role(User.Role.FORMATEUR)
                            .enabled(true)
                            .build());

            Formateur formateur = formateurRepository.save(
                    Formateur.builder()
                            .nom("Dupont")
                            .prenom("Jean")
                            .email("formateur@formation.com")
                            .specialite("Informatique")
                            .user(formateurUser)
                            .build());
            log.info("Formateur créé (username: formateur, password: formateur123)");

            // Créer un étudiant de démonstration
            User etudiantUser = userRepository.save(
                    User.builder()
                            .username("etudiant")
                            .password(passwordEncoder.encode("etudiant123"))
                            .email("etudiant@formation.com")
                            .role(User.Role.ETUDIANT)
                            .enabled(true)
                            .build());

            Etudiant etudiant = etudiantRepository.save(
                    Etudiant.builder()
                            .matricule("ETU20250001")
                            .nom("Martin")
                            .prenom("Sophie")
                            .email("etudiant@formation.com")
                            .dateInscription(LocalDate.now())
                            .groupe(groupe1)
                            .user(etudiantUser)
                            .build());
            log.info("Étudiant créé (username: etudiant, password: etudiant123)");

            // Créer quelques cours
            Cours cours1 = coursRepository.save(
                    Cours.builder()
                            .code("JAVA101")
                            .titre("Programmation Java")
                            .description("Introduction à la programmation orientée objet avec Java")
                            .credits(6)
                            .formateur(formateur)
                            .specialite(informatique)
                            .session(session)
                            .build());

            Cours cours2 = coursRepository.save(
                    Cours.builder()
                            .code("SPRING101")
                            .titre("Spring Boot")
                            .description("Développement d'applications web avec Spring Boot")
                            .credits(6)
                            .formateur(formateur)
                            .specialite(informatique)
                            .session(session)
                            .build());

            Cours cours3 = coursRepository.save(
                    Cours.builder()
                            .code("ML101")
                            .titre("Machine Learning")
                            .description("Introduction au Machine Learning")
                            .credits(6)
                            .specialite(ia)
                            .session(session)
                            .build());

            log.info("Données de démonstration initialisées avec succès!");
        }
    }
}
