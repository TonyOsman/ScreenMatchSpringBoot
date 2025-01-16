package br.com.alura.screenmatch;

import br.com.alura.screenmatch.Principal.Principal;
import br.com.alura.screenmatch.Service.ConsumoAPI;
import br.com.alura.screenmatch.model.DadosTemporada;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(ScreenmatchApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		ConsumoAPI consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=2c1cb14c");

		Principal principal  = new Principal();
		principal.exibeMenu();








	}
}
