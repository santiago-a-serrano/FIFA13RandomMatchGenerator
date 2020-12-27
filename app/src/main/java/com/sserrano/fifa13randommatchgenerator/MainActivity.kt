package com.sserrano.fifa13randommatchgenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.RatingBar
import com.sserrano.fifa13randommatchgenerator.model.*
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    //All teams included in the resource text files will be stored here:
    private val teams = mutableSetOf<Team>()
    //In this set will be the teams that match the constraints set by the user:
    private var filteredTeams: Set<Team> = teams
    private lateinit var team1: Team //The first team that is being shown to the user
    private lateinit var team2: Team //The second team that is being shown to the user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        populateTeamsList()
        setImportantListeners()
        onRandomButtonClick(randomButton)
    }

    //We create all the teams defined by the information in res/raw and store them in the global
    //variable "teams"
    private fun populateTeamsList()
    {
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

    private fun setImportantListeners()
    {
        //Since no team in FIFA 13 has less than 0.5 stars:
        customRatingBar.setOnRatingBarChangeListener {
            ratingBar: RatingBar, rating: Float, _: Boolean ->
            if (rating < 0.5f) ratingBar.rating = 0.5f
            filteredTeams =
                teams.filter{it.halfStars == (ratingBar.rating * 2).toInt().toShort()}.toSet()
        }
    }

    private fun getListOfLines(resID: Int) : List<String>
    {
        val resource = resources.openRawResource(resID)
        return resource.bufferedReader().useLines {it.toList()}
    }

    //Shows the information of the team passed as parameter in the corresponding views of the
    //activity
    private fun displayTeam1(team: Team)
    {
        team1NameView.text = team.name
        team1CountryView.text = team.country
        team1LeagueView.text = team.league
        team1RatingView.rating = team.halfStars / 2.0f
        team1AttackScoreView.text = team.scores.attack.toString()
        team1MidfieldScoreView.text = team.scores.midfield.toString()
        team1DefenseScoreView.text = team.scores.defense.toString()
    }

    private fun displayTeam2(team: Team)
    {
        team2NameView.text = team.name
        team2CountryView.text = team.country
        team2LeagueView.text = team.league
        team2RatingView.rating = team.halfStars / 2.0f
        team2AttackScoreView.text = team.scores.attack.toString()
        team2MidfieldScoreView.text = team.scores.midfield.toString()
        team2DefenseScoreView.text = team.scores.defense.toString()
    }

    //TODO: Testear que siempre se generen equipos diferentes. Podemos mirar esto seleccionando estrellas con pocos equipos.
    //TODO: ¿Qué pasa si hay muy pocos equipos en determinada categoría? (Testear con dos equipos: debería entrar en un loop infinito) (Testear con tres: debería servir)
    //TODO: ¿Será que testear por referencia si son iguales los equipos funciona más adelante? (Cuando hagamos filtros de lista y eso)
    fun onRandomButtonClick(view: View)
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
            previousTeamsSame = (this::team1.isInitialized && this::team2.isInitialized) &&
                                        (newTeam1 === team1 && newTeam2 === team2)
            differentRatings = newTeam1.halfStars != newTeam2.halfStars
        } while (bothTeamsSame || previousTeamsSame ||
                (differentRatings && sameRatingSwitch.isChecked))
        team1 = newTeam1;
        team2 = newTeam2;
        displayTeam1(team1)
        displayTeam2(team2)
    }

    fun onSameRatingSwitch(view: View)
    {
        if(sameRatingSwitch.isChecked)
        {
            selectRatingSwitch.isChecked = false
            selectRatingSwitch.visibility = VISIBLE
        } else {
            selectRatingSwitch.visibility = INVISIBLE
            customRatingBar.visibility = INVISIBLE
        }
    }

    fun onSelectRatingSwitch(view: View)
    {
        if(selectRatingSwitch.isChecked)
        {
            customRatingBar.visibility = VISIBLE
            filteredTeams =
                teams.filter{it.halfStars == (customRatingBar.rating * 2).toInt().toShort()}.toSet()
        } else {
            customRatingBar.visibility = INVISIBLE
            filteredTeams = teams
        }
    }
}
