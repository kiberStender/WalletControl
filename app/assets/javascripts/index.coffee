{IOPerformer: {main, alertIO, consoleIO}, get, post, del, put, seq} = fpJS

main -> consoleIO(
  get "/auth", true
    .then (x) ->
      {failed, result, description} = x
      if failed then window.location = "/spreadsheet"
      else
        document.querySelector("#loginTypes").innerHTML = ((seq.apply(@, result).fmap (x) ->
          {provider, authUri} = x
          """<a href="#{authUri}">#{provider}</a><br/>"""
        ).foldLeft "") (acc) -> (actual) -> acc + actual
    .catch alert
)