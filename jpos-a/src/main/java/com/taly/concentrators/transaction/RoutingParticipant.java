package com.taly.concentrators.transaction;

import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.ContextConstants;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.util.Log;

import java.io.Serializable;

public class RoutingParticipant extends Log implements TransactionParticipant {

    public static final String DESTINATION =
            "ROUTING_DESTINATION";

    public static final String JPOS_B =
            "JPOS-B";

    public static final String SMARTVISTA_FE =
            "SMARTVISTA-FE";

   @Override
public int prepare(long id, Serializable context) {
    Context ctx = (Context) context;

    ISOMsg request =
            (ISOMsg) ctx.get(ContextConstants.REQUEST.toString());

    if (request == null) {
        warn("Routing failed: REQUEST is missing");
        return ABORTED;
    }

    String pan = request.getString(2);

    if (pan == null || pan.isBlank()) {
        warn("Routing failed: PAN (DE2) is missing");
        return ABORTED;
    }

    String destination;

    if (pan.startsWith("5")) {
        destination = SMARTVISTA_FE;
    } else {
        destination = JPOS_B;
    }

    ctx.put(DESTINATION, destination);

    info("Routing decision: PAN prefix="
            + pan.substring(0, 1)
            + " destination=" + destination);

    return PREPARED;
}
}