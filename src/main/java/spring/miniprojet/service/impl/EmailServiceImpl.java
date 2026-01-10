package spring.miniprojet.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import spring.miniprojet.service.EmailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void sendInscriptionConfirmation(String to, String etudiantNom, String coursNom) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Confirmation d'inscription - " + coursNom);
            message.setText(String.format(
                    "Bonjour %s,\n\n" +
                            "Votre inscription au cours \"%s\" a été enregistrée avec succès.\n\n" +
                            "Vous recevrez une confirmation définitive prochainement.\n\n" +
                            "Cordialement,\n" +
                            "L'équipe du Centre de Formation",
                    etudiantNom, coursNom));
            mailSender.send(message);
            log.info("Email d'inscription envoyé à {}", to);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email d'inscription à {}: {}", to, e.getMessage());
        }
    }

    @Override
    @Async
    public void sendNouvelleNote(String to, String etudiantNom, String coursNom, Double note) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Nouvelle note - " + coursNom);
            message.setText(String.format(
                    "Bonjour %s,\n\n" +
                            "Une nouvelle note a été attribuée pour le cours \"%s\".\n\n" +
                            "Note: %.2f/20\n\n" +
                            "Consultez votre espace étudiant pour plus de détails.\n\n" +
                            "Cordialement,\n" +
                            "L'équipe du Centre de Formation",
                    etudiantNom, coursNom, note));
            mailSender.send(message);
            log.info("Email de note envoyé à {}", to);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email de note à {}: {}", to, e.getMessage());
        }
    }

    @Override
    @Async
    public void notifyFormateurInscription(String to, String formateurNom, String etudiantNom, String coursNom,
            boolean inscription) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            String action = inscription ? "inscription" : "désinscription";
            message.setSubject("Notification d'" + action + " - " + coursNom);
            message.setText(String.format(
                    "Bonjour %s,\n\n" +
                            "L'étudiant %s s'est %s du cours \"%s\".\n\n" +
                            "Cordialement,\n" +
                            "L'équipe du Centre de Formation",
                    formateurNom, etudiantNom, inscription ? "inscrit à" : "désinscrit de", coursNom));
            mailSender.send(message);
            log.info("Email de notification envoyé au formateur {}", to);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de la notification au formateur {}: {}", to, e.getMessage());
        }
    }

    @Override
    @Async
    public void sendPasswordReset(String to, String username, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Réinitialisation de mot de passe");
            message.setText(String.format(
                    "Bonjour %s,\n\n" +
                            "Une demande de réinitialisation de mot de passe a été effectuée.\n\n" +
                            "Cliquez sur le lien suivant pour réinitialiser votre mot de passe:\n%s\n\n" +
                            "Ce lien expire dans 24 heures.\n\n" +
                            "Si vous n'avez pas demandé cette réinitialisation, ignorez cet email.\n\n" +
                            "Cordialement,\n" +
                            "L'équipe du Centre de Formation",
                    username, resetLink));
            mailSender.send(message);
            log.info("Email de réinitialisation envoyé à {}", to);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email de réinitialisation à {}: {}", to, e.getMessage());
        }
    }

    @Override
    @Async
    public void sendWelcomeEmail(String to, String nom, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Bienvenue au Centre de Formation");
            message.setText(String.format(
                    "Bonjour %s,\n\n" +
                            "Bienvenue au Centre de Formation!\n\n" +
                            "Votre compte a été créé avec succès.\n" +
                            "Nom d'utilisateur: %s\n\n" +
                            "Connectez-vous à votre espace personnel pour accéder à tous les services.\n\n" +
                            "Cordialement,\n" +
                            "L'équipe du Centre de Formation",
                    nom, username));
            mailSender.send(message);
            log.info("Email de bienvenue envoyé à {}", to);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email de bienvenue à {}: {}", to, e.getMessage());
        }
    }
}
