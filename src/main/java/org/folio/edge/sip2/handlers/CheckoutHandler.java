package org.folio.edge.sip2.handlers;

import io.vertx.core.Future;
import org.folio.edge.sip2.domain.messages.requests.Checkout;

public class CheckoutHandler implements ISip2RequestHandler {

  @Override
  public Future<String> execute(Object message) {
    final Checkout checkout = (Checkout) message;
    //call FOLIO

    //format into SIP message
    return Future.succeededFuture("Successfully checked out "
        + checkout.toString());

  }
}