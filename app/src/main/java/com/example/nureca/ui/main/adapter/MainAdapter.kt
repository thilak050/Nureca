package com.example.nureca.ui.main.adapter
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nureca.data.model.StateList
import com.example.nureca.databinding.ItemLayoutBinding
import com.example.nureca.ui.main.view.DetaillsPage

class MainAdapter : RecyclerView.Adapter<MainViewHolder>() {

    var states = mutableListOf<StateList>()

    fun setMovieList(states: List<StateList>) {
        this.states = states.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val state = states[position]
        holder.binding.dateandstate.text =
            "Last Updated Date: " + state.lastUpdateEt + "\n " + "State: " + state.state


        holder.binding.tvHospitalized.text = state.hospitalized.toString()
        holder.binding.tvDeathconfirmed.text = state.deathConfirmed
        holder.binding.tvPositivecase.text = state.positive.toString()
        holder.binding.tvRecovered.text = state.recovered

        if (TextUtils.isEmpty(state.hospitalized.toString())) {
            holder.binding.tvHospitalized.text = "N/A"

        }

        if (TextUtils.isEmpty(state.deathConfirmed)) {
            holder.binding.tvDeathconfirmed.text = "N/A"

        }

        if (TextUtils.isEmpty(state.positive.toString())) {
            holder.binding.tvPositivecase.text = "N/A"

        }

        if (TextUtils.isEmpty(state.recovered)) {
            holder.binding.tvRecovered.text = "N/A"

        }

        // Log.e("dddd",state.toString())

        holder.binding.tvViewdetails.setOnClickListener {
            val context = holder.binding.tvRecovered.context
            val intent = Intent(context, DetaillsPage::class.java)


            intent.putExtra("positive", state.positive.toString())
            intent.putExtra("negative", state.negative.toString())

            intent.putExtra("death", state.death.toString())
            intent.putExtra("hospitalized", state.hospitalized.toString())

            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return states.size
    }
}

class MainViewHolder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

}