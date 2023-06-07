package cz.martin.notification.service.containerService.impl;

import cz.martin.notification.dto.ContainerDto;
import cz.martin.notification.email.service.EmailSenderService;
import cz.martin.notification.entity.Container;
import cz.martin.notification.repository.ContainerRepository;
import cz.martin.notification.email.service.impl.EmailSenderServiceImpl;
import cz.martin.notification.service.containerService.ContainerService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;



@Service
@AllArgsConstructor
public class ContainerServiceImpl implements ContainerService {

    private final Logger logger = Logger.getLogger(ContainerServiceImpl.class.getName());
    private ContainerRepository containerRepository;
    private EmailSenderService emailSenderService;
    private ModelMapper modelMapper;


    @Override
    public List<Container> saveContainers(List<ContainerDto> containerDto) throws MessagingException {

        List<Container> containersFromHTML = containerDto.stream()
                .map(dto -> modelMapper.map(dto, Container.class))
                .collect(Collectors.toList());

        List<Container> containersFromDB = containerRepository.findAll();

        List<Container> newContainers = verifyIfThereIsNewContainer(containersFromHTML, containersFromDB);

        if (!newContainers.isEmpty()) {
            logger.info("I'm sending the email, and I'm saving new event into the database.");
            emailSenderService.sendEmail();
            return   containerRepository.saveAll(containersFromHTML);
        } else {
            logger.info("There in nothing new to notice.");
            return null;
        }
    }

    private List<Container> verifyIfThereIsNewContainer(List<Container> containersFromHTML, List<Container> containersFromDB) {
        //htmlCont =  object of container from containersFromHTML list
        //dbCont = object of existing container from containersFromDB list
        List<Container> newContainers = containersFromHTML.stream()
                .filter(htmlCont -> containersFromDB.stream()
                        .noneMatch(dbCont -> dbCont.getContainerDate().equals(htmlCont.getContainerDate())
                                && dbCont.getContainerFrom().equals(htmlCont.getContainerFrom())
                                && dbCont.getContainerTo().equals(htmlCont.getContainerTo())))
                .toList();
        logger.info("I have just verified if there is new event on scanning web page.");
        return newContainers;
    }
}

