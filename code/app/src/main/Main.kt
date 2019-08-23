package main


fun main(args: Array<String>) {

var cmd : String = "";

while(cmd !=  "exit" ){

		print("> ");
		cmd = readLine()!!;
		var cmd_parts = cmd.split(" ");

		when(cmd_parts[0]){
			"cd" -> cd_command(cmd_parts);
			"ls" -> ls_command(cmd_parts);
		}

	}
}

fun  cd_command(cmd_parts: List<String>){
	println("Você digitou CD, o restante dos parametros é $cmd_parts ");
}

fun  ls_command(cmd_parts: List<String>){
	println("Você digitou LS, o restante dos parametros é $cmd_parts ");
}
