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

    $("#allStarsDiv").on('mouseleave', renderScore);

    $(".starDiv").on('click', function() {

        var s = Number($(this).children("div").attr("sequence")) + 1;
        userScore = s;
        $.ajax({
            url: "/tstapp/mobileApp/addScore/" + appId + "?rating=" + s,
            complete: reloadScore(),
        });
    });

    renderScore();
 });

 function renderScore() {

   $("#appScore").val(appScore);
     $("#scoreStar_" + i).removeClass("silverStar")
   for(var i = 0; i < userScore; i ++)
     $("#scoreStar_" + i).addClass("goldStar");
 }

 function reloadScore() {

    $.ajax({
        url: "/tstapp/mobileApp/fetchScore/" + appId,
        dataType: "json",
        success: function(response) {
            appScore = response.appScore;
            renderScore();
        }
    });

 }

