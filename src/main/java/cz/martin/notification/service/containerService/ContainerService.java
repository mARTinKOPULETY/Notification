package cz.martin.notification.service.containerService;

import cz.martin.notification.dto.ContainerDto;
import cz.martin.notification.entity.Container;
import jakarta.mail.MessagingException;

import java.util.List;

public interface ContainerService {
    List<Container> saveContainers(List<ContainerDto> containerDto) throws MessagingException;
}
