function showNeighbors() {
    $("#radiusForm").hide();
    $("location").hide();
    $("#results").hide();
    document.getElementById("rForm").reset();
    document.getElementById("nForm").reset();
    $("#neighborsForm").show();
}

function showRadius() {
    $("#neighborsForm").hide();
    $("location").hide();
    $("#results").hide();
    document.getElementById("rForm").reset();
    document.getElementById("nForm").reset();
    $("#radiusForm").show();
}

function showStarNameNeighbors() {
    $("#coordinatesNeighbors").hide();
    document.getElementById("xN").value = null;
    document.getElementById("yN").value = null;
    document.getElementById("zN").value = null;
    $("#starNameNeighborsDiv").show();
    return false;
}

function showCoordinatesNeighbors() {
    $("#starNameNeighborsDiv").hide();
    document.getElementById("starNameNeighbors").value =  null;
    $("#coordinatesNeighbors").show();
    return false;
}

function showStarNameRadius() {
    $("#coordinatesRadius").hide();
    document.getElementById("xR").value = null;
    document.getElementById("yR").value = null;
    document.getElementById("zR").value = null;
    $("#starNameRadiusDiv").show();
    return false;
}

function showCoordinatesRadius() {
    $("#starNameRadiusDiv").hide();
    document.getElementById("starNameRadius").value =  null;
    $("#coordinatesRadius").show();
    return false;
}