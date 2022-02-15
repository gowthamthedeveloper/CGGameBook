package com.impeccthreads.cggamebook.application

interface Constants {

    companion object {

        var appVersionName = "3.0"
        var fullDateFormat = "EEE, d MMM yyyy HH:mm"
    }

    interface SharedPrefKeys {

        companion object {
            val isGameStarted = "IS_GAME_STARTED"
            val currentGameDetails = "CURRENT_GAME_DETAILS"
        }
    }

    interface AppKeys {
        companion object {
            val DESTINATION_FOLDER_NAME = "CGGameBook"
        }
    }

    interface AlertMessages {
        companion object {
            val enterPlayersList = "Enter players details"
            val enterPlayerPoint = "Enter players points"
            val endGameConfirmationTitle = "Confirmateion"
            var endGameConfirmationMsg = "Are you sure do you want end the game?"
            val comingSoon = "Coming Soon..!"
        }
    }
}
