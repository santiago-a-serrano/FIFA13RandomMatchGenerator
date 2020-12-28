package com.sserrano.fifa13randommatchgenerator.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.RatingBar
import com.sserrano.fifa13randommatchgenerator.R
import com.sserrano.fifa13randommatchgenerator.model.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var teams: Teams
    private lateinit var team1: Team //The first team that is being shown to the user
    private lateinit var team2: Team //The second team that is being shown to the user
    //TODO: Create two variables, ratingFilter and matchTypeFilter, where we store the lambdas or conditions that we then pass to the filter function

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        teams = Teams(applicationContext)
        setImportantListeners()
        onRandomButtonClick(randomButton)
    }

    private fun setImportantListeners()
    {
        //Since no team in FIFA 13 has less than 0.5 stars:
        customRatingBar.setOnRatingBarChangeListener {
            ratingBar: RatingBar, rating: Float, _: Boolean ->
            if (rating < 0.5f) ratingBar.rating = 0.5f
            teams.setRatingCondition(ratingBar.rating)
        }
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
            newTeam1 = teams.getRandom()
            newTeam2 = teams.getRandom()
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
            teams.setRatingCondition(-1f)
        }
    }

    fun onSelectRatingSwitch(view: View)
    {
        if(selectRatingSwitch.isChecked)
        {
            customRatingBar.visibility = VISIBLE
            teams.setRatingCondition(customRatingBar.rating)
        } else {
            customRatingBar.visibility = INVISIBLE
            teams.setRatingCondition(-1f)
        }
    }
}
