class AbstractRequest
  constructor: (@requestType) ->
  stringify: -> JSON.stringify @

root = exports ? window
root.AbstractRequest = AbstractRequest