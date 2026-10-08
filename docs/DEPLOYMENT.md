# Documentation de déploiement

Vous trouverez ici les informations concernant le déploiement de l'application fsc-tournament : la vue d'ensemble de l'architecture, puis le déploiement de l'API fsc-tournament-back. Le déploiement de l'interface est documenté dans son propre dépôt.

## 1. Vue d'ensemble

![Architecture de déploiement](diagrams/architecture-deploiement.svg)

Voici le schéma d'organisation de l'application :

- le navigateur charge l'interface Angular (HTML, JS, CSS) depuis Vercel ;
- le navigateur effectue ensuite lui-même les appels vers l'API fsc-tournament-back, hébergée sur Render dans un conteneur Docker ;
- l'API lit et enregistre les données dans une base PostgreSQL hébergée par Supabase, via une connexion chiffrée (SSL/TLS).

| Élément   | URL publique                                 | Dépôt                                                                          |
| --------- | -------------------------------------------- | ------------------------------------------------------------------------------ |
| Interface | https://fsc-tournament-front.vercel.app/     | [fsc-tournament-front](https://github.com/Laeti-Challant/fsc-tournament-front) |
| API       | https://fsc-tournament-back.onrender.com/api | [fsc-tournament-back](https://github.com/Laeti-Challant/fsc-tournament-back)   |

## 2. Prérequis

### Poste de développement

Pour installer l'application en local :

- Git, pour récupérer le dépôt
- JDK 21, pour compiler et exécuter l'API. La version 21 de Java est nécessaire à Gradle, qui sert à construire l'application Java Spring Boot.
- Docker, pour la base de données et lancer les tests d'intégration qui utilisent Testcontainers

Gradle n'est pas à installer : il est téléchargé par `gradlew` avec la version nécessaire.

### Services en ligne

Pour reproduire le déploiement en production :

- GitHub, pour héberger le dépôt et faire fonctionner la CI
- Render, pour héberger l'API en déployant via GitHub
- Supabase, pour héberger la base de données PostgreSQL

## 3. Configuration

### Les profils

- Le fichier `application.properties` est chargé systématiquement par l'application.
- Le fichier `application-local.properties` le surcharge pour lancer l'application en local.
- Le fichier `application-prod.properties` le surcharge pour le lancement en production, avec des variables d'environnement pour préserver les secrets, à renseigner dans Render.

#### Activation des profils

En production, le `Dockerfile` active le bon profil. En local, il faut lancer l'application avec l'argument suivant :
`--args='--spring.profiles.active=local'`

#### Pour lancer l'application en local

1. Copier le fichier `.env.example` en `.env`, puis compléter le nom de la base, l'utilisateur et le mot de passe.

2. Créer le fichier `src/main/resources/application-local.properties` avec le contenu suivant, en reprenant les valeurs du `.env` :

```properties
jwt.secret=clé à générer, voir ci-dessous

spring.datasource.url=jdbc:postgresql://localhost:5433/nom_de_la_base
spring.datasource.username=nom_de_l_utilisateur
spring.datasource.password=mot_de_passe

app.cors.allowed-origins=http://localhost:4200
app.cookie.secure=false
app.cookie.same-site=Lax
```

La clé JWT doit être encodée en Base64 et faire au moins 256 bits. Pour la générer en local :

```bash
openssl rand -base64 32
```

3. Démarrer la base de données :

```bash
docker compose up -d
```

4. Lancer l'application via Gradle :

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

5. L'URL d'appel à l'API est la suivante : `http://localhost:8080/filsanguinairecholetais`

6. Pour arrêter l'application, faire un `Ctrl + C`.

### Les variables d'environnement

