package cz.martin.notification.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContainerDto {

    private Long containerId;
    private String containerDate;
    private String containerFrom;
    private String containerTo;
}
