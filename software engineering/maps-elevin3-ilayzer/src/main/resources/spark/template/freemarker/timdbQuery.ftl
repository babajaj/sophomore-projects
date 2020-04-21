<#assign content>

  <div id="topLeftLinkBox"> <a href="/stars" id = "topLeftLink"> To Stars Webpage </a> </div>

  <h1> Connect Actors </h1>

  <h2> <div id="explanation"> Find the Connection Between Two Actors! </br> Please enter two actor names (with quotes) </div>  </h2>


  <form method="GET" action="/connect">
    <textarea name="actor1" id="text" placeholder="Starting Actor"></textarea><br>
    <textarea name="actor2" id="text" placeholder="Ending Actor"></textarea><br>
    <input type="submit" id="button", value="connect">
  </form>

    <div id="errorMessage">${errorMessage}</div>

    <ol>
  <#list connectOutput as line>
  <li class="outputList">
      <a href="/actor/${line[0]}" class="link"> ${line[1]}</a> ->
      <a href="/actor/${line[2]}" class="link"> ${line[3]}</a> :
      <a href="/movie/${line[4]}" class="link"> ${line[5]}</a>
  </li>
  </#list>
    </ol>

  <ol>
    <#list noConnection as el>
      <li class="outputList">
        <a href="/actor/${el[0]}"class="link"> ${el[1]}</a> -/-
        <a href="/actor/${el[2]}"class="link"> ${el[3]}</a>
      </li>

    </#list>
  </ol>



</#assign>
<#include "mainTimdb.ftl">

