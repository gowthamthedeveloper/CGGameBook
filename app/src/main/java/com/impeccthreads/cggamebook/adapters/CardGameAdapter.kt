package com.impeccthreads.cggamebook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.impeccthreads.cggamebook.R
import com.impeccthreads.cggamebook.dto.Player
import com.impeccthreads.cggamebook.utility.listen

class CardGameAdapter(val mContext: Context, var players: ArrayList<Player>, val delegate: CardGameAdapterListener?): RecyclerView.Adapter<CardGameAdapter.CardGameViewHolder>() {

    interface CardGameAdapterListener {

        fun didSelectPlayerAtPosition(position: Int)
    }

    override fun getItemCount(): Int {
        return players.size
    }

    fun setPlayersList(playerList: ArrayList<Player>) {
        this.players = playerList
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardGameViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.item_card_game_player_list, parent, false)
        val viewHolder = CardGameViewHolder(cellForRow).listen { position, type ->
            delegate?.didSelectPlayerAtPosition(position)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CardGameViewHolder, position: Int) {

        val player = players.get(position)

        holder.textViewPlayerName.text = player.name.capitalize()
        holder.textViewPointTotal.text = player.pointsTotal.toString()
        holder.textViewPlayerPoints.text = player.pointsString.capitalize()
    }


    class CardGameViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val textViewPlayerName = view.findViewById<TextView>(R.id.textViewPlayerName) as TextView
        val textViewPointTotal = view.findViewById<TextView>(R.id.textViewPointTotal) as TextView
        val textViewPlayerPoints = view.findViewById<TextView>(R.id.textViewPlayerPoints) as TextView

    }
}