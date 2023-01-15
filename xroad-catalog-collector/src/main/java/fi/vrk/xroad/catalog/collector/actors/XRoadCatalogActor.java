/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS) Copyright (c) 2016-2022 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.collector.actors;

import akka.actor.UntypedAbstractActor;
import fi.vrk.xroad.catalog.collector.util.CatalogCollectorRuntimeException;
import akka.actor.Terminated;
import lombok.extern.slf4j.Slf4j;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public abstract class XRoadCatalogActor extends UntypedAbstractActor {

    public static final String START_COLLECTING = "StartCollecting";

    protected abstract boolean handleMessage(Object message) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException;

    @Override
    public void onReceive(Object message) throws Exception {
        log.info("{} handleXRoadCatalogMessage {}", this.hashCode());
        if (handleMessage(message)) {
            return;
        } else if (message instanceof Terminated) {
            throw new CatalogCollectorRuntimeException("Terminated: " + message);
        } else {
            log.error("Unable to handle message {}", message);
            throw new CatalogCollectorRuntimeException("Unable to handle message");
        }
    }

}
