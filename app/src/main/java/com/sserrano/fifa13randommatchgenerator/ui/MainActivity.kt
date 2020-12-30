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

    private fun displayMatch(match: Match)
    {
        displayTeam1(match.team1)
        displayTeam2(match.team2)
    }

    //TODO: ¿Qué pasa si hay muy pocos equipos en determinada categoría? (Testear con dos equipos: debería entrar en un loop infinito) (Testear con tres: debería servir)
    //TODO: ¿Será que testear por referencia si son iguales los equipos funciona más adelante? (Cuando hagamos filtros de lista y eso)
    fun onRandomButtonClick(view: View)
    {
        val match = teams.getRandomMatch()
        displayMatch(match)
    }

    fun onSameRatingSwitch(view: View)
    {
        if(sameRatingSwitch.isChecked)
        {
            teams.sameRatingCondition = true
            selectRatingSwitch.isChecked = false
            selectRatingSwitch.visibility = VISIBLE
        } else {
            teams.sameRatingCondition = false
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
