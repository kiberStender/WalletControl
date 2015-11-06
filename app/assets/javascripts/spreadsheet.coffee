do ({DOM, Observable: {fromPromise, fromEvent}} = Rx, {arrayToSeq} = fpJS, {spreadsheet} = Spreadsheet) ->
  applyNewButton = (dt) -> fromEvent((document.getElementById "newItem"), "click").subscribe -> new ItemInsertDialog(dt).draw()

  DOM.ready()
    .map -> spreadsheet("#spreadsheetdiv").render()
    .flatMap((spread) -> DOM.getJSON("/getData").map ({failed, description, result}) ->
      if failed then {failed, description}
      else
        {logonData: {accuserid, username, usermail, profilePicture}, state} = result

        document.querySelector("#logedUserProfile").src = profilePicture
        document.querySelector("#logedUsername").innerHTML = "Hello #{username}"
        document.querySelector("#logedUsermail").innerHTML = usermail

        {result: [spread, accuserid, state]}
    ).flatMap(({failed, description, result: [spread, accuserid, state]}) ->
      if failed then {failed, description}
      else DOM.getJSON("/spreadsheet/#{state}/#{accuserid}").map ({failed, description, result}) -> if failed then {failed, description}
      else {result: [spread, accuserid, state, result]}
    ).map ({failed, description, result: [spread, accuserid, state, result]}) -> applyNewButton.andThen(-> {failed, description, result: [spread, accuserid, state, result]}) [accuserid, state]
    .subscribe ({failed, description, result: [spread, accuserid, state, result]}) -> if failed then alert description else spread.withItems(arrayToSeq(result)).render()