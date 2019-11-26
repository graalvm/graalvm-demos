<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <title>Graal-Todos</title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-sm"></div>
        <div class="col-sm">
            <h2>Graal-Todos</h2>
            <div>
                <form class="form-inline" action="/todos" method="post">
                            <input type="text" class="form-control mb-2 mr-sm-2" name="content" placeholder="next up"/>
                            <button type="submit" class="btn btn-primary mb-2">Add</button>
                </form>
            </div>
            <ul class="list-group list-group-flush">
                <#list allTodos as val>
                    <li class="list-group-item d-flex justify-content-between" id="${val.id}"><p class="p-0 m-0 flex-grow-1">${val.content}</p>
                        <button class="btn-danger" onclick="deleteTodo('${val.id}')">x</button>
                    </li>
                </#list>
            </ul>
        </div>
        <div class="col-sm"></div>
    </div>
</div>
<script>
    function deleteTodo(id) {
        xhr = new XMLHttpRequest();
        xhr.open('DELETE', '/todos/'+id);
        xhr.onload = function (ev) {
            console.info(ev);
            location.reload(true);
        };
        xhr.send();
    }
</script>
</body>
</html>