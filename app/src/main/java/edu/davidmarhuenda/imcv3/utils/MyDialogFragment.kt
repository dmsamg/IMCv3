package edu.davidmarhuenda.imcv3.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import edu.davidmarhuenda.imcv3.MainActivity
import edu.davidmarhuenda.imcv3.R

class MyDialogFragment : DialogFragment() {

    // instancia de la interfaz para enviar el resultado del DialogFragment
    internal lateinit var listener: NoticeDialogListener

    //MainActivity debe implementar esta interfaz para recibir el resultado
    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    // se vincula MainActivity con el DialogFragment
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // se verifica si MainActivity implementa la interfaz
        try {
            listener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            // si no la implementa, lanza una excepción
            throw ClassCastException((context.toString() +
                    " debe implementar NoticeDialogListener"))
        }

    }

    // Se crea la estructura del diálogo.
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setMessage(R.string.mssg_dialog)
                .setPositiveButton(
                    android.R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        // envía resultado positivo a MainActivity
                        listener.onDialogPositiveClick(this)})
                .setNegativeButton(
                    android.R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        // envía resultado negativo a MainActivity
                        listener.onDialogNegativeClick(this)})
            builder.create()
        } ?: throw IllegalStateException("La Activity no puede ser nula")
    }
}