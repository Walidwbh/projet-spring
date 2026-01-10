
package spring.miniprojet.service;
public interface EmailService {

    void sendInscriptionConfirmation(String to, String etudiantNom, String coursNom);

    void sendNouvelleNote(String to, String etudiantNom, String coursNom, Double note);

    void notifyFormateurInscription(String to, String formateurNom, String etudiantNom, String coursNom,
            boolean inscription);

    void sendPasswordReset(String to, String username, String resetLink);

    void sendWelcomeEmail(String to, String nom, String username);
}
