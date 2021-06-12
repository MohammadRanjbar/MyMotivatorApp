package com.example.mymotivator.utils

fun SeperateStrings(rawSentences:String): List<Pair<String, String>> {
// split sentence by "/" an create new array
    var splitByBackSlash = rawSentences.split("/")


    var SentecesAndAutherArray = mutableListOf<Pair<String,String>>()

    for ((j, i) in splitByBackSlash.withIndex()){
        var splitedBy = i.split(":")
        SentecesAndAutherArray.add(j, splitedBy[0] to splitedBy[1])

    }


    return SentecesAndAutherArray



}