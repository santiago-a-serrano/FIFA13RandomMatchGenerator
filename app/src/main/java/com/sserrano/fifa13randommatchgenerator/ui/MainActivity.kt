package com.sserrano.fifa13randommatchgenerator.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.RatingBar
import android.widget.Toast
import com.sserrano.fifa13randommatchgenerator.R
import com.sserrano.fifa13randommatchgenerator.model.*
import com.sserrano.fifa13randommatchgenerator.model.enums.LeagueCondition
import com.sserrano.fifa13randommatchgenerator.model.exceptions.NoTeamsRandomMatchException
import com.sserrano.fifa13randommatchgenerator.model.exceptions.OneTeamRandomMatchException
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var teams: Teams
    private lateinit var noTeamsToast: Toast
    private lateinit var onlyOneTeamToast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeLateinits()
        setImportantListeners()
        onRandomButtonClick(randomButton)
    }

    private fun setImportantListeners()
    {
        //Since no team in FIFA 13 has less than 0.5 stars:
        customRatingBar.setOnRatingBarChangeListener {
            ratingBar: RatingBar, rating: Float, _: Boolean ->
            if (rating < 0.5f) ratingBar.rating = 0.5f
            teams.halfStarCondition = (ratingBar.rating * 2).toInt().toShort()
        }

        matchTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int,
                id: Long)
            {
                teams.leagueCondition = when(id.toInt())
                {
                    0 -> LeagueCondition.ALL
                    1 -> LeagueCondition.INTERNATIONAL
                    2 -> LeagueCondition.NON_INTERNATIONAL
                    else -> LeagueCondition.ALL
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    //Since applicationContext cannot be obtained before some things are set up
    private fun initializeLateinits()
    {
        teams = Teams(applicationContext)
        noTeamsToast =
            Toast.makeText(applicationContext, R.string.no_teams_message, Toast.LENGTH_LONG)
        onlyOneTeamToast =
            Toast.makeText(applicationContext, R.string.only_one_team_message, Toast.LENGTH_LONG)
    }

    //Shows the information of the team passed as parameter in the corresponding views of the
    //activity
    private fun displayTeam1(team: Team)
    {
        team1Layout.visibility = VISIBLE
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
        team2Layout.visibility = VISIBLE
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

    private fun clearMatchDisplay()
    {
        team1Layout.visibility = INVISIBLE
        team2Layout.visibility = INVISIBLE
    }

    fun onRandomButtonClick(view: View)
    {
        //TODO: Test the case when there are two teams (I haven´t made a filter that only leaves two teams)
        //it should work, every time the random button is pressed, the two teams should change between local and visitor
        try
        {
            val match = teams.getRandomMatch()
            displayMatch(match)
        } catch(exception: NoTeamsRandomMatchException)
        {
            noTeamsToast.show()
            clearMatchDisplay()
        } catch(exception: OneTeamRandomMatchException)
        {
            onlyOneTeamToast.show()
            clearMatchDisplay()
            displayTeam1(exception.team)
        }
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
            teams.halfStarCondition = -1
        }
    }

    fun onSelectRatingSwitch(view: View)
    {
        if(selectRatingSwitch.isChecked)
        {
            customRatingBar.visibility = VISIBLE
            teams.halfStarCondition = (customRatingBar.rating * 2).toInt().toShort()
        } else {
            customRatingBar.visibility = INVISIBLE
            teams.halfStarCondition = -1
        }
    }
}
