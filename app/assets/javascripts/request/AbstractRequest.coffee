class AbstractRequest
  constructor: (@requestType, @body = "") ->
  stringify: -> JSON.stringify @

root = exports ? window
root.AbstractRequest = AbstractRequest