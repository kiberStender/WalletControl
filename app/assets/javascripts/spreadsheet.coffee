{IOPerformer: {main, consoleIO}, get, seq} = fpJS

main -> consoleIO (
  get "/getData", true
    .then (json) ->
      {failed, description, result} = json

      if failed then alert description
      else
        {logonData: {username, usermail, profilePicture}} = result
        document.querySelector("#logedUserProfile").src = profilePicture
        document.querySelector("#logedUsername").innerHTML = "Hello #{username}"
        document.querySelector("#logedUsermail").innerHTML = "Hello #{usermail}"
    .catch alert
)