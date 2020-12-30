package com.sserrano.fifa13randommatchgenerator.model

import android.content.Context
import com.sserrano.fifa13randommatchgenerator.R
import kotlinx.android.synthetic.main.activity_main.*

//This class will contain all information about teams
class Teams constructor(private val context: Context)
{
    //All teams included in the resource text files will be stored here:
    private val teams = mutableSetOf<Team>()
    //In this set will be the teams that match the conditions set by the user:
    private var filteredTeams: Set<Team> = teams

    //Here will be stored the two last-generated teams (the last-generated match)
    private lateinit var match: Match

    //Conditions set by user will be stored here:
    private var halfStarCondition: Short = -1 //any negative or 0 means there's no condition
    var sameRatingCondition: Boolean = false

    init
    {
        //We create all the teams defined by the information in res/raw and store them in the global
        //variable "teams"
        //Important: the first line of every text file must correspond to information of the first
        //team, the second line to info. of the second team, and so on...
        val teamNames = getListOfLines(R.raw.team_names)
        val teamCountries = getListOfLines(R.raw.team_countries)
        val teamLeagues = getListOfLines(R.raw.team_leagues)
        val teamHalfStarRatings = getListOfLines(R.raw.team_half_star_ratings)
        val teamAttackScores = getListOfLines(R.raw.team_attack_scores)
        val teamMidfieldScores = getListOfLines(R.raw.team_midfield_scores)
        val teamAverageScores = getListOfLines(R.raw.team_average_scores)
        val teamDefenseScores = getListOfLines(R.raw.team_defense_scores)

        val numOfTeams = teamNames.size

        //We populate the teams set with Teams
        for(teamNum in 0 until numOfTeams)
        {
            val scores = TeamScores(teamAttackScores[teamNum].toShort(),
                teamMidfieldScores[teamNum].toShort(), teamDefenseScores[teamNum].toShort(),
                teamAverageScores[teamNum].toShort())
            teams.add(
                Team(teamNames[teamNum], teamCountries[teamNum], teamLeagues[teamNum],
                    teamHalfStarRatings[teamNum].toShort(), scores)
            )
        }
    }

    private fun getListOfLines(resID: Int) : List<String>
    {
        val resource = context.resources.openRawResource(resID)
        return resource.bufferedReader().useLines {it.toList()}
    }

    public fun getRandomMatch(): Match
    {
        var newTeam1: Team
        var newTeam2: Team
        var bothTeamsSame: Boolean
        var previousTeamsSame: Boolean
        var differentRatings: Boolean
        do
        {
            newTeam1 = filteredTeams.random()
            newTeam2 = filteredTeams.random()
            bothTeamsSame = newTeam1 === newTeam2
            previousTeamsSame = (this::match.isInitialized) &&
                    (newTeam1 === match.team1 && newTeam2 === match.team2)
            differentRatings = newTeam1.halfStars != newTeam2.halfStars
        } while (bothTeamsSame || previousTeamsSame ||
            (differentRatings && sameRatingCondition))
        match = Match(newTeam1, newTeam2)
        return match
    }

    public fun setRatingCondition(stars: Float)
    {
        halfStarCondition = (stars * 2).toInt().toShort()
        updateFilteredTeams()
    }

    private fun updateFilteredTeams()
    {
        filteredTeams = if(halfStarCondition > 0)
            teams.filter{it.halfStars == halfStarCondition}.toSet()
        else
            teams
    }
}