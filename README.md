Installation du projet
Code source du projet :

1) T�l�charger Play � l'adresse :
http://downloads.typesafe.com/play/2.2.1/play-2.2.1.zip

2) Mettre le dossier en variable d'environnement

3) Installation de git :
$ sudo apt-get install git

5) Aller dans le dossier souhait� pour l'installation du projet:

$ git clone git@gitlab.etude.eisti.fr:bastien.chares/projet-entreprise-exercices.git
$ cd projet-entreprise-exercices
$ play idea
Maintenant le projet est reconnu par intelliJ.


Base de donn�es NoSQL : MongoDB
6) T�l�charger mongods � l'adresse :
http://www.mongodb.org/dr/fastdl.mongodb.org/linux/mongodb-linux-x86_64-2.4.9.tgz/download

7)Mettre le dossier en variable d'environnement

8) d�marrer mongo avec :
$ mongod

9) Installer TTH :
TTH est un programme open source dont on se sert dans la fonctionnalit� d'upload
t�l�charger  http://hutchinson.belmont.ma.us/tth/tth-noncom/tth_linux.tar.gz
important : extraire et mettre le dossier en variable d'environnement

10) Lancement du serveur play :
� Quitter le shell qui servira � lancer le serveur play (shell classique ou intelliJ) afin que
les variables d'environnement soient prises en compte (surtout pour TTH)
� par le shell : aller dans le dossier projet-entreprise-exercices
$ play ~run
� depuis intelliJ : faire la commande depuis le terminal
11) Play utilise le port 9000, pour acc�der � la page d'accueil une fois le serveur lanc� :
localhost:9000