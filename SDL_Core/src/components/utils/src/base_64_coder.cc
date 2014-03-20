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

#include <string.h>

#include <vector>

#include "utils/base_64_coder.h"

/**
 * Code is taken from here: http://www.adp-gmbh.ch/cpp/common/base64.html;
 * only minor changes have been done regarding return values and intermediate results storing
 */

using namespace NsProfileManager;

static const std::string base64_chars =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    "abcdefghijklmnopqrstuvwxyz"
    "0123456789+/";

static inline bool is_base64(unsigned char c)
{
    return (isalnum(c) || (c == '+') || (c == '/'));
}

char * Base64Coder::decode(const char * toDecode, int toDecodeLength, int & resultLength)
{
    int i = 0;
    int j = 0;
    int in_ = 0;
    unsigned char char_array_4[4], char_array_3[3];
    std::vector<char> decodedBuf;
    decodedBuf.reserve(toDecodeLength);

    while (toDecodeLength-- && ( toDecode[in_] != '=') && is_base64(toDecode[in_]))
    {
        char_array_4[i++] = toDecode[in_];
        in_++;
        if (i ==4)
        {
            for (i = 0; i <4; i++)
            {
                char_array_4[i] = base64_chars.find(char_array_4[i]);
            }

            char_array_3[0] = (char_array_4[0] << 2) + ((char_array_4[1] & 0x30) >> 4);
            char_array_3[1] = ((char_array_4[1] & 0xf) << 4) + ((char_array_4[2] & 0x3c) >> 2);
            char_array_3[2] = ((char_array_4[2] & 0x3) << 6) + char_array_4[3];

            for (i = 0; (i < 3); i++)
            {
                decodedBuf.push_back(char_array_3[i]);
            }
            i = 0;
        }
    }

    if (i)
    {
        for (j = i; j <4; j++)
        {
            char_array_4[j] = 0;
        }

        for (j = 0; j <4; j++)
        {
            char_array_4[j] = base64_chars.find(char_array_4[j]);
        }

        char_array_3[0] = (char_array_4[0] << 2) + ((char_array_4[1] & 0x30) >> 4);
        char_array_3[1] = ((char_array_4[1] & 0xf) << 4) + ((char_array_4[2] & 0x3c) >> 2);
        char_array_3[2] = ((char_array_4[2] & 0x3) << 6) + char_array_4[3];

        for (j = 0; (j < i - 1); j++)
        {
            decodedBuf.push_back(char_array_3[j]);
        }
    }

    resultLength = decodedBuf.size();
    char * retval = new char[resultLength];
    for (int i = 0; i < resultLength; i++)
    {
        retval[i] = decodedBuf[i];
    }
    return retval;
}

char * Base64Coder::encode(const char * toEncode, int toEncodeLength, int & resultLength)
{
    std::vector<char> encodedBuf;
    encodedBuf.reserve(toEncodeLength * 3);
    int i = 0;
    int j = 0;
    unsigned char char_array_3[3];
    unsigned char char_array_4[4];

    while (toEncodeLength--)
    {
        char_array_3[i++] = *(toEncode++);
        if (i == 3)
        {
            char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
            char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
            char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
            char_array_4[3] = char_array_3[2] & 0x3f;

            for(i = 0; (i <4) ; i++)
            {
                encodedBuf.push_back(base64_chars[char_array_4[i]]);
            }
            i = 0;
        }
    }

    if (i)
    {
        for(j = i; j < 3; j++)
        {
            char_array_3[j] = '\0';
        }

        char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
        char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
        char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
        char_array_4[3] = char_array_3[2] & 0x3f;

        for (j = 0; (j < i + 1); j++)
        {
            encodedBuf.push_back(base64_chars[char_array_4[j]]);
        }

        while((i++ < 3))
        {
            encodedBuf.push_back('=');
        }
    }
    encodedBuf.push_back('\0');
    char * encoded = new char[encodedBuf.size()];
    resultLength = encodedBuf.size();
    for (int i = 0; i < resultLength; i ++)
    {
        encoded[i] = encodedBuf[i];
    }
    return encoded;
}
