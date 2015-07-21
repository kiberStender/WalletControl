package br.com.wallet.types.token

/**
 * Created by sirkleber on 21/07/15.
 */
case class OauthToken(accessToken: String, tokenType: String, expiresIn: Int, idToken: String, refreshToken: String)
