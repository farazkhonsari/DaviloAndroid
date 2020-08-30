
AppClient.random = function(){
  if(arguments.length > 0) return Math.floor(Math.random()*arguments[0]);
  return Math.random();
}

var completed = false;
function checkCompletion (){
    console.log("inja1")
 if(AppClient!=null && AppClient.isSolved && !completed ){
     console.log("inja2")
     completed=true;
     androidApp.appDone()
 }
}

setInterval(checkCompletion,500);

function setReadyTrigger(){
console.log("inja3")
  if(!AppClientConnection) {
    setTimeout(setReadyTrigger,100);
    return;
  }
  AppClient.postMessage("AppReady");
  AppClient.postMessage("AppTool|"+AppClient.Tool);
  AppClient.postMessage("AppTask|"+AppClient.Task);
  AppClient.postMessage("AppTitle|"+AppClient.Title);
  AppClient.postMessage("AppFeedback|"+AppClient.getParameter("feedback"));
}

setTimeout(setReadyTrigger,100);