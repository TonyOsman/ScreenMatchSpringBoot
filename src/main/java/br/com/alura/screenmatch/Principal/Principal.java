package br.com.alura.screenmatch.Principal;

import br.com.alura.screenmatch.Service.ConsumoAPI;
import br.com.alura.screenmatch.Service.ConverteDados;
import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    public static void main(String[] args) {


            Scanner entrada = new Scanner(System.in);

             Scanner scanner = new Scanner(System.in);
             ConsumoAPI consumoAPI = new ConsumoAPI();
             ConverteDados conversor = new ConverteDados();

             final String ENDERECO = "https://www.omdbapi.com/?t=";
             final String API_KEY = "&apikey=2c1cb14c";

        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = scanner.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ","+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json,DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ","+") +"&season=" + i  + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);


//        for (int i = 0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        //Jeito mais simples de fazer o que está comentado acima
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

//        List<String> nomes = Arrays.asList("Jacque","Iasmin","Paulo","Rodrigo","Nico");
//
//        nomes.stream()
//                .sorted()
//                .limit(3)
//                .filter(n -> n.startsWith("N"))
//                .map(n -> n.toUpperCase())
//                .forEach(System.out::println);
//
        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("TOP 5 episodios: ");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                .map(d -> new Episodio(t.numero(),d)))
                .collect(Collectors.toList());


        episodios.forEach(System.out::println);

        System.out.println("A PARTIR DE QUE ANO VOCÊ DESEJA VER OS EPISÓDIOS? ");
        var ano = entrada.nextInt();
        entrada.nextLine();

        LocalDate dataBusca = LocalDate.of(ano,1,1);

        //formatador de data para padrão BR
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e ->e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: "+e.getTitulo()+
                                "  - Episodio: "+e.getTitulo()+
                                " - Data Lançamento: "+ e.getDataLancamento().format(formatador)
                ));
    }

    }
