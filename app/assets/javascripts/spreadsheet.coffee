{IOPerformer: {main, consoleIO}, query, get, seq, arrayToSeq, webSocket} = fpJS

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

formatBody = (saldo, body) -> body.foldLeft([totalEmpty(saldo), ""]) (acc) -> (item) ->
  {itemId, description, purchaseDate, trtType, value} = item
  [oTotal, trs] = acc
  {entrada, saida, saldoTotal} = oTotal
  nSaldo = saldoTotal + value
  [ent, sai] = if value > 0 then [value, 0.00] else [0.00, value]
  total = new Total(entrada + ent, saida + sai, nSaldo)

  drawSaldo = (saldo) -> if saldo > 0 then "<td>R$ #{parseFloat(saldo).toFixed 2}</td>"
  else """<td class="negative">R$ #{parseFloat(saldo).toFixed 2}</td>"""

  [total, """#{trs}<tr>
    <td class="mdl-data-table__cell--non-numeric">#{formatDate new Date purchaseDate}</td>
    <td>#{description}</td>
    <td>R$ #{parseFloat(ent).toFixed 2}</td>
    <td>R$ #{parseFloat(sai).toFixed 2}</td>
    #{drawSaldo nSaldo}
  </tr>"""]

formatSpreadsheet = (arr) -> query("#spreadsheet tbody").writeHtml arrayToSeq(arr).foldLeft("") (act) -> (acc) ->
  {description, balances: [balance], items} = acc
  {realbalance} = balance
  [foot, body] = formatBody realbalance, arrayToSeq items
  "#{act}#{formatHeader description, realbalance}#{body}#{foot.toString()}"

main -> consoleIO (
  get "/getData", true
    .then (json) ->
      {failed, description, result} = json

      if failed then alert description
      else
        {logonData: {accuserid, username, usermail, profilePicture}, state} = result

        query("#logedUserProfile").writeSrc(profilePicture).then ->
          query("#logedUsername").writeHtml("Hello #{username}").then ->
            query("#logedUsermail").writeHtml("Hello #{usermail}").then ->
              webSocket "ws://localhost:1234/spreadsheetWs/#{state}"
              .onOpen (evt, sender) -> sender.send new SpreadsheetRequestGet(accuserid).stringify()
              .onClose (evt) -> alert "Closed connection"
              .whenMessageComes (msg, sender) -> formatSpreadsheet (JSON.parse msg).response

    .catch alert
)