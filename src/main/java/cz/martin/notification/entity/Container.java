package cz.martin.notification.entity;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="container")
@NoArgsConstructor
public class Container {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long containerId;
    @NotBlank
    private String containerDate;
    @NotBlank
    private String containerUntil;
    @NotBlank
    private String containerTo;


}