| Variable             | Rôle                                              | Exemple                                 | Secret |
| -------------------- | ------------------------------------------------- | --------------------------------------- | ------ |
| POSTGRES_HOST        | hébergeur de la base de données                   | fourni par Supabase                     | Non    |
| POSTGRES_PORT        | port de la base de données                        | 5432                                    | Non    |
| POSTGRES_DB          | nom de la base de données chez l'hébergeur        | postgres                                | Non    |
| POSTGRES_USER        | utilisateur de la base de données                 | -                                       | Oui    |
| POSTGRES_PASSWORD    | mot de passe de la base de données                | -                                       | Oui    |
| JWT_SECRET           | clé secrète pour JWT, en Base64, 256 bits minimum | -                                       | Oui    |
| CORS_ALLOWED_ORIGINS | URL des clients autorisés à appeler l'API         | https://fsc-tournament-front.vercel.app | Non    |
| PORT                 | port d'écoute de l'API                            | 8080 par défaut, fourni par Render      | Non    |

### Les secrets

Ces secrets sont importants car ils préservent l'intégrité de l'application et sa sécurité. Les fichiers `.env` et `application-local.properties` sont systématiquement exclus du dépôt par le `.gitignore`. Pour la mise en production, les secrets sont à enregistrer dans les paramètres du service sur Render, onglet Environment.

## 4. Construction de l'image

Render ne propose pas d'environnement Java natif. L'application est donc livrée sous forme d'image Docker, construite à partir du `Dockerfile` à la racine du dépôt. Ce fichier se décompose en deux étapes.

### Étape 1 : le build

- Elle part d'une image contenant Gradle 8.14 et un JDK 21.
- Elle copie d'abord les fichiers de build (`build.gradle`, `settings.gradle`), puis télécharge les dépendances.
- Elle copie ensuite le code source et construit le `.jar` exécutable avec `gradle bootJar`, sans processus en arrière-plan (`--no-daemon`) et sans lancer les tests (`-x test`).

### Étape 2 : le runtime

Dans une nouvelle image, une distribution Linux Alpine avec un JRE 21, elle copie uniquement le `.jar` construit à l'étape 1, sans le code source ni les outils de build. Le conteneur démarre ensuite l'application avec le profil `prod`.

### Pourquoi ce découpage

- **Taille** : l'image finale ne contient que le JRE et le `.jar`.
- **Sécurité** : ni code source, ni compilateur, ni Gradle en production. Moins d'outils dans l'image, c'est moins de prise pour un attaquant.

### Le cache des couches

Chaque instruction `COPY` ou `RUN` crée une couche, que Docker garde en cache. Au build suivant, une couche inchangée est réutilisée, mais dès qu'une couche change, toutes les suivantes sont reconstruites. Les fichiers de build sont donc copiés avant le code source : une modification du code ne provoque pas un nouveau téléchargement des dépendances.

### Pourquoi les tests ne sont pas lancés

- Le code qui arrive sur `main` a déjà été testé : la branche est protégée, et une pull request ne peut être mergée que si la CI est verte (voir section 7).
- Les tests d'intégration utilisent Testcontainers, qui a besoin de Docker. Docker n'est pas disponible pendant la construction de l'image.

### Construire l'image en local

```bash
docker build -t fsc-tournament-back .
docker images fsc-tournament-back
```

On obtient une image de 131 Mo compressée (ce qui est téléchargé), soit 405 Mo une fois décompressée sur le disque.

## 5. Base de données

