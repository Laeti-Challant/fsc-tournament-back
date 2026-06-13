# fsc-tournament-back

API REST de gestion de tournoi Blood Bowl, développée en stage pour l'association Fil Sanguinaire.

---

## Contexte

L'association Fil Sanguinaire organise des tournois de Blood Bowl (jeu de plateau). L'objectif du projet est de remplacer la gestion manuelle des tournois par une application web dédiée, permettant d'automatiser le suivi des coachs, des rounds, des matchs et des résultats.

---

## Fonctionnalités

- Authentification sécurisée avec JWT (inscription, connexion, gestion des rôles)
- Gestion des coachs inscrits au tournoi
- Création et suivi des rounds
- Saisie et consultation des résultats de matchs
- Algorithme de pairing automatique des équipes entre chaque round
- Gestion des règles du tournoi

---

## Stack technique

- **Java 21**
- **Spring Boot**
- **Spring Security** (authentification JWT)
- **PostgreSQL**
- **JPA / Hibernate**

---

## Architecture

L'application suit une architecture en couches :

- **Controller** : exposition des endpoints REST
- **BLL (Business Logic Layer)** : logique métier et algorithme de pairing
- **DAL (Data Access Layer)** : accès aux données via JPA
- **BO (Business Objects)** : entités métier

---

## État du projet

Projet en cours de développement, réalisé en stage. Les fonctionnalités principales sont implémentées ; des évolutions sont prévues pour finaliser certains modules.
