package com.example.prova1

class Estoque {
    companion object {
        val listaProdutos = mutableListOf<Produto>()

        fun adicionarProduto(produto: Produto) {
            listaProdutos.add(produto)
        }

        fun calcularValorTotalEstoque(): Float {
            var total = 0f
            for (it in listaProdutos) {
                total += it.preco.toFloat() * it.qtdEstoque.toInt()
            }
            return total
        }

        fun calcularQtdEstoque(): Int {
            var qtd = listaProdutos.sumOf { it.qtdEstoque.toInt() }
            return qtd
        }
    }
}