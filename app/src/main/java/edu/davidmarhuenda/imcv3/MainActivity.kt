package edu.davidmarhuenda.imcv3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import edu.davidmarhuenda.imcv3.data.MyIMC
import edu.davidmarhuenda.imcv3.databinding.ActivityMainBinding
import edu.davidmarhuenda.imcv3.utils.MyDialogFragment
import edu.davidmarhuenda.imcv3.utils.MyFunctions

class MainActivity : AppCompatActivity(), MyDialogFragment.NoticeDialogListener {

    val datosIMC = MyIMC()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCalcular.setOnClickListener {
            binding.tvIMC.text = ""
            binding.tvIMCText.text = ""

            if ((binding.etPeso.text.isNotEmpty()) && (binding.etAltura.text.isNotEmpty())) {
                binding.tvIMC.text = getString(R.string.textZero)

                if ((binding.etPeso.text.toString()
                        .toDouble() > 0.00) && (binding.etAltura.text.toString().toDouble() > 0.00)
                ) {

                    datosIMC.fecha = MyFunctions().getFecha()

                    datosIMC.imc = MyFunctions().obtenerIMC(
                        binding.etPeso.text.toString().toDouble(),
                        binding.etAltura.text.toString().toDouble()
                    )

                    if (binding.rbHombre.isChecked) {
                        datosIMC.sexo = binding.rbHombre.text.toString()
                        datosIMC.estado = MyFunctions().detalleIMC(
                            this,
                            datosIMC.imc!!,
                            binding.rbHombre.text.toString()
                        )
                    } else {
                        datosIMC.sexo = binding.rbMujer.text.toString()
                        datosIMC.estado = MyFunctions().detalleIMC(
                            this,
                            datosIMC.imc!!,
                            binding.rbMujer.text.toString()
                        )
                    }

                    //añadimos el peso y la altura
                    datosIMC.peso = binding.etPeso.text.toString().toDouble()
                    datosIMC.altura = binding.etAltura.text.toString().toDouble()

                    binding.tvIMC.text = String.format("%.2f", datosIMC.imc!!)
                    binding.tvIMCText.text = datosIMC.estado

                    //llamamos al método para mostrar el DialogFragment
                    mostrarDialogFragment()

                } else {
                    Snackbar.make(
                        binding.root,
                        R.string.msgErrorZeros,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            } else {
                Snackbar.make(
                    binding.root,
                    R.string.msgErrorInputs,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.btnHistorico.setOnClickListener {
            val intentHistorico = Intent(this, HistoricoActivity::class.java)
            startActivity(intentHistorico)
        }
    }

    fun mostrarDialogFragment(){

        // se crea una instancia del DialogFragment y se muestra
        val dialog = MyDialogFragment()
        dialog.show(supportFragmentManager, "NoticeDialogFragment")

    }

    /**
     * se recogen los resultados positivo y negativo del DialogFragment
     */
    override fun onDialogPositiveClick(dialog: DialogFragment) {

        //acciones si se pulsa OK en el DialogFragment
        MyFunctions().writeFile(this, datosIMC)

        Snackbar.make(
            binding.root,
            R.string.mssg_saved,
            Snackbar.LENGTH_LONG
        ).show()

    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {

        Snackbar.make(
            binding.root,
            R.string.mssg_not_saved,
            Snackbar.LENGTH_LONG
        ).show()

    }
}