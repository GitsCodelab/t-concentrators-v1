package com.taly.concentrators.transaction;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOException;
import org.jpos.transaction.Context;
import org.jpos.transaction.ContextConstants;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.util.Log;

import java.io.Serializable;

public class ProcessingParticipant extends Log implements TransactionParticipant {

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;

        ISOMsg request = (ISOMsg) ctx.get(ContextConstants.REQUEST.toString());

        if (request == null) {
            warn("Processing failed: REQUEST is missing");
            return ABORTED;
        }

        try {
            String mti = request.getMTI();

            info("Processing transaction: MTI=" + mti
                    + " DE11=" + request.getString(11)
                    + " DE41=" + request.getString(41));

            ISOMsg response = (ISOMsg) request.clone();

            response.setResponseMTI();
            response.set(39, "00");

            ctx.put(
                    ContextConstants.RESPONSE.toString(),
                    response
            );

            info("Processing successful: response MTI="
                    + response.getMTI()
                    + " DE39=" + response.getString(39));

            return PREPARED;

        } catch (ISOException e) {
            error("Processing failed: unable to create response", e);
            return ABORTED;
        }
    }
}