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

import java.util.Hashtable;

import com.smartdevicelink.proxy.constants.Names;
import com.smartdevicelink.proxy.rpc.enums.MobileAppState;

public class AppStateChanged extends RPCProfileManagerRequest {

    public AppStateChanged(String profileID, Integer correlationID, MobileAppState state) {
        super(Names.appStateChanged, profileID, correlationID);
        setState(state);
    }

    public AppStateChanged(Hashtable hash) {
        super(hash);
    }

    public MobileAppState getState() {
        return RPCProfileManagerHelper.getEnumFromHashtableString(parameters, HashTableKeys.MOBILE_APP_STATE,
                MobileAppState.class);
    }

    public void setState(MobileAppState state) {
        RPCProfileManagerHelper.storeEnumInHashtable(parameters, HashTableKeys.MOBILE_APP_STATE, state);
    }
}
