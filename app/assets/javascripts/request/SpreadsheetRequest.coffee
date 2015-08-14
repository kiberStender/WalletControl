class SpreadsheetRequest extends AbstractRequest then constructor: (userid, body) -> super "spreadsheet", userid, body

class SpreadsheetRequestGet extends SpreadsheetRequest then constructor: (userid) -> super userid, {method: "get"}

class SpreadsheetRequestPut extends SpreadsheetRequest then constructor: (userid, data) -> super userid, {method: "put", data}

class SpreadsheetRequestDelete extends SpreadsheetRequest then constructor: (userid, data) -> super userid, {method: "delete", data}

class SpreadsheetRequestUpdate extends SpreadsheetRequest then constructor: (userid, data) -> super userid, {method: "update", data}

root = exports ? window
root.SpreadsheetRequestGet = SpreadsheetRequestGet
root.SpreadsheetRequestPut = SpreadsheetRequestPut
root.SpreadsheetRequestDelete = SpreadsheetRequestDelete
root.SpreadsheetRequestUpdate = SpreadsheetRequestUpdate