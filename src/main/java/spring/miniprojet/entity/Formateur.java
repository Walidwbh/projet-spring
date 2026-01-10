package spring.miniprojet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "formateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Formateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    private String specialite;

    @Email(message = "Email invalide")
    @Column(unique = true)
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "formateur", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Cours> cours = new HashSet<>();

    public String getNomComplet() {
        return prenom + " " + nom;
    }
}
