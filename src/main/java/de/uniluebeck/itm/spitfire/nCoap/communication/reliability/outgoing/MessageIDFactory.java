/**
* Copyright (c) 2012, Oliver Kleine, Institute of Telematics, University of Luebeck
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
* following conditions are met:
*
* - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
* disclaimer.
* - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
* following disclaimer in the documentation and/or other materials provided with the distribution.
* - Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
* products derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
* INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
* INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
* GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
* LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
* OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package de.uniluebeck.itm.spitfire.nCoap.communication.reliability.outgoing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class is to create and manage message IDs for outgoing messages. The usage of this class to create
 * new message IDs ensures that a message ID is not used twice within {@link #ALLOCATION_TIMEOUT} seconds.
 *
 * @author Oliver Kleine
 */
public class MessageIDFactory extends Observable{

    private static Logger log = LoggerFactory.getLogger(MessageIDFactory.class.getName());

    //public static int ALLOCATION_TIMEOUT = Configuration.getInstance().getInt("messageID.allocation.timeout", 120);
    public static int ALLOCATION_TIMEOUT = 120;

    //private static Random random = new Random(System.currentTimeMillis());
    private ScheduledExecutorService executorService;

    //Allocated message IDs
    private final Set<Integer> allocatedMessageIDs = Collections.synchronizedSet(new HashSet<Integer>());

    int nextMessageID = 0;

    /**
     * Returns the one and only instance of the message ID factory
     * @return the one and only instance of the message ID factory
     */
    private static MessageIDFactory getInstance(){
        return instance;
    }

    private MessageIDFactory(){}

    private static MessageIDFactory instance = new MessageIDFactory();

    public static void setExecutorService(ScheduledExecutorService executorService){
        getInstance().executorService = executorService;
    }

    public static void registerObserver(Observer observer){
        instance.addObserver(observer);
    }



    /**
     * Returns the next available message ID within range 1 to (2^16)-1 and allocates this message ID for
     * {@link MessageIDFactory#ALLOCATION_TIMEOUT} milliseconds. That means, the same message ID will not be used
     * as long as it is allocated.
     *
     * @return the next available message ID within range 1 to (2^16)-1
     */
    public static int nextMessageID(){
        instance.produceNextMessageID();

        final int messageID = instance.nextMessageID;
        instance.executorService.schedule(new Runnable(){

            @Override
            public void run() {
                if(instance.allocatedMessageIDs.remove(messageID))
                    log.debug("Deallocated message ID " + messageID);
                else
                    log.error("Message ID " + messageID + " could not be removed! This should never happen.");

                instance.setChanged();
                instance.notifyObservers(messageID);
            }
        }, ALLOCATION_TIMEOUT, TimeUnit.SECONDS);

        return messageID;
    }

    private synchronized void produceNextMessageID(){
        boolean created;
        do{
            nextMessageID = (nextMessageID + 1) & 0x0000FFF;
            created = allocatedMessageIDs.add(nextMessageID);
        }
        while(!created);
    }
}