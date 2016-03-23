package fi.vrk.xroad.catalog.lister;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.SERVER)
public class WsdlNotFoundException extends Exception {
    public WsdlNotFoundException() {
    }

    public WsdlNotFoundException(String message) {
        super(message);
    }

    public WsdlNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WsdlNotFoundException(Throwable cause) {
        super(cause);
    }

    public WsdlNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
