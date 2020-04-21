<#assign content>

  <div id="topLeftLinkBox"> <a href="/timdb" id="topLeftLink"> To Timdb Webpage</a> </div>

  <h1> Query </h1>

  <h2> <div id="div1"> by NEIGHBORS </div>  </h2>

  <div id="blackbox"> <p class="explanation"> Find the closest Neighbors of a star, or the closest stars to a point in space! </p> </div>

  <form method="GET" action="/neighbors">

    <div id="div2">
    <label for="text">
    <p class = instruction>

    Please enter the number of closest neighbors you would like to find followed by
    the <br/> coordinates that you would like to search around, or the name of a star you would like<br/> to search
    around surrounded by quotes. Example: 10 5 4 3 or 10 "Sol"

    </p> </label></div>
    <textarea name="text" id="text"></textarea><br>
    <input type="submit" id="button">
  </form>
  <div id="blackbox"> <p id="output">${outputNeighbor} </p> </div>

  <br/><br>

  <h2> <div id="div1"> by RADIUS </h2>
  <div id="blackbox"> <p class="explanation"> Find all stars within the given radius from a point in space or a star! </p> </div>
  <form method="GET" action="/radius">


    <div id="div2">
    <label for="text" >

      <p class = instruction>

      Please enter the radius within which you would like to search, followed
      by the coordinates <br>you would like to search around or the name of the
      star you would
      like to search around in <br> quotes. Example: 50.5 0 1 2 or 50.5 "Sol"

      </p> </label>  </div>

    <textarea name="text" id="text"></textarea><br>
    <input type="submit" id = "button">
  </form>
  <div id="blackbox"> <p id="output"> ${outputRadius} </p> </div>

</#assign>
<#include "mainStars.ftl">

