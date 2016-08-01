package test;

import java.util.Scanner; 
import java.io.File; 
import java.io.FileNotFoundException;

/**
 *  Esta clase ilustra c�mo explorar la informaci�n textual almacenada en un fichero
 *  de datos
 */

public class PruebaE {

    /*
     * Atributos o campos de cada objeto con el siguiente significado:
     *     [numLineas] : n�mero de l�neas del texto asociado al objeto
     *     [numCaracteres] : n�mero de caracteres del texto asociado al objeto
     *     [frecuencias] : tabla de frecuencias de cada una de las letras en el texto
     *                     asociado al objeto
     */
    private int numLineas = 0, numCaracteres = 0;
    private int[] frecuencias = new int['Z'-'A'+1];

    /**
     *  Crea un objeto de la clase Texto, analiza el fichero de texto de nombre [nombreTexto]
     *  y, para ese fichero, calcula los valores de los atributos [numLineas], [numCaracteres]
     *  y [frecuencias] y les asigna el valor que les corresponde
     */
    public PruebaE (String nombreTexto) {
        try {
            Scanner f = new Scanner(new File(nombreTexto));
            while (f.hasNextLine()) {
                String linea = f.nextLine().toUpperCase();
                numLineas++;
                numCaracteres += linea.length();
                for (int i=0; i<linea.length(); i++)
                    if ((linea.charAt(i)>='A') && (linea.charAt(i)<='Z'))
                        frecuencias[linea.charAt(i)-'A']++;
            }
        }
        catch (FileNotFoundException e) {
            System.out.printf("El fichero %s no ha podido ser abierto%n", nombreTexto);
        }
    }

    /**
     * Pre: ---
     * Post: Devuelve el n�mero de l�neas del fichero asociado al objeto
     */
    public int lineas () {
        return numLineas;
    }

    /**
     * Pre: ---
     * Post: Devuelve el n�mero de caracteres que almacena el fichero asociado al objeto
     */
    public int caracteres () {
        return numCaracteres;
    }

    /**
     * Pre: letra>='A' y letra<='Z'
     * Post: Devuelve el n�mero de veces que est� almacenada en el fichero asociado al objeto
     *       la letra [letra], sin hacer distinci�n entre may�sculas y min�sculas
     */
    public int frecuencia (char letra) {
        return frecuencias[letra-'A'];
    }
    
    /**
     * Pre: --
     * Post: Presenta por pantalla informaci�n sobre el n�mero de l�neas, el n�mero
     *       de caracteres y el n�mero de veces que figura cada letra en el fichero
     *       "datos\alumnos.txt"
     */
    public static void main(String[] args) {
        String nombre = "C:\\Teresa\\Cursos\\Curso INAP\\Curso Firma\\semana 2\\prueba.txt";
        PruebaE texto = new PruebaE(nombre);
        System.out.printf("El fichero %s almacena un texto%n" +
                          "con %d caracteres escritos en %d l�neas%n%n",
                          nombre, texto.caracteres(), texto.lineas());
        for (char letra='A'; letra<='Z'; letra++)
            System.out.printf("%c - %4d%n", letra, texto.frecuencia(letra));
    }

}
