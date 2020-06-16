# BitcoinMania-3ESGI
Application permettant d’étudier les variations du cours du Bitcoin en JAVA

# Start project
- Télécharger et configurer Maven
- Installer les dépendances via la commande: `maven install` 
- Lancer le projet via la commande: `mvn clean javafx:run` 

# Configuration de la BDD
- Importer le fichier `bitcoinmania.sql` sql dans votre base de donnée mysql
- Modifier les variables `LOGIN` `PASSWORD` dans le fichier `org.bitcoin.utils.Database` qui correspondent au login/password de mysql

# Configuration du dossier de logs
- Modifier la variable `logsdirPath` dans le fichier `org.bitcoin.utils.Logger` qui correspond au dossier ou sont créé les fichiers de logs
## Utilisation des logs
   Pour utilisé les logs il suffit d'utilisé l'instance `Logger` de la classe principale `org.bitcoin.app.App`
   - Exemple :
        - `App.logger.info("Start application Info")`
        - `App.logger.debug("Start application Debug");`
        - `App.logger.warn(1001, "Start application Warning", null);`
        - `App.logger.error(101, "Start Application Error", Exception);`