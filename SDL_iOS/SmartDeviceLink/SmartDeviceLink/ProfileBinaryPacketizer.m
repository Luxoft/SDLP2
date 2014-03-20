/*
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

#import <SmartDeviceLink/ProfileBinaryPacketizer.h>
#import <SmartDeviceLink/SDLAddProfile.h>

#define MAX_SIZE 300000 // matches android version
#define SIZE_OF_HEADER 8
#define MAX_PAYLOAD_SIZE (MAX_SIZE - SIZE_OF_HEADER)

@implementation ProfileBinaryPacketizer

+(NSArray*) createAddProfileRequestsForData: (NSData*)data forProfile:(NSString*)profileName withCorrId:(int)correlationID {
    if ([data length] == 0 || data == nil)
    {
        return nil;
    }
    int totalDataSize = [data length];
    div_t divresult = div(totalDataSize, MAX_PAYLOAD_SIZE);
    int packetsNumber = divresult.quot;
    if (divresult.rem != 0)
    {
        packetsNumber ++;
        NSLog(@"Totaal packets number = %i", packetsNumber);
    }
    NSMutableArray * result = [[NSMutableArray alloc] initWithCapacity:packetsNumber];
    int offset = 0;
    int payloadSize = 0;
    int packetSize;
    int frameCountN = htonl(packetsNumber);
    int frameNumberN;
    for (int i = 0; i < packetsNumber; i++)
    {
        frameNumberN = htonl(i);
        payloadSize = totalDataSize - offset;
        if (payloadSize > MAX_PAYLOAD_SIZE)
        {
            payloadSize = MAX_PAYLOAD_SIZE;
        }
        packetSize = payloadSize + SIZE_OF_HEADER;
        char * packet = malloc(packetSize * sizeof(char));
        memcpy(packet, &frameCountN, 4);
        memcpy(packet + 4, &frameNumberN, 4);
        memcpy(packet + SIZE_OF_HEADER, [data bytes] + offset, payloadSize);
        NSData * packetWrapped = [NSData dataWithBytesNoCopy:packet length:packetSize];
        SDLAddProfile * addProfile = [[SDLAddProfile alloc] init];
        [addProfile setProfileName:profileName];
        [addProfile setCorrelationID: [NSNumber numberWithInt: correlationID++]];
        [addProfile setProfileBinary: packetWrapped];
        [result addObject: addProfile];
        [addProfile release];
        offset += payloadSize;
    }
    return [result autorelease];
}

@end
