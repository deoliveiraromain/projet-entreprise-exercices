/**
 * Created by Administrator on 26/02/14.
 */

/**
 * Created by Administrator on 11/01/14.
 */

CKEDITOR.editorConfig = function( config )
{
    config.language = 'fr';
    config.uiColor = '#b1c1d1';

    config.toolbar = 'Full';
    config.height = '452px';

    config.extraPlugins = 'list,pbckcode';
    config.toolbar_Full =
        [
            { name: 'document', items: [ 'Source', '-', 'Save', 'NewPage', 'DocProps', 'Preview', 'Print'] },
            { name: 'clipboard', items: [ 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo' ] },
            { name: 'links', items: [ 'Link', 'Unlink', 'Anchor' ] },
            { name: 'colors', items: [ 'TextColor', 'BGColor'] },
            { name: 'insert', items: [ 'Image', 'Table', 'HorizontalRule', 'SpecialChar', 'PageBreak','pbckcode'] },
            '/',
            { name: 'paragraph', items: [ 'NumberedList', 'BulletedList', 'QuestionList', 'SubQuestionList', '-', 'Outdent', 'Indent', '-', 'Blockquote',
                '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock' ] },
            { name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'RemoveFormat' ] },
            { name: 'editing', items: [ 'Find', 'Replace', '-', 'SelectAll', '-', 'SpellChecker', 'Scayt' ] },

            '/',
            { name: 'styles', items: [ 'Styles', 'Font', 'FontSize', 'Format'] },
            { name: 'tools', items: [ 'Maximize']}
        ];


    config.pbckcode = {
        highlighter: "SYNTAX_HIGHLIGHTER"
    };
    config.allowedContent = true
};

