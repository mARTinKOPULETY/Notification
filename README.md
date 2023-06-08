# Notification app v1

***
This app sending notification by email if new date for take away of organic waste and bulky waste published on local authority of Prague 3 site.
## Motivation:
I have a little garden in community garden in  Prague 3. On sites of Prague 3 aren't actual dates of  taking away of organic and bulky waste by big content containers. The sites don't support service that sends notification email if dates are updated.

I'm not interested in everyday checking their site when they updated dates.
## Functionality:
The application regularly parses the website of Prague 3. Finds needed text in HTML.  Specifically names of two streets (crossroad). Creates Container object that contains  name of  crossroad, date, start and  to. It adds object to list A. It gets list B of containers from database. It creates list C where is added result of comparison of list A and list B.If listA equals to list B it  means that there is no new dates and list C is empty. If list C is empty application does nothing. If list C is not empty it is saved into a database and it sends notification email with url of site where is published new dates. 
***
## Version:
This is the version V1. It doesn't allow to be used aby another user . It doesn't have any controller to create another user who wants to receive notifications by email too. This functionality will come with V2.
***
## Author notice: 
Site of local authority was updated with dates till the end of year during final functionality verification of app. So  this very useful app is  now very totally unuseful :D. So I will  deploy this application when next spring approaches. When their site will not be  updated:).
***
## Used Technology:
    Java, Spring Boot, Maven, JPA, MySQL, Hibernate, Lombok, Jakarta Validation, ModelMapper, Jsoup, Email
***
## App Structure
<pre>
    main
    └── cz
        └── martin
            └── notification
                ├── config
                ├── dto
                ├── email
                │   └── service
                │       └── impl
                ├── repository
                └── service
                    └── containerService
                        └── impl
                    └── htmlReaderService
                        └── impl
</pre>
***
## Classes & Interfaces:
### AppConfig - Configuration class
Contains two beans. First is used for ModelMapper creating, second is used for EmailMessage creating.
 
<pre>
   @Bean
    public ModelMapper modelMapper(){return new ModelMapper();
    }

    @Bean
    public EmailMessage emailMessage(){return new EmailMessage();}
</pre>
***
### ContainerDto - POJO class
Pojo class for creating Dto objects with following attributes:
<pre>
    private Long containerId;
    private String containerDate;
    private String containerFrom;
    private String containerTo;
</pre>
***
### EmailSenderService - Interface
Contains one method that send email.
<pre>
    void sendEmail() throws MessagingException;
</pre>
***
###EmailSenderServiceImpl - Class that implements the EmailSenderService Interface

