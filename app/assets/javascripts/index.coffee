{IOPerformer: {main, alertIO, consoleIO}, get, post, del, put, seq} = fpJS

main -> consoleIO(
  capitalize = (str) -> str.charAt(0).toUpperCase() + (str.slice 1).toLowerCase()
  get "/auth", true
    .then (x) ->
      {failed, result, description} = x
      if failed then window.location = "/spreadsheet"
      else document.querySelector(".jumbotron").innerHTML = ((seq.apply(@, result).fmap (x) ->
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