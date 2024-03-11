package com.royalenfield.configuration

import com.bosch.softtec.components.nephele.BackendConfiguration
import com.bosch.softtec.components.nephele.CloudConfiguration

import com.royalenfield.reprime.R
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.utils.REUtils
import java.net.URL

/**
 * @author Martin Hellwig
 * @since  2019-08-15
 */
/*
//Tenzing Server 1
object CloudConfigurationProvider {
    private const val BASE_URL = "https://ccf-oauth.auth.eu-central-1.amazoncognito.com"


    private val oauthConfiguration = OauthConfiguration(
        oauthDomain = "ccf-oauth.auth.eu-central-1.amazoncognito.com",
        authorizationUrl = "$BASE_URL/oauth2/authorize",
        accessTokenUrl = "$BASE_URL/oauth2/token",
        clientId = "4gro7gc4josvq6ug49tk9lftlk",
        clientSecret = "ksmiq176v52ao3cvrs7tovcggcpj2a237vc74k79g6us8gf27lc",
        scope = "trips/access",
        scheme = "midgard",
        host = "www.tenzing.com",
        signInPath = "/signin",
        signOutPath = "/signout"
    )

    private val backendConfiguration = BackendConfiguration(
        backendUrl = "https://tenzing.test.msmicro.ccf-services-001.de"
//        backendUrl = "http://52.172.217.220:5160/"
    )
    val cloudConfiguration = CloudConfiguration(oauthConfiguration, backendConfiguration)
}*/
/*
//Tenzing Server 2
object CloudConfigurationProvider {

    private val oauthServerUrl = URL("https://ccf-oauth.auth.eu-central-1.amazoncognito.com")
    private val backendServerUrl = URL("https://tenzing.test.msmicro.ccf-services-001.de")

    private val oauthConfiguration = OauthConfiguration(
             oauthDomain = oauthServerUrl.host,
    authorizationUrl = "$oauthServerUrl/oauth2/authorize",
    accessTokenUrl = "$oauthServerUrl/oauth2/token",
    clientId = "7ulaluek550ndeo4e0uifs9dsv",
    clientSecret = "1rpmmav1r3om5j9hqour54oj5k7fjn7hevp3c8dskr6kso40ij8q",
    scope = "openid,trips/access",
    scheme = "midgard",
    host = "www.tenzing.com",
    signInPath = "/signin",
    signOutPath = "/signout"
    )

    private val backendConfiguration = BackendConfiguration(
            backendUrl = backendServerUrl.toString()
    )

    val cloudConfiguration = CloudConfiguration(oauthConfiguration, backendConfiguration)
}*/

//RE Build Configuration
object CloudConfigurationProvider {
    private const val BASE_URL = "https://ccf-oauth.auth.eu-central-1.amazoncognito.com"
/*
    private val oauthConfiguration = OauthConfiguration(
            oauthDomain = "ccf-oauth.auth.eu-central-1.amazoncognito.com",
            authorizationUrl = "$BASE_URL/oauth2/authorize",
            accessTokenUrl = "$BASE_URL/oauth2/token",
            clientId = "72h27fbqm25898l4fkrn3c04tl",
            clientSecret = "oj76l92cbp5e8kc9vlrl2hrsrsd76j22ogmt78qg54o10pen6pe",
            scope = "trips/access",
            scheme = "midgard",
            host = "www.tenzing.com",
            signInPath = "/signin",
            signOutPath = "/signout"
    )*/

    private val backendConfiguration = BackendConfiguration(
            //backendUrl = "https://tenzing.test.msmicro.ccf-services-001.de"
            backendUrl = REUtils.getMobileappbaseURLs().getBctURL()
    )
    val cloudConfiguration = CloudConfiguration( backendConfiguration)
}
