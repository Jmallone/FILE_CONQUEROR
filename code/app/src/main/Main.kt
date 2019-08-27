//Para Rodar o Kotlin use o commando para compilar
//
//kotlinc hello.kt -include-runtime -d hello.jar
//
//java -jar hello.jar

package main
import java.io.File


// Sistema de Cores
const val ESC = "\u001B"
const val NORMAL = ESC + "[0"
const val BOLD   = ESC + "[1"
const val BLACK_B  = ESC + "[0;40m"  // black background
const val WHITE_F  = ESC + "[0;37m"  // normal white foreground

const val RED = ";31m"
const val GREEN = ";32m"
const val YELLOW = ";33m"
const val BLUE = ";34m"
const val MAGENTA = ";35m"
const val CYAN = ";36m"
const val WHITE = ";37m"
const val TESTE = ";92m"


var currentFolder = ArrayList<String>()


//Funcao que monta o shell.
fun main(args: Array<String>) {
	logo_command()
	currentFolder.add("./")
	var cmd : String = "";

	while(cmd !=  "exit" ){
			print(stackFolder());
			print(" > ");
			cmd = readLine()!!;
			var cmd_parts = cmd.split(" ")
			when(cmd_parts[0]){
				"cd" -> cd_command(cmd_parts)
				"ls" -> ls_command()
				"mkdir" -> mkdir_command(cmd_parts)
				"rmdir" -> rmdir_command(cmd_parts)
				"clear" -> print("${ESC}c")
				"man" -> man_command(cmd_parts)
				"cat" -> cat_command(cmd_parts)
				"rm" -> rm_command(cmd_parts)
				"mv" -> mv_command(cmd_parts)
				"exit" -> println("Saindo do Shell!")
				else -> println("Esse comando não existe, tente 'man help'.")
			}

	}
}

//Funcao que escreve o logo no terminal.
fun logo_command(): String{
	print("${ESC}c") //Limpa a Tela

	println("\n$NORMAL;92m    88      a8P                    88 88              ad88888ba  88                     88 88 $WHITE_F")  
 	println("$NORMAL;93m    88    ,88'               ,d    88 ''             d8'     '8b 88                     88 88 $WHITE_F")  
	println("$NORMAL;94m    88  ,88'                 88    88                Y8,         88                     88 88 $WHITE_F")  
 	println("$NORMAL;95m    88,d88'      ,adPPYba, MM88MMM 88 88 8b,dPPYba,  `Y8aaaaa,   88,dPPYba,   ,adPPYba, 88 88 $WHITE_F")  
 	println("$NORMAL;96m    888888,    a8''     '8a  88    88 88 88P'   ''8a   `'''''8b, 88P'    '8a a8P_____88 88 88 $WHITE_F")  
 	println("$NORMAL;97m    88P   Y8b   8b       d8  88    88 88 88       88         `8b 88       88 8PP''''''' 88 88 $WHITE_F")
 	println("$NORMAL;98m    88     '88, '8a,   ,a8'  88,   88 88 88       88 Y8a     a8P 88       88 '8b,   ,aa 88 88 $WHITE_F")  
 	println("$NORMAL;99m    88       Y8b `'YbbdP''   'Y888 88 88 88       88  'Y88888P'  88       88  `'Ybbd8'' 88 88 $WHITE_F\n\n")

	print("Versão 0.1 ")
	println("$BOLD;92m                                         Criado por Michel Gomes & Juliano Petini$WHITE_F\n")
	
	return ""

}

//Funcao de help para os comandos existentes.
fun man_command(cmd_parts: List<String>){
	if (existParam(cmd_parts, "man")){	
		when(cmd_parts[1]){
			"cd" -> println("\nCD: comando para navegar entre as Pastas.\nEx: cd teste\n Usa-se 'cd ./' para voltar para a pasta raiz.\n")
			"ls" -> println("\nLS: comando para mostrar o conteudo da pasta atual.\n")
			"rmdir" -> println("\nRMDIR: comando para excluir pastas.\nEx: rmdir teste.\n")
			"mkdir" -> println("\nMKDIR: comando para criar pastas.\nEx: mkdir teste.\n")
			"exit" -> println("\nEXIT: comando para sair do shell.\n")
			"clear" -> println("\nCLEAR: comando para limpar a tela.\n")
			"cat" -> println("\nCAT: comando para mostrar o conteudo de um arquivo.\nEx: cat ola.txt")
			"rm" -> println("\nRM: comando para deletar um arquivo.\nEx: rm ola.txt")
			"mv" -> println("\nMV: comando para mover arquivos de diretorios.\nEx: mv ola.txt pastadestino")
			"help" -> println(logo_command()+"\nComandos Disponiveis: cd, ls, rmdir, mkdir, help, man, exit.\nPara saber mais informações é só digitar 'man <comando>'.\nEx: man ls\n")
			"man" -> println("MAN: comando do manual, digite 'man help' para obter mais informações.");
			else -> println("Esse comando não existe, tente 'man help'.")	
		}
	}
}

