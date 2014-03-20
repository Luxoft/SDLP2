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

/**
 * \brief Callbacks for SDLTcpDiscoverer. All methods are invoked on the main thread.
 * \warning received list of devices should be retained explicitly if you intend to keep it
 * \author Elena Bratanova <ebratanova@luxoft.com>
 * \date 28/06/13
 */

#import <Foundation/Foundation.h>

@protocol SDLTcpDiscovererListener <NSObject>
-(void) onFoundDevices:(NSArray*)listOfDevices;
-(void) onFoundNothing;
@end
