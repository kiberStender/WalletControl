{IOPerformer: {main, alertIO, consoleIO}, get, post, del, put, seq} = fpJS

main -> consoleIO(
  get "/auth", true
    .then (x) ->
      {failed, result, description} = x
      if failed then window.location = "/spreadsheet"
      else document.querySelector("#loginTypes").innerHTML = ((seq.apply(@, result).fmap (x) ->
        {provider, authUri} = x
        """
          <div class="mdl-card mdl-shadow--2dp demo-card-wide">
            <div class="mdl-card__title #{provider}-type">
                <h2 class="mdl-card__title-text">#{provider}</h2>
            </div>
            <div class="mdl-card__supporting-text">
              Faça login usando o #{provider}
            </div>
            <div class="mdl-card__actions mdl-card--border">
              <a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect" href="#{authUri}">
                Login
              </a>
            </div>
          </div>
        """
      ).foldLeft "") (acc) -> (actual) -> acc + actual
    .catch alert
)