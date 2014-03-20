/**
 *
 * SDLP SDK
 * Cross Platform Application Communication Stack for In-Vehicle Applications
 *
 * Copyright (C) 2013, Luxoft Professional Corp., member of IBS group
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; version 2.1.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 *
 */
package com.smartdevicelink.proxy.rpc.pm;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.smartdevicelink.protocol.SmartDeviceLinkProtocol;

/**
 * Splits profile binary into multiple packets in AddProfile requests.
 */
public class ProfileBinaryPacketizer {

    private static final int SIZE_OF_HEADER_BYTES = 8;

    public static List<AddProfile> createAddProfileRequests(byte[] data, String profileID, Integer correlationID) {
        List<byte[]> packets = splitToPackets(data);
        List<AddProfile> messages = new ArrayList<AddProfile>(packets.size());
        for (byte[] packet: packets) {
            messages.add(new AddProfile(profileID, correlationID++, packet));
        }
        return messages;
    }

    private static List<byte[]> splitToPackets(byte[] rawBinaryData) {
        final int maxPacketSize = getApproximateMaxPacketSize();
        final int maxDataPayloadSize = maxPacketSize - SIZE_OF_HEADER_BYTES;
        int frameCount = (int) ((double) rawBinaryData.length / (double) (maxDataPayloadSize));
        if (rawBinaryData.length % (maxDataPayloadSize) > 0) {
            frameCount++;
        }
        List<byte[]> result = new ArrayList<byte[]>(frameCount);
        int offset = 0;
        int payloadSize = 0;
        for (int i = 0; i < frameCount; i++) {
            payloadSize = rawBinaryData.length - offset;
            if (payloadSize > maxDataPayloadSize) {
                payloadSize = maxDataPayloadSize;
            }
            byte[] buffer = new byte[payloadSize + SIZE_OF_HEADER_BYTES];
            writePacketHeaderToByteArray(buffer, frameCount, i);
            System.arraycopy(rawBinaryData, offset, buffer, SIZE_OF_HEADER_BYTES, payloadSize);
            offset += payloadSize;

            result.add(buffer);
        }
        return result;
    }

    private static void writePacketHeaderToByteArray(byte[] array, int frameCount, int frameNumber) {
        ByteBuffer.wrap(array).putInt(frameCount).putInt(frameNumber);
    }

    private static int getApproximateMaxPacketSize() {
        return SmartDeviceLinkProtocol.MAX_DATA_SIZE;
    }
}
