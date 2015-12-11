do (
  {DOM, Observable: {fromPromise, fromEvent}} = Rx, {arrayToSeq, set, Either} = fpJS.withFnExtension(),
  {spreadsheet} = Spreadsheet, {itemInsertDialog} = ItemInsertDialog
) ->
  whenNotFail = (fn) -> ({failed, description, result}) -> if failed then {failed, description} else fn result

  select = (el) -> document.querySelector el

  logoutButton = fromEvent((select "#logoutItem"), "click").map -> location.href = "/logout"

  newItemButton = document.querySelector "#newItem"

  loadData = (spread) -> DOM.getJSON("/getData").map whenNotFail ({logonData: {accuserid, username, usermail, profilePicture}, state}) ->
    (-> select("#logedUserProfile").src = profilePicture).andThen -> select("#logedUsername").innerHTML = "Hello #{username}"
      .andThen(-> select("#logedUsermail").innerHTML = usermail).andThen -> newItemButton.name = "#{accuserid}_#{state}"
      .andThen(-> {result: [spread, accuserid, state]})()

  loadItems = whenNotFail ([spread, accuserid, state]) ->
    DOM.getJSON("/spreadsheet/#{state}/#{accuserid}").map whenNotFail (result) -> {result: [spread, accuserid, state, result]}

  render = ({failed, description, result: [spread, accuserid, state, result]}) ->
    if failed then description else spread.withAccTypes(arrayToSeq(result).fmap(AccountType.accountType).toSet()).render()

  newButtonListener = -> itemInsertDialog(newItemButton.name.split "_").draw()

  newButtonObs = (-> newItemButton.removeEventListener("click", newButtonListener)).andThen -> fromEvent(newItemButton, "click").flatMap newButtonListener

  scan_ = (acc, x, i, src) -> if not (x instanceof Either) then acc
  else
    if x.isRight() then acc.withAccType(x.value()).render()
    else (-> alert x.value() if x.value() isnt "").andThen(-> acc)()

  DOM.ready()
    .map spreadsheet("#spreadsheetdiv").render
    .merge logoutButton
    .flatMap loadData
    .flatMap loadItems
    .map render
    .merge newButtonObs()
    .scan scan_
    .subscribe(
      (x) -> console.log x
      (err) -> console.log "Something wrong: #{err}"
      -> console.log "Completed"
    )