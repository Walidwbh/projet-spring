# Documentation API REST - Centre de Formation

**Version:** 1.0.0  
**Date:** 10 Janvier 2026  
**Format de réponse:** JSON  
**Base URL:** `http://localhost:9090/api`

---

## 📋 Table des Matières

1. [Vue d'ensemble](#vue-densemble)
2. [Authentification & Sécurité](#authentification--sécurité)
3. [Ressources API](#ressources-api)
    - [Étudiants](#1-étudiants)
    - [Formateurs](#2-formateurs)
    - [Cours](#3-cours)
    - [Inscriptions](#4-inscriptions)
    - [Notes](#5-notes)
    - [Séances](#6-séances)
4. [Codes Statut HTTP](#codes-statut-http)

---

## Vue d'ensemble

Cette API permet la gestion complète du centre de formation, incluant la gestion administrative (CRUD), pédagogique (cours, inscriptions, notes) et la planification (séances). Elle est conçue pour être consommée par des clients frontend (SPA, Mobile) ou des systèmes tiers.

---

## Authentification & Sécurité

L'API est sécurisée. La plupart des endpoints nécessitent une authentification.
Les niveaux d'accès sont définis par les rôles utilisateurs :

| Rôle | Description & Permissions |
|------|---------------------------|
| **ADMIN** | Accès complet à toutes les ressources (lecture/écriture/suppression). |
| **FORMATEUR** | Lecture globale. Modification de ses propres cours, saisie des notes pour ses cours. |
| **ETUDIANT** | Lecture seule de ses propres données (notes, emploi du temps, inscriptions). |

---

## Ressources API

### 1. Étudiants
Gestion des dossiers étudiants.

#### `GET /api/etudiants`
Récupère la liste de tous les étudiants.
- **Réponse 200 OK:**
```json
[
  {
    "id": 1,
    "matricule": "ETD001",
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean.dupont@example.com",
    "dateInscription": "2024-01-15",
    "user": { "id": 1, "username": "etudiant1", "role": "ETUDIANT" }
  }
]
```

#### `GET /api/etudiants/{id}`
Récupère un étudiant par son ID.

#### `GET /api/etudiants/matricule/{matricule}`
Récupère un étudiant par son matricule.

#### `GET /api/etudiants/search?keyword={mot_cle}`
Recherche des étudiants par nom, prénom ou email.

#### `POST /api/etudiants`
Crée un nouvel étudiant.
- **Body:**
```json
{
  "matricule": "ETD002",
  "nom": "Martin",
  "prenom": "Marie",
  "email": "marie@example.com",
  "groupeId": 1,
  "userId": 2
}
```
- **Réponse 201 Created**

#### `PUT /api/etudiants/{id}`
Met à jour les informations d'un étudiant.

#### `DELETE /api/etudiants/{id}`
Supprime un étudiant.

#### `GET /api/etudiants/{id}/moyenne`
Calcule la moyenne générale d'un étudiant.
- **Réponse 200 OK:** `15.5`

---

### 2. Formateurs
Gestion du corps enseignant.

#### `GET /api/formateurs`
Liste tous les formateurs.

#### `GET /api/formateurs/{id}`
Détails d'un formateur.

#### `GET /api/formateurs/search?keyword={mot_cle}`
Recherche textuelle de formateurs.

#### `GET /api/formateurs/specialite/{specialite}`
Filtre les formateurs par spécialité (ex: "Informatique", "Mathématiques").

#### `POST /api/formateurs`
Enregistre un nouveau formateur.
- **Body:**
```json
{
  "nom": "Lefebvre",
  "prenom": "Sophie",
  "email": "sophie@example.com",
  "specialite": "Mathématiques",
  "userId": 3
}
```

#### `PUT /api/formateurs/{id}`
Modifie un formateur existant.

#### `DELETE /api/formateurs/{id}`
Supprime un formateur.

---

### 3. Cours
Gestion du catalogue de cours et des affectations.

#### `GET /api/cours`
Liste complète des cours.

#### `GET /api/cours/{id}`
Détails d'un cours.

#### `GET /api/cours/code/{code}`
Recherche un cours par son code (ex: "INF101").

#### `GET /api/cours/formateur/{formateurId}`
Liste les cours dispensés par un formateur spécifique.

#### `POST /api/cours`
Crée un nouveau cours.
- **Body:**
```json
{
  "code": "INF102",
  "titre": "Programmation Java",
  "description": "Niveau avancé",
  "credits": 4,
  "formateurId": 1,
  "specialiteId": 1,
  "sessionId": 1
}
```

#### `POST /api/cours/{coursId}/formateur/{formateurId}`
Assigne un formateur à un cours.

#### `POST /api/cours/{coursId}/groupe/{groupeId}`
Associe un groupe d'étudiants à un cours.

#### `GET /api/cours/{id}/stats`
Obtient les statistiques du cours (nombre d'inscrits, taux de réussite).
- **Réponse 200 OK:**
```json
{
  "nombreInscrits": 25,
  "tauxReussite": 84.5
}
```

---

### 4. Inscriptions
Gestion des inscriptions aux cours.

#### `POST /api/inscriptions`
Inscrit un étudiant à un cours.
- **Body:**
```json
{ "etudiantId": 1, "coursId": 1 }
```
- **Logique:** Crée une inscription avec statut "EN_ATTENTE".

#### `PUT /api/inscriptions/{id}/confirmer`
Valide une inscription (passage au statut "CONFIRMEE").

#### `PUT /api/inscriptions/{id}/annuler`
Annule une inscription.

#### `GET /api/inscriptions/etudiant/{etudiantId}`
Liste les inscriptions d'un étudiant.

#### `GET /api/inscriptions/check?etudiantId=1&coursId=1`
Vérifie si un étudiant est déjà inscrit à un cours spécifique (retourne booléen).

---

### 5. Notes
Gestion des évaluations.

#### `POST /api/notes`
Attribue une note à un étudiant.
- **Body:**
```json
{
  "etudiantId": 1,
  "coursId": 1,
  "valeur": 17.5,
  "commentaire": "Excellent examen final"
}
```
- **Contraintes:** La note doit être entre 0 et 20.

#### `GET /api/notes/etudiant/{etudiantId}`
Relevé de notes complet d'un étudiant.

#### `GET /api/notes/cours/{coursId}`
Liste de toutes les notes attribuées pour un cours.

#### `GET /api/notes/etudiant/{etudiantId}/moyenne`
- **Réponse:** `{"moyenne": 15.75}`

#### `GET /api/notes/cours/{coursId}/stats`
Statistiques de notes pour un cours (moyenne de la classe, taux de réussite).

---

### 6. Séances (Emploi du temps)
Planification des cours.

#### `POST /api/seances`
Planifie une nouvelle séance.
- **Body:**
```json
{
  "coursId": 1,
  "date": "2024-02-20",
  "heureDebut": "08:00:00",
  "heureFin": "09:30:00",
  "salle": "Salle A101",
  "type": "COURS"
}
```
- **Types:** COURS, TD, TP, EXAMEN.

#### `GET /api/seances/etudiant/{etudiantId}/emploi-du-temps`
Récupère l'emploi du temps d'un étudiant pour une période donnée.
- **Params:** `dateDebut`, `dateFin` (YYYY-MM-DD)

#### `GET /api/seances/formateur/{formateurId}/emploi-du-temps`
Récupère l'agenda d'un formateur pour une date spécifique.

#### `GET /api/seances/date/{date}`
Liste toutes les séances prévues à une date donnée (Vue globale planning).

---

## Codes Statut HTTP

L'API utilise les codes HTTP standard pour indiquer le succès ou l'échec d'une requête.

| Code | Signification | Description |
|------|---------------|-------------|
| **200** | OK | La requête a réussi. |
| **201** | Created | La ressource a été créée avec succès. |
| **204** | No Content | La suppression a réussi ou l'action n'a pas de retour. |
| **400** | Bad Request | Données invalides (ex: note > 20, ou conflit d'horaire). |
| **401** | Unauthorized | Authentification requise ou invalide. |
| **403** | Forbidden | Droits insuffisants pour effectuer l'action. |
| **404** | Not Found | Ressource introuvable (ID inexistant). |
| **500** | Server Error | Erreur interne du serveur. |
