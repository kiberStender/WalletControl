{IOPerformer: {main, alertIO, consoleIO}, get, post, del, put} = fpJS

main -> consoleIO(
  get "/auth", true
    .then (x) ->
      {failed, result, description} = x
      window.location = if not failed then "/spreadsheet" else result
    .catch alert
)