package com.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * https://docs.oracle.com/javase/tutorial/collections/interfaces/map.html
 * 
 */
public class App {
    public static void main(String[] args) {

        /*
         * ¿Que es un mapa?
         * 
         * Es un tipo de coleccion que almacena la informacion en parejas de clave (key)
         * y valor (value),
         * donde las claves no se pueden repetir, es un tipo de hash.
         * 
         * No hereda de la interface Collection, por lo cual se dice que no es una
         * verdadera coleccion, aunque
         * se puede tratar como una collecion utilizando las Collection Views (Vistas de
         * Collection)
         */

        // ¿Como se puede crear un mapa? Para ejemplificar, eterminar la frecuencia de
        // ocurrencia de
        // una palabra en un array o lista de palabras

        List<String> palabras = Arrays.asList("Antonio", "Antonio", "Juan", "Antonio", "Ruben", "Marcos", "Ruben");

        Map<String, Long> m = new HashMap<>();

        // Para rellenar el mapa m, primero utilizaremos una sentencia for mejorado

        for (String palabra : palabras) {

            Long frecuenciaOcurrencia = m.get(palabra);

            m.put(palabra, frecuenciaOcurrencia == null ? 1L : ++frecuenciaOcurrencia);
        }

        System.out.println(m);

        /*
         * Lo anterior esta bien pero es antiguo, actualmente se puede obtener el mismo
         * resultado
         * con OPERACIONES DE AGREGADO (Metodos de la clase Stream, tuberias, lambdas,
         * metodos
         * por referencia, en fin, PROGRAMACION FUNCIONAL)
         */

        // palabra -> palabra es similar a utilizar Function.identity()

        Map<String, Long> m2 = palabras.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println(m2);

        /* Ejemplos tipos de creacion de mapas, colecciones Map Interface */

        /*
         * Ejemplo # 1
         * 
         * Recorrer la coleccion de personas y obtener un mapa que agrupe las personas
         * por Genero
         * 
         */

        List<Persona> personas = Persona.getPersonas();

        // Siempre que se recorra una lista del mismo tipo que el valor del mapa
        // NO hay que hacer absolutamente nada para obtener el valor del mapa
        Map<Genero, List<Persona>> personasPorGenero = personas.stream()
                .collect(Collectors.groupingBy(Persona::getGenero));

        System.out.println(("Collecion de personas agrupadas por Genero"));
        System.out.println(personasPorGenero);

        /*
         * Ejemplo # 2
         * 
         * Recorrer la lista de personas y obtener una nueva coleccion que agrupe
         * nombres de persona,
         * separados por punto y coma, por genero.
         * 
         */

        Map<Genero, String> nombresPorGenero = personas.stream()
                .collect(Collectors.groupingBy(Persona::getGenero,
                        Collectors.mapping(Persona::getNombre, Collectors.joining(";"))));

        System.out.println(nombresPorGenero);

        /*
         * Un Map Interfaces no es realmente una colecion, porque no hereda de la
         * interfaz Collection,
         * pero se puede tratar como tal utilizando las llamadas vistas de coleccion
         * (Collection Views),
         * para en cada momento acceder a las claves del mapa, a los valores o a ambos
         * 
         * https://docs.oracle.com/javase/tutorial/collections/interfaces/map.html
         * (Collection Views)
         */

        /*
         * Recorrer el mapa de personas agrupadas por Genero, para mostrar solamente las
         * personas que tengan un salario superior a los 4000 euros
         */

        /* Utilizando una sentencia for mejorada */

        for (Map.Entry<Genero, List<Persona>> entry : personasPorGenero.entrySet()) {

            Genero key = entry.getKey();
            List<Persona> value = entry.getValue();

            System.out.println("Del Genero " + key);
            System.out.println("Personas con salario superior a 4000");

            for (Persona persona : value) {
                if (persona.getSalario() > 4000)
                    System.out.println(persona);
            }
        }

        /* Utilizando Operaciones de Agregado */

        System.out.println("Utilizando Operaciones de Agregado ...");

        personasPorGenero.entrySet().stream().forEach(entry -> {
            System.out.println("Del Genero: " + entry.getKey());
            System.out.println("Personas con salario superior a 4000");

            entry.getValue().stream().filter(p -> p.getSalario() > 4000).forEach(System.out::println);
        });

        /*
         * Ejercicio # 1:
         * 
         * Crear una coleccion que agrupe Personas por Genero y Edad de la persona.
         * 
         * Recorrer la coleccion obtenida y mostras solamente las personas del Genero
         * HOMBRE, que tengan un salario superior
         * a la media.
         */

        Map<Genero, Map<Long, List<Persona>>> personasGeneroEdad = personas.stream()
                .collect(Collectors.groupingBy(Persona::getGenero,
                        Collectors.groupingBy(Persona::edad)));

        // Recuperar el salario promedio
        final Double salarioMedio = personas.stream().mapToDouble(Persona::getSalario)
                .average().orElseThrow(() -> new RuntimeException());

        // Recorrer la coleccion personasGeneroEdad
        personasGeneroEdad.entrySet().stream().forEach(entry1 -> {

            Genero genero = entry1.getKey();
            System.out.println("Genero: " + genero);

            Map<Long, List<Persona>> entry2 = entry1.getValue();

            // Collection<List<Persona>> persons = entry2.values();

            // persons.stream().flatMap(lista -> lista.stream())
            // .filter(persona -> persona.getGenero().equals(Genero.HOMBRE) &&
            // persona.getSalario() > salarioMedio)
            // .forEach(System.out::println);

            entry2.entrySet().stream().forEach(entry -> {
                List<Persona> persons = entry.getValue();
                persons.stream().filter(p -> p.getGenero().equals(Genero.HOMBRE) &&
                        p.getSalario() > salarioMedio)
                        .forEach(System.out::println);
            });

        });
    }
}
