package eu.acme.demo.exception;

import eu.acme.demo.error.ValidationMessageKeys;
import org.zalando.problem.AbstractThrowableProblem;

import java.net.URI;

import static org.zalando.problem.Status.BAD_REQUEST;

public class BadRequestException extends AbstractThrowableProblem {


    public BadRequestException( String orderId) {
        super(URI.create("http://api.okto-demo.eu/orders".concat(ValidationMessageKeys.ORDER_DOESNOT_EXISTS)),
                ValidationMessageKeys.ORDER_DOESNOT_EXISTS,BAD_REQUEST, String.format("Order with id %s doesn't exist",orderId));
    }

}