La base de production est une base PostgreSQL hébergée par [Supabase](https://supabase.com/), en offre gratuite. Elle doit être créée, avec son schéma, avant le premier déploiement de l'API.

### Création du projet Supabase

Après la création de son compte, cliquer sur **New project**, puis renseigner :

- **GitHub** (optionnel) : non utilisé. Cette intégration gère les migrations depuis un dossier `supabase/` dans le dépôt, alors que le schéma de ce projet est fourni par `init-db/schema.sql`.
- **Project name** : le nom du projet, par exemple `fsc-tournament-db`.
- **Database password** : le mot de passe de la base. Il peut être généré par Supabase. Il faut le copier tout de suite : il ne se réaffiche jamais (il peut seulement être réinitialisé dans **Settings > Database**). Il deviendra la variable `POSTGRES_PASSWORD`.
- **Region** : une région européenne, pour que les données restent hébergées dans l'Union européenne (RGPD). Choisir de préférence une région proche de celle de Render (Frankfurt), pour limiter la latence entre l'API et la base.
- **Security** :
  - décocher **Enable Data API** ;
  - décocher **Automatically expose new tables** ;
  - laisser **Enable automatic RLS** décoché.

L'API Spring Boot accède à la base en JDBC. La Data API de Supabase, qui exposerait les tables en HTTP, est désactivée pour ne pas ouvrir une autre voie d'accès aux données. Le RLS (Row Level Security) protège surtout les accès par cette Data API : il n'apporterait rien ici, l'API se connectant avec le propriétaire des tables.

Cliquer enfin sur **Create new project**. La base démarre en quelques minutes.

### Création du schéma

1. Ouvrir le **SQL Editor**.
2. Coller tout le contenu de `init-db/schema.sql`.
3. Cliquer sur **Run**. Supabase propose alors d'activer le RLS : choisir l'option **Without RLS**, pour la raison expliquée ci-dessus.
4. Le résultat attendu est `Success. No rows returned` : une création de table ne renvoie aucune ligne.
5. Vérifier dans le **Table Editor** que les 11 tables sont créées.

### Paramètres de connexion

Supabase propose plusieurs modes de connexion, regroupés dans l'onglet **Direct** du bouton **Connect**. Il faut choisir **Session pooler** :

| Mode               | Utilisable ici | Raison                                                                                                     |
| ------------------ | -------------- | ---------------------------------------------------------------------------------------------------------- |
| Direct connection  | Non            | uniquement en IPv6, alors que Render ne sort qu'en IPv4 : l'API ne pourrait pas se connecter               |
| Session pooler     | **Oui**        | accessible en IPv4, gratuit, une vraie session par connexion, adaptée au pool de connexions de Spring Boot |
| Transaction pooler | Non            | partage les connexions entre transactions, incompatible avec les requêtes préparées d'Hibernate            |

L'add-on IPv4 de Supabase rendrait la connexion directe utilisable, mais il est payant.

Relever ensuite les valeurs à reporter dans Render (voir section 6) :

| Variable          | Valeur                                                     |
| ----------------- | ---------------------------------------------------------- |
| POSTGRES_HOST     | `aws-0-<région>.pooler.supabase.com`                       |
| POSTGRES_PORT     | `5432`                                                     |
| POSTGRES_DB       | `postgres`                                                 |
| POSTGRES_USER     | `postgres.<identifiant-du-projet>`, et non `postgres` seul |
| POSTGRES_PASSWORD | le mot de passe choisi à la création du projet             |

La connexion est chiffrée : l'URL JDBC de `application-prod.properties` impose `sslmode=require`.

### Validation du schéma au démarrage

En production, `spring.jpa.hibernate.ddl-auto` vaut `validate` : au démarrage, Hibernate compare les tables de la base aux entités Java. Au moindre écart (table, colonne ou type manquant), l'API refuse de démarrer.

Ce réglage est plus sûr que `update`, qui modifierait lui-même le schéma de production sans contrôle ni trace. Avec `validate`, une incohérence est détectée au déploiement, et non par un utilisateur.

### Évolution du schéma

Aujourd'hui, le schéma n'est pas versionné par un outil de migration :

- `init-db/schema.sql` décrit le schéma complet. Il a été réaligné sur les entités le 05/10/2026, après la découverte de 7 écarts, puis vérifié dans un conteneur Docker éphémère ;
- le schéma de production a été modifié à la main dans Supabase par le passé, sans trace dans le dépôt.

Toute évolution d'une entité impose donc de mettre à jour `schema.sql` et d'appliquer la modification à la main dans Supabase avant de déployer, faute de quoi `validate` bloquera le démarrage.

Perspective : adopter **Flyway**, qui applique au démarrage des scripts de migration versionnés et numérotés, et garde dans la base l'historique de ceux déjà joués. Chaque évolution du schéma serait alors tracée dans le dépôt, relue en pull request et appliquée automatiquement.

### Sauvegarde et restauration

Les scripts du dossier `scripts/` concernent **uniquement la base locale** : ils passent par `docker exec` sur le conteneur `fsc_tournament_db`.

```bash
# Sauvegarde, dans ./backups/backup_<date>.sql
./scripts/backup-db.sh

# Restauration d'une sauvegarde
./scripts/restore-db.sh backups/backup_20261007_210000.sql
```

Ils ne sauvegardent pas la base de production. Pour celle-ci, une sauvegarde manuelle reste possible avec `pg_dump` via le Session pooler. Les sauvegardes proposées par Supabase dépendent de l'offre souscrite (voir section 9).

## 6. Déploiement sur Render

La base de données doit exister, avec son schéma, avant le premier déploiement : sinon l'API refuse de démarrer (voir section 5).

### Création du service

Sur [Render](https://render.com/), après la création de son compte (plus rapide avec GitHub), il faut créer un nouveau **Web Service**, autoriser Render à accéder à GitHub, puis sélectionner le dépôt `fsc-tournament-back`.

Sur la page suivante, il faut choisir **Docker** comme **Language** et vérifier que c'est bien la branche `main` qui est sélectionnée dans **Branch**. Le chemin du `Dockerfile` (`./Dockerfile`) et le dossier de construction (`.`, la racine du dépôt) gardent leurs valeurs par défaut. Il faut également sélectionner la région de déploiement **Frankfurt (EU Central)**, pour que les données restent dans l'Union européenne, et enfin l'offre gratuite (**Free**).

### Les variables d'environnement

Sur la même page, une section permet de saisir les variables d'environnement. Ajouter chaque variable du tableau de la section 3 avec le bouton **+ Add Environment Variable**, sauf `PORT`, qui est fourni par Render. Le profil `prod`, qui lit ces variables, est activé par le `Dockerfile` (voir section 3).

### Déclenchement du déploiement

Dans la section **Advanced**, il faut régler **Auto-Deploy** sur **After CI Checks Pass**, puis cliquer sur **Deploy Web Service**. Render construit alors l'image à l'aide du `Dockerfile` et démarre le conteneur, ce qui lance l'API.

Ce premier déploiement part immédiatement. Ensuite, chaque push sur `main`, donc chaque merge de pull request, déclenche un nouveau déploiement, mais Render attend que la CI soit verte sur ce commit avant de le lancer (voir section 7). Ce réglage reste modifiable dans **Settings**, rubrique **Deploy**.

### Vérification après déploiement

L'API de production répond à l'adresse `https://fsc-tournament-back.onrender.com/api`. En production, le chemin de base est `/api` (défini dans `application-prod.properties`), et non `/filsanguinairecholetais` comme en local.

La route des classements est publique en lecture : les requêtes suivantes ne demandent aucune connexion.

| Requête                   | Attendu                                           | Ce que ça prouve                                                                  |
| ------------------------- | ------------------------------------------------- | --------------------------------------------------------------------------------- |
| `GET /api/rankings/1`     | 200 + classement en JSON                          | l'API fonctionne, et la connexion à Supabase aussi : le classement est lu en base |
| `GET /api/rankings/99999` | 404 Not Found                                     | la gestion des erreurs fonctionne                                                 |
| `GET /api/rankings/abc`   | 400 + message `paramètre invalide : tournamentId` | une entrée invalide ne provoque ni erreur 500 ni fuite d'informations techniques  |

Avec `curl`, l'option `-i` affiche le code de retour :

```bash
curl -i https://fsc-tournament-back.onrender.com/api/rankings/abc
```

En offre gratuite, Render met le service en veille après une période d'inactivité : la première requête peut prendre plus de 2 minutes (voir section 9).

## 7. Intégration continue

### Le rôle de la CI

L'intégration continue (CI) consiste à **vérifier automatiquement chaque modification du code** avant qu'elle rejoigne la branche principale. Dans ce projet, elle compile l'application et **lance toute la suite de tests** (unitaires et d'intégration) sur une machine neutre, et non sur le poste de la développeuse.

Elle détecte ainsi les **régressions** : une modification qui casse un comportement déjà testé est repérée avant le merge. Cette protection vaut pour ce que les tests couvrent, soit 27 % du code à ce jour (voir section 9).

Le workflow est décrit dans `.github/workflows/ci.yml` et s'exécute sur **GitHub Actions**.

### Les déclencheurs

Le projet suit le **GitHub Flow** : chaque développement se fait sur une branche dédiée, puis rejoint `main` par une **pull request**.

La CI se déclenche dans deux cas :

| Événement                | Rôle                                                          |
| ------------------------ | ------------------------------------------------------------- |
| Pull request vers `main` | vérifier la branche **avant** le merge                        |
| Push sur `main`          | vérifier le résultat **après** le merge, avant le déploiement |

### Les étapes du job `test`

Le job s'exécute sur une machine virtuelle Ubuntu fournie par GitHub (`ubuntu-latest`) et enchaîne les étapes suivantes :

1. **Récupération du code** (`actions/checkout`) ;
2. **Installation du JDK 21** (`actions/setup-java`, distribution Temurin) ;
3. **Configuration de Gradle** (`gradle/actions/setup-gradle`), qui met en cache les dépendances d'une exécution à l'autre ;
4. **Lancement des tests** avec `./gradlew test`. Les tests d'intégration démarrent une vraie base PostgreSQL grâce à **Testcontainers** : Docker est déjà installé sur la machine virtuelle, aucune configuration supplémentaire n'est nécessaire ;
5. **Publication des rapports** : le rapport de tests et le rapport de couverture JaCoCo.

### Les rapports

Les deux rapports sont publiés en **artifacts**, téléchargeables depuis la page de l'exécution sur GitHub (onglet **Actions**).

Ils sont publiés **même en cas d'échec**, grâce à la condition `if: always()`. Sans elle, l'échec des tests arrêterait le job avant la publication, et le rapport manquerait précisément quand il est utile : il permet de voir tout de suite quel test a échoué et pourquoi.

### Les barrières

La CI s'insère dans deux barrières, qui forment une chaîne complète de **CI/CD** :

1. **Avant le merge** : la branche `main` est protégée par le ruleset `protect-main`. Une pull request ne peut être mergée que si le check `test` est vert, et le push direct ou forcé sur `main` est bloqué.
2. **Avant le déploiement** : Render est réglé sur **After CI Checks Pass** (voir section 6). Après un merge, il attend que la CI soit verte sur le commit de `main` avant de déployer.

Ainsi, **aucun code non testé n'arrive en production**.

## 8. Mise à jour et retour en arrière

### Mettre à jour l'application

Le projet suit le **GitHub Flow** : chaque évolution est développée sur une branche dédiée, puis rejoint `main` par une **pull request** une fois terminée.

Une mise à jour suit donc toujours le même cycle :

1. ouverture d'une pull request vers `main` : la CI lance la suite de tests, et le merge reste bloqué tant qu'elle n'est pas verte (voir [section 7](#7-intégration-continue)) ;
2. merge de la pull request : la CI s'exécute à nouveau, cette fois sur le commit de `main` ;
3. une fois cette CI verte, Render construit la nouvelle image et la déploie (voir [section 6](#6-déploiement-sur-render)).

Render ne lance pas les tests lui-même : il attend le résultat de la CI de GitHub, puis construit l'image en sautant les tests (voir [section 4](#4-construction-de-limage)).

Une fois le déploiement terminé, refaire [les vérifications de la section 6](#vérification-après-déploiement).

### Revenir à une version précédente

En cas de problème en production, deux mécanismes se complètent :

| Mécanisme              | Où     | Effet                                                                                      |
| ---------------------- | ------ | ------------------------------------------------------------------------------------------ |
| **Rollback**           | Render | **immédiat** : redéploie l'image d'un déploiement précédent, sans reconstruire             |
| **`git revert`** en PR | GitHub | **durable** : annule le commit fautif sur `main`, en repassant par la CI et le déploiement |

Pour un rollback : ouvrir le service sur Render, aller dans **Deploy**, choisir un déploiement précédent réussi, puis cliquer sur **Rollback**.

Render **désactive alors l'Auto-Deploy** (un message le signale avant confirmation) : aucun nouveau merge ne sera déployé automatiquement tant qu'il n'est pas réactivé.

Le rollback seul ne suffit pas : le commit fautif reste sur `main`, et le redéploierait dès la réactivation de l'Auto-Deploy. Il faut donc ensuite corriger `main`, par un `git revert` ou un correctif, en pull request, puis réactiver l'Auto-Deploy (**Settings**, rubrique **Deploy**, réglage **After CI Checks Pass**).

### La base de données

Le retour en arrière ne concerne que **le code**. La base de données ne revient pas en arrière avec lui :

- les **données** écrites depuis le déploiement fautif restent en base ;
- si une version a modifié le **schéma**, l'ancienne version de l'application ne correspond plus à ce schéma, et `validate` bloquera son démarrage. Il faudrait alors annuler la modification du schéma à la main dans Supabase.

Les évolutions du schéma ne sont pas encore gérées par un outil de migration ([voir section 5, « Évolution du schéma »](#évolution-du-schéma)). Cette limite est reprise en section 9.

## 9. Limites connues

| Limite                             | Constat                                                                                  | Conséquence                                                                                              | Piste d'amélioration                                                                                                                                     |
| ---------------------------------- | ---------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Mise en veille sur Render          | En offre gratuite, le service s'endort après une période d'inactivité                    | La première requête peut prendre **plus de 2 minutes**                                                   | **Acceptable aujourd'hui**, faute d'utilisateurs réels. Correction directe : l'offre payante de Render, sans mise en veille                              |
| Couverture de tests                | **27 %** du code est couvert, les classes testées l'étant à 100 %, sans exclusion        | Le code non couvert n'est pas protégé contre les **régressions** par la CI                               | Étendre les tests en priorité à la logique métier non couverte                                                                                           |
| Schéma sans outil de migration     | La base de production a été modifiée à la main, sans trace dans le dépôt                 | Chaque évolution du schéma doit être appliquée **à la main** dans Supabase, avec un risque d'oubli       | Adopter **Flyway** : migrations versionnées dans le dépôt, relues en pull request et appliquées au démarrage (voir section 5)                             |
| Pas de sauvegarde de la production | L'offre gratuite de Supabase n'inclut **aucune sauvegarde**                              | En cas d'incident, **perte définitive des données**. Seul un `pg_dump` manuel protège la base            | Planifier un `pg_dump` par une GitHub Action (déclencheur `schedule`), stocké hors de Supabase, ou passer à l'offre payante, qui inclut des sauvegardes |
| Mise en pause de Supabase          | L'offre gratuite met en pause un projet dont la base reçoit trop peu de requêtes sur 7 jours               | La base devient inaccessible : l'API ne peut plus lire ni écrire de données jusqu'à sa relance manuelle | Contournement actuel : le site est consulté chaque jour, ce qui maintient le projet actif. Piste : automatiser cette consultation par une GitHub Action planifiée qui interroge l'API, ou passer à l'offre payante |
| Version de Gradle du `Dockerfile`  | L'image de build utilise `gradle:8.14`, alors que le wrapper du projet est en **9.4.1**  | L'image n'est pas construite avec la même version de Gradle qu'en local et en CI : risque d'écart de comportement | Correctif prévu : construire avec le wrapper (`./gradlew`) sur une image JDK 21 sans Gradle                                                             |

### Perspective d'hébergement

L'instance déployée sert aujourd'hui de démonstration, sans utilisateurs réels. Une évolution de l'application vers un **SaaS** de gestion de tournois, destiné à plusieurs associations locales, est à l'étude.

Si des associations se montrent intéressées, l'hébergement pourrait migrer vers un **serveur privé chez Hetzner** (entreprise allemande, serveurs dans l'Union européenne) :

- **avantages** : plus de mise en veille, sauvegardes maîtrisées, et un hébergeur européen, là où Render et Supabase reposent sur des infrastructures américaines soumises au Cloud Act ;
- **contrepartie** : l'administration du serveur (mises à jour de sécurité, pare-feu, certificat HTTPS, sauvegardes, supervision) serait alors entièrement à la charge du projet.
