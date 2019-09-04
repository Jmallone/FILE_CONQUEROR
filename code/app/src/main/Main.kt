//------------------------------------------------------------
//				PROJETO FILE CONQUEROR - BCC35A
//
//Projeto com o intuito de fazer um sistema de arquivo
//em kotlin sem usar chamada de sistema "syscall".
//Comandos de [ls,cd,mkdir,mv,rmdir,rm,clear,man,locate,cat]
//
//Autores: Michel Gomes & Juliano Petini
//------------------------------------------------------------

//------------------------------------------------------------
//						!IMPORTANTE!
//
//Para Rodar o Kotlin use o commando para compilar:
//
//kotlinc <nome do arquivo> -include-runtime -d hello.jar
//
//java -jar hello.jar
//------------------------------------------------------------

package main
import java.io.File

//------------------------------------------------------------
// 					Sistema de Cores
const val ESC = "\u001B"
const val NORMAL = ESC + "[0"		//Fonte normal
const val BOLD   = ESC + "[1"		//Negrito
const val BLACK_B  = ESC + "[0;40m" //Black background
const val WHITE_F  = ESC + "[0;37m" //Normal white foreground
const val RED = ";31m"				//Cor vermelha
const val GREEN = ";32m"			//Cor verde
const val YELLOW = ";33m"			//Cor amarela
const val BLUE = ";34m"				//Cor azul
const val MAGENTA = ";35m"			//Cor magenta
const val CYAN = ";36m"				//Cor de ciano
const val WHITE = ";37m"			//Cor branca
const val TESTE = ";92m"			//Cor de teste
//------------------------------------------------------------

var currentFolder = ArrayList<String>() //Diretório atual

//------------------------------------------------------------
//Funcao que monta e controla o shell.
fun main(args: Array<String>) {
	logo_command()
	

	try{
		if(File(args[0]).isDirectory()) {
			currentFolder.add(args[0])
		}
		else{
			println("Diretório invalido, definido o diretório padrao.")
			currentFolder.add("./")
		}
	}catch(e: Exception){
		currentFolder.add("./")
	}

	var cmd : String = "";

	while(cmd !=  "exit" ){
			print(stackFolder());
			print("> ");
			cmd = readLine()!!;
			var cmd_parts = cmd.split(" ")
			when(cmd_parts[0]){
				"cd" -> cd_command(cmd_parts)
				"ls" -> ls_command(cmd_parts)
				"mkdir" -> mkdir_command(cmd_parts)
				"rmdir" -> rmdir_command(cmd_parts)
				"clear" -> print("${ESC}c")
				"man" -> man_command(cmd_parts)
				"cat" -> cat_command(cmd_parts)
				"rm" -> rm_command(cmd_parts)
				"mv" -> mv_command(cmd_parts)
				"mkfile" -> mkfile_command(cmd_parts)
				"locate" -> locate_command(cmd_parts,stackFolder())
				"exit" -> println("Saindo do Shell!")
				else -> println("Esse comando não existe, tente 'man help'.")
			}

	}
}

//------------------------------------------------------------
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

	print("$NORMAL;90m    Versão 1.0 Beta ")
	println("$BOLD;92m                                 Criado por Michel Gomes & Juliano Petini$WHITE_F\n")
	
	return ""

}

//------------------------------------------------------------
//Funcao de help para os comandos existentes.
fun man_command(cmd_parts: List<String>){
	if (existParam(cmd_parts, "man")){	
		when(cmd_parts[1]){
			"cd" -> println("\nNOME - CD\n\n    cd - comando para navegar entre as pastas.\n    cd ./ - para voltar para a pasta raiz.\n    cd .. - para voltar a uma pasta anterior.\n\nExemplo: cd test\n")
			"ls" -> println("\nNOME - LS\n\n    ls: comando para mostrar o conteudo da pasta atual.\n")
			"rmdir" -> println("\nNOME - RMDIR\n\n    rmdir: comando para excluir pastas.\n\nExemplo: rmdir teste.\n")
			"mkdir" -> println("\nNOME - MKDIR\n\n    mkdir - comando para criar pastas.\n\nExemplo: mkdir teste.\n")
			"exit" -> println("\nNOME - EXIT\n\n    exit - comando para sair do shell.\n")
			"clear" -> println("\nNOME - CLEAR\n\n    clear - comando para limpar a tela.\n")
			"locate" -> println("\nNOME - CLEAR\n\n    locate - comando para listar recursivamente arquivos de um diretorio.\n")
			"mkfile" -> println("\nNOME - MKFILE\n\n	mkfile - comando para criar arquvios.\n")
			"cat" -> println("\nNOME - CAT\n\n    cat - comando para mostrar o conteudo de um arquivo.\n\nExemplo: cat ola.txt\n")
			"rm" -> println("\nNOME - RM\n\n    rm - comando para deletar um arquivo.\n\nExemplo: rm ola.txt\n")
			"mv" -> println("\nNOME - MV\n\n    mv - comando para mover arquivo de diretorio.\n\nExemplo: mv ola.txt pastadestino\n")
			"help" -> println(logo_command()+"\nNOME - HELP\n\n    Comandos Disponiveis:[ cd, ls, cat, rm, rmdir, mkdir, help,clear, man, exit ]\n\nPara saber mais informações é só digitar 'man <comando>'.\nExemplo: man ls\n")
			"man" -> println("\nNOME - MAN\n\n   man - comando de manual de refêrencia, digite 'man help' para obter mais informações.\n");
			else -> println("Esse comando não existe, tente 'man help'.\n")	
		}
	}
}

