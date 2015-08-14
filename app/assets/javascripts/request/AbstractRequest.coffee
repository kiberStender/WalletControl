class AbstractRequest
  constructor: (@requestType, @userid, @body = {}) ->
  stringify: -> JSON.stringify @

root = exports ? window
root.AbstractRequest = AbstractRequest