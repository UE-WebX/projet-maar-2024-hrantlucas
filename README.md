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

# Les instructions comment utiliser notre API pour les recettes de plats
1. Quand l'application est lancé, ouvrez votre browser préféré ou un logiciel dedié pour faire des requêtes HTTP (Postman ou Insomnia).
2. Pour voir une recette aleatoire d'un certain type de cuisine, vous pouvez envoyer une requête GET avec l'URL http://localhost:8080/meal/{cuisineType} où cuisineType est votre type de cuisine désiré.

# Les instructions comment utiliser notre API pour les recettes de cocktails
1. Quand l'application est lancé, ouvrez votre browser préféré ou un logiciel dedié pour faire des requêtes HTTP (Postman ou Insomnia).
2. Pour voir une recette aleatoire (peu importe si c'est un cocktail avec alcool ou sans), vous pouvez envoyer une requête GET avec l'URL http://localhost:8080/recipe/drink.
3. Pour voir une recette aleatoire en précisant si c'est un cocktail avec alcool ou sans, vous pouvez envoyer une requête GET avec l'URL http://localhost:8080/recipe/drink?a={alcool} où alcool est true si vous voulez un cocktail avec alcool et false si sans alcool.

# Les instructions comment lancer les tests de l'application Recipe

## Avec un IDE
Lancer la class **RecipeEndpointTest.java** qui se trouve dans le dossier "src/test/java/org/hrantlucas", juste en appuyant sur le triangle vert à côté du nom.

## Depuis la console (avec "mvn")
1. Depuis la console, allez sur le dossier racine du projet.
2. Si vous voulez lancer tous les tests possibles, exécuter la commande suivante : **mvn test**
3. Si vous voulez lancer les tests d'une classe spécifique (RecipeEndpointTest par exemple), exécuter la commande suivante : **mvn -Dtest=RecipeEndpointTest test**