class Item then constructor: (itemId, description, value, purchaseDate, trtType) ->
  formatDate = (dt) ->
      formatDay = (day) -> if day > 9 then day else "0#{day}"
      formatMonth = (month) -> if month > 9 then month else "0#{month}"
      "#{formatDay dt.getDate()}-#{formatMonth dt.getMonth() + 1}-#{dt.getFullYear()}"

  fixTo2 = (n) -> parseFloat(n).toFixed 2

  @draw = (saldo) ->
    [ent, sai] = if value > 0 then [value, 0.00] else [0.00, value]

    drawSaldo = (saldo) -> if saldo > 0 then "<td>R$ #{fixTo2 saldo}</td>" else """<td class="negative">R$ #{fixTo2 saldo}</td>"""

    """<tr id="item_#{itemId}">
      <td class="mdl-data-table__cell--non-numeric">#{formatDate new Date purchaseDate}</td>
      <td>#{description}</td>
      <td>R$ #{fixTo2 ent}</td>
      <td>R$ #{fixTo2 sai}</td>
      #{drawSaldo saldo + value}
    </tr>"""

root = exports ? window
root.Item = Item