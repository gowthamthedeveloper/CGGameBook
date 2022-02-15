package com.impeccthreads.cggamebook.dto

class Player {
    var name: String = ""
    var points: ArrayList<Int> = arrayListOf()
    var pointsString: String = ""
    var pointsTotal: Int = 0

    constructor(name: String) {
        this.name = name
        this.points = arrayListOf()
        this.pointsString = ""
        this.pointsTotal = 0
    }
}

