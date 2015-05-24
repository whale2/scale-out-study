 $(document).ready(function(){

    $(".starDiv").on('mouseenter',function() {

        var s = $(this).children("div").attr("sequence");
        for(var i = 0; i <= s; i ++) {

            $("#scoreStar_" + i).removeClass("silverStar");
            $("#scoreStar_" + i).addClass("goldStar");
        }
    });

    $(".starDiv").on('mouseleave',function() {
        var s = $(this).children("div").attr("sequence");
        for(var i = 0; i <= s; i ++) {

            $("#scoreStar_" + i).addClass("silverStar");
            $("#scoreStar_" + i).removeClass("goldStar");
        }
    });

    $(".starDiv").on('click', function() {

        var s = $(this).children("div").attr("sequence");
        $.ajax({
            url: "/tstapp/mobileApp/addScore/" + appId + "?rating=" + s,

        }).done(reloadScore());
    });

 });

 function reloadScore() {}

