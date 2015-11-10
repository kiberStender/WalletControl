do ({set, arrayToSeq} = fpJS.withFnExtension()) ->
  class Spreadsheet then constructor: (el, placed, accTypes = set()) ->
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

    renderNotPlaced = -> document.querySelector(el).innerHTML = base()
    renderPlaced = ->
      document.querySelector("#spreadsheet tbody").innerHTML = accTypes.foldLeft("") (acc) -> (acctype) -> acc + acctype.draw()

    render_ = -> if placed then renderPlaced() else renderNotPlaced()

    @render = -> render_.andThen(-> new Spreadsheet el, true, accTypes)()

    @withItems = (nAccTypes) -> new Spreadsheet(el, placed, accTypes.concat nAccTypes)

  root = exports ? window
  root.Spreadsheet = Spreadsheet
  root.Spreadsheet.spreadsheet = (el) -> new Spreadsheet(el, false)