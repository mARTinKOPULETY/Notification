package cz.martin.notification.service.htmlReaderService;

import cz.martin.notification.dto.ContainerDto;

import java.util.List;

public interface HtmlReaderService {
    List<ContainerDto> getListFromHtml();
}
