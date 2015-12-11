do ({Ordering, arrayToSeq, set, seq} = fpJS.withFnExtension(), Item) ->
  class AccountType extends Ordering then constructor: (@accountTypeId, @accName, @description, @closingDay, @items, @balances) ->
    #[{realbalance}] = balances

    @compare = (accType) -> if accName is accType.accName then 0 else (if accName < accType.accName then -1 else 1)

    @toString = -> JSON.stringify @

    header = -> do ([{realbalance}] = balances) -> """<tr>
      <td>#{accName}</td>
      <td></td>
      <td></td>
      <td></td>
      <td>R$ #{parseFloat(realbalance).toFixed 2}</td>
    </tr>"""

    foot = ([entrada, saida, saldoTotal]) ->
      saldo = (saldoTotal) -> if saldoTotal > 0 then "<td>R$ #{saldoTotal}</td>" else """<td class="negative">R$ #{saldoTotal}</td>"""
      """<tr></tr><tr>
           <td colspan="2"></td>
           <td>R$ #{parseFloat(entrada).toFixed 2}</td>
           <td>R$ #{parseFloat(saida).toFixed 2}</td>
           #{saldo parseFloat(entrada + saida).toFixed 2}
         </tr>"""

    body = -> do ([{realbalance}] = balances) -> items.foldLeft([[0, 0, realbalance], ""]) ([[entrada, saida, saldoTotal], trs]) -> (item) ->
      [ent, sai] = if item.value > 0 then [item.value, 0.00] else [0.00, item.value]
      total =  [entrada + ent, saida + sai, saldoTotal + item.value]
      [total, "#{trs}#{item.draw saldoTotal}"]

    @draw = ->
      [tfoot, tbody] = body()
      "#{header()}#{tbody}#{foot tfoot}"

    @withItems = (items_ = seq()) ->
      new AccountType accountTypeId, accName, description, closingDay, (items.concat items_), balances

    @withItem = (item) -> new AccountType accountTypeId, accName, description, closingDay, (items.cons item), balances

  accountType = ({accountTypeId, accName, description, closingDay, items, balances}) ->
    items_ = arrayToSeq(items).fmap(Item.item)
    new AccountType accountTypeId, accName, description, closingDay, items_, balances

  root = exports ? window
  root.AccountType = AccountType
  root.AccountType.accountType = accountType