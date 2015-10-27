do ({DOM, Observable: {fromPromise, fromEvent}} = Rx, {arrayToSeq} = fpJS, bootbox = bootbox) ->
  formatHeader = ([accname, saldo]) -> """<tr>
    <td>#{accname}</td>
    <td></td>
    <td></td>
    <td></td>
    <td>R$ #{parseFloat(saldo).toFixed 2}</td>
  </tr>"""

  formatBody = ([saldo, body]) -> body.foldLeft([totalEmpty(saldo), ""]) ([{entrada, saida, saldoTotal}, trs]) -> (item) ->
    nSaldo = saldoTotal + value
    total = -> new Total(entrada + ent, saida + sai, nSaldo)
    [total(), "#{trs}#{item.draw nSaldo}"]

  formatSpreadsheet = (arr) -> arrayToSeq(arr).foldLeft("") (act) -> ({description, balances: [{realbalance}], items}) ->
    [foot, body] = formatBody [realbalance, arrayToSeq(items).fmap Item.item]
    "#{act}#{formatHeader [description, realbalance]}#{body}#{foot}"

  getUserData = ({failed, description, result}) -> if failed then Obervable.fromPromise Promise.resolve {failed, description}
  else
    {logonData: {accuserid, username, usermail, profilePicture}, state} = result

    document.querySelector("#logedUserProfile").src = profilePicture
    document.querySelector("#logedUsername").innerHTML = "Hello #{username}"
    document.querySelector("#logedUsermail").innerHTML = usermail

    (DOM.getJSON "/spreadsheet/#{state}/#{accuserid}").flatMap (json) -> if json.failed
      fromPromise Promise.resolve json
    else
      fromEvent((document.getElementById "newItem"), "click").subscribe -> new ItemInsertDialog([state, accuserid]).draw()
      fromPromise Promise.resolve json

  DOM.ready()
    .flatMap -> DOM.getJSON "/getData"
    .flatMap getUserData
    .subscribe ({failed, description, result}) -> if failed then alert description
    else document.querySelector("#spreadsheet tbody").innerHTML = formatSpreadsheet result