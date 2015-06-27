{IOPerformer: {main, alertIO, consoleIO}, get, post, del, put} = fpJS

main -> consoleIO(
  get "/auth", true
    .then (x) ->
      {logged, desc} = x
      window.location =  = if logged then "/spreadsheet" else desc
    .catch alert
)