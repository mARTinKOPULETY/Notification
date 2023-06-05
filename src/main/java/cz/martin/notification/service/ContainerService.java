package cz.martin.notification.service;

import cz.martin.notification.dto.ContainerDto;
import cz.martin.notification.entity.Container;

public interface ContainerService {
    Container saveContainer(ContainerDto containerDto);
}
