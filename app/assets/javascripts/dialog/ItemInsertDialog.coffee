do (InsertDialog, Item, {right, left} = fpJS, $ = jQuery) ->
  class ItemInsertDialog extends InsertDialog then constructor: ([state, accuserid]) ->
    form = -> """<div>
      <form id="itemForm">
         <div class="row">
           <div class="col-xs-4">
             <div class="input-group">
               <span class="input-group-addon"><span class="glyphicon glyphicon-pencil"></span></span>
               <input id="desc" type="text" class="form-control" autofocus placeholder="Descrição">
             </div>
           </div>

           <div class="col-xs-4 selectContainer">
             <select class="form-control" name="size" id="itemType">
               <option value="out">Saída</option>
               <option value="in">Entrada</option>
             </select>
           </div>

           <div class="col-xs-4">
             <div class="input-group">
               <span class="input-group-addon">R$</span>
                 <input id="value" type="text" class="form-control" placeholder="Valor">
                 <span class="input-group-addon">.00</span>
               </div>
             </div>
         </div>

         <br/>

         <div class="row">
           <div class="col-xs-4">
             <div class="input-group">
               <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
               <input id="purchaseDate" type="date" class="form-control">
             </div>
           </div>
         </div>
      </form>
    </div>"""

    validateItem = ([desc, type, value, purchaseDt]) -> if desc is "" then left "Descrição não pode ser vazia"
    else if value <= 0 then left "Valor não pode ser 0 ou menor"
    else if (new Date purchaseDt).toString() is "Invalid Date" then left "Data inválida"
    else right new Item "", desc, (if type is "in" then value else -value), purchaseDate.value, "id00"

    formToItem = ->
      desc = -> document.querySelector("#desc").value
      itemType = -> document.querySelector("#itemType").value
      value = -> parseFloat document.querySelector("#value").value
      purchaseDate = -> document.querySelector("#purchaseDate").value

      validateItem [desc(), itemType(), value(), purchaseDate()]

    callback = -> do (item = formToItem()) -> if item.isRight()
      Rx.Observable.defer(-> $.ajax({
        url: "/spreadsheet/#{state}/#{accuserid}"
        type: "POST", data: JSON.stringify(item.value()), contentType: "application/json;charset=utf-8"
      })).subscribe ({failed, description, result}) -> alert if failed then description else "Item inserido"
    else alert item.value()

    super form, callback, "Novo Item"

  root = exports ? window
  root.ItemInsertDialog = ItemInsertDialog