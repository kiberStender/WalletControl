{IOPerformer: {main, consoleIO}, query, get, seq, arrayToSeq, webSocket} = fpJS

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
              .onClose (evt) -> evt
              .whenMessageComes (msg, sender) ->
                {response} = JSON.parse msg
                tr = ((arrayToSeq response).flatMap (acc) -> (arrayToSeq acc.items).fmap (item) ->
                  {itemId, description, purchaseDate, trtType} = item
                  """
                  <tr>
                    <td class="mdl-data-table__cell--non-numeric">#{purchaseDate}</td>
                    <td>#{description}</td>
                    <td>0</td>
                    <td>0</td>
                    <td>0</td>
                    <td>#{acc.description}</td>
                  </tr>"""

                ).foldLeft("") (acc) -> (item) -> acc + item
                query("#spreadsheet tbody").writeHtml(tr)

    .catch alert
)