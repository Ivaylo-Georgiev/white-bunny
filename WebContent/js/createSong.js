$("#create-song-form").submit(function(event) {
  event.preventDefault();
  let songName = $("#song-name").val();
  let songArtist = $("#song-artist").val();
  let songAlbum = $("#song-album").val();

  let song = new Object();
  song.name = songName;
  song.artist = songArtist;
  song.album = songAlbum;

  $.ajax({
            type: "POST",
            url: "https://b29vzjbgh8.execute-api.us-east-1.amazonaws.com/prod/uploadsong",
            data: JSON.stringify(song),
            crossDomain: true,
            contentType: "application/json",
            dataType: "json",
            success: function(data) {
              redirectToUploadSong();
            }
          });
});
