package grafo_arreglobfs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


class Vertices{
        String nombre;
        boolean visitado;
        
        public Vertices(String nombre){
            this.nombre = nombre;
            this.visitado = false;
        }
}

class Grafo {
    private final int MAX_VERTICES = 30;
    public Vertices vertices[];
    public int MatrizAdy[][]; 
    public int NumVert; //Numero de vertices existentes
    public Grafo() {    //Constructor
        this.vertices = new Vertices[MAX_VERTICES];
        for (int i = 0; i < MAX_VERTICES; i++) {
            this.vertices[i] = new Vertices("");
        }        
        this.MatrizAdy = new int [MAX_VERTICES][MAX_VERTICES];
        for(int i = 0;i<MAX_VERTICES; i++){
            for(int j = 0;j<MAX_VERTICES; j++){
                MatrizAdy[i][j] = 0;
            }
        }
        this.NumVert = 0;
    }
    public void AgregaVertice(String nombre){
        int i = 0;
        int contador = 0;
        while(i<this.NumVert){
            if(this.vertices[i].nombre.equals(nombre)){
                contador++;
            }
            i++;
        }
        if(contador == 0){
            vertices[this.NumVert].nombre = nombre;
            this.NumVert++;            
        }
    }
    public void AgregaConexion(String desde, String hacia, String peso){
        int indOrigen = obtenIndice(this, desde);
        int indDestino= obtenIndice(this, hacia);
        this.MatrizAdy[indOrigen][indDestino] = Integer.parseInt(peso);
    }
    public Integer obtenIndice(Grafo G, String vert){
        int i = 0;
        while(i<G.NumVert){
            if(G.vertices[i].nombre.equals(vert)){
                return i;
            }
            i++;
        }
        return 0;
    }  
}

public class Grafo_ArregloBFS {

 
    public static void main(String[] args) throws IOException {
        if (args.length>=1){
            String archivo = args[0];
            Grafo g = CargaArchivo( archivo );
            DespliegaMatriz(g);            
            Graba_grafo( g );
            
        if (args.length >= 3){            
            DFS( g, args[1], args[2]);
        }
                      
        }        
                        
    }
    
    public  static Grafo CargaArchivo(String archivo_csv){
        Grafo g = new Grafo();
        try {
            Scanner sc = new Scanner(new File(archivo_csv));
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                if (linea.isEmpty()){
                    continue;
                }
                String[] arreglo = linea.split(",");
                String origen    = arreglo[0].toLowerCase();
                String destino   = arreglo[1].toLowerCase();
                String distancia = arreglo[2].toLowerCase();
                g.AgregaVertice(origen);
                g.AgregaVertice(destino);
                g.AgregaConexion(origen, destino, distancia);
            }
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return g;
    }
    
    public static void DespliegaMatriz(Grafo g){        
        
        for (int k = 0; k < g.NumVert; k++) {
            System.out.println(k + ": " +  g.vertices[k].nombre);
        }
        System.out.print("  ");
        for (int i = 0; i < g.NumVert; i++) {
            System.out.print(" " + i);
        }
        System.out.println(" ");
        System.out.println("   ----------------------");    
        
        for (int i = 0; i < g.NumVert; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < g.NumVert; j++) {
                System.out.print(" " + g.MatrizAdy[i][j]);
            }
            System.out.println(" ");
        }                 
    }
         
    public static void Graba_grafo(Grafo g) throws IOException{        
        File salida = new File("salida.txt");        
        PrintStream ps = new PrintStream(salida);
        
        for (int i = 0; i < g.NumVert; i++) {
            for (int j = 0; j < g.NumVert; j++) {
                ps.println("\"" + g.vertices[i].nombre + "\"" + " -> " + "\"" + g.vertices[j].nombre+ "\"" + " [Label= " + g.MatrizAdy[i][j]+ "]");
            }            
        }                        
        ps.flush();        
    }
    
    public static void DFS(Grafo g,String desde, String hacia){
        boolean encontrado = false;                        
        Queue<String> nodo = new LinkedList<>();
        nodo.add(desde);
            
        while((nodo.isEmpty() != true) && (encontrado == false)){
            System.out.println(nodo);
            String vertice = nodo.poll();
            
            if (vertice.equals(hacia)) {
                encontrado = true;
                
            } else {
                
                push_adyacent_edges(nodo, g, vertice);                
            }
               
        }
        
        if (encontrado) System.out.println("Si hay camino de " + desde + " a " + hacia + " :)");
        
        else System.out.println("No hay camino de " + desde + " a " + hacia + " :')");
        
    }
    
    public static void push_adyacent_edges(Queue<String> s, Grafo g, String vertice){
        
        for (int i = 0; i < g.vertices.length; i++) {            
            if (g.vertices[i].nombre.equals(vertice)) {                
                for (int j = 0; j < g.vertices.length; j++) {                    
                    if (g.MatrizAdy[i][j] != 0 ) {
                        if (g.vertices[j].visitado == false) {
                            g.vertices[j].visitado = true;
                            s.add(g.vertices[j].nombre);
                        }
                    }                    
                }                                                    
            }
        }            
    }   
    
 
    
}
