package com.example.prova1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navegacao()
        }
    }
}

@Composable
fun Navegacao(){
    val navController = rememberNavController()

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){

    }
        NavHost(navController = navController, startDestination = "telacadastro"){
            composable("telacadastro") { TelaCadastro(navController) }
            composable("produtoscadastrados") { ProdutosCadastrados(navController) }
            composable("estatisticas") { Estatisticas(navController) }
            composable("detalhesprodutos/{produtoJson}") { backStackEntry ->
                val produtoJson = backStackEntry.arguments?.getString("produtoJson")
                val produto = Gson().fromJson(produtoJson, Produto::class.java)

                DetalhesProdutos(navController = navController, produto = produto)
            }



    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastro(navController: NavController){

    var nomeProduto by remember { mutableStateOf("")}
    var catProduto by remember { mutableStateOf("")}
    var precoProduto by remember { mutableStateOf("")}
    var qtdEstq by remember { mutableStateOf("")}

    var listaProdutos by remember { mutableStateOf(listOf<Produto>())}

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Cadastro de Produtos", fontSize = 25.sp)
        Spacer(modifier = Modifier.height(20.dp))
        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            ,value = nomeProduto, onValueChange = {nomeProduto = it},
            label = { Text(text = "Nome do Produto")})

        Spacer(modifier = Modifier.height(15.dp))
        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            ,value = catProduto, onValueChange = {catProduto = it},
            label = { Text(text = "Categoria do Produto")})

        Spacer(modifier = Modifier.height(15.dp))
        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            ,value = precoProduto, onValueChange = {precoProduto = it},
            label = { Text(text = "Preço do Produto")}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(15.dp))
        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            ,value = qtdEstq, onValueChange = {qtdEstq = it},
            label = { Text(text = "Quantidade em estoque")}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        
        Button(modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(10.dp)
            , onClick = {
                if(nomeProduto.isNotBlank() and qtdEstq.isNotBlank() and precoProduto.isNotBlank() and qtdEstq.isNotBlank()){
                    if (qtdEstq.toInt() <= 0 || precoProduto.toFloat() <= 0) {
                        Toast.makeText(context, "Quantidade e preço devem ser maiores que 0", Toast.LENGTH_SHORT).show()
                    } else {


                        Estoque.adicionarProduto(Produto(nomeProduto, catProduto, precoProduto, qtdEstq))
                        Toast.makeText(context, "Produto: ${nomeProduto} cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                        nomeProduto = ""
                        catProduto = ""
                        qtdEstq = ""
                        precoProduto = ""
                        navController.navigate("produtoscadastrados")
                    }}
                else{
                        Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                    }


            }) {
            Text(text = "Cadastrar")
        }


    }


}

@Composable
fun ProdutosCadastrados(navController: NavController){

    val listaProds = Estoque.listaProdutos

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Produtos Cadastrados: ", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(30.dp))

        LazyColumn {
            items(listaProds) {
                produtos -> Text(text = "Produto: ${produtos.nome}\tQTD: ${produtos.qtdEstoque}", fontSize = 15.sp, modifier = Modifier.padding(10.dp))
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(10.dp)
                    ,onClick = {
                        val produtoJson = Gson().toJson(produtos)
                        navController.navigate("detalhesprodutos/${produtoJson}")
                    }) {
                    Text(text = "Detalhes")
                }
            }

        }
        
        Button(onClick = { navController.navigate("estatisticas") }) {
            Text(text = "Estatísticas")
            
        }

    }
}

@Composable
fun DetalhesProdutos(navController: NavController, produto: Produto){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Detalhes do produto", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "Produto: ${produto.nome}", fontSize = 15.sp)
        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "Categoria: ${produto.categoria}", fontSize = 15.sp)
        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "Preço: R$${produto.preco}", fontSize = 15.sp)
        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "Qtd. em Estoque: ${produto.qtdEstoque}", fontSize = 15.sp)
        Spacer(modifier = Modifier.height(15.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Voltar")
        }
    }
}

@Composable
fun Estatisticas(navController: NavController){
    val valorEstq = Estoque.calcularValorTotalEstoque()
    val qtdEstq = Estoque.calcularQtdEstoque()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Estatísticas", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(text = "Valor total do estoque: R$${valorEstq}", fontSize = 15.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Quantidade total de produtos em estoque: ${qtdEstq}", fontSize = 15.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Voltar")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun LayoutPreview() {
    Navegacao()
}