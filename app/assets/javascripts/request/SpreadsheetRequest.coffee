class SpreadsheetRequest extends AbstractRequest then constructor: (body) -> super "spreadsheet", body

class SpreadsheetRequestGet extends SpredsheetRequest then constructor: -> super {method: "get"}

class SpreadsheetRequestPut extends SpredsheetRequest then constructor: (data) -> super {method: "put", data}

class SpreadsheetRequestDelete extends SpredsheetRequest then constructor: (data) -> super {method: "delete", data}

class SpreadsheetRequestUpdate extends SpredsheetRequest then constructor: (data) -> super {method: "update", data}

root = exports ? window
root.SpreadsheetRequest = SpreadsheetRequest