It uses JavaMailSender library . EmailMessage object is used to set up template of email (email address, subject, body of message). MimeMessage object is used for a html form of a body of an email.  Object MimeMessageHelper is  helper of MimeMessage where is noticed html format (multipart: true). Parameters as an email address  (setTo() ), subject (setSubject() ) and body of email(setText() ) are sets by helper. There is String of html for set up message.
<pre>
    public void sendEmail() throws MessagingException, MessagingException {
        EmailMessage emailMessage = new EmailMessage();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(emailMessage.getTo());
        helper.setSubject(emailMessage.getSubject());

        // Creating HTML form of message
        `String htmlMessage = "&lt;h3&gt"; + emailMessage.getMessage() + "&lt;/h3&gt;" +
               "&lt;h3&gt" + emailMessage.getLink() + "&lt;/h3&gt;";

        helper.setText(htmlMessage, true);

        javaMailSender.send(mimeMessage);

    }</pre>
***
###EmailMessage - Template class
This class serves as a template for  email creating. There  is hard coded  link, to (email address "TO_EMAIL" is Environmental Variable!),  subject and message.
<pre>
    public static final String TO_EMAIL = System.getenv("TO_EMAIL");

    private String link = "https://www.praha3.cz/potrebuji-zaridit/zivotni-situace/uklid-a-udrzba/velkoobjemove-kontejnery";
    private String to = TO_EMAIL;
    private String subject= "nové termíny svozu kontejnerů";
    private String message= "&lt;h3&gt Byly vypsány nové termíny pro svoz kontejnerů na rostlinný a velkoobjemový odpad. Klikni na link: &lt;/h3&gt";
</pre>
***
###Container - Entity class
This is a persistent class for persisting objects to database.  @NotBlank annotations are used for input validation.
<pre>
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
    private String containerTo;</pre>
***
###ContainerRepository 
Repository is extended by  JpaRepository to take care of work with database.
***
###ContainerService - Interface
Contains one method that save container object.
<pre>
List&lt;Container&gt saveContainers(List&lt;ContainerDto&gt containerDto) throws MessagingException;
</pre>
***
### ContainerServiceImpl - Class implements ContainerService Interface
There are two methods. First one saveContainers(List<ContainerDto> containerDto) is used for saving entities into the  database (db). It uses ModelMapper to map Dto objects to common objets into a list (containersFromHTML) . Then gets all records from db into a list (containersFromDB). Then  uses helper method for verifying if containersFromHTML contains new records. The result is stored into a list (newContainers). If doesent contain any records and it is empty then returns null. If it is not empty and contains new records it save newContainers into db by  containerRepository.
<pre>
        List&lt;Container> containersFromHTML = containerDto.stream()
                .map(dto -> modelMapper.map(dto, Container.class))
                .collect(Collectors.toList());

        List&lt;Container> containersFromDB = containerRepository.findAll();

        List&lt;Container> newContainers = verifyIfThereIsNewContainer(containersFromHTML, containersFromDB);

        if (!newContainers.isEmpty()) {
            logger.info("I'm sending the email, and I'm saving new event into the database.");
            emailSenderService.sendEmail();
            return   containerRepository.saveAll(newContainers);
        } else {
            logger.info("There in nothing new to notice.");
            return null;
        }
    }
</pre>

Helper method verifyIfThereIsNewContainer(List<Container> containersFromHTML, List<Container> containersFromDB) returns newContainers. It is filtered by stream. Searches for inequality of unique combination of all attributes except id in containersFromDB  list and containersFromHTML list.
<pre>   
private List&lt;Container> verifyIfThereIsNewContainer(List&lt;Container> containersFromHTML, List&lt;Container> containersFromDB) {
        //htmlCont =  object of container from containersFromHTML list
        //dbCont = object of existing container from containersFromDB list
        List&lt;Container> newContainers = containersFromHTML.stream()
                .filter(htmlCont -> containersFromDB.stream()
                        .noneMatch(dbCont -> dbCont.getContainerDate().equals(htmlCont.getContainerDate())
                                && dbCont.getContainerFrom().equals(htmlCont.getContainerFrom())
                                && dbCont.getContainerTo().equals(htmlCont.getContainerTo())))
                .toList();
        logger.info("I have just verified if there is new event on scanning web page.");
        return newContainers;
    }
</pre>
***
###HtmlReaderService - Interface
Contains method that get and return list of containerDto.
***
### HtmlReaderServiceImpl - Class implements HtmlReaderService Interface
This service uses Jsoup for reading HTML tags from URL. There is only one method. It selects all rows that contains needed crossroad ("Pod Vrcholem × Na Balkáně"). Then map  cells of row to needed attributes of container object. Then  calls containerService to save result. For error cases there are two exceptions:
IOException and MessagingException. Method returns list of ContainerDto. This method is scheduled. It runs once per day.


```
@Override
//    @Scheduled(fixedDelay = 10000) //Runs once per  10 sec
    @Scheduled(cron = "0 0 0 * * *") //Runs once per one day
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
```
HTML SOURCE:
```

    <tr height="20" style="height:15.0pt"> 
        <td class="xl17" height="20" style="width:205pt;height:15.0pt" width="273">Pod Vrcholem × Na Balkáně</td> 
        <td class="xl21" style="width:63pt" width="84">27.&nbsp;8.&nbsp;2023</td> 
        <td class="xl17" style="width:32pt" width="42">13:00</td> 
        <td class="xl17" style="width:32pt" width="42">16:00</td> 
    </tr>
```
###NotificationApplication - Main Class
There is only one static method that run Spring Boot application.
```
	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
	}
```
### application.properties
Set up for server port, MySQL database, Hibernate and JavaMailSender
```
server.port=8042

# MySQL database settings
spring.datasource.url=jdbc:mysql://localhost:3306/container
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=${DB_PASS}

# Hibernate settings for table creation
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JavaMailSender settings
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${EMAIL}
spring.mail.password=${EMAIL_PASS}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```
