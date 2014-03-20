/*
 *
 * SDLP SDK
 * Cross Platform Application Communication Stack for In-Vehicle Applications
 *
 * Copyright (C) 2012, Luxoft Professional Corp., member of IBS group
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
/* \file UUID.cpp
 * \brief UUID/Name pair storage
 * \author Nikolay Zamotaev
 */

#include <sys/types.h>
#include <sys/stat.h>
#include <uuid/uuid.h>

#include "utils/uuid_store.h"
#include "json/json.h"
       
UUIDStore::UUIDStore(std::string path)
{
    mRoot = new Json::Value();
    mPath = getenv("HOME");
    mPath += "/";
    mPath += path;
    mkdir(mPath.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);

    mPath += "/uuid.json";

    load();
}

UUIDStore::~UUIDStore()
{
    save();
    delete mRoot;
}

void UUIDStore::clear()
{
    mRoot->clear();
    save();
}

void UUIDStore::deleteEntry(std::string const& name)
{
    if (mRoot->isMember(name))
    {
        mRoot->removeMember(name);
        save();
    }
}

std::string UUIDStore::getByName(std::string const& name)
{
    if (mRoot->isMember(name))
    {
        return (*mRoot)[name].asString();
    }
    else
    {
        uuid_t myUUID;
        uuid_generate(myUUID);
        char buffer[UUID_LENGTH];
        uuid_unparse(myUUID, buffer);
        std::string uuidStr(buffer, UUID_LENGTH);
        (*mRoot)[name] = uuidStr;
        save();
        return uuidStr;
    }
}

void UUIDStore::load()
{
    Json::Reader reader;
    
    std::ifstream file;
    file.open(mPath.c_str());

    if (!file.fail())
    {
        reader.parse(file, *mRoot);
    }

    file.close();
}

void UUIDStore::save()
{
    std::ofstream file;
    file.open(mPath.c_str());
    file << mRoot->toStyledString();
    file.close();
}
