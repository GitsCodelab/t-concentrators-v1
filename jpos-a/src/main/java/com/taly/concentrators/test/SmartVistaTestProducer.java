package com.taly.concentrators.test;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.q2.QBeanSupport;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;

public class SmartVistaTestProducer extends QBeanSupport {

    @Override
    protected void startService() throws Exception {
        GenericPackager packager =
                new GenericPackager("cfg/iso87.xml");

        ISOMsg msg = new ISOMsg();
        msg.setPackager(packager);

        msg.setMTI("0200");
        msg.set(2, "5555555555554444");
        msg.set(3, "000000");
        msg.set(4, "000000001000");
        msg.set(7, "0811143000");
        msg.set(11, "654321");
        msg.set(41, "TERM0002");

        Space<String, ISOMsg> space = SpaceFactory.getSpace();

        space.out("jpos-a-to-sv-send", msg);

        System.out.println(
                "SmartVista test ISO message placed on queue: jpos-a-to-sv-send"
        );
    }
}