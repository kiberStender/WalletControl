do ({set, arrayToSeq} = fpJS.withFnExtension()) ->
  class Spreadsheet then constructor: (el, placed, accTypes = set()) ->
    select = (el) -> document.querySelector el

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
                      <tbody>#{accTypes.foldLeft("") (acc) -> (acctype) -> acc + acctype.draw()}</tbody>
                      <tfoot>
                        <tr>
                          <td colspan="2">Totais</td>
                          <td>R$</td>
                          <td>R$</td>
                          <td>R$</td>
                        </tr>
                      </tfoot>
                    </table>"""

    renderNotPlaced = -> select(el).innerHTML = base()

    @render = renderNotPlaced.andThen -> new Spreadsheet el, true, accTypes

    @withAccTypes = (nAccTypes) -> new Spreadsheet(el, placed, accTypes.concat nAccTypes)

    @withAccType = (accType) ->
      new Spreadsheet el, placed, accTypes.fmap (acc) -> if acc.equals accType then acc.withItems accType.items else acc

    @toString = -> accTypes.toString()

  root = exports ? window
  root.Spreadsheet = Spreadsheet
  root.Spreadsheet.spreadsheet = (el) -> new Spreadsheet(el, false)