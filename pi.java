
 import java.util.*;
 import java.util.Scanner;

 public class pi
 {
    public static void main(String[] args)
    {
        //Mensaje de bienvenida
        System.out.println("|----------------------------------------------------------------------------|");
        //System.out.println("|--------------------------------Integrantes---------------------------------|");
        //System.out.println("|A01113049 - Luis Alfonso Rojo Sánchez---------------------------------------|");
        //System.out.println("|----------------------------------------------------------------------------|");
        System.out.println("|Hola!, este es un programa que calcula el valor de pi basandose en la serie:|");
        System.out.println("|       pi = 4 X (1 - 1/3 + 1/5 - 1/7 + 1/9 - 1/11 + 1/13 - 1/15 + ...)      |");
        System.out.println("|----------------------------------------------------------------------------|");

        //Declaración de variables
        double dValorPiEstimado = 0;    //Valor estimado de Pi
        double dAcumulador = 0;         //Valor a ser restado a 4
        int iValorMaximo;               //Valor máximo del denominador
        double dDenominador = 3;

        Scanner scan = new Scanner(System.in);

        System.out.print("Teclea el número que será el mayor denominador de la sucesión: ");
        iValorMaximo = scan.nextInt();
        
        //System.out.println("El valor máximo es: " + iValorMaximo);
        
        int iSaltos = (((iValorMaximo - 3) / 2) + 1);
        //System.out.println("Número de saltos: " + iSaltos);
        
        for(double x = 1; x <= iSaltos; x++)
        {
            if(x % 2 != 0)
            {
                //System.out.println("impar");
                dAcumulador -= (1/dDenominador);
                //System.out.println(dAcumulador);
            }
            else if(x % 2 == 0)
            {
                //System.out.println("par");
                dAcumulador += (1/dDenominador);
                //System.out.println(dAcumulador);
            }
            
            dDenominador += 2;
        }
        
        dValorPiEstimado = 4.0 * (1.0 + dAcumulador);
        
        System.out.println("Valor calculado de pi: " + dValorPiEstimado);
        
        System.out.println("El valor de pi es: " + Math.PI);
        
        System.out.println("El valor calculado con respecto a Math.PI es: " + ((dValorPiEstimado * 100) / Math.PI) + " %");
        
    }
}
