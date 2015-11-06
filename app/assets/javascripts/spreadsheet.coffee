do ({DOM, Observable: {fromPromise, fromEvent}} = Rx, {arrayToSeq} = fpJS, {spreadsheet} = Spreadsheet) ->
  whenNotFail = (fn) -> ({failed, description, result}) -> if failed then {failed, description} else fn result
  loadData = (spread) -> DOM.getJSON("/getData").map whenNotFail ({logonData: {accuserid, username, usermail, profilePicture}, state}) ->
    document.querySelector("#logedUserProfile").src = profilePicture
    document.querySelector("#logedUsername").innerHTML = "Hello #{username}"
    document.querySelector("#logedUsermail").innerHTML = usermail

    {result: [spread, accuserid, state]}

  loadItems = whenNotFail ([spread, accuserid, state]) -> DOM.getJSON("/spreadsheet/#{state}/#{accuserid}").map whenNotFail (result) -> {result: [spread, accuserid, state, result]}

  applyNewButton = (dt) -> fromEvent((document.getElementById "newItem"), "click").subscribe -> new ItemInsertDialog(dt).draw()

  addActionToButton = whenNotFail ([spread, accuserid, state, result]) -> applyNewButton.andThen(-> {result: [spread, accuserid, state, result]}) [accuserid, state]

  DOM.ready()
    .map -> spreadsheet("#spreadsheetdiv").render()
    .flatMap(loadData).flatMap(loadItems).map addActionToButton
    .subscribe ({failed, description, result: [spread, accuserid, state, result]}) -> if failed then alert description else spread.withItems(arrayToSeq(result)).render()