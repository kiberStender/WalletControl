class SpreadsheetRequest extends AbstractRequest then constructor: (body) -> super "spreadsheet", body

class SpreadsheetRequestGet extends SpreadsheetRequest then constructor: -> super {method: "get"}

class SpreadsheetRequestPut extends SpreadsheetRequest then constructor: (data) -> super {method: "put", data}

class SpreadsheetRequestDelete extends SpreadsheetRequest then constructor: (data) -> super {method: "delete", data}

class SpreadsheetRequestUpdate extends SpreadsheetRequest then constructor: (data) -> super {method: "update", data}

root = exports ? window
root.SpreadsheetRequestGet = SpreadsheetRequestGet
root.SpreadsheetRequestPut = SpreadsheetRequestPut
root.SpreadsheetRequestDelete = SpreadsheetRequestDelete
root.SpreadsheetRequestUpdate = SpreadsheetRequestUpdate