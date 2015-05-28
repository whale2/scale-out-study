 $(document).ready(function(){

    if (!userScore) {
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

            var s = Number($(this).children("div").attr("sequence")) + 1;
            localScore = s;
            $.ajax({
                url: "/tstapp/mobileApp/addScore/" + appId + "?rating=" + s,
                dataType: "json",
                success: function(response) {
                    appScore = response.appScore;
                    renderScore(true);
                }
            });
        });
    }

    renderScore(false);
 });

 function renderScore(useLocal) {

   if (useLocal) {
     userScore = localScore;
     // Remove handlers - only single vote is allowed
     $(".starDiv").off('click');
     $("#allStarsDiv").off('mouseleave');
     $(".starDiv").off('mouseleave');
     $(".starDiv").off('mouseenter');
   }

   $("#appScore").html(appScore);
   for(var i = 0; i < 5; i ++)
     $("#scoreStar_" + i).removeClass("goldStar")

   for(var i = 0; i < userScore; i ++)
     $("#scoreStar_" + i).addClass("goldStar");

   for(var i = userScore + 1; i < 5; i ++) {
     $("#scoreStar_" + i).removeClass("goldStar");
     $("#scoreStar_" + i).addClass("silverStar");
   }

   if (userScore != 0)
     $("#userHasVoted").html("Вы проголосовали");
 }


