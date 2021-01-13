package com.sserrano.fifa13randommatchgenerator.model.exceptions

class NoTeamsRandomMatchException(): RandomMatchException()
{
    override val message = super.message +
            "You tried generating a random match using a set with no teams in it."
}