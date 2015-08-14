class SpreadsheetRequest extends AbstractRequest then constructor: (userid, method, data) -> super "spreadsheet", {userid, method, data}

class SpreadsheetRequestGet extends SpreadsheetRequest then constructor: (userid) -> super userid, "get", {}

class SpreadsheetRequestPut extends SpreadsheetRequest then constructor: (userid, data) -> super userid, "put", data

class SpreadsheetRequestDelete extends SpreadsheetRequest then constructor: (userid, data) -> super userid, "delete", data

class SpreadsheetRequestUpdate extends SpreadsheetRequest then constructor: (userid, data) -> super userid, "update", data

root = exports ? window
root.SpreadsheetRequestGet = SpreadsheetRequestGet
root.SpreadsheetRequestPut = SpreadsheetRequestPut
root.SpreadsheetRequestDelete = SpreadsheetRequestDelete
root.SpreadsheetRequestUpdate = SpreadsheetRequestUpdate