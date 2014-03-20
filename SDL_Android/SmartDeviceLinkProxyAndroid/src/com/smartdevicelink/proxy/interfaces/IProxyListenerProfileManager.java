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
package com.smartdevicelink.proxy.interfaces;

import com.smartdevicelink.proxy.rpc.pm.AddProfileResponse;
import com.smartdevicelink.proxy.rpc.pm.AppStateChangedResponse;
import com.smartdevicelink.proxy.rpc.pm.LoadProfileResponse;
import com.smartdevicelink.proxy.rpc.pm.OnProfileClosed;
import com.smartdevicelink.proxy.rpc.pm.OnSendProfileToAppMessage;
import com.smartdevicelink.proxy.rpc.pm.RemoveProfileResponse;
import com.smartdevicelink.proxy.rpc.pm.SendAppToProfileMessageResponse;
import com.smartdevicelink.proxy.rpc.pm.UnloadProfileResponse;

/**
 * Callbacks from Profile Manager
 */
public interface IProxyListenerProfileManager {
    // ************************* responses **********************//

    public void onAddProfileResponse(AddProfileResponse response);

    public void onRemoveProfileResponse(RemoveProfileResponse response);

    public void onLoadProfileReponse(LoadProfileResponse response);

    public void onUnloadProfileResponse(UnloadProfileResponse response);

    public void onSendMessageToProfileResponse(SendAppToProfileMessageResponse response);

    public void onAppStateChangedResponse(AppStateChangedResponse response);

    // ************************* notifications **********************//

    public void onProfileClosed(OnProfileClosed notification);

    public void onReceiveMessageFromProfile(OnSendProfileToAppMessage notification);
}
