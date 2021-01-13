package com.sserrano.fifa13randommatchgenerator.model.exceptions

import com.sserrano.fifa13randommatchgenerator.model.Team

class OneTeamRandomMatchException(val team: Team): RandomMatchException()
{
    override val message = super.message +
            "You tried generating a random match using a set with only one team in it.\n" +
            "You can obtain that team by getting the team attribute from this exception."
}