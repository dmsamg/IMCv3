package edu.davidmarhuenda.imcv3.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import edu.davidmarhuenda.imcv3.R
import edu.davidmarhuenda.imcv3.data.MyIMC
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow

class MyFunctions {

    fun obtenerIMC(peso: Double, altura: Double): Double {
        // Se pasa la altura de centímetros a metros.
        return peso / (altura / 100).pow(2.00)
    }

    /**
     * Composición corporal Índice de masa corporal (IMC)
     * Peso inferior al normal < 18.5
     * Normal >= 18.5 && <= 24.9(H) || <= 23.9(M)
     * Peso superior al normal >= 25.0 && <=29.9(H) || >=24 && <=28.9(M)
     * Obesidad Más de > 30.0(H) || >29.0(M)
     */
    fun detalleIMC(context: Context, imc: Double, sexo: String): String {
        if (sexo.equals(context.getString(R.string.radioButtonHombre))) {
            when {
                imc < 18.5 -> return "Peso inferior al normal"
                imc in 18.5..24.99 -> return "Normal"
                imc in 25.00..29.99 -> return "Sobrepeso"
                imc >= 30.00 -> return "Obesidad"
                else -> return "No hay datos suficientes"
            }
        } else {
            when {
                imc < 18.50 -> return "Peso inferior al normal"
                imc in 18.50..23.99 -> return "Normal"
                imc in 24.00..28.99 -> return "Sobrepeso"
                imc >= 29.00 -> return "Obesidad"
                else -> return "No hay datos suficientes"
            }
        }
    }

    fun getFecha(): String {
        val hoy = Calendar.getInstance()
        // ENERO - 0, FEBRERO - 1, ..., DICIEMBRE - 11
        return "${hoy.get(Calendar.DAY_OF_MONTH)}" +
                "-${hoy.get(Calendar.MONTH) + 1}" +
                "-${hoy.get(Calendar.YEAR)}"
    }

    fun writeFile(context: Context, datos: MyIMC) {
        try {
            val salida = OutputStreamWriter(
                context.openFileOutput(
                    context.getString(R.string.filename),
                    Activity.MODE_APPEND
                )
            )

            salida.write("${datos.fecha};${datos.sexo};${datos.imc};${datos.estado};" +
                    "${datos.peso};${datos.altura}\n")

            salida.flush()
            salida.close()
        } catch (e: IOException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun readFile(context: Context): MutableList<MyIMC> {
        var lista: MutableList<MyIMC> = ArrayList()
        if (context.fileList().contains(context.getString(R.string.filename))) {
            try {
                val entrada =
                    InputStreamReader(
                        context.openFileInput(
                            context.getString(R.string.filename)
                        )
                    )
                val br = BufferedReader(entrada)

                var linea = br.readLine()
                while (!linea.isNullOrEmpty()) {
                    val dato = MyIMC()
                    dato.fecha = linea.split(";").get(0)
                    dato.sexo = linea.split(";").get(1)
                    dato.imc = linea.split(";").get(2).toDouble()
                    dato.estado = linea.split(";").get(3)
                    dato.peso = linea.split(";").get(4).toDouble()
                    dato.altura = linea.split(";").get(5).toDouble()
                    Log.d("FILE", "${dato.fecha} + ${dato.sexo} + ${dato.imc} + ${dato.estado} + ${dato.peso} + ${dato.altura}")
                    lista.add(dato)
                    linea = br.readLine()
                }

                br.close()
                entrada.close()
            } catch (e: IOException) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        } else {
            Log.i("FILE", "No existe el fichero ${context.getString(R.string.filename)}.")
        }
        return lista
    }
}