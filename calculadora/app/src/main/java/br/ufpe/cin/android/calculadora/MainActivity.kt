package br.ufpe.cin.android.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Variavel que armazena o valor atual mostrado na tela da calculadora
        var expr : String = text_calc.text.toString()

        /**
         * 2. Funções para configurar os listeners para cada um dos botões da
         * calculadora
         */

        btn_0.setOnClickListener {
            expr += "0"
            text_calc.setText(expr)
        }
        btn_1.setOnClickListener {
            expr += "1"
            text_calc.setText(expr)
        }
        btn_2.setOnClickListener {
            expr += "2"
            text_calc.setText(expr)
        }
        btn_3.setOnClickListener {
            expr += "3"
            text_calc.setText(expr)
        }
        btn_4.setOnClickListener {
            expr += "4"
            text_calc.setText(expr)
        }
        btn_5.setOnClickListener {
            expr += "5"
            text_calc.setText(expr)
        }
        btn_6.setOnClickListener {
            expr += "6"
            text_calc.setText(expr)
        }
        btn_7.setOnClickListener {
            expr += "7"
            text_calc.setText(expr)
        }
        btn_8.setOnClickListener {
            expr += "8"
            text_calc.setText(expr)
        }
        btn_9.setOnClickListener {
            expr += "9"
            text_calc.setText(expr)
        }
        btn_Add.setOnClickListener {
            expr += "+"
            text_calc.setText(expr)
        }
        btn_Multiply.setOnClickListener {
            expr += "*"
            text_calc.setText(expr)
        }
        btn_Divide.setOnClickListener {
            expr += "/"
            text_calc.setText(expr)
        }
        btn_Dot.setOnClickListener {
            expr += "."
            text_calc.setText(expr)
        }
        btn_LParen.setOnClickListener {
            expr += "("
            text_calc.setText(expr)
        }
        btn_RParen.setOnClickListener {
            expr += ")"
            text_calc.setText(expr)
        }
        btn_Power.setOnClickListener {
            expr += "^"
            text_calc.setText(expr)
        }
        btn_Subtract.setOnClickListener {
            expr += "-"
            text_calc.setText(expr)
        }
        // 3. Função para calcular o valor da expressão ('=')
        btn_Equal.setOnClickListener {
            text_info.setText(eval(expr).toString())
            expr = ""
            text_calc.setText(expr)
        }
        btn_Clear.setOnClickListener {
            expr = ""
            text_calc.setText(expr)
            text_info.setText(expr)
        }
    }


    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
