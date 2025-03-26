package microservice.customer.microservicecustomer.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

@NoArgsConstructor
@Slf4j
public final class HeaderUtil
{
    public static HttpHeaders createAlert
            (String applicationName, String message, String param)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-alert", message);
        headers.add("X-" + applicationName + "-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert
            (String applicationName, boolean enableTransaction, String entityName, String param)
    {
        String applicationNameMessage = applicationName.concat(".").concat(entityName).concat(".created");
        String entityNameMessage = "A new".concat(entityName).concat("is created with identifier ").concat(param);
        String message = enableTransaction ? applicationNameMessage : entityNameMessage;
        return createAlert(applicationName, message, param);
    }

    public static HttpHeaders createEntityUpdateAlert
            (String applicationName, boolean enableTranslation, String entityName, String param)
    {
        String applicationNameMessage = applicationName.concat(".").concat(entityName).concat(".updated");
        String entityNameMessage = "A " + entityName + " is updated with identifier " + param;
        String message = enableTranslation ? applicationNameMessage : entityNameMessage;
        return createAlert(applicationName, message, param);
    }

    public static HttpHeaders createEntityDeletionAlert
            (String applicationName, boolean enableTranslation, String entityName, String param)
    {
        String applicationNameMessage = applicationName.concat(".").concat(entityName).concat(".deleted");
        String entityNameMessage = "A " + entityName + " is deleted with identifier " + param;
        String message = enableTranslation ? applicationNameMessage : entityNameMessage;
        return createAlert(applicationName, message, param);
    }

    public static HttpHeaders createFailureAlert
            (String applicationName, boolean enableTranslation, String entityName, String errorKey, String defaultMessage)
    {
        log.error("Entity processing failed, {}", defaultMessage);
        String message = enableTranslation ? "error." + errorKey : defaultMessage;
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-error", message);
        headers.add("X-" + applicationName + "-params", entityName);
        return headers;
    }
}