//------------------------------------------------------------
//Funcao que possibilita a navegacao entre diretorios.
fun  cd_command(cmd_parts: List<String>){
	
	if (existParam(cmd_parts, "cd")){
		
		if(cmd_parts[1] == "./") {
			currentFolder.clear()
			currentFolder.add(cmd_parts[1])
		}else if(cmd_parts[1] == ".."){ 
			stackFolderBack()
		}
		else if (!existFIle(cmd_parts[1])) {
			println("cd: ${cmd_parts[1]}: Arquivo ou diretório inexistente.");
		}else{	
			currentFolder.add("${cmd_parts[1]}/")
		}
	
	}
}

//------------------------------------------------------------
//Funcao que lista todo conteudo do diretorio atual.
fun  ls_command(cmd_parts: List<String>){
	if(!(cmd_parts.size < 2 || cmd_parts[1] == "")){
		println("FAZER DEPOIS");
	}
	else{
		val folders = File(stackFolder()).listFiles().map{ it.name }
		for (name in folders){
			if(File(stackFolder()+name).isDirectory()){
				print("$NORMAL$CYAN$name $WHITE_F")
			}else{
				if(name.contains(".jar")){
					print("$NORMAL$RED$name $WHITE_F")
				}else{
					print("$name ")
				}
			}
		}
		println("")
	}
}

//------------------------------------------------------------
//Funcao que lista o arquivo selecionado nos diretorios recursiamente.
fun locate_command(cmd_parts: List<String>,dir:String){
	if(existParam(cmd_parts, "locate")){
	var tmp = File(dir).listFiles().map{ it.name }
		for (folder in tmp){
			if(folder.contains(cmd_parts[1])) println(dir+"/"+folder)
			if(File(dir+"/"+folder).isDirectory())
			{	
				locate_command(cmd_parts,dir+"/"+folder)
			}
	}
	}
}

//------------------------------------------------------------
//Funcao que move arquivos de diretorios.
fun mv_command(cmd_parts: List<String>){
	if(existParam(cmd_parts, "mv")){
		if(existFIle(cmd_parts[1]) && cmd_parts.size == 3 && !File(stackFolder()+cmd_parts[1]).isDirectory()){
			var tmp = File(stackFolder()+cmd_parts[1]).readText()
			rm_command(cmd_parts)
			var file = File(stackFolder()+cmd_parts[2])
			file.writeText(tmp)
		}else{
			println("mv: Tente 'man mv' para mais informações.")
		}
	}
}

//------------------------------------------------------------
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

//------------------------------------------------------------
//Funcao que cria um diretorio.
fun mkdir_command(cmd_parts: List<String>){

	if(existParam(cmd_parts, "mkdir")){

		if (existFIle(cmd_parts[1])) {
			println("mkdir: não foi possível criar o diretório “${cmd_parts[1]}”: Arquivo existe.");
		}else{
			val file = File(stackFolder(),cmd_parts[1])
			try{
				file.mkdir()
			}catch(e: Exception){
				println("Houve Erro de Exceção");
				e.printStackTrace()
			}
		}
	}
}

//------------------------------------------------------------
//Funcao que cria arquivo.
fun mkfile_command(cmd_parts: List<String>){
	if(existParam(cmd_parts, "mkfile")){
		if (!existFIle(cmd_parts[1])) {
			var texto = ""
			var i = 2;
			while(i < cmd_parts.size){
				texto = texto +cmd_parts[i].replace("\"" , "")
				texto = texto + " "
				i++;
			} 
			File(cmd_parts[1]).writeText(texto)
		}
	}
}

//------------------------------------------------------------
//Funcao que remove um diretorio ou arquivo.
fun rmdir_command(cmd_parts: List<String>){

	if(existParam(cmd_parts, "rmdir")){

		if (!existFIle(cmd_parts[1])) {
			println("rmdir: falhou em remover “${cmd_parts[1]}”: Arquivo ou diretório inexistente.");
		}else{
			val file = File(stackFolder(),cmd_parts[1])
			file.deleteRecursively()
		}
	}
}

//------------------------------------------------------------
//Funcao que remove um arquivo.
fun rm_command(cmd_parts: List<String>){

	if(existParam(cmd_parts, "rm")){
		if(!File(stackFolder()+cmd_parts[1]).isDirectory() && existFIle(cmd_parts[1])){
			
			File(stackFolder(),cmd_parts[1]).delete()
		}
		else{
			println("rm: não foi possivel remover '${cmd_parts[1]}': É um diretório ou não existe.")
		}
	}
}

//------------------------------------------------------------
//Funcao que verifica a existencia de um arquivo ou diretorio.
fun existFIle(folder: String) = File(stackFolder()).listFiles().map{ it.name }.contains(folder)

//------------------------------------------------------------
//Funcao que retorna o caminho completo do diretorio atual.
fun stackFolder():String{
	var tmpFolder = ""
	for (i in currentFolder){
		tmpFolder = "$tmpFolder$i"
	}
	return tmpFolder
}

//------------------------------------------------------------
//Funcao que volta um diretorio.
fun stackFolderBack(){
	if(currentFolder.size > 1){
		currentFolder.removeAt(currentFolder.size-1)
	}
}

//------------------------------------------------------------
//Funcao que verifica se os parametros do comando estao certo.
fun existParam(cmd_parts: List<String>, command: String ): Boolean{
	if(cmd_parts.size < 2 || cmd_parts[1] == ""){
		println("Tente 'man $command' para mais informações.")
		return false
	}
	return true
} 
//------------------------------------------------------------
