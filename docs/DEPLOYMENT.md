# Documentation de déploiement

Vous trouverez ici les informations concernant le déploiement de l'application fsc-tournament : la vue d'ensemble de l'architecture, puis le déploiement de l'API fsc-tournament-back. Le déploiement de l'interface est documenté dans son propre dépôt.

## 1. Vue d'ensemble

![Architecture de déploiement](diagrams/architecture-deploiement.svg)

Voici le schéma d'organisation de l'application :

- le navigateur charge l'interface Angular (HTML, JS, CSS) depuis Vercel ;
- le navigateur effectue ensuite lui-même les appels vers l'API fsc-tournament-back, hébergée sur Render dans un conteneur Docker ;
- l'API lit et enregistre les données dans une base PostgreSQL hébergée par Supabase, via une connexion chiffrée (SSL/TLS).

| Élément | URL publique | Dépôt |
|---|---|---|
| Interface | https://fsc-tournament-front.vercel.app/ | [fsc-tournament-front](https://github.com/Laeti-Challant/fsc-tournament-front) |
| API | https://fsc-tournament-back.onrender.com/api | [fsc-tournament-back](https://github.com/Laeti-Challant/fsc-tournament-back) |
