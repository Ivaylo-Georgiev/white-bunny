$("#create-album-form").submit(function(event) {
  event.preventDefault();
  let albumTitle = $("#album-title").val();

  let album = new Object();
  album.title = albumTitle;

  $.ajax({
            type: "POST",
            url: "https://61frap5t50.execute-api.us-east-1.amazonaws.com/prod/uploadalbum",
            data: JSON.stringify(album),
            crossDomain: true,
            contentType: "application/json",
            dataType: "json",
            success: function(data) {
              backToHome();
            }
          });
});
