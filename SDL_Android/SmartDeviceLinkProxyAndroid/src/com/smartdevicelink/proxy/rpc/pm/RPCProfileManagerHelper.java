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
import java.util.Vector;

import android.util.Base64;

/**
 * Class with convenience functions for serialization/deserialization of data
 * and enums
 */
class RPCProfileManagerHelper {

    public static String getProfileId(Hashtable parameters) {
        return getStringForKey(parameters, HashTableKeys.PROFILE_ID);
    }

    public static void setProfileId(Hashtable parameters, String profileId) {
        setStringForKey(parameters, profileId, HashTableKeys.PROFILE_ID);
    }

    public static void setStringForKey(Hashtable parameters, String data, String key) {
        if (parameters != null && data != null && key != null) {
            parameters.put(key, data);
        }
    }

    public static String getStringForKey(Hashtable parameters, String key) {
        if (parameters != null && key != null) {
            Object obj = parameters.get(key);
            if (obj instanceof String) {
                return (String) obj;
            }
        }
        return null;
    }

    public static void setByteArrayForKey(Hashtable parameters, byte[] data, String key) {
        if (parameters != null && data != null && key != null) {
            parameters.put(key, data);
        }
    }

    public static void setUnsignedByteArrayForKey(Hashtable parameters, byte[] data, String key) {
        if (parameters != null && data != null && key != null) {
            int[] newData = new int[data.length];
            for (int i = 0; i < data.length; i++) {
                newData[i] = data[i] & 0xFF;
            }
            parameters.put(key, newData);
        }
    }

    public static void setByteArrayAsStringForKey(Hashtable parameters, byte[] data, String key) {
        if (parameters != null && data != null && key != null) {
            setStringForKey(parameters, new String(data), key);
        }
    }

    public static byte[] getByteArrayFromStoredString(Hashtable parameters, String key) {
        String data = getStringForKey(parameters, key);
        if (data != null) {
            return data.getBytes();
        }
        return null;
    }

    public static byte[] getBytesForKeyFrom64String(Hashtable parameters, String key) {
        String decoded = getStringForKey(parameters, key);
        if (decoded != null) {
            return Base64.decode(decoded, Base64.NO_WRAP );
        }
        return null;
    }

    public static void setBytesForKeyAs64String(Hashtable parameters, String key, byte[] data) {
        if (data != null) {
            String encoded = Base64.encodeToString(data, Base64.NO_WRAP );
            setStringForKey(parameters, encoded, key);
        }
    }

    public static byte[] getBytesForKey(Hashtable parameters, String key) {
        if (parameters == null || key == null) {
            return null;
        }
        Object obj = parameters.get(key);
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        } else if (obj instanceof Vector<?>) {
            Vector<?> castPayload = (Vector<?>) obj;
            if (castPayload.size() == 0) {
                return null;
            }
            byte[] payload = new byte[castPayload.size()];
            for (int i = 0; i < payload.length; i++) {
                if (castPayload.get(i) instanceof Integer) {
                    payload[i] = ((Integer) castPayload.get(i)).byteValue();
                } else {
                    // all contents of the vector must be Integer
                    return null;
                }
            }
            return payload;
        }
        return null;
    }

    public static <E extends Enum<E>> void storeEnumInHashtable(Hashtable parameters, String key, E toStore) {
        if (parameters != null && key != null && toStore != null) {
            parameters.put(key, toStore);
        }
    }

    public static <E extends Enum<E>> void storeEnumInHashtableAsString(Hashtable parameters, String key, E toStore) {
        if (parameters != null && key != null && toStore != null) {
            parameters.put(key, toStore.toString());
        }
    }

    public static <E extends Enum<E>> void storeEnumInHashtableAsInt(Hashtable parameters, String key, E toStore) {
        if (parameters != null && key != null && toStore != null) {
            parameters.put(key, toStore.ordinal());
        }
    }

    public static <E extends Enum<E>> E getEnumFromHashtableInt(Hashtable parameters, String key, Class<E> clazz) {
        if (parameters == null || key == null || clazz == null) {
            return null;
        }
        try {
            Object obj = parameters.get(key);
            if (obj instanceof Integer) {
                Enum<?>[] constants = clazz.getEnumConstants();
                if (constants != null) {
                    int unwrapped = ((Integer) obj).intValue();
                    if (unwrapped < constants.length) {
                        return (E) constants[unwrapped];
                    }
                }
            } else if (obj instanceof Enum<?>) {
                return (E) obj;
            }
        } catch (Exception e) {
            // ClassCastException or IllegalArgumentException may be thrown
            e.printStackTrace();
        }
        return null;
    }

    public static <E extends Enum<E>> E getEnumFromHashtableString(Hashtable parameters, String key, Class<E> clazz) {
        if (parameters == null || key == null || clazz == null) {
            return null;
        }
        try {
            Object obj = parameters.get(key);
            if (obj instanceof String) {
                return Enum.valueOf(clazz, (String) obj);
            } else if (obj instanceof Enum<?>) {
                return (E) obj;
            }
        } catch (Exception e) {
            // ClassCastException or IllegalArgumentException may be thrown
            e.printStackTrace();
        }
        return null;
    }
}
