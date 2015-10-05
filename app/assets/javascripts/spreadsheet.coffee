do ({DOM, Observable} = Rx, {arrayToSeq} = fpJS) ->
  formatDate = (dt) ->
    formatDay = (day) -> if day > 9 then day else "0#{day}"
    formatMonth = (month) -> if month > 9 then month else "0#{month}"
    "#{formatDay dt.getDate()}-#{formatMonth dt.getMonth() + 1}-#{dt.getFullYear()}"

  formatHeader = (accname, saldo) -> """<tr>
    <td>#{accname}</td>
    <td></td>
    <td></td>
    <td></td>
    <td>R$ #{parseFloat(saldo).toFixed 2}</td>
  </tr>"""

  formatBody = (saldo, body) -> body.foldLeft([totalEmpty(saldo), ""]) ([{entrada, saida, saldoTotal}, trs]) -> ({itemId, description, purchaseDate, trtType, value}) ->
    nSaldo = saldoTotal + value
    [ent, sai] = if value > 0 then [value, 0.00] else [0.00, value]
    total = -> new Total(entrada + ent, saida + sai, nSaldo)

    drawSaldo = (saldo) -> if saldo > 0 then "<td>R$ #{parseFloat(saldo).toFixed 2}</td>"
    else """<td class="negative">R$ #{parseFloat(saldo).toFixed 2}</td>"""

    [total(), """#{trs}<tr>
      <td class="mdl-data-table__cell--non-numeric">#{formatDate new Date purchaseDate}</td>
      <td>#{description}</td>
      <td>R$ #{parseFloat(ent).toFixed 2}</td>
      <td>R$ #{parseFloat(sai).toFixed 2}</td>
      #{drawSaldo nSaldo}
    </tr>"""]

  formatSpreadsheet = (arr) -> arrayToSeq(arr).foldLeft("") (act) -> ({description, balances: [{realbalance}], items}) ->
    [foot, body] = formatBody realbalance, arrayToSeq items
    "#{act}#{formatHeader description, realbalance}#{body}#{foot.toString()}"

  getUserData = ({failed, description, result}) -> if failed then Obervable.fromPromise Promise.resolve {failed, description}
  else
    {logonData: {accuserid, username, usermail, profilePicture}, state} = result

    document.querySelector("#logedUserProfile").src = profilePicture
    document.querySelector("#logedUsername").innerHTML = "Hello #{username}"
    document.querySelector("#logedUsermail").innerHTML = usermail

    DOM.getJSON "/spreadsheet/#{state}/#{accuserid}"

  DOM.ready()
    .flatMap -> DOM.getJSON "/getData"
    .flatMap getUserData
    .subscribe ({failed, description, result}) -> if failed then alert description else formatSpreadsheet result