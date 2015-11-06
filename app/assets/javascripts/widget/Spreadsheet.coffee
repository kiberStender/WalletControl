do ({seq, arrayToSeq} = fpJS) ->
  class Spreadsheet then constructor: (el, placed, rows = seq()) ->
    base = -> """<table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp full-width" id="spreadsheet">
                      <thead>
                        <tr>
                          <th class="mdl-data-table__cell--non-numeric">Data</th>
                          <th>Descrição</th>
                          <th>Entradas</th>
                          <th>Saídas</th>
                          <th>Saldo</th>
                        </tr>
                      </thead>
                      <tbody></tbody>
                      <tfoot>
                        <tr>
                          <td colspan="2">Totais</td>
                          <td>R$</td>
                          <td>R$</td>
                          <td>R$</td>
                        </tr>
                      </tfoot>
                    </table>"""

    formatHeader = ([accname, saldo]) -> """<tr>
        <td>#{accname}</td>
        <td></td>
        <td></td>
        <td></td>
        <td>R$ #{parseFloat(saldo).toFixed 2}</td>
      </tr>"""

    formatBody = ([saldo, body]) -> body.foldLeft([totalEmpty(saldo), ""]) ([{entrada, saida, saldoTotal}, trs]) -> (item) ->
        [ent, sai] = if item.value > 0 then [item.value, 0.00] else [0.00, item.value]
        total = -> new Total(entrada + ent, saida + sai, saldoTotal + item.value)
        [total(), "#{trs}#{item.draw saldoTotal}"]

    formatSpreadsheet = (arr) -> arr.foldLeft("") (act) -> ({description, balances: [{realbalance}], items}) ->
        [foot, body] = formatBody [realbalance, arrayToSeq(items).fmap Item.item]
        "#{act}#{formatHeader [description, realbalance]}#{body}#{foot}"

    renderNotPlaced = -> document.querySelector(el).innerHTML = base()
    renderPlaced = -> document.querySelector("#spreadsheet tbody").innerHTML = formatSpreadsheet rows

    render_ = -> if placed then renderPlaced() else renderNotPlaced()

    @render = -> render_.andThen(-> new Spreadsheet el, true, rows)()

    @withItems = (nRows) -> new Spreadsheet(el, placed, rows.concat nRows)

  root = exports ? window
  root.Spreadsheet = Spreadsheet
  root.Spreadsheet.spreadsheet = (el) -> new Spreadsheet(el, false)