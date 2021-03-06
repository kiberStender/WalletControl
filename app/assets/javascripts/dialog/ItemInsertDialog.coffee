do (InsertDialog, Item, {right, left, seq} = fpJS, $ = jQuery) ->
  class ItemInsertDialog extends InsertDialog then constructor: ([accuserid, state]) ->

    validateItem = ([desc, type, value, purchaseDt]) -> if desc is "" then left "Descrição não pode ser vazia"
    else if value <= 0 then left "Valor não pode ser 0 ou menor"
    else if (new Date purchaseDt).toString() is "Invalid Date" then left "Data inválida"
    else right new Item "", desc, (if type is "in" then value else -value), purchaseDate.value, "id00"

    @isValid = ->
      desc = -> document.querySelector("#desc").value
      itemType = -> document.querySelector("#itemType").value
      value = -> parseFloat document.querySelector("#value").value
      purchaseDate = -> document.querySelector("#purchaseDate").value

      validateItem [desc(), itemType(), value(), purchaseDate()]

    callback = (item) -> Rx.Observable.defer(-> $.ajax({
      url: "/spreadsheet/#{state}/#{accuserid}"
      type: "POST", data: JSON.stringify(item.value()), contentType: "application/json;charset=utf-8"
    })).map ({failed, description, result}) -> if failed then left description
    else right new AccountType "5cfb525a5c5a5dd29beb6f257b6a7f22d157c630", "", "", "", seq(item.value()), []

    super "#insertModal", callback

  itemInsertDialog = (tp) -> new ItemInsertDialog tp

  root = exports ? window
  root.ItemInsertDialog = ItemInsertDialog
  root.ItemInsertDialog.itemInsertDialog = itemInsertDialog