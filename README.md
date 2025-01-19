# Architecture et choix de conception de l'application

## Introduction
Ce document fournit un aperçu de l'architecture et des choix de conception réalisés pour l'application `fr.singwithme.BarbotaudJourden`. L'application est construite en utilisant Kotlin et Java, avec Gradle comme système de build. Elle utilise plusieurs bibliothèques et frameworks modernes pour garantir une base de code robuste et maintenable.

## Structure du projet
Le projet est organisé dans les principaux répertoires suivants :
- `src/main/java/fr/singwithme/barbotaudJourden` : Contient le code principal de l'application.
- `src/test/java/fr/singwithme/barbotaudJourden` : Contient les tests unitaires de l'application.
- `src/main/res` : Contient les fichiers de ressources tels que les layouts, les chaînes de caractères et les assets graphiques.

## Bibliothèques et frameworks clés
- **Kotlin** : Le langage de programmation principal utilisé pour l'application.
- **AndroidX** : Fournit des versions rétrocompatibles des API du framework Android.
- **Jetpack Compose** : Utilisé pour construire l'interface utilisateur de manière déclarative.
- **Retrofit** : Un client HTTP type-safe pour effectuer des requêtes réseau.
- **ExoPlayer** : Utilisé pour la lecture de médias.
- **Mockito** : Un framework pour créer des objets mock dans les tests.

## Configuration de build
La configuration de build est gérée à l'aide de Gradle. Les configurations clés incluent :
- **compileSdk** : 34
- **minSdk** : 33
- **targetSdk** : 34
- **jvmTarget** : 11

## Dépendances
Le projet inclut plusieurs dépendances pour supporter diverses fonctionnalités :
- **AndroidX Core** : Bibliothèques de base pour le développement Android.
- **Jetpack Compose** : Bibliothèques pour construire l'interface utilisateur.
- **Retrofit** : Pour les opérations réseau.
- **ExoPlayer** : Pour la lecture de médias.
- **Mockito** : Pour les tests unitaires.

## Composants de l'application
### Couche UI
La couche UI est construite en utilisant Jetpack Compose, ce qui permet une approche déclarative pour construire l'interface utilisateur. Cela rend le code de l'interface utilisateur plus lisible et plus facile à maintenir.

### Couche de données
La couche de données est responsable de la gestion des opérations de données. Retrofit est utilisé pour effectuer des requêtes réseau et récupérer des données à partir de serveurs distants. ScalarsConverterFactory est utilisé pour gérer les réponses en texte brut.

### Couche de domaine
La couche de domaine contient la logique métier de l'application. Elle inclut des modèles tels que `MusicModel` et `LyricModel`, qui représentent les structures de données utilisées dans l'application.

## Tests
Les tests unitaires sont écrits en utilisant JUnit et Mockito. Les tests garantissent que la logique de l'application est correcte et aident à prévenir les régressions. La classe `MdConverterFactoryTest` teste la fonctionnalité de la classe `MdConverterFactory`, en s'assurant qu'elle analyse et convertit correctement les corps de réponse.

## Conclusion
L'architecture et les choix de conception réalisés pour cette application visent à créer une base de code maintenable, évolutive et testable. En utilisant des bibliothèques et des frameworks modernes, l'application est construite pour être robuste et efficace.