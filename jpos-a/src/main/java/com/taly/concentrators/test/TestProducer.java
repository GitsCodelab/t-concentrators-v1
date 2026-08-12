package com.taly.concentrators.test;

import org.jpos.iso.ISOMsg;
import org.jpos.q2.QBeanSupport;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;
import org.jpos.transaction.ContextConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestProducer extends QBeanSupport {

    private static final String TXN_QUEUE = "TXN";

    private Space space;

    @Override
    protected void startService() {
        space = SpaceFactory.getSpace();

        try {
            ISOMsg msg = new ISOMsg();

            msg.setMTI("0200");
            // msg.set(2, "4123456789012345"); // VISA test PAN
            msg.set(2, "5200315367444204"); // MasterCard test PAN
            msg.set(3, "000000");
            msg.set(4, "000000001000");
            msg.set(
                    7,
                    new SimpleDateFormat("MMddHHmmss").format(new Date())
            );
            msg.set(11, "123456");
            msg.set(41, "TERM0001");

            getLog().info(
                    "Sending test ISO message: MTI="
                    + msg.getMTI()
                    + " PAN=" + msg.getString(2)
                    + " DE11=" + msg.getString(11)
                    + " DE41=" + msg.getString(41)
            );

            /*
             * TransactionManager expects a Context in the TXN queue.
             * The ISO request is stored inside the Context as REQUEST.
             */
            Context ctx = new Context();

            ctx.put(
                    ContextConstants.REQUEST.toString(),
                    msg
            );

            space.out(TXN_QUEUE, ctx);

            getLog().info(
                    "Test ISO message placed on queue: " + TXN_QUEUE
            );

        } catch (Exception e) {
            getLog().error(
                    "Failed to create/send test ISO message",
                    e
            );
        }
    }
}