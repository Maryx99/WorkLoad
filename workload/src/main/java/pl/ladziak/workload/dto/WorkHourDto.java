package pl.ladziak.workload.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

// wszystkie pola sa finalne i prywatne
// toString, equals, hashcode
// konstruktor ze wszystkimi argumentami
// jest niemutowalne - nie mozna zmienic stanu po utworzeniu
@Builder
public record WorkHourDto(String uuid, LocalDateTime start, LocalDateTime end, boolean accepted) {

}

//@Getter
//@RequiredArgsConstructor
//@ToString
//@EqualsAndHashCode
//class Dto {
//    private final String u;
//
//    public String getU() {
//        return u;
//    }
//
//    public Dto(String u) {
//        this.u = u;
//    }
//
//    @Override
//    public String toString() {
//        return "Dto{" +
//                "u='" + u + ''' +
//                '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Dto dto = (Dto) o;
//        return Objects.equals(u, dto.u);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(u);
//    }
//}