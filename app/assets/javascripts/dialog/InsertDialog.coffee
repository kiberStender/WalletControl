do (bootbox, {Observable} = Rx) ->
  class InsertDialog then constructor: (form, callback, title, postLoad = ->) ->

    @draw = -> bootbox.dialog {
      message: """<script type="text/javascript">Rx.DOM.ready().subscribe(#{postLoad});</script>#{form()}"""
      title: title
      buttons: {
        send: {
          label: "Inserir"
          className: "btn btn-primary"
          callback
        }
        cancel: {
          label: "Cancelar"
          className: "btn btn-warning"
          callback: ->
        }
      }
    }

  root = exports ? window
  root.InsertDialog = InsertDialog