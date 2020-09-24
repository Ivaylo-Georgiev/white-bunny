$(document).ready(function() {

  const urlParams = new URLSearchParams(window.location.search);
  const albumName = urlParams.get("album");
  $("#album-title").append("Songs in " + albumName + "...");

  let album = new Object();
  album.title=albumName;

  $.ajax({
            type: "POST",
            url: " https://qnx1l2rdd2.execute-api.us-east-1.amazonaws.com/prod/fetchsongs",
            data: JSON.stringify(album),
            crossDomain: true,
            contentType: "application/json",
            dataType: "json",
            success: function(songs) {
              drawSongs(songs);
            }
          });
});

function drawSongs(songs) {
  for(let song of songs) {
    $(".song-name-in-album").append("<a>" + song.name + "</a>");
    $(".song-artist-in-album").append("<p>" + song.artist + "</p>");
  }
}

$(document).on("click", ".song-name-in-album a", function() {
  redirectToPlayer(this.innerText);
});


$("#new-song").click(function() {
  redirectToCreateSong();
});
