do (bootbox, {Observable: {fromPromise, fromEvent}} = Rx, {left} = fpJS) ->
  class InsertDialog then constructor: (modalEl, callback) ->
    send_ = null
    cancel_ = null
    close_ = null

    select = (el) -> document.querySelector el

    send = -> if send_ is null then send_ = select "#insertItem" else send_
    cancel = -> if cancel_ is null then cancel_ = select "#notInsertItem" else cancel_
    close = -> if close_ is null then close_ = select "#closeBtn" else close_

    #Listeners
    sendListener = => do (item = @isValid()) -> if item.isRight()
      (-> $(modalEl).modal "hide").andThen(-> callback item)()
    else fromPromise Promise.resolve item

    cancelListener = (-> $(modalEl).modal "hide").andThen(-> left "")

    #Events
    sendEvent = (-> send().removeEventListener("click", sendListener)).andThen -> fromEvent(send(), "click").flatMap sendListener

    cancelEvent = (-> cancel().removeEventListener("click", cancelListener)).andThen -> fromEvent(cancel(), "click").map cancelListener

    closeEvent = (-> close().removeEventListener("click", cancelListener)).andThen -> fromEvent(close(), "click").map cancelListener

    @draw = -> fromPromise(Promise.resolve $(modalEl).modal "show").merge(cancelEvent()).merge(sendEvent()).merge(closeEvent())

  root = exports ? window
  root.InsertDialog = InsertDialog