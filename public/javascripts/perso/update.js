/**
 * Created by Administrator on 26/02/14.
 */

// Page update.scala.html
// Ce fichier gère l'ajout d'un nouveau corrigé depuis la page update.scala.html (clic sur le bouton pencil), la suppression des corrigés
// l'ajout d'une formule mathématique (en utilisant le panel mathématique) dans l'onglet courant
//
$(function() {

    // CKEditor de l'exercice
    CKEDITOR.replace ( 'editorB', {
        customConfig : 'myConfigUpdate.js'
    });

    // Permet de cacher le volet de gauche
    $("#exoPanelGaucheMinified" ).css('visibility','hidden');
    $("#hidePanelGauche" ).click(function (){
        $("#exoPanelGauche" ).hide();
        $("#exoPanelGaucheMinified" ).css('visibility','visible');
        $("#exoCkeditor").attr('class', 'col-xs-12 col-sm-12 col-md-12 col-lg-12 exo-padding-ckeditor');
    });
    $("#showPanelGauche" ).click(function (){
        $("#exoPanelGauche" ).show();
        $("#exoPanelGaucheMinified" ).css('visibility','hidden');
        $("#exoCkeditor").attr('class', 'col-xs-12 col-sm-9 col-md-9 col-lg-9 exo-padding-ckeditor');
    });

    // affiche le bouton "supprimer" (icône poubelle rouge) correspondant à l'onglet affiché
    function changeCurrentTab() {
        var id = $(this ).attr('href');
        $("#buttonDeleteTabs a[id^='delete']" ).hide();
        $("#delete"+id.substring(4)).show();
    }

    $("#myTab li a" ).click(changeCurrentTab);
    // Lorsqu'on clique pour la première fois sur un onglet, on recharge le contenu pour que le contenu mathématique soit interprété.
    $("#myTab li a" ).one('click', function() {
        var id = $(this ).attr('href');
        var idTextarea = id.substring(4);
        if(idTextarea == "Exercise") {
            CKEDITOR.instances.editorB.setData(CKEDITOR.instances.editorB.getData());
        }  else {
            CKEDITOR.instances[idTextarea].setData(CKEDITOR.instances[idTextarea].getData());
        }
    });

    $("#exoButtonRefresh").click(function () {
        var idTextarea = $("#myTab li.active a").attr('href').substring(4);
        if(idTextarea == "Exercise") {
            CKEDITOR.instances.editorB.setData(CKEDITOR.instances.editorB.getData());
        }  else {
            CKEDITOR.instances[idTextarea].setData(CKEDITOR.instances[idTextarea].getData());
        }
    });

    // Clic pour ajouter un nouveau corrigé (icône pencil)
    $("#idAddCorrige" ).click(function (){

        // On récupère le nombre d'onglets existants
        var nbCorrige = $("#myTab li" ).size();
        // On crée les nouveaux id pour le titre de l'onglet (li) pour le conteu (textarea), pour le bouton "supprimer" (icône poubelle)
        var idNewCorrige = 'tabcorrige_'+parseInt(nbCorrige - 1)+"_";
        var idTextArea = 'corrige_'+parseInt(nbCorrige - 1)+"_";
        var idDelete = 'deletecorrige_'+parseInt(nbCorrige - 1)+"_";
        $("#myTab li" ).removeClass('active');
        // Création du nouveau titre
        var tabTitle = '<li class="active"><a href="#'+idNewCorrige+'" data-toggle="tab">Corrige N°'+nbCorrige+'</a></li>';
        // Création du nouveau contenu
        var tabContent = '<div class="tab-pane active" id="'+idNewCorrige+'">'+
            '<textarea name="corrige['+parseInt(nbCorrige-1)+']" id="'+idTextArea+'">' +
            'Corrige : </textarea></div>';

        $("#myTab" ).append(tabTitle);
        // Permet de changer le bouton "supprimer" lors du changement d'onglets
        $("#myTab li a" ).click(changeCurrentTab);
        $("#myTabContent div" ).removeClass('active');
        $("#myTabContent" ).append(tabContent);

        // Création du bouton "supprimer"
        $("#buttonDeleteTabs a[id^='delete']" ).hide();
        var buttonDelete = '<a id="'+idDelete+'" style="background-color: #DD4B39;"' +
            'class="btn btn-link pull-right exo-bouton-validation">' +
            '<i class="fa fa-lg fa-trash-o"></i></a>';

        $("#buttonDeleteTabs").append(buttonDelete);
        // Clic sur le bouton supprimer
        $("#"+idDelete ).click(function () {

            // rend l'onglet de l'exercice visible
            $("#myTabContent div:first-child" ).addClass('active');
            // supprime le titre de l'onglet de cet élément
            $('#myTab li:last-child').remove();
            $("#myTab li:first-child" ).addClass('active');
            // supprime le contenu
            $('#'+idNewCorrige).remove();
            // affiche le bouton supprimer de l'exercice
            $("#buttonDeleteTabs a[id^='delete']:first-child" ).show();
            // supprime le bouton
            $(this).remove();
        });

        // ajoute un ckeditor
        CKEDITOR.replace (idTextArea, {
            customConfig : 'myConfigUpdate.js'
        });
    });

    // Pour le bouton "ajouter" du panel mathématique, permet d'ajouter la formule dans le ckeditor courant (l'onglet ouvert)
    $("#insertInEditor").click(function () {
        var currentInstanceTab = $("#myTab li.active a").attr('href');
        var currentInstance = '';
        if(currentInstanceTab == "#tabExercise") {
            currentInstance='editorB';
        } else {
            currentInstance=currentInstanceTab.substring(4);
        }
        CKEDITOR.instances[currentInstance].insertHtml('<p><span data-latex=" $' + $("#MathInput").val() + '$" class="math-tex">\\( ' + $("#MathInput").val() + ' \\)</span><span> </span></p>');
        $("#MathOutput").empty();
        $("#MathInput").val('');
    });

    // Lorsqu'on arrive sur la page, masque tous les boutons "supprimer" (icône poubelle rouge) sauf celui de l'onglet courant.
    $("#buttonDeleteTabs a[id^='delete']" ).hide();
    $("#delete"+$("#myTab li.active a").attr('href').substring(4)).show();
    $(".exo-bouton-validation").tooltip();
});