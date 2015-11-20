$(document).ready(function () {

    $(".exoIdTable").hide();
   if(   $('#inputpromo\\.classe').val() !="ING3") {
       $('#optiongroup').hide();
    }



    $('#inputpromo\\.classe').change(function () {
        var $promoSelected = $(this).val();
        if ($promoSelected == "ING3") {
            $('#optiongroup').show();
        }
        else {
            $('#optiongroup').hide();
        }
    });

    if( $('#carac\\.typefeuille').val() !="Examen") {
        $('#dateexamengroup').hide();
    }


    $('#carac\\.typefeuille').change(function(){
        var $typeselected = $(this).val();
        if ($typeselected == "Examen") {
            $('#dateexamengroup').show();
        }
        else {
            $('#dateexamengroup').hide();
        }


    });
    $('#inputtitle').keyup(function () {
        var $titre = $('#inputtitle').val();
        if ($titre == '') {
            $('#titregroup').addClass('has-error');
        }
        else {
            $('#titregroup').removeClass('has-error');
        }
    });



    $('#inputmatiere').keyup(function () {
        var $matiere = $('#inputmatiere').val();
        if ($matiere == '') {
            $('#matieregroup').addClass('has-error');
        }
        else {
            $('#matieregroup').removeClass('has-error');
        }
    });


    $('#inputmodule').keyup(function () {
        var $module = $('#inputmodule').val();
        if ($module == '') {
            $('#modulegroup').addClass('has-error');
        }
        else {
            $('#modulegroup').removeClass('has-error');
        }
    });



    $('#step2boutton').click( function(e) {



        var rows =  $myTable.fnGetNodes();
        var arrayCheckedId = [] ;
        var rowCollection = $myTable.$('.checkboxexo:checked',{'page' : 'all'});
        rowCollection.each(function(index, element){
            arrayCheckedId.push(element.value);

        });
        $('#success').hide();
        if(arrayCheckedId.length==0)return;

        $('#creationfeuille').show();

        var tabRes = [];
        for (var i = 0; i<rows.length;++i){
            var data=$myTable.fnGetData($myTable.fnGetPosition(rows[i]));
            arrayCheckedId.forEach(function (element){
                if(element.toString() == data[0].toString()) {
                    tabRes.push(data);
                }
            });
        }

        var buildTable = '' +
            '<table class="table table-bordered table-striped">'+
            '<th class="exoIdTable"></th>' +
            '<th></th>' +
            '<th>Titre</th>'+
            '<th>Module </th>'+
            '<th>Matière</th>'+
            '<th>Durée</th>'+
            '<th>Dernière utilisation</th>'+
            '<th>Ordre des exercices</th>'

        tabRes.forEach(function (element, index) {

            buildTable+= '<tr>' +
                '<td class="exoIdTable">' +
                element[1] +
                '</td>'+
              '<td>' +
                element[3] +
                '</td>'+
                '<td>' +
                element[4] +
                '</td>'+
                '<td>' +
                element[5] +
                '</td>'+
                '<td>' +
                element[6] +
                '</td>'+
                '<td>' +
                element[7] +
                '</td>'+
                '<td>' +
                element[8] +
                '</td>'+
                '<td><input type="number" class="form-control" name="listeExosNumber['+index+']"></td>'+
                '</tr>';

        });
        buildTable+='</table>';
        $("#tabtocreate").append(buildTable);
        $(".exoIdTable").hide();
        $('#step1').hide();
        return false;
    } );


    $(".exoIdTable").hide();


    var $myTable = $("#exostable").dataTable();


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



    var modifDuration = function() {

        var $name= $(this).attr("name");

        ///extration via une regex de l'index :
        /^listeExos\[([0-9]+)\]$/.exec($name);
        var $index =RegExp.$1;

        //On capture la durée de l'exo correspondant à l'index récupéré de la checkbox toujours via une regex
         var $dureeCell= $('#exoDuree\\[' + $index + '\\]');

        //on selectionne l'objet label contenant la durée totale., on capture la durée via une regex
        var $dureeText = $('#input-carac-tempsimparti');
        var $dureeText2 = $('.span-carac-tempsimparti');
        /^([0-9]) h ([0-9]+) min$/.exec($dureeText.val());
        //on récupère l'heure et les minutes

        var $heure1 = RegExp.$1;
        var $min1 = RegExp.$2;


        //on fait de même avec  l'exo selectionné : on capture la durée
        /^([0-9]) h ([0-9]+) min$/.exec($dureeCell.text());
        var $heure2 = RegExp.$1;
        var $min2   = RegExp.$2;

        //on crée des dates avec moment.js pour pouvoir faire des additions ou des soustractions simplement.
        var $duree1 =moment.duration({ minutes: $min1, hours: $heure1});
        var $duree2 = moment.duration({minutes: $min2, hours : $heure2});


        var $dureefinale;
        if( $(this).prop('checked')) {
            $dureefinale=$duree1.add($duree2);
        }
        else {
            $dureefinale=$duree1.subtract($duree2);
            if($dureefinale.minutes.length ==1){
            }
        }
        $dureeText.val($dureefinale.hours() + " h " + $dureefinale.minutes() + " min");
        $dureeText2.text($dureefinale.hours() + " h " + $dureefinale.minutes() + " min");

    };

    $("#exostable_wrapper").on("click", ":checkbox",modifDuration);



    $('#realtempsimparti').popover();
    $('#realtempsimparti').blur(function(){
        $(this).popover('hide');
    });

    $('#datetimepicker').datetimepicker({
        dateFormat : 'dd-mm-yy',
        pickTime: false
    });



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
        for(var i=0;i<size;++i){
            datafeuille[i]= $(this).attr('data-feuille-'+i);
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
            $('#chapitre').empty().append(chapitre+'</br>');
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
        for(var i=0;i<size;++i){
            $('#listefeuille').append(datafeuille[i]);
        }

        $("#exoContenuModal").html(contenu);
        MathJax.Hub.Queue(["Typeset", MathJax.Hub, "myModal"]);

    });
});