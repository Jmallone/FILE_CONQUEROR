package main
import java.io.File
//http://thetechnocafe.com/build-a-file-explorer-in-kotlin-part-5-creating-deleting-files-and-folders/
//Referencias

var currentFolder = ArrayList<String>()


fun main(args: Array<String>) {
currentFolder.add("./")
var cmd : String = "";

while(cmd !=  "exit" ){
		print(stackFolder());
		print(" > ");
		cmd = readLine()!!;
		var cmd_parts = cmd.split(" ");
		when(cmd_parts[0]){
			"cd" -> cd_command(cmd_parts);
			"ls" -> ls_command();
			"mkdir" -> mkdir_command(cmd_parts);
			"rmdir" -> rmdir_command(cmd_parts);
			"man" -> man_command(cmd_parts);
			else -> println("Esse comando não existe, tente 'man help'.")
		}

	}
}

fun man_command(cmd_parts: List<String>){
	if (existParam(cmd_parts, "man")){	
		when(cmd_parts[1]){
			"cd" -> println("\nCD: comando para navegar entre as Pastas.\nEx: cd teste\n Usa-se 'cd ./' para voltar para a pasta raiz.\n")
			"ls" -> println("\nLS: comando para mostrar o conteudo da pasta atual.\n")
			"rmdir" -> println("\nRMDIR: comando para excluir pastas.\nEx: rmdir teste.\n")
			"mkdir" -> println("\nMKDIR: comando para criar pastas.\nEx: mkdir teste.\n")
			"exit" -> println("\nExit: comando para sair do shell.\n")
			"help" -> println("\nComandos Disponiveis: cd, ls, rmdir, mkdir, help, man, exit.\nPara saber mais informações é só digitar 'man <comando>'.\nEx: man ls\n")
			"man" -> println("MAN: comando do manual, digite 'man help' para obter mais informações.")
			else -> println("Esse comando não existe, tente 'man help'.")	
		}
	}
}

fun  cd_command(cmd_parts: List<String>){
	
	if (existParam(cmd_parts, "cd")){

		//if(cmd_parts[1] == "./") currentFolder = "./"

		if(cmd_parts[1] == ".."){ 
			stackFolderBack()
		}
		else if (!existFolder(cmd_parts[1])) {
			println("cd: ${cmd_parts[1]}: Arquivo ou diretório inexistente");
		}else{	
			currentFolder.add("${cmd_parts[1]}/")
		}
	
	}
}

fun  ls_command(){
	val folders = File(stackFolder()).listFiles().map{ it.name }
	for (name in folders){
		print("$name ")
	}
	println("")
}

fun mkdir_command(cmd_parts: List<String>){

	if(existParam(cmd_parts, "mkdir")){

		if (existFolder(cmd_parts[1])) {
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

fun rmdir_command(cmd_parts: List<String>){

	if(existParam(cmd_parts, "rmdir")){

		if (!existFolder(cmd_parts[1])) {
			println("rmdir: falhou em remover “${cmd_parts[1]}”: Arquivo ou diretório inexistente");
		}else{
			val file = File(stackFolder(),cmd_parts[1])
			//if (file.isDirectory) {
			file.deleteRecursively()
			//} else {
			//	file.delete()
			//}
		}
	}
}


fun existParam(cmd_parts: List<String>, command: String ): Boolean{
	if(cmd_parts.size < 2 || cmd_parts[1] == ""){
		println("Tente 'man $command' para mais informações.")
		return false
	}
	return true
} 

fun existFolder(folder: String) = File(stackFolder()).listFiles().map{ it.name }.contains(folder)

fun stackFolder():String{
	var tmpFolder = ""
	for (i in currentFolder){
		tmpFolder = "$tmpFolder$i"
	}

	return tmpFolder
}

fun stackFolderBack(){
	if(currentFolder.size > 1){
		currentFolder.removeAt(currentFolder.size-1)
	}
}
