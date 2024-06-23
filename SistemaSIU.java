package aed;

import java.util.List;

public class SistemaSIU {
    private Trie<Carreras> carreras;
    private Trie<Estudiantes> estudiantes;

    enum CargoDocente {
        AY2, AY1, JTP, PROF
    }

    // constructor
    public SistemaSIU() {
        this.carreras = new Trie<>();
        this.estudiantes = new Trie<>();
    } // Complejidad: O(1)

    // constructor con InfoMateria y libretas universitarias
    public SistemaSIU(InfoMateria[] infoMaterias, String[] libretasUniversitarias) {
        this();
        for (InfoMateria info : infoMaterias) {
            for (ParCarreraMateria par : info.getParesCarreraMateria()) {
                Carreras carrera = carreras.obtenerDef(par.getCarrera());
                if (carrera == null) {
                    carrera = new Carreras(par.getCarrera());
                    carreras.insertar(par.getCarrera(), carrera);
                }
                Materia materia = carrera.obtenerMateria(par.getNombreMateria());
                if (materia == null) {
                    carrera.agregarMateria(par.getNombreMateria(), new ParCarreraMateria[]{par});
                }
            }
        }
        for (String lu : libretasUniversitarias) {
            Estudiantes estudiante = new Estudiantes();
            estudiantes.insertar(lu, estudiante);
        }
    } // Complejidad: O(Pc * |c| * |Mc| + Pm * |Nm| + E)

    public void inscribir(String estudiante, String carrera, String materia) { // inscribe al estudiante en la materia `nombreMateria` de la carrera `carrera`.
        Estudiantes est = estudiantes.obtenerDef(estudiante);
        if (est != null) {
            Carreras car = carreras.obtenerDef(carrera);
            if (car != null) {
                Materia mat = car.obtenerMateria(materia);
                if (mat != null) {
                    mat.agregarAlumno(estudiante);
                    est.inscribirMateria(materia, new ParCarreraMateria[]{new ParCarreraMateria(carrera, materia)});
                }
            }
        }
    } // Complejidad: O(|c| + |Mc|)

    public void agregarDocente(CargoDocente cargo, String carrera, String materia) {// agrega un docente con el cargo `cargo` a la materia `nombreMateria` de la carrera `carrera`.
        Carreras car = carreras.obtenerDef(carrera);
        if (car != null) {
            Materia mat = car.obtenerMateria(materia);
            if (mat != null) {
                mat.agregarDocente(cargo.toString());
            }
        }
    }// Complejidad: O(|c| + |Mc|)

    public int[] plantelDocente(String materia, String carrera) { // devuelve un arreglo con la cantidad de docentes de cada tipo para la materia `nombreMateria` de la carrera `carrera`.
        Carreras car = carreras.obtenerDef(carrera);
        if (car != null) {
            Materia mat = car.obtenerMateria(materia);
            if (mat != null) {
                return mat.obtenerDocentes();
            }
        }
        return new int[]{0, 0, 0, 0};
    } // Complejidad: O(|c| + |Mc|)

    public void cerrarMateria(String materia, String carrera) { // Cierra la materia `nombreMateria` en todas las carreras por falta o exceso de docentes.
        Carreras car = carreras.obtenerDef(carrera);
        if (car != null) {
            car.eliminarMateria(materia);
        }
    } // Complejidad: O(|c| + |Mc| + |Nm| + Em)

    public int inscriptos(String materia, String carrera) { // devuelve la cantidad de estudiantes inscriptos en la materia `nombreMateria` de la carrera `carrera`.
        Carreras car = carreras.obtenerDef(carrera);
        if (car != null) {
            Materia mat = car.obtenerMateria(materia);
            if (mat != null) {
                return mat.obtenerCantidadAlumnos();
            }
        }
        return 0;
    } // Complejidad: O(|c| + |Mc|)

    public boolean excedeCupo(String materia, String carrera) { // indica si la cantidad de inscriptos en la materia `nombreMateria` de la carrera `carrera` excede el cupo.
        Carreras car = carreras.obtenerDef(carrera);
        if (car != null) {
            Materia mat = car.obtenerMateria(materia);
            if (mat != null) {
                return mat.excedeCupo();
            }
        }
        return false;
    } // Complejidad: O(|c| + |Mc|)

    public String[] carreras() { // devuelve un arreglo ordenado alfabéticamente con todas las carreras del sistema.
        List<String> listaCarreras = carreras.obtenerClaves(); 
        return listaCarreras.toArray(new String[0]);
    } // Complejidad: O(Pc * |c|)

    public String[] materias(String carrera) { // devuelve un arreglo ordenado alfabéticamente con todas las materias de la carrera `carrera`.
        // Complejidad: O(|c| + Pmc * |mc|)
        Carreras car = carreras.obtenerDef(carrera);
        if (car != null) {
            List<String> listaMaterias = car.obtenerTodasLasMaterias();
            return listaMaterias.toArray(new String[0]);
        }
        return new String[0];
    } // Complejidad: O(|c| + Pmc * |mc|)

    public int materiasInscriptas(String estudiante) { // devuelve la cantidad de materias en las que está inscrito el estudiante `estudiante`.
        Estudiantes est = estudiantes.obtenerDef(estudiante);
        if (est != null) {
            return est.materiasInscriptas().size();
        }
        return 0;
    }// Complejidad: O(1)
}
