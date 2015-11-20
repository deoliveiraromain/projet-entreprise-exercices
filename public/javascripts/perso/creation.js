/**
 * Created by Administrator on 26/02/14.
 */
(function() {
    // ajoute un ckeditor pour la création
    CKEDITOR.replace ( 'editorA', {customConfig : 'myConfig.js'});

    // permet de rabattre le volet de gauche et de le rouvrir.
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


    // permet d'insérer une formule mathématique du volet de gauche dans le ckeditor.
    $("#insertInEditor").click(function () {
        CKEDITOR.instances.editorA.insertHtml('<p><span data-latex="$' + $("#MathInput").val() + '$" class="math-tex">\\( ' + $("#MathInput").val() + ' \\)</span><span> </span></p>');
        $("#MathOutput").empty();
        $("#MathInput").val('');
    });

    $('.exo-bouton-validation').tooltip();
})();