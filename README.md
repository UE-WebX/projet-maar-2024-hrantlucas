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
2. Pour voir une recette aleatoire d'un certain type de cuisine, vous pouvez envoyer une requête GET avec l'URL: 
* http://localhost:8080/recipe/meal/{cuisineType} qui renvoie une structure XML.
* http://localhost:8080/v2/recipe/meal/{cuisineType} qui renvoie une structure JSON. 

Dans les deux cas cuisineType est votre type de cuisine désiré.

# Les instructions comment utiliser notre API pour les recettes de cocktails
1. Quand l'application est lancé, ouvrez votre browser préféré ou un logiciel dedié pour faire des requêtes HTTP (Postman ou Insomnia).
2. Pour voir une recette aleatoire (peu importe si c'est un cocktail avec alcool ou sans), vous pouvez envoyer une requête GET avec l'URL:
* http://localhost:8080/recipe/drink qui renvoie une structure XML.
* http://localhost:8080/v2/recipe/drink qui renvoie une structure JSON.
3. Pour voir une recette aleatoire en précisant si c'est un cocktail avec alcool ou sans, vous pouvez envoyer une requête GET avec l'URL:
* http://localhost:8080/recipe/drink?a={alcool} qui renvoie une structure XML.
* http://localhost:8080/v2/recipe/drink?a={alcool} qui renvoie une structure JSON.

Dans les deux cas alcool est true si vous voulez un cocktail avec alcool et false si sans alcool.

# Les instructions comment utiliser notre API pour les menus

L'API pour les menus permet de générer un menu complet comprenant une entrée, un plat principal, un dessert, et une boisson. Les utilisateurs peuvent spécifier des filtres pour personnaliser le menu selon leurs préférences alimentaires.

## Générer un menu complet
1. Quand l'application est lancée, ouvrez votre navigateur préféré ou un logiciel dédié pour faire des requêtes HTTP (comme Postman ou Insomnia).
2. Pour générer un menu complet, vous pouvez envoyer une requête POST avec l'URL:
    * http://localhost:8080/menu
3. La requête doit inclure un corps JSON avec les paramètres désirés, par exemple:
   ```json
   {
       "cuisineType": "Italian",
       "alcoholic": false,
       "presentIngredient": "water",
       "maxPreparationTime": 90,
       "constraints": ["vegetarian"]
   }

# Les instructions comment lancer les tests de l'application

## Avec un IDE
Lancer la class **RecipeEndpointTest.java** qui se trouve dans le dossier "src/test/java/org/hrantlucas", juste en appuyant sur le triangle vert à côté du nom.

## Depuis la console (avec "mvn")
1. Depuis la console, allez sur le dossier racine du projet.
2. Si vous voulez lancer tous les tests possibles, exécuter la commande suivante : **mvn test**
3. Si vous voulez lancer les tests d'une classe spécifique (RecipeEndpointTest par exemple), exécuter la commande suivante : **mvn -Dtest=RecipeEndpointTest test**

# instructions pour générer les fichiers OpenAPI
1. Depuis la console, aller sur le dossier racine du projet.
2. exécuter la commande suivante **mvn clean compile swagger:resolve**
3. les fichiers générés sont dans dans le dossier **/target/openapi**