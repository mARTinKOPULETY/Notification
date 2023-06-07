package cz.martin.notification.service.htmlReaderService.impl;

import cz.martin.notification.dto.ContainerDto;
import cz.martin.notification.email.template.EmailMessage;
import cz.martin.notification.service.containerService.ContainerService;
import cz.martin.notification.service.htmlReaderService.HtmlReaderService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Service
@EnableScheduling
@AllArgsConstructor
public class HtmlReaderServiceImpl implements HtmlReaderService {

    private ContainerService containerService;
    private EmailMessage emailMessage;
    private final Logger logger = Logger.getLogger(HtmlReaderServiceImpl.class.getName());


    @Override
    @Scheduled(fixedDelay = 10000) //One  run per  10 sec
  //  @Scheduled(cron = "0 0 0 * * *") //One run per one day
    public List<ContainerDto> getListFromHtml() {
        List<ContainerDto> results = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(emailMessage.getLink()).get();

            Elements rows = doc.select("tr:has(td:containsOwn(Pod Vrcholem × Na Balkáně))");

            rows.stream()
                    .map(row -> row.select("td"))
                    .forEach(cells -> {
                        String containerDate = cells.get(1).text();
                        String containerFrom = cells.get(2).text();
                        String containerTo = cells.get(3).text();
                        ContainerDto containerDto = new ContainerDto();
                        containerDto.setContainerDate(containerDate);
                        containerDto.setContainerFrom(containerFrom);
                        containerDto.setContainerTo(containerTo);
                        results.add(containerDto);
                    });

            results.forEach(container -> logger.info("DTO:  " + container));

        containerService.saveContainers(results);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
     return results;
    }


}
