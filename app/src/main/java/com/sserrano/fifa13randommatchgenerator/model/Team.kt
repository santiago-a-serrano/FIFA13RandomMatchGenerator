package com.sserrano.fifa13randommatchgenerator.model

data class Team constructor(val name: String, val country: String, val league: String,
                            val halfStars: Short, val scores: TeamScores)