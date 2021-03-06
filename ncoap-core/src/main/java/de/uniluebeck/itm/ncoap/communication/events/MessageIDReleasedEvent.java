/**
 * Copyright (c) 2012, Oliver Kleine, Institute of Telematics, University of Luebeck
 * All rights reserved
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *  - Redistributions of source messageCode must retain the above copyright notice, this list of conditions and the following
 *    disclaimer.
 *
 *  - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *  - Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 *    products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.uniluebeck.itm.ncoap.communication.events;

import java.net.InetSocketAddress;

/**
 * Instances of {@link de.uniluebeck.itm.ncoap.communication.events.MessageIDReleasedEvent} are sent upstream
 * if a message ID was released, i.e. the ID can be used for new instances of
 * {@link de.uniluebeck.itm.ncoap.communication.reliability.MessageTransfer}
 *
 * @author Oliver Kleine
 */
public class MessageIDReleasedEvent {

    private InetSocketAddress remoteEndpoint;
    private int messageID;

    /**
     * Creates a new instance of {@link de.uniluebeck.itm.ncoap.communication.events.MessageIDReleasedEvent}
     * @param remoteEndpoint the communication partner the released ID was used for
     * @param messageID the released message ID
     */
    public MessageIDReleasedEvent(InetSocketAddress remoteEndpoint, int messageID) {
        this.remoteEndpoint = remoteEndpoint;
        this.messageID = messageID;
    }

    /**
     * Returns the released message ID
     * @return the released message ID
     */
    public int getMessageID() {
        return messageID;
    }

    /**
     * Returns the communication partner the released ID was used for
     * @return the communication partner the released ID was used for
     */
    public InetSocketAddress getRemoteEndpoint() {
        return remoteEndpoint;
    }
}