//Funcao que possibilita a navegacao entre diretorios.
fun  cd_command(cmd_parts: List<String>){
	
	if (existParam(cmd_parts, "cd")){

		if(cmd_parts[1] == ".."){ 
			stackFolderBack()
		}
		else if (!existFIle(cmd_parts[1])) {
			println("cd: ${cmd_parts[1]}: Arquivo ou diretório inexistente");
		}else{	
			currentFolder.add("${cmd_parts[1]}/")
		}
	
	}
}

//Funcao que lista todo conteudo do diretorio atual.
fun  ls_command(){
	val folders = File(stackFolder()).listFiles().map{ it.name }
	for (name in folders){
		if(File(stackFolder()+name).isDirectory()){
			print("$NORMAL$CYAN$name $WHITE_F")
		}else{
			print("$name ")
		}

	}
	println("")
}

//Funcao que move arquivos de diretorios.
fun mv_command(cmd_parts: List<String>){
	if(existParam(cmd_parts, "mv")){
		if(existFIle(cmd_parts[1]) && cmd_parts.size == 3 && !File(stackFolder()+cmd_parts[1]).isDirectory()){
			println(cat_command(cmd_parts))
		}
	}
}

//Funcao que le conteudos de um arquivo.
fun cat_command(cmd_parts: List<String>):String{
	if(existParam(cmd_parts, "cat")){
		if(!File(stackFolder()+cmd_parts[1]).isDirectory() && existFIle(cmd_parts[1])){
			var tmp = File(stackFolder()+cmd_parts[1]).readText()
			println(tmp)
			return tmp
		}
		else{
			println("cat: ${cmd_parts[1]}: É um diretorio ou arquivo não existente.")
		}
	}
	return ""
}

//Funcao que cria um diretorio.
fun mkdir_command(cmd_parts: List<String>){

	if(existParam(cmd_parts, "mkdir")){

		if (existFIle(cmd_parts[1])) {
			println("mkdir: não foi possível criar o diretório “${cmd_parts[1]}”: Arquivo existe");
		}else{
			val file = File(stackFolder(),cmd_parts[1])
			try{
				file.mkdir()
			}catch(e: Exception){
				println("Houve Erro de Exception");
				e.printStackTrace()
			}
		}
	}
}

//Funcao que remove um diretorio ou arquivo.
fun rmdir_command(cmd_parts: List<String>){

	if(existParam(cmd_parts, "rmdir")){

		if (!existFIle(cmd_parts[1])) {
			println("rmdir: falhou em remover “${cmd_parts[1]}”: Arquivo ou diretório inexistente");
		}else{
			val file = File(stackFolder(),cmd_parts[1])
			file.deleteRecursively()
		}
	}
}

//Funcao que remove um arquivo.
fun rm_command(cmd_parts: List<String>){

	if(existParam(cmd_parts, "rm")){
		if(!File(stackFolder()+cmd_parts[1]).isDirectory() && existFIle(cmd_parts[1])){
			
			File(stackFolder(),cmd_parts[1]).delete()
		}
		else{
			println("rm: não foi possivel remover '${cmd_parts[1]}': É um diretório")
		}
	}
}

//Funcao que verifica a existencia de um arquivo ou diretorio.
fun existFIle(folder: String) = File(stackFolder()).listFiles().map{ it.name }.contains(folder)

//Funcao que retorna o caminho completo do diretorio atual.
fun stackFolder():String{
	var tmpFolder = ""
	for (i in currentFolder){
		tmpFolder = "$tmpFolder$i"
	}
	return tmpFolder
}

//Funcao que volta um diretorio.
fun stackFolderBack(){
	if(currentFolder.size > 1){
		currentFolder.removeAt(currentFolder.size-1)
	}
}

//Funcao que verifica se os parametros do comando estao certo.
fun existParam(cmd_parts: List<String>, command: String ): Boolean{
	if(cmd_parts.size < 2 || cmd_parts[1] == ""){
		println("Tente 'man $command' para mais informações.")
		return false
	}
	return true
} 