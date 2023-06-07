package cz.martin.notification.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="container")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Container {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="container_id")
    private Long containerId;

    @NotBlank
    @Column(name="container_date")
    private String containerDate;

    @NotBlank
    @Column(name="container_from")
    private String containerFrom;

    @NotBlank
    @Column(name="container_to")
    private String containerTo;

}
