<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../css/normalize.css">
    <link rel="stylesheet" href="../css/html5bp.css">
    <link rel="stylesheet" href="../css/actorMoviePage.css">
    <link rel="stylesheet" href="../css/topLink.css">

</head>
<body id="movieBody">

   <div id="topLeftLinkBox"> <a href="/timdb" id="topLeftLink"> tIMDb Home </a> </div>

    <div id = "pageName"><h1> ${title} </h1></div>


    <div id = "listBox">
        <p>
    <ol id="list" >
         <#list actorList as line>
            <li>
                <a href="/actor/${line[0]}" id="listLink"> ${line[1]}</a>
            </li>

         </#list>
    </ol>
        </p>
    </div>

</body>
</html>