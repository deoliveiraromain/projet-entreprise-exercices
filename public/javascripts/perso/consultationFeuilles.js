
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
        var dateCreation = $(this).attr('data-date-creation');
        var dateModification = $(this).attr('data-date-modification');

        var promo =  $(this).attr('data-promo');
        var option = $(this).attr('data-option');
        var module =  $(this).attr('data-module');
        var matiere =  $(this).attr('data-matiere');

        var type =  $(this).attr('data-type');
        var date =  $(this).attr('data-date-examen');
        var dureeInd =  $(this).attr('data-duree-indicative');
        var dureeReelle =  $(this).attr('data-duree-reelle');
        var size =  $(this).attr('data-size');
        var dataExos = [];
        var dataExosTitres=[];
        for(var i=0;i<size;++i){
            dataExos[i]= $(this).attr('data-exo-'+i);
            dataExosTitres[i]= $(this).attr('data-exo-titre-'+i);
        }




        $("#idTitreModal").empty().append(titre);

        $('#datecreation').empty().append(dateCreation);
        $('#datemodification').empty().append(dateModification);
        if(option=="non"){
            $('#promo').empty().append(promo);
        } else {
            $('#promo').empty().append(promo +'-'+option);
        }
        $('#module').empty().append(module);
        $('#matiere').empty().append(matiere);


        $('#type').empty().append(type);
        if(date!=="non"){
            $('#dateexamen').empty().append('Date Examen : '+date+'<br/>');
        }
        else{
            $('#dateexamen').empty();
        }

        $('#dureereelle').empty().append(dureeReelle);
        $('#dureeindicative').empty().append(dureeInd);
        $("#exoContenuModal").empty();


            for(var l=0;l<size;++l){


            $('#exoContenuModal').append('' +
                '<div class="panel-group" id="accordion'+l+'">'+
                    '<div class="panel panel-default"> ' +
                        '<div class="panel-heading"> ' +
                            '<h4 class="panel-title"> ' +
                            '<a data-toggle="collapse" data-parent="#accordion'+l+'" href="#collapse' +l+'">'+
                                dataExosTitres[l]+
                            '</a>' +
                            '</h4>' +
                        '</div>' +
                    '<div id="collapse'+l+'" class="panel-collapse collapse out"'+
                    '<div class="panel-body">' +
                                dataExos[l] +
                    '</div>' +
                    '</div>' +
                '</div>'  +
                '</div>'
            );
            }
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "myModal"]);

    });

} );