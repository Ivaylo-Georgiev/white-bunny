$("#create-album").click(function(){
  window.location.href = "../html/createAlbum.html";
});

function backToHome() {
  window.location.href = "../html/index.html";
}

function redirectToSongs(album) {
  window.location.href = "../html/songs.html?album=" + album;
}

function redirectToCreateSong() {
  window.location.href = "../html/createSong.html";
}

function redirectToUploadSong() {
  window.location.href = "../html/uploadSong.html";
}

function redirectToPlayer(song) {
  window.location.href = "../html/player.html?song=" + song;
}
