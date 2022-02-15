package com.impeccthreads.cggamebook.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.impeccthreads.cggamebook.R
import com.impeccthreads.cggamebook.adapters.CardGameAdapter
import com.impeccthreads.cggamebook.application.BaseActivity
import com.impeccthreads.cggamebook.application.Constants
import com.impeccthreads.cggamebook.dto.CardGameDetails
import com.impeccthreads.cggamebook.dto.Player
import com.impeccthreads.cggamebook.utility.PreferenceHelper
import com.impeccthreads.cggamebook.utility.hideKeyboard
import kotlinx.android.synthetic.main.activity_card_game.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class CardGameActivity : BaseActivity(), CardGameAdapter.CardGameAdapterListener {

    var cardGameDetails = CardGameDetails()
    var selectedPlayerIndex: Int = -1
    var isAddingAdditionalPlayer = false

    companion object {

        val currentDateTime: String
            get() {
                val sdf = SimpleDateFormat(Constants.fullDateFormat)
                return sdf.format(Date())
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_card_game)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

//        cardGameDetails.gameStartedTime = "Mon, 16 Aug 2021 13:10"
//        cardGameDetails.gameEndedTime = "Mon, 16 Aug 2021 14:20"
//        getHoursMinutesString()

        PreferenceHelper.isGameStarted().let {
            if (it != null) {
                if (it) {

                    PreferenceHelper.getCurrentGameDetails().let {
                        if (it != null) {
                            cardGameDetails = it
                            if (cardGameDetails.players.count() > 0)
                            {
                                showPlayersList()
                            }
                            else
                            {
                                PreferenceHelper.setGameStarted(false)
                            }
                        }
                    }
                }
            }
        }

        listViewPlayer.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        listViewPlayer.adapter = CardGameAdapter(this, cardGameDetails.players, this)
    }

    fun getHoursMinutesString(): String {
        var hrsMinString = ""

        if (cardGameDetails.gameStartedTime.isNotEmpty() && cardGameDetails.gameEndedTime.isNotEmpty())
        {
            val startDate = getDateFromDateString(cardGameDetails.gameStartedTime, Constants.fullDateFormat, TimeZone.getDefault())!!
            val endDate = getDateFromDateString(cardGameDetails.gameEndedTime, Constants.fullDateFormat, TimeZone.getDefault())!!

            val diff: Long = endDate.getTime() - startDate.getTime()
//        val diffInDays: Long = TimeUnit.MILLISECONDS.toDays(diff)
//        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diff)
            val diffInMin: Long = TimeUnit.MILLISECONDS.toMinutes(diff)
//        val diffInSec: Long = TimeUnit.MILLISECONDS.toSeconds(diff)

            val hours = diffInMin/60
            val minutes = diffInMin%60


            if (hours > 0)
            {
                hrsMinString += "${hours} hrs"
            }

            if (minutes > 0)
            {
                hrsMinString += " ${minutes} mins"
            }
        }

        return hrsMinString
    }

    fun btnSettingsOnClick(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    fun isPlayerListValidationSuccess(): Boolean {

        if (editTextPlayersList.text.isEmpty()) {
            showToast(Constants.AlertMessages.enterPlayersList)
            return false
        }

        return true
    }

    fun getPlayersList() {
        if (!isAddingAdditionalPlayer)
        {
            cardGameDetails.gameStartedTime = currentDateTime
            cardGameDetails.players.clear()
        }

        var playersDetails = editTextPlayersList.text!!.split("\n")

        for (info in playersDetails) {
            if (info.isNotEmpty())
            {
                cardGameDetails.players.add(Player(info))
            }
        }

        PreferenceHelper.setCurrentGameDetails(cardGameDetails)

        (listViewPlayer.adapter as CardGameAdapter).setPlayersList(cardGameDetails.players)
        (listViewPlayer.adapter as CardGameAdapter).notifyDataSetChanged()

        showPlayersList()
    }

    fun showPlayersList() {
        btnStartGame.visibility = View.GONE
        listViewPlayer.visibility = View.VISIBLE
        layoutGameHelper.visibility = View.VISIBLE
    }

    fun hidePlayersList() {
        btnStartGame.visibility = View.VISIBLE
        listViewPlayer.visibility = View.GONE
        layoutGameHelper.visibility = View.GONE
    }

    fun btnShowScoreCardOnClick(view: View) {
        showScoreCard()
    }

    fun btnAddAdditionalPlayerOnClick(view: View) {
        isAddingAdditionalPlayer = true

        showAddPlayerView()
    }

    fun showAddPlayerView() {
        layoutAddPlayer.visibility = View.VISIBLE
        editTextPlayersList.setText("")
    }

    fun btnEndGameOnClick(view: View) {

        val builder = AlertDialog.Builder(this)

        builder.setTitle(Constants.AlertMessages.endGameConfirmationTitle)

        builder.setMessage(Constants.AlertMessages.endGameConfirmationMsg)

        builder.setPositiveButton("YES") { dialog, which ->
            // Do something when user press the positive button
            cardGameDetails.gameEndedTime = currentDateTime

            hidePlayersList()

            showScoreCard()

            resetCardGameDetails()
        }

        builder.setNegativeButton("No") { dialog, which ->

        }

        val dialog: AlertDialog = builder.create()

        dialog.show()

        // Get the alert dialog buttons reference
        val positiveButton: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton: Button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//        val neutralButton: Button = dialog.getButton(AlertDialog.BUTTON_NEUTRAL)

        // Change the alert dialog buttons text and background color
        positiveButton.setTextColor(Color.parseColor("#ffffff"))
//        positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"))

        negativeButton.setTextColor(Color.parseColor("#ffffff"))
//        negativeButton.setBackgroundColor(Color.parseColor("#FFFCB9B7"))

//        neutralButton.setTextColor(Color.parseColor("#FF1B5AAC"))
//        neutralButton.setBackgroundColor(Color.parseColor("#FFD9E9FF"))

    }

    fun resetCardGameDetails() {
        isAddingAdditionalPlayer = false

        cardGameDetails.noOfMatchPlayed = 0
        cardGameDetails.gameStartedTime = ""
        cardGameDetails.gameEndedTime = ""
        cardGameDetails.players.clear()

        PreferenceHelper.setGameStarted(false)
        PreferenceHelper.setCurrentGameDetails(cardGameDetails)

        (listViewPlayer.adapter as CardGameAdapter).setPlayersList(cardGameDetails.players)
        (listViewPlayer.adapter as CardGameAdapter).notifyDataSetChanged()

       hidePlayersList()
    }

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)

        builder.setTitle("")

        builder.setMessage("Are you sure do you want exit?")

        builder.setPositiveButton("YES"){dialog, which ->
            // Do something when user press the positive button
            finishAffinity()
        }

        builder.setNegativeButton("No"){dialog,which ->

        }

        val dialog: AlertDialog = builder.create()

        dialog.show()

        // Get the alert dialog buttons reference
        val positiveButton: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton: Button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//        val neutralButton: Button = dialog.getButton(AlertDialog.BUTTON_NEUTRAL)

        // Change the alert dialog buttons text and background color
        positiveButton.setTextColor(Color.parseColor("#ffffff"))
//        positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"))

        negativeButton.setTextColor(Color.parseColor("#ffffff"))
//        negativeButton.setBackgroundColor(Color.parseColor("#FFFCB9B7"))

//        neutralButton.setTextColor(Color.parseColor("#FF1B5AAC"))
//        neutralButton.setBackgroundColor(Color.parseColor("#FFD9E9FF"))
    }

    fun showScoreCard() {
        layoutScoreCard.visibility = View.VISIBLE

        var sortedPlayers: ArrayList<Player> = ArrayList(cardGameDetails.players.sortedByDescending { it.pointsTotal!! }.reversed())

        var scoreCardStr = ""

        for ((index, player) in sortedPlayers.withIndex())
        {
            scoreCardStr += "${index + 1}. ${player.name} - ${player.pointsTotal}\n"
        }

        if (scoreCardStr.isNotEmpty())
        {
            scoreCardStr += "\nMatches Played: ${cardGameDetails.noOfMatchPlayed}"
        }

        val hrsMinsStr = getHoursMinutesString()

        if (hrsMinsStr.isNotEmpty())
        {
            scoreCardStr += "\nPlayed Time: ${hrsMinsStr}"
        }

        textViewScoreCard.text = scoreCardStr
    }

    fun showUpdatePointsView(index: Int) {
        checkBoxEditPoint.isChecked = false
        checkBoxAddPoint.isChecked = true
        editTextPlayerPoint.setText("")
        editTextPlayerPoint.requestFocus()
        selectedPlayerIndex = index
        textViewSelectedPlayer.text = "${cardGameDetails.players[selectedPlayerIndex].name.uppercase()}"
        layoutUpdatePoint.visibility = View.VISIBLE
    }

    fun btnCloseScoreCardOnClick(view: View) {

        layoutScoreCard.visibility = View.GONE
    }

    fun btnAddPlayersOnClick(view: View) {

        if (isPlayerListValidationSuccess()) {
            hideKeyboard()
            layoutAddPlayer.visibility = View.GONE
            getPlayersList()
        }
    }

    fun btnCancelPlayersOnClick(view: View) {
        if (!isAddingAdditionalPlayer)
        {
            btnStartGame.visibility = View.VISIBLE
        }

        layoutAddPlayer.visibility = View.GONE
    }

    fun btnUpdatePlayerPointOnClick(view: View) {

        if (editTextPlayerPoint.text.isNotEmpty()) {
            hideUpdatePointView(editTextPlayerPoint.text!!.toString().toInt())
        } else {
            showToast(Constants.AlertMessages.enterPlayerPoint)
        }
    }

    fun checkboxAddOnClick(view: View)  {
        checkBoxEditPoint.isChecked = false
        checkBoxAddPoint.isChecked = true
    }

    fun checkboxEditOnClick(view: View)  {
        checkBoxEditPoint.isChecked = true
        checkBoxAddPoint.isChecked = false
    }

    fun btnInitalCloseOnClick(view: View)  {
        hideUpdatePointView(20)
    }

    fun btnHalfScoopOnClick(view: View)  {
        hideUpdatePointView(40)
    }

    fun btnFullScoopOnClick(view: View)  {
        hideUpdatePointView(80)
    }

    fun btnDickOnclick(view: View)  {
        hideUpdatePointView(0)
    }

    fun btnCloseUpdateViewOnClick(view: View) {
        hideKeyboard()

        layoutUpdatePoint.visibility = View.GONE
        editTextPlayersList.setText("")
    }

    fun hideUpdatePointView(point: Int) {
        hideKeyboard()

        var selectedPlayer = cardGameDetails.players[selectedPlayerIndex]
        selectedPlayer.pointsTotal = 0
        selectedPlayer.pointsString = ""

        //Adding Point
        if (selectedPlayer.points.count() == 0)
        {
            selectedPlayer.points.add(point)
        }
        else
        {
            if (checkBoxEditPoint.isChecked)
            {
                selectedPlayer.points.removeLast()
                selectedPlayer.points.add(point)
            }
            else
            {
                selectedPlayer.points.add(point)
            }
        }

        for (point in selectedPlayer.points)
        {
            //Update Point Total
            selectedPlayer.pointsTotal += point

            //Update Point String
            if (selectedPlayer.pointsString.isNotEmpty())
            {
                selectedPlayer.pointsString += " + "
            }

            selectedPlayer.pointsString += point
        }

        //Update Card Game Details
        cardGameDetails.noOfMatchPlayed = selectedPlayer.points.count()

        cardGameDetails.players.set(selectedPlayerIndex, selectedPlayer)

//        for ((index, player) in playersList.withIndex())
//        {
//            if (index == selectedPlayerIndex)
//            {
//                playersList[index].pointsTotal += point
//            }
//        }

        PreferenceHelper.setCurrentGameDetails(cardGameDetails)

        (listViewPlayer.adapter as CardGameAdapter).setPlayersList(cardGameDetails.players)
        (listViewPlayer.adapter as CardGameAdapter).notifyDataSetChanged()

        selectedPlayerIndex = -1
        layoutUpdatePoint.visibility = View.GONE
    }

    fun btnStartGameOnCliek(view: View) {
        isAddingAdditionalPlayer = false

        PreferenceHelper.setGameStarted(true)

        btnStartGame.visibility = View.GONE

        showAddPlayerView()
//        editTextPlayersList.setText("Gowtham\nDeepak\nCibi\nSaranya")
    }

    fun getDateFromDateString(dateString: String, format: String, timeZone: TimeZone?): Date? {
        val simpleDateFormat = SimpleDateFormat(format)
        if (null != timeZone) {
            simpleDateFormat.timeZone = timeZone
        }

        try {
            return simpleDateFormat.parse(dateString)
        } catch (var5: ParseException) {
            var5.printStackTrace()
            return null
        }

    }

    override fun didSelectPlayerAtPosition(position: Int) {
        showUpdatePointsView(position)
    }
}