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

    // cities table:
    var citiesSkip = 0;
    var citiesPageSize = 10;
    reloadCities = function(newCitiesSkip) {
        citiesSkip = newCitiesSkip;
        var query = { limit: citiesPageSize, skip: citiesSkip };
        $('.pager li').addClass('disabled');
        $.get('http://localhost:12836/cities', query, function(data) {
            let html = '';
            data.data.forEach(x => html += `<tr><td>${x.name}</td><td>${x.country}</td><td>${x.population}</td><td>${x.latitude}</td><td>${x.longitude}</td><td>${x.temperature}</td></tr>`);
            $('#cities-table tbody').html(html);
            if (citiesSkip + citiesPageSize < data.totalCount) {
                $('.pager .next').removeClass('disabled');
            }
            if (citiesSkip > 0) {
                $('.pager .previous').removeClass('disabled');
            }
        });
    };
    navigationClick = function(e, elem, newCitiesSkip) {
        if (!$(elem).hasClass('disabled')) {
          reloadCities(newCitiesSkip);
        }
        e.preventDefault();
        return false;
    };
    $('.pager .previous').click(function(e) { navigationClick(e, this, citiesSkip - citiesPageSize); });
    $('.pager .next').click(function(e) { navigationClick(e, this, citiesSkip + citiesPageSize); });
    reloadCities(0);

    $('#refresh-temperatures').click(function(e) {
        var glyphicon = $(this).find('glyphicon');
        glyphicon.addClass('glyphicon-spin');
        $.post('http://localhost:12836/update-tempratures/', function() {
            glyphicon.removeClass('glyphicon-spin');
            reloadCities(citiesSkip);
        });
        e.preventDefault();
        return false;
    });

    // Weather prediction:
    var loadPlot = function() {
        $.get('http://localhost:12836/model-plot', function(data) {
            $('#plot-spinner').hide();
            $('#plot').html(data).show();
        });
    };
    loadPlot();

    $('#regenerate').click(function(e) {
        $('.viz').hide();
        $('.viz-spinner').show();
        $('.action-button').prop('disabled', true);
        $.post('http://localhost:12836/regenerate/' + $('#model-size').val(), function() {
            loadPlot();
            $('.action-button').prop('disabled', false);
        });
        e.preventDefault();
        return false;
    });

    $('#predict').click(function(e) {
        $('#not-found').hide();
        $('#prediction').hide();
        $.get('http://localhost:12836/predict/' + $('#cityName').val(), function(data) {
            $('#result-predicted').text(data.predicted);
            $('#result-real').text(data.real);
            $('#prediction').show();
        }).fail(function() {
            $('#not-found').show();
        });
        e.preventDefault();
        return false;
    });
});
