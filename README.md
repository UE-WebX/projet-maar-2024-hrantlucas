# Les membres de notre équipe

- Hrant ARAKELYAN (TP51)
- Lucas BIHANNIC (TP51)

# Les instructions comment lancer l'application Recipe

## Avec un IDE
1. Lancer la methode "main" de la class **RecipeApplication.java** qui se trouve dans le dossier "src/main/java/org/hrantlucas", juste en appuyant sur le triangle vert à côté du nom.
2. Si vous voyez "Jersey app started with endpoints available at ..." dans la console, cela veut dire que l'application est bien lancé.

## Depuis la console (avec "mvn")
1. Depuis la console, allez sur le dossier racine du projet.
2. Exécuter la commande suivante : **mvn compile exec:java -Dexec.mainClass="com.example.RecipeApplication"**
3. Si vous voyez "Jersey app started with endpoints available at ..." dans la console, cela veut dire que l'application est bien lancé.

# Les instructions comment lancer les tests de l'application Recipe

## Avec un IDE
Lancer la class **RecipeEndpointTest.java** qui se trouve dans le dossier "src/test/java/org/hrantlucas", juste en appuyant sur le triangle vert à côté du nom.

## Depuis la console (avec "mvn")
1. Depuis la console, allez sur le dossier racine du projet.
2. Si vous voulez lancer tous les tests possibles, exécuter la commande suivante : **mvn test**
3. Si vous voulez lancer les tests d'une classe spécifique (RecipeEndpointTest par exemple), exécuter la commande suivante : **mvn -Dtest=RecipeEndpointTest test**