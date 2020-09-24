const s3  = "https://fmi-aws-album-generator.s3.amazonaws.com/";

$(document).ready(function(){
  const urlParams = new URLSearchParams(window.location.search);
  const songName = urlParams.get("song");

  $("#player").append("<audio src=\"" + s3 + songName + "\" controls></audio>");
});
