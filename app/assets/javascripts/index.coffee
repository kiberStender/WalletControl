{IOPerformer: {main, consoleIO}, get, arrayToSeq, seq} = fpJS

main -> consoleIO(
  get "/auth", true
    .then (x) ->
      capitalize = (str) -> str.charAt(0).toUpperCase() + (str.slice 1).toLowerCase()
      {failed, result, description} = x

      if failed then window.location = "/spreadsheet"
      else document.querySelector(".jumbotron").innerHTML = (((arrayToSeq result).fmap (x) ->
        {provider, authUri} = x
        """
           <p>
             <a class="btn btn-lg btn-social btn-#{provider}" href="#{authUri}">
               <i class="fa fa-#{provider}"></i> Sign in with #{ capitalize provider }
             </a>
           </p>
        """
      ).foldLeft "") (acc) -> (actual) -> acc + actual
    .catch alert
)