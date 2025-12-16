# video_game_java
## 1 Récupérer le projet :
```shell
# ou fork depuis GitHub
git clone https://github.com/mithridatem/video_game_java.git 
cd video_game_java

```
## 2 Récupérer la dépendance 
mysql-connector-j-9.5.0.jar  
depuis le site officiel de MySQL :  
https://dev.mysql.com/downloads/connector/j/  
et la placer dans le projet (racine).    
project structure -> libraries -> + -> Java -> sélectionner le .jar  

## 3 Configurer la base de données
Lancer MySQL et créer une base de données nommée "videogame"
Avec le script SQL fourni dans le fichier db.sql

## 4 Configurer les paramètres de connexion
Editer les informations de connexion dans la classe Env.java  
Avec vos propres identifiants.
```java
public static final String DB_URL = "jdbc:mysql://server:3306/nom_bdd";
public static final String DB_USER = "";
public static final String DB_PASSWORD = "";
```
