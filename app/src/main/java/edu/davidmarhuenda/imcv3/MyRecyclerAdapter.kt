package edu.davidmarhuenda.imcv3

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.davidmarhuenda.imcv3.data.MyIMC
import edu.davidmarhuenda.imcv3.databinding.ItemImcBinding

class MyRecyclerAdapter : RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>() {

    private var imcsList: MutableList<MyIMC> = ArrayList()
    private lateinit var context: Context

    fun MyRecyclerAdapter(context: Context, imcs: MutableList<MyIMC>) {
        this.context = context
        this.imcsList = imcs
        Log.d("RV", "constructor")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("RV", "onBindViewHolder")
        val item = imcsList.get(position)
        holder.bind(item)
    }

    // Devuelve el ViewHolder ya configurado.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("RV", "onCreateViewHolder")
        return ViewHolder(
            ItemImcBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root,
        )
    }

    // Devuelve el tamaño de la lista.
    override fun getItemCount(): Int {
        return imcsList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemImcBinding.bind(view)

        fun bind(imc: MyIMC) {
            Log.d("RV", "en el bind")

            binding.tvMes.text =
                context.resources.getStringArray(R.array.meses)[imc.fecha!!.split("-")[1].toInt() - 1]
            binding.tvDia.text = imc.fecha!!.split("-")[0]
            binding.tvAnyo.text = imc.fecha!!.split("-")[2]
            binding.tvSexo.text = imc.sexo
            binding.tvIMChistorico.text = String.format("%.2f", imc.imc)
            binding.tvEstado.text = imc.estado
            binding.tvPeso.text = "Peso: " + String.format("%.2f", imc.peso) + " Kg"
            binding.tvAltura.text = "Altura: " + String.format("%.2f", imc.altura) + " cm"

            itemView.setOnClickListener {
                Snackbar.make(
                    binding.root,
                    "${binding.tvEstado.text} (${binding.tvIMChistorico.text})",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}