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

#ifndef SmartDeviceLinkProxy_IVINames_h
#define SmartDeviceLinkProxy_IVINames_h

#define IVI_NAMES_addProfile @"AddProfile"
#define IVI_NAMES_removeProfile @"RemoveProfile"
#define IVI_NAMES_loadProfile @"LoadProfile"
#define IVI_NAMES_unloadProfile @"UnloadProfile"
#define IVI_NAMES_sendAppToProfile @"SendAppToProfileMessage"
#define IVI_NAMES_appStateChanged @"AppStateChanged"

#define IVI_NAMES_onProfileToApp @"OnProfileToAppMessage"
#define IVI_NAMES_onProfileClosed @"OnProfileUnloaded"


// JSON TAGS
#define IVI_NAMES_TAG_profileName @"profileName"
#define IVI_NAMES_TAG_mobileAppState @"state"

#endif
