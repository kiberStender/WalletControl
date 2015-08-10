class AbstractRequest
  constructor: (@requestType) ->
  stringify: -> JSON.stringify @
