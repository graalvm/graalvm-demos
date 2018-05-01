$(document).ready(function() {
    // navigation between pages:
    $('.navbar-nav a').click(function(e) {
        $('.page').hide();
        $('.navbar-nav .active').removeClass('active')
        $('#' + $(this).data('page')).show();
        $(this).parent().addClass('active')
        e.preventDefault();
        return false;
    });

    // data loading
    var refresh = function(page) {
        var query = {};
        let inputs = page.find('.plot-form .input').each((idx, e) => query[$(e).attr('id')] = $(e).val());
        $.get('http://localhost:12837/' + page.attr('id'), query, function(data) {
            page.find('.plot').html(data);
        });        
    }

    refresh($('#kmeans'));
    refresh($('#cars'));
    refresh($('#lm'));

    $('.input').change(function(e) {
        refresh($(this).parents('.page:first'));
        e.preventDefault();
        return false;
    });

    // linear regression
    $('#predict-button').click((e) => {
        let height = $('#height').val();
        $('#prediction-text').show(2000);
        $.get('http://localhost:12837/lm/predict/', {height: height}, function(data) {
            $('#prediction').html(data);
        });
        e.preventDefault();
        return false;
    });
});
