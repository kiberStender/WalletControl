{arrayToSeq, seq} = fpJS

capitalize = (str) -> str.charAt(0).toUpperCase() + (str.slice 1).toLowerCase()

Rx.Observable.fromEvent(document, 'DOMContentLoaded')
  .flatMap Rx.Observable.fromPromise $.ajax({url: "/auth", method: "GET"}).promise()
  .subscribe ({failed, result, description}) -> if failed then window.location = "/spreadsheet"
  else document.querySelector(".jumbotron").innerHTML = (arrayToSeq result
    .fmap ({provider, authUri}) -> """<p>
      <a class="btn btn-lg btn-social btn-#{provider}" href="#{authUri}">
        <i class="fa fa-#{provider}"></i> Sign in with #{ capitalize provider }
      </a>
    </p>"""
    .foldLeft("") (acc) -> (actual) -> acc + actual)