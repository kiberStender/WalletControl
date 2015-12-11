do ({Ordering} = fpJS.withFnExtension()) ->
  class Item extends Ordering then constructor: (@itemId, @description, @value, @purchaseDate, @trtType) ->
    @toString = -> JSON.stringify @

    toDate = (str) -> new Date(str)

    fixTo2 = (n) -> parseFloat(n).toFixed 2

    @compare = (item) ->
      this_dt = toDate(purchaseDate).getTime()
      that_dt = toDate(item.purchaseDate).getTime()
      if this_dt is that_dt then 0 else (if this_dt < that_dt then -1 else 1)

    formatDate = (dt) ->
        formatDay = (day) -> if day > 9 then day else "0#{day}"
        formatMonth = (month) -> if month > 9 then month else "0#{month}"
        "#{formatDay dt.getDate() + 1}-#{formatMonth dt.getMonth() + 1}-#{dt.getFullYear()}"

    @draw = (saldo) ->
      [ent, sai] = if value > 0 then [value, 0.00] else [0.00, value]

      drawSaldo = (saldo) -> if saldo > 0 then "<td>R$ #{fixTo2 saldo}</td>" else """<td class="negative">R$ #{fixTo2 saldo}</td>"""

      """<tr id="item_#{itemId}">
        <td class="mdl-data-table__cell--non-numeric">#{toDate.andThen(formatDate) purchaseDate}</td>
        <td>#{description}</td>
        <td>R$ #{fixTo2 ent}</td>
        <td>R$ #{fixTo2 sai}</td>
        #{drawSaldo saldo + value}
      </tr>"""

  item = ({itemId, description, purchaseDate, trtType, value}) -> new Item itemId, description, value, purchaseDate, trtType

  root = exports ? window
  root.Item = Item
  root.Item.item = item