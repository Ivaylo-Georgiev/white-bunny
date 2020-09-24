$( document ).ready(function() {
  $.ajax({
            type: "GET",
            url: "https://gg5vt8zze4.execute-api.us-east-1.amazonaws.com/prod/fetchalbums",
            crossDomain: true,
            contentType: "application/json",
            dataType: "json",
            success: function(data) {
              drawAlbums(JSON.parse(data.albums));
            }
          });
});

function drawAlbums(albums) {
  for(let album of albums) {
    $("#albums").append("<button id=\"" + album + "\" class=\"album box-shadow\">" + album + "</button>");
  }
}

$(document).on("click", ".album", function() {
  let albumName = this.id;

  if(albumName !== "create-album") {
    redirectToSongs(albumName);
  }
});
