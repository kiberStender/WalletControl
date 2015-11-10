do ({Ordering, arrayToSeq, set} = fpJS.withFnExtension(), Item) ->
  class AccountType extends Ordering then constructor: (@accountTypeId, @accName, @description, @closingDay, @items = set(), @balances) ->
    @compare = (accType) -> accName.compare acctype.accName

    header = ->

    @draw = ->

  accountType = ({accountTypeId, accName, description, closingDay, items, balances}) ->
    new AccountType accountTypeId, accName, description, closingDay, ArrayToSe(items).fmap(Item.item).toSet(), balances

  root = exports ? window
  root.AccountType = AccountType
  root.AccountType.accountType = accountType