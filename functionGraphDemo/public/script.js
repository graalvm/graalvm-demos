$(document).ready(function() {
  $('#draw').click(function(e) {
    var expression = $('#expression').val();
    $.post('/graph', {expr: expression}, function(data) {
      $('#plot').html(data).show();
    });
    e.preventDefault();
    return false;
  });
});
