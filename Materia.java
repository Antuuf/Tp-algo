package aed;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Materia {
    private int[] docentes;
    private ListaEnlazada<String> alumnos;
    private ParCarreraMateria[] otrosNombres; //(nombredeltrie,nombremateria) //lista de los parCarrera
    private ArrayList<Trie<Materia>> referencias; //guarda la referencia del trie para poder llamarlo despues y que no se vaya la complejidad

    public Materia(ParCarreraMateria[] info){
        this.docentes= new int[4];
        this.alumnos= new ListaEnlazada<>();
        this.otrosNombres= info;
        this.referencias= new ArrayList<Trie<Materia>>();
    }

    public void agregarReferencia(Trie<Materia> referencia){ 
        this.referencias.add(referencia);
    }

    public ArrayList<Trie<Materia>> darReferencia(){
        return referencias;
    }
    
    public void agregarAlumno(String alumno){
        this.alumnos.agregarAtras(alumno);
    }

    public void agregarDocente(String cargo){
        if (cargo=="PROFE"){
            this.docentes[0]++;
        }
        if (cargo=="JTP"){
            this.docentes[1]++;
        }
        if (cargo=="AY1"){
            this.docentes[2]++;
        }
        if (cargo=="AY2"){
            this.docentes[3]++;
        }
    }

    public int[] obtenerDocentes(){
        return this.docentes;
    }

    public ListaEnlazada<String> obtenerListaAlumnos(){
        return alumnos;
    }

    public ParCarreraMateria[] obtenerNombres(){
        return otrosNombres;
    }
   
    public int obtenerCantidadAlumnos(){
        return this.alumnos.longitud();
    }

    public boolean excedeCupo(){ // true si excede el cupo de alumnos, false si no
        int cant= obtenerCantidadAlumnos();
        if (docentes[0]*250<cant){
            return true;
        }
        if (docentes[1]*100<cant){
            return true;
        }
        if (docentes[2]*20<cant){
            return true;
        }
        if (docentes[3]*30<cant){
            return true;
        }
        return false;
    }
    
}
