package com.taly.concentrators.test;

import org.jpos.iso.ISOMsg;
import org.jpos.q2.QBeanSupport;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestProducer extends QBeanSupport {
    private static final String SEND_QUEUE = "jpos-a-to-b-send";
    private static final String READY_KEY = "jpos-a-to-b.ready";

    private Space space;

    @Override
    protected void startService() {
        space = SpaceFactory.getSpace();

        // Wait until ChannelAdaptor is connected to jPOS B
        Object ready = space.rd(READY_KEY, 10000);

        if (ready == null) {
            getLog().error("ChannelAdaptor is not ready: " + READY_KEY);
            return;
        }

        try {
            ISOMsg msg = new ISOMsg();

            msg.setMTI("0200");
            msg.set(3, "000000");
            msg.set(4, "000000001000");
            msg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
            msg.set(11, "123456");
            msg.set(41, "TERM0001");

            getLog().info("Sending test ISO message: " + msg);

            space.out(SEND_QUEUE, msg);

            getLog().info("Test ISO message placed on queue: " + SEND_QUEUE);

        } catch (Exception e) {
            getLog().error("Failed to create/send test ISO message", e);
        }
    }
}
