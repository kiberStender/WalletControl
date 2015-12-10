do (bootbox, {Observable: {fromPromise, fromEvent}} = Rx, {right, left} = fpJS) ->
  class InsertDialog then constructor: (modalEl, callback) ->
    send_ = null
    cancel_ = null

    select = (el) -> document.querySelector el

    send = -> if send_ is null then send_ = select "#insertItem" else send_
    cancel = -> if cancel_ is null then cancel_ = select "#notInsertItem, #closeBtn" else cancel_

    sendEvent = => fromEvent(send(), "click").flatMap => do (item = @isValid()) -> if item.isRight()
      (-> $(modalEl).modal("hide")).andThen( -> callback item)()
    else fromPromise Promise.resolve item

    cancelEvent = -> fromEvent(cancel(), "click").map(-> left "")

    @draw = -> fromPromise(Promise.resolve $(modalEl).modal "show").merge(cancelEvent()).merge(sendEvent()).scan (acc, x) -> x

  root = exports ? window
  root.InsertDialog = InsertDialog