import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Распределение студентов по преподавателям
 * Холодов А.С.
 */
public class Main {

    /** Номера студентов по списку */
    private static final List<Integer> students = new ArrayList<>();

    /** Преподаватели и кол-во студентов */
    private static final List<Lecturer> lecturers = new ArrayList<>();

    static {
        students.addAll(IntStream.rangeClosed(1, 21).boxed().collect(Collectors.toList()));
//        students.addAll(Arrays.asList(2, 3, 5, 6, 7, 9, 12, 13, 14, 15, 18, 20));
        lecturers.addAll(Arrays.asList(
                new Lecturer("Холодов А.С.", 3),
                new Lecturer("Нухулов С.М.", 3),
                new Lecturer("Рогозинников Е.И.", 3),
                new Lecturer("Рыжков А.К.", 3)
        ));
    }


    public static void main(String[] args) {

        /* Перемешивание */
        IntStream.range(0, (int) (Math.random()*10)).forEach(i ->{
            Collections.shuffle(students);
            Collections.shuffle(lecturers);
        });

        /* Распределение */
        while(lecturers.stream().filter(l -> !l.isFull()).findFirst().orElse(null) != null)
            lecturers.forEach((l) -> { if (!l.isFull()) l.getStudents().add(randomStudent()); });

        /* Результаты */
        for(Lecturer l: lecturers){
            Collections.sort(l.getStudents());
            StringBuilder res = new StringBuilder(l.getName()).append(" : { ");
            for(int s: l.getStudents()) res.append(s).append(" ");
            System.out.println(res + "}");
        }
        if(!students.isEmpty()) {
            Collections.sort(students);
            StringBuilder err = new StringBuilder("Есть нераспределенные студенты: { ").insert(0, "\n");
            for (int s: students) err.append(s).append(" ");
            System.err.println(err + "}");
        }
    }

    /** Рандомный студент */
    private static int randomStudent() {
        AtomicInteger st = new AtomicInteger((int) (Math.random() * students.stream().max(Comparator.comparingInt(o -> o)).get()));

        if(!students.contains(st.get())) {
            Integer next = students.stream().filter(n -> n >= st.get()).findFirst().orElse(null);
            Collections.shuffle(students);
            Integer prev = students.stream().filter(n -> n <= st.get()).findFirst().orElse(null);
            st.set(next != null ? next : prev);
        }

        students.remove((Object) st.get());
        return st.get();
    }


    
    /** Преподаватель */
    @Getter @AllArgsConstructor
    private static class Lecturer {
        private String name;
        private int maxStudents;
        private final List<Integer> students = new ArrayList<>();
        public boolean isFull() { return students.size() >= maxStudents; }
    }
}
