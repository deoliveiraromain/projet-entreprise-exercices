$(document).ready(function() {
    $('#exo-link-services').click(function(){
        console.log("lol");
        var the_id = $(this).attr("href");
        $('html, body').animate({
            scrollTop:$(the_id).offset().top
        }, 'slow');
        return false;
    });
});