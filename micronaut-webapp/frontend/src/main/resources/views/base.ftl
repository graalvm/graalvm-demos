<html>
<head>
   <title>Graal-Todos</title>
</head>
<body>
<div>
   <h2>Todos</h2>
    <div>
        <form action="/todos" method="post">
            <table>
                <tr>
                    <td><input name="content"/></td>
                    <td><button>Add</button></td>
                </tr>
            </table>
        </form>
    </div>
   <table>
   <#list allTodos as val>
       <tr>
           <td><input type="checkbox" id="${val.id}"/>${val.content}</td>
       </tr>
   </#list>
   </table>
</div>
</body>
</html>