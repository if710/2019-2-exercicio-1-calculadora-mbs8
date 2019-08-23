package br.ufpe.cin.android.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.TextViewCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var expr : String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Variavel que armazena o valor atual mostrado na tela da calculadora
        text_calc.setText(expr)

        /**
         * 2. Funções para configurar os listeners para cada um dos botões da
         * calculadora
         */
        btn_0.setOnClickListener {
            putChar("0")
        }
        btn_1.setOnClickListener {
            putChar("1")
        }
        btn_2.setOnClickListener {
            putChar("2")
        }
        btn_3.setOnClickListener {
            putChar("3")
        }
        btn_4.setOnClickListener {
            putChar("4")
        }
        btn_5.setOnClickListener {
            putChar("5")
        }
        btn_6.setOnClickListener {
            putChar("6")
        }
        btn_7.setOnClickListener {
            putChar("7")
        }
        btn_8.setOnClickListener {
            putChar("8")
        }
        btn_9.setOnClickListener {
            putChar("9")
        }
        btn_Add.setOnClickListener {
            putChar("+")
        }
        btn_Multiply.setOnClickListener {
            putChar("*")
        }
        btn_Divide.setOnClickListener {
            putChar("/")
        }
        btn_Dot.setOnClickListener {
            putChar(".")
        }
        btn_LParen.setOnClickListener {
            putChar("(")
        }
        btn_RParen.setOnClickListener {
            putChar(")")
        }
        btn_Power.setOnClickListener {
            putChar("^")
        }
        btn_Subtract.setOnClickListener {
            putChar("-")
        }
        btn_Clear.setOnClickListener {
            expr = "0"
            text_calc.setText(expr)
            text_info.setText("")
        }
        // 3. Função para calcular o valor da expressão ('=')
        btn_Equal.setOnClickListener {
            text_info.setText(eval(expr).toString())
            expr = "0"
            text_calc.setText(expr)
        }


    }

    //Função auxiliar para colocar um digito na calculadora
    fun putChar(info: String) {
        if(expr == "0")
            expr = info
        else
            expr += info
        text_calc.setText(expr)
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
                if (pos < str.length) {
                    //5. Coloca um toast para notificar o usuário nos casos em que a aplicação quebrava
                    Toast.makeText(getApplicationContext(), "Caractere inesperado", Toast.LENGTH_SHORT).show()
                    return 0.0
                }
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
                    else {
                        //5.
                        Toast.makeText(getApplicationContext(), "Função desconhecida", Toast.LENGTH_SHORT).show()
                        return 0.0
                    }
                  } else {
                    //5.
                    Toast.makeText(getApplicationContext(), "Caractere inesperado", Toast.LENGTH_SHORT).show()
                    return 0.0
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
