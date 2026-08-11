package com.taly.concentrators.transaction;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOException;
import org.jpos.transaction.Context;
import org.jpos.transaction.ContextConstants;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.util.Log;

import java.io.Serializable;

public class ValidationParticipant extends Log implements TransactionParticipant {

    @Override
    public int prepare(long id, Serializable context) {
        Context ctx = (Context) context;

        ISOMsg request = (ISOMsg) ctx.get(ContextConstants.REQUEST.toString());

        if (request == null) {
            warn("Validation failed: REQUEST is missing");
            return ABORTED;
        }

        try {
            String mti = request.getMTI();

            if (!"0200".equals(mti)) {
                warn("Validation failed: unsupported MTI=" + mti);
                return ABORTED;
            }

            if (!request.hasField(3)) {
                warn("Validation failed: DE3 is missing");
                return ABORTED;
            }

            if (!request.hasField(4)) {
                warn("Validation failed: DE4 is missing");
                return ABORTED;
            }

            if (!request.hasField(11)) {
                warn("Validation failed: DE11 is missing");
                return ABORTED;
            }

            if (!request.hasField(41)) {
                warn("Validation failed: DE41 is missing");
                return ABORTED;
            }

            info("Validation successful: MTI=" + mti
                    + " DE11=" + request.getString(11)
                    + " DE41=" + request.getString(41));

            return PREPARED;

        } catch (ISOException e) {
            error("Validation failed: unable to read ISO message", e);
            return ABORTED;
        }
    }
}