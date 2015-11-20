$(document).ready(function() {

    var exoDataTable = $("#exostable").dataTable();

    $('#exostable_wrapper input').addClass("form-control");
    $('#exostable_wrapper select').addClass("form-control");
    $('#exostable_wrapper select').addClass("form-control");


    var $element = $('#exostable_filter');

    $element.addClass("input-group col-md-6 col-md-offset-6");
    $element.prepend($('#exostable_filter .form-control'));
     $('#exostable_filter label').remove();

    var $element2 = $('<span>', {
        class: 'input-group-addon '});

    var $icone = $('<span>', {
        class: 'fa fa-search fa-flip-horizontal '
    });
    $element2.prepend($icone);
    $element.append($element2);

    $('#exostable_length').prepend($('#exostable_length .form-control'));
    $('#exostable_length label').remove();
    $('#exostable_length').append(" exercices par pages");


    $('#exostable_wrapper .row:first').css({'margin-bottom' : '30px'});

    $('.link').tooltip();



    $(document).on('click','a[href^="#titreModal"]',function () {
        var titre= $(this).attr('data-titre');
        var corrige = $(this).attr('data-corrige');
        var dateCreation = $(this).attr('data-date-creation');
        var dateModification = $(this).attr('data-date-modification');
        var lastused = $(this).attr('data-last-used');
        var module =  $(this).attr('data-module');
        var matiere =  $(this).attr('data-matiere');
        var chapitre =  $(this).attr('data-chapitre');
        var type =  $(this).attr('data-type');
        var duree =  $(this).attr('data-duree');
        var size =  $(this).attr('data-size');
        var contenu =$(this).attr('data-contenu');
        var datafeuille = [];
        var datafeuilleCorrige = [];
        for(var i=0;i<size;++i){
            datafeuille[i]= $(this).attr('data-feuille-'+i);
        }

        var sizeCorrige = $(this).attr('data-size-corrige');

        for(var j=0;j<sizeCorrige;++j){
            datafeuilleCorrige[j]= $(this).attr('data-corrige-'+j);
        }




        $("#idTitreModal").empty().append(titre);
        if(corrige==="oui"){
            $('#idIconeCorrige').empty().append('<span class="glyphicon glyphicon-ok "></span><br/>');
        }
        else if (corrige==="non"){
            $('#idIconeCorrige').empty().append('<span class="glyphicon glyphicon-remove "></span><br/>');
        }
        $('#datecreation').empty().append(dateCreation);
        $('#datemodification').empty().append(dateModification);
        if(lastused !=="non"){
            $('#lastused').empty().append(lastused+'<br/>');
        }
        else{
            $('#lastused').empty();
        }
        $('#module').empty().append(module);
        $('#matiere').empty().append(matiere);

        if(chapitre!=="non"){
            $('#chapitre').empty().append(chapitre+'<br/>');
        }
        else{
            $('#chapitre').empty();
        }
        $('#type').empty().append(type);
        $('#duree').empty().append(duree);

        if(size){
            $('#presentfeuilles').empty().append('Exercice présent dans les feuilles :<br/>');
        }
        else{
            $('#presentfeuilles').empty();
        }
        $('#listefeuille').empty();
        for(var k=0;k<size;++k){
            $('#listefeuille').append(datafeuille[k]);
        }

        $("#exoContenuModal").empty().append(contenu);
        if(corrige==="oui"){
            $('#exoCorrigeContenu').empty();

            for(var l=0;l<sizeCorrige;++l){
            $('#exoCorrigeContenu').append('' +
                    '<div class="panel-group" id="accordion">'+
                    '<div class="panel panel-default"> ' +
                        '<div class="panel-heading"> ' +
                            '<h4 class="panel-title"> ' +
                            '<a data-toggle="collapse" data-parent="#accordion" href="#collapse' +l+'">'+
                             'Corrige' +(l+1)+
                            '</a>' +
                            '</h4>' +
                        '</div>' +
                    '<div id="collapse'+l+'" class="panel-collapse collapse out"'+
                    '<div class="panel-body">' +
                        datafeuilleCorrige[l] +
                    '</div>' +
                    '</div>' +
                '</div>' +'</div>'

            )     ;
            }
        }
        else{
            $('#exoCorrigeContenu').empty();
        }
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "myModal"]);
    });
} );