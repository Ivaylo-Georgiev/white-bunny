$("#upload-song-form").submit(function(e) {
  $.ajax( {
      url: 'https://ror9nkk22g.execute-api.us-east-1.amazonaws.com/prod/uploadsong',
      type: 'POST',
      data: new FormData( this ),
      processData: false,
      contentType: false,
      success: function(){
        backToHome();
      }
    } );
    e.preventDefault();

})
