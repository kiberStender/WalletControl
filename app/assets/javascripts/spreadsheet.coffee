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
  <td>#{saldo}</td>
</tr>"""

formatBody = (saldo, body) -> body.foldLeft([saldo, ""]) (acc) -> (item) ->
  {itemId, description, purchaseDate, trtType, value} = item
  [oSaldo, trs] = acc
  nSaldo = oSaldo + value

  inOrOut = -> if value > 0 then "<td>#{value}</td><td>0</td>" else "<td>0</td><td>#{value * -1}</td>"
  drawSaldo = -> if nSaldo > 0 then "<td>#{nSaldo}</td>" else """<td class="negative">#{nSaldo}</td>"""

  [nSaldo, """#{trs}<tr>
    <td class="mdl-data-table__cell--non-numeric">#{formatDate new Date purchaseDate}</td>
    <td>#{description}</td>
    #{inOrOut()}#{drawSaldo()}
  </tr>"""]

formatSpreadsheet = (arr) -> query("#spreadsheet tbody").writeHtml arrayToSeq(arr).foldLeft("") (act) -> (acc) ->
  {description, balances: [balance], items} = acc
  {realbalance} = balance
  [_, body] = formatBody realbalance, arrayToSeq items
  "#{act}#{formatHeader description, realbalance}#{body}"

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