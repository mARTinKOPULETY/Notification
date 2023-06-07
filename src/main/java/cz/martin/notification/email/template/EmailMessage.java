package cz.martin.notification.email.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {
    public static final String TO_EMAIL = System.getenv("TO_EMAIL");

    private String link = "https://www.praha3.cz/potrebuji-zaridit/zivotni-situace/uklid-a-udrzba/velkoobjemove-kontejnery";
    private String to = TO_EMAIL;
    private String subject= "nové termíny svozu kontejnerů";
    private String message= "<h3>Byly vypsány nové termíny pro svoz kontejnerů na rostlinný a velkoobjemový odpad. Klikni na link: </h3>";
}
