package id.scode.kadeooredoo.data.db.network

import android.net.Uri
import id.scode.kadeooredoo.BuildConfig

/**
 * @Authors scode | Yogi Arif Widodo
 * Created on 04 11/4/19 7:06 AM 2019
 * id.scode.kadeooredoo.data.db.network
 * https://github.com/yogithesymbian
 * Android Studio 3.5.1
 * Build #AI-191.8026.42.35.5900203, built on September 26, 2019
 * JRE: 1.8.0_202-release-1483-b49-5587405 amd64
 * JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
 * Linux 5.2.0-kali3-amd64
 *
 *
 */
object TheSportDbApi {

    private const val API = "api"
    private const val VERSI = "v1"
    private const val JSON = "json"

    fun getLeagueTeams(idLeague: String): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath("api")
            .appendPath("v1")
            .appendPath("json")
            .appendPath(BuildConfig.TSDB_API_KEY)
            .appendPath("search_all_teams.php")
            .appendQueryParameter("l", idLeague)
            .build()
            .toString()
    }


    fun getLookupTeams(idTeam: String): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath(API)
            .appendPath(VERSI)
            .appendPath(JSON)
            .appendPath(BuildConfig.TSDB_API_KEY)
            .appendPath("lookupteam.php")
            .appendQueryParameter("id", idTeam)
            .build()
            .toString()
    }

    fun getDetailLeagueTeams(idLeague: String): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath(API)
            .appendPath(VERSI)
            .appendPath(JSON)
            .appendPath(BuildConfig.TSDB_API_KEY)
            .appendPath("lookupleague.php")
            .appendQueryParameter("id", idLeague)
            .build()
            .toString()
    }

    fun getNextMatchTeams(idLeague: String): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath(API)
            .appendPath(VERSI)
            .appendPath(JSON)
            .appendPath(BuildConfig.TSDB_API_KEY)
            .appendPath("eventsnextleague.php")
            .appendQueryParameter("id", idLeague)
            .build()
            .toString()
    }

    fun getPreviousMatchTeams(idLeague: String): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath(API)
            .appendPath(VERSI)
            .appendPath(JSON)
            .appendPath(BuildConfig.TSDB_API_KEY)
            .appendPath("eventspastleague.php")
            .appendQueryParameter("id", idLeague)
            .build()
            .toString()
    }


    fun getDetailMatchEventTeams(idEvent: String): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath(API)
            .appendPath(VERSI)
            .appendPath(JSON)
            .appendPath(BuildConfig.TSDB_API_KEY)
            .appendPath("lookupevent.php")
            .appendQueryParameter("id", idEvent)
            .build()
            .toString()
    }

    fun searchTeams(teamVsTeam: String): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath(API)
            .appendPath(VERSI)
            .appendPath(JSON)
            .appendPath(BuildConfig.TSDB_API_KEY)
            .appendPath("searchevents.php")
            .appendQueryParameter("e", teamVsTeam)
            .build()
            .toString()
    }
}