package com.example.calc3d

import java.util.*
import kotlin.math.*

fun evalExpression(input: String): Double {
    if (input.isBlank()) return 0.0
    val tokens = tokenize(input)
    val rpn = shuntingYard(tokens)
    return evalRPN(rpn)
}

private fun tokenize(s: String): List<String> {
    val t = mutableListOf<String>()
    var i = 0
    while (i < s.length) {
        val c = s[i]
        when {
            c.isWhitespace() -> i++
            c.isDigit() || c == '.' -> {
                val sb = StringBuilder()
                while (i < s.length && (s[i].isDigit() || s[i]=='.')) {
                    sb.append(s[i]); i++
                }
                t.add(sb.toString())
            }
            c == '+' || c == '-' || c == '*' || c == '/' || c=='(' || c==')' -> {
                t.add(c.toString()); i++
            }
            else -> throw IllegalArgumentException("Invalid char: $c")
        }
    }
    return t
}

private fun prec(op: String): Int = when(op) {
    "+", "-" -> 1
    "*", "/" -> 2
    else -> 0
}

private fun shuntingYard(tokens: List<String>): List<String> {
    val out = mutableListOf<String>()
    val ops = ArrayDeque<String>()
    for (tk in tokens) {
        if (tk.toDoubleOrNull() != null) {
            out.add(tk)
        } else if (tk == "(") {
            ops.push(tk)
        } else if (tk == ")") {
            while(ops.isNotEmpty() && ops.peek() != "(") out.add(ops.pop())
            if (ops.isEmpty() || ops.pop() != "(") throw IllegalArgumentException("Mismatched parentheses")
        } else {
            while(ops.isNotEmpty() && prec(ops.peek()) >= prec(tk)) {
                out.add(ops.pop())
            }
            ops.push(tk)
        }
    }
    while(ops.isNotEmpty()) {
        val o = ops.pop()
        if (o=="(" || o==")") throw IllegalArgumentException("Mismatched parentheses")
        out.add(o)
    }
    return out
}

private fun evalRPN(rpn: List<String>): Double {
    val st = ArrayDeque<Double>()
    for (tk in rpn) {
        val num = tk.toDoubleOrNull()
        if (num != null) {
            st.push(num)
        } else {
            val b = st.popOrElse { 0.0 }
            val a = st.popOrElse { 0.0 }
            val res = when (tk) {
                "+" -> a + b
                "-" -> a - b
                "*" -> a * b
                "/" -> a / b
                else -> throw IllegalArgumentException("Unknown op $tk")
            }
            st.push(res)
        }
    }
    return if (st.isEmpty()) 0.0 else st.pop()
}

private fun <T> ArrayDeque<T>.popOrElse(default: ()->T): T = if (this.isEmpty()) default() else this.pop()
private fun formatDouble(v: Double): String {
    return if (v % 1.0 == 0.0) v.toLong().toString() else String.format("%.6f", v).trimEnd('0').trimEnd('.')
}
