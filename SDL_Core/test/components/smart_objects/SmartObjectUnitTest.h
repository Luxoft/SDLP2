// Copyright (c) 2013, Ford Motor Company
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// Redistributions of source code must retain the above copyright notice, this
// list of conditions and the following disclaimer.
//
// Redistributions in binary form must reproduce the above copyright notice,
// this list of conditions and the following
// disclaimer in the documentation and/or other materials provided with the
// distribution.
//
// Neither the name of the Ford Motor Company nor the names of its contributors
// may be used to endorse or promote products derived from this software
// without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR 'A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

#ifndef TEST_COMPONENTS_SMARTOBJECTS_SMARTOBJECTUNITTEST_H_
#define TEST_COMPONENTS_SMARTOBJECTS_SMARTOBJECTUNITTEST_H_

#include "gtest/gtest.h"
#include "gmock/gmock.h"

#include "smart_objects/smart_object.h"

namespace test { namespace components { namespace SmartObjects { namespace SmartObjectUnitTest {

    using namespace NsSmartDeviceLink::NsSmartObjects;


    class TestHelper : public ::testing::Test
    {
    protected:

        void makeMapObject(SmartObject &obj, const int size) const
        {
            char i_key[8], j_key[8], k_key[8], value[8];

            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    for (int k = 0; k < size; k++)
                    {
                        sprintf(i_key, "i_%d", i);
                        sprintf(j_key, "j_%d", j);
                        sprintf(k_key, "k_%d", k);
                        sprintf(value, "%d", i + j + k);
                        obj[i_key][j_key][k_key] = value;
                    }
        }

        void checkMapObject(SmartObject &obj, const int size) const
        {
            char i_key[8], j_key[8], k_key[8], value[8];

            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    for (int k = 0; k < size; k++)
                    {
                        sprintf(i_key, "i_%d", i);
                        sprintf(j_key, "j_%d", j);
                        sprintf(k_key, "k_%d", k);
                        sprintf(value, "%d", i + j + k);

                        ASSERT_EQ(std::string(value), static_cast<std::string>(obj[i_key][j_key][k_key])) <<
                                "Wrong value in the map at [" << i_key << "][" << j_key << "][" << k_key << "]";
                    }
        }

       void makeArrayObject(SmartObject &obj, int size, int base=0)
       {
           for (int i = 0; i < size; i++)
               for (int j = 0; j < size; j++)
                   for (int k = 0; k < size; k++)
                   {
                       obj[i][j][k] = base + i + j + k;
                   }
       }

       void checkArrayObject(SmartObject &obj, int size, int base=0)
       {
           for (int i = 0; i < size; i++)
               for (int j = 0; j < size; j++)
                   for (int k = 0; k < size; k++)
                   {
                       ASSERT_EQ(base + i + j + k, static_cast<int>(obj[i][j][k])) <<
                               "Wrong value in the array at index: " << i << ", " << j << ", " << k;
                   }
       }
    };

    /*
     * Tests different types sequentially
     */
    TEST(BasicMixtedTypes, test_SmartObjectUnitTest)
    {
        SmartObject obj;

        ASSERT_EQ(invalid_int_value, static_cast<int>(obj)) << "Wrong cast to int just after construction";

        obj = 10;
        ASSERT_EQ(10, static_cast<int>(obj)) << "Wrong cast to int";

        obj = "some string";
        ASSERT_EQ("some string", static_cast<std::string>(obj)) << "Wrong cast to std::string";

        obj = false;
        ASSERT_FALSE(static_cast<bool>(obj)) << "Wrong cast to bool";

        obj = 'A';
        ASSERT_EQ('A', static_cast<char>(obj)) << "Wrong cast to char";

        obj = 3.14;
        ASSERT_EQ(3.14, static_cast<double>(obj)) << "Wrong cast to double";

        // array test
        for (int i = 0; i < 100; i++)
        {
            obj[i] = i;
            ASSERT_EQ(i, static_cast<int>(obj[i]));
        }

        // map test
        for (int i = 0; i < 100; i++)
        {
            char key[8];
            sprintf(key, "%d", i);
            obj[key] = i;
            ASSERT_EQ(i, static_cast<int>(obj[key]));
        }
    }

    TEST_F(TestHelper, BasicArrayTest)
    {
        SmartObject obj;

        ASSERT_EQ(invalid_int_value, static_cast<int>(obj[0])) << "Wrong value at accessing non existent index";
        ASSERT_EQ(invalid_int_value, static_cast<int>(obj["non_existent_key"])) << "Wrong value at accessing non existent key";

        obj[0] = 1;
        ASSERT_EQ(1, static_cast<int>(obj[0])) << "Wrong value at 0 index";
        obj[1] = 2;
        ASSERT_EQ(2, static_cast<int>(obj[1])) << "Wrong value at 1 index";

        obj[0][0] = 3;
        obj[1][0] = 1;
        ASSERT_EQ(3, static_cast<int>(obj[0][0])) << "Wrong value at index 0, 0";

        obj[0][0][0] = 4;
        obj[0][1][0] = 5;
        ASSERT_EQ(4, static_cast<int>(obj[0][0][0])) << "Wrong value at index 0, 0, 0";

        const int size = 32;
        makeArrayObject(obj, size);

        checkArrayObject(obj, size);
    }


    TEST_F(TestHelper, BasicMapTest)
    {
        SmartObject obj;

        ASSERT_EQ(invalid_int_value, static_cast<int>(obj["non_existent_key"])) << "Wrong value for non existent key";

        obj["abc"]["def"]["ghi"] = 5;
        ASSERT_EQ(5, static_cast<int>(obj["abc"]["def"]["ghi"])) << "Wrong value for triple map";

        obj["123"]["456"]["789"] = "string test";

        ASSERT_EQ("string test", static_cast<std::string>(obj["123"]["456"]["789"])) << "Wrong value for triple map";

        const int size = 32;

        makeMapObject(obj, size);

        checkMapObject(obj, size);
    }

    TEST(ConstructorsTest, test_SmartObjectUnitTest)
    {
        SmartObject objInt(5678);
        ASSERT_EQ(5678, static_cast<int>(objInt)) << "Wrong constructor with int param";

        char c_str[] = "test c_string";
        SmartObject obj_c_str(c_str);
        ASSERT_EQ("test c_string", static_cast<std::string>(obj_c_str)) << "Wrong constructor with c_str param";

        SmartObject obj_std_str(std::string("test std_string"));
        ASSERT_EQ(std::string("test std_string"), static_cast<std::string>(obj_std_str));

        SmartObject obj_char('R');
        ASSERT_EQ('R', static_cast<char>(obj_char)) << "Wrong constructor with char param";

        SmartObject obj_double(-0.4321);
        ASSERT_EQ(-0.4321, static_cast<double>(obj_double)) << "Wrong constructor with double param";

        SmartObject obj_bool(true);
        ASSERT_TRUE(static_cast<bool>(obj_bool)) << "Wrong constructor with bool param";

        SmartObject src_obj;

        src_obj["key_1"] = "value_1";     // FIXME: String assignment crashes test
        src_obj["key_2"]["sub_key_1"] = "value_2";

        SmartObject dst_obj(src_obj);
        ASSERT_EQ("value_1", static_cast<std::string>(dst_obj["key_1"])) << "Copy constructor is not correct";
        ASSERT_EQ("value_2", static_cast<std::string>(dst_obj["key_2"]["sub_key_1"])) << "Copy constructor is not correct";
    }

    TEST(FromString, TypeConversion)
    {
        {   // String to bool
            SmartObject obj;
            ASSERT_EQ(invalid_bool_value, static_cast<bool>(obj));
            obj = "true";
            ASSERT_EQ(invalid_bool_value, static_cast<bool>(obj));
            obj = "false";
            ASSERT_EQ(invalid_bool_value, static_cast<bool>(obj));
            obj = true;
            ASSERT_TRUE(static_cast<bool>(obj));
        }
        {   // String to int
            SmartObject obj;
            ASSERT_EQ(invalid_int_value, static_cast<int>(obj));
            obj = "0";
            ASSERT_EQ(0, static_cast<int>(obj));
            obj = "-34323";
            ASSERT_EQ(-34323, static_cast<int>(obj));
            obj = "+1234";
            ASSERT_EQ(1234, static_cast<int>(obj));
            obj = "3232.0";
            ASSERT_EQ(invalid_int_value, static_cast<int>(obj));        // FIXME:
            obj = "123wtf";
            ASSERT_EQ(invalid_int_value, static_cast<int>(obj));        // FIXME:
            obj = "";
            ASSERT_EQ(invalid_int_value, static_cast<int>(obj));        // FIXME:
            obj = " 123 ";
            ASSERT_EQ(invalid_int_value, static_cast<int>(obj));        // FIXME:
            obj = " 123";
            ASSERT_EQ(invalid_int_value, static_cast<int>(obj));        // FIXME:
        }
        {   // String to char
            SmartObject obj;
            ASSERT_EQ(invalid_char_value, static_cast<char>(obj));
            obj = "C";
            ASSERT_EQ('C', static_cast<char>(obj));
            obj = "\n";
            ASSERT_EQ('\n', static_cast<char>(obj));
            obj = " A";
            ASSERT_EQ(invalid_char_value, static_cast<char>(obj));
            obj = "";
            ASSERT_EQ(invalid_char_value, static_cast<char>(obj));
        }
        {   // String to double
            SmartObject obj;
            ASSERT_EQ(invalid_double_value, static_cast<double>(obj));
            obj = "1234";
            ASSERT_EQ(1234, static_cast<double>(obj));
            obj = "-0.1234";
            ASSERT_EQ(-0.1234, static_cast<double>(obj));
            obj = ".54321";
            ASSERT_EQ(.54321, static_cast<double>(obj));
            obj = "123.45.6";
            ASSERT_EQ(invalid_double_value, static_cast<double>(obj));        // FIXME:
            obj = "123 wtf";
            ASSERT_EQ(invalid_double_value, static_cast<double>(obj));        // FIXME:
            obj = " 0.5";
            ASSERT_EQ(invalid_double_value, static_cast<double>(obj));        // FIXME:
        }
        {   // String to Map
            SmartObject obj;
            ASSERT_EQ(invalid_int_value, static_cast<int>(obj["key"]));
            obj = "this is not a map";
            ASSERT_EQ(invalid_char_value, static_cast<char>(obj["some_key"]));
        }
        {   // String to Array
            SmartObject obj;
            ASSERT_EQ(invalid_bool_value, static_cast<bool>(obj[0]));
            obj = "this is not an array";
            ASSERT_EQ(invalid_double_value, static_cast<double>(obj[0]));
        }
        {   // String to Binary
            SmartObject obj;
            ASSERT_EQ(invalid_binary_value, obj.asBinary());
            obj = "this is not an array";
            ASSERT_EQ(invalid_binary_value, obj.asBinary());
        }
    }

    TEST(FromBool, TypeConversion)
    {
        SmartObject obj;

        obj = true;

        ASSERT_EQ(invalid_string_value, static_cast<std::string>(obj));
        ASSERT_TRUE(static_cast<bool>(obj));
        ASSERT_EQ(1, static_cast<int>(obj));
        ASSERT_EQ(invalid_char_value, static_cast<char>(obj));
        ASSERT_EQ(1.0, static_cast<double>(obj));
        ASSERT_EQ(invalid_int_value, static_cast<int>(obj["key"]));
        ASSERT_EQ(invalid_char_value, static_cast<char>(obj[0]));
        ASSERT_EQ(invalid_binary_value, obj.asBinary());

        obj = false;

        ASSERT_EQ(invalid_string_value, static_cast<std::string>(obj));
        ASSERT_FALSE(static_cast<bool>(obj));
        ASSERT_EQ(0, static_cast<int>(obj));
        ASSERT_EQ(invalid_char_value, static_cast<char>(obj));
        ASSERT_EQ(0, static_cast<double>(obj));
        ASSERT_EQ(invalid_int_value, static_cast<int>(obj["key"]));
        ASSERT_EQ(invalid_char_value, static_cast<char>(obj[0]));
        ASSERT_EQ(invalid_binary_value, obj.asBinary());
    }

    TEST(FromInt, TypeConversion)
    {
        SmartObject obj;

        obj = 123;

        ASSERT_EQ("123", static_cast<std::string>(obj));
        ASSERT_TRUE(static_cast<bool>(obj));
        ASSERT_EQ(invalid_char_value, static_cast<char>(obj));
        ASSERT_EQ(123.0, static_cast<double>(obj));
        ASSERT_EQ(invalid_binary_value, obj.asBinary());

        obj = 5;
        ASSERT_EQ("5", static_cast<std::string>(obj));
        ASSERT_EQ(invalid_char_value, static_cast<char>(obj));

        obj = 0;
        ASSERT_EQ("0", static_cast<std::string>(obj));
        ASSERT_FALSE(static_cast<bool>(obj));

        obj = 1;
        ASSERT_TRUE(static_cast<bool>(obj));

        obj = -1234;
        ASSERT_EQ(-1234, static_cast<int>(obj));
        ASSERT_EQ("-1234", static_cast<std::string>(obj));
        ASSERT_EQ(-1234.0, static_cast<double>(obj));
        ASSERT_EQ(invalid_char_value, static_cast<char>(obj));
        ASSERT_TRUE(static_cast<bool>(obj));
        ASSERT_EQ(invalid_binary_value, obj.asBinary());
    }

    TEST(FromChar, TypeConversion)
    {
        SmartObject obj;

        obj = '1';

        ASSERT_EQ("1", static_cast<std::string>(obj));
        ASSERT_EQ(invalid_int_value, static_cast<int>(obj));
        ASSERT_EQ('1', static_cast<char>(obj));
        ASSERT_EQ(invalid_double_value, static_cast<double>(obj));
        ASSERT_EQ(invalid_int_value, static_cast<int>(obj["key"]));
        ASSERT_EQ(invalid_char_value, static_cast<char>(obj[0]));
        ASSERT_EQ(invalid_binary_value, obj.asBinary());

        obj = '0';

        ASSERT_EQ("0", static_cast<std::string>(obj));
        ASSERT_EQ(invalid_int_value, static_cast<int>(obj));
        ASSERT_EQ('0', static_cast<char>(obj));
        ASSERT_EQ(invalid_double_value, static_cast<double>(obj));
        ASSERT_EQ(invalid_int_value, static_cast<int>(obj["key"]));
        ASSERT_EQ(invalid_char_value, static_cast<char>(obj[0]));
        ASSERT_EQ(invalid_binary_value, obj.asBinary());
    }

    TEST(FromDouble, TypeConversion)
    {
        SmartObject obj;

        obj = 0.1;
        ASSERT_EQ("0.1", static_cast<std::string>(obj));        // FIXME: result 0.100000
        ASSERT_EQ(0, static_cast<int>(obj));
        ASSERT_EQ(invalid_char_value, static_cast<char>(obj));
        ASSERT_EQ(0.1, static_cast<double>(obj));
        ASSERT_TRUE(static_cast<bool>(obj));
        ASSERT_EQ(invalid_binary_value, obj.asBinary());

        obj = 0.9;
        ASSERT_EQ("0.9", static_cast<std::string>(obj));
        ASSERT_EQ(0, static_cast<int>(obj));
        ASSERT_TRUE(static_cast<bool>(obj));

        obj = -12323.999;
        ASSERT_EQ("-12323.999", static_cast<std::string>(obj));
        ASSERT_EQ(-12323, static_cast<int>(obj));
        ASSERT_TRUE(static_cast<bool>(obj));

        obj = 0.0;
        ASSERT_EQ("0", static_cast<std::string>(obj));
        ASSERT_EQ(0, static_cast<int>(obj));
        ASSERT_FALSE(static_cast<bool>(obj));
        ASSERT_EQ(invalid_binary_value, obj.asBinary());
    }

    TEST(FromMap, TypeConversion)
    {
        SmartObject obj;

        obj["key1"] = 123;

        ASSERT_EQ(invalid_string_value, static_cast<std::string>(obj));
        ASSERT_EQ(invalid_int_value, static_cast<int>(obj));
        ASSERT_EQ(invalid_char_value, static_cast<char>(obj));
        ASSERT_EQ(invalid_double_value, static_cast<double>(obj));
        ASSERT_EQ(123, static_cast<int>(obj["key1"]));
        ASSERT_EQ(invalid_char_value, static_cast<char>(obj[0]));
        ASSERT_EQ(invalid_binary_value, obj.asBinary());
    }

    TEST(FromArray, TypeConversion)
    {
        SmartObject obj;

        obj[0] = 'A';
        obj[1] = -123;

        ASSERT_EQ(invalid_string_value, static_cast<std::string>(obj));
        ASSERT_EQ(invalid_int_value, static_cast<int>(obj));
        ASSERT_EQ(invalid_char_value, static_cast<char>(obj));
        ASSERT_EQ(invalid_double_value, static_cast<double>(obj));
        ASSERT_EQ('A', static_cast<char>(obj[0]));
        ASSERT_EQ(invalid_int_value, static_cast<int>(obj["key1"]));
        ASSERT_EQ(invalid_binary_value, obj.asBinary());
    }

    TEST_F(TestHelper, AssignmentTest)
    {
        SmartObject objSrc, objDst;

        objSrc = -6;
        objDst = 7;
        objDst = objSrc;
        ASSERT_EQ(-6, static_cast<int>(objDst)) << "Wrong assignment for int object";

        objSrc = "Some test string";
        objDst = "Other string";
        objDst = objSrc;
        ASSERT_EQ("Some test string", static_cast<std::string>(objDst)) << "Wrong assignment for std::string object";

        objSrc = 0.5;
        objDst = 4;
        objDst = objSrc;
        ASSERT_EQ(0.5, static_cast<double>(objDst)) << "Wrong assignment for double object";

        objSrc = true;
        objDst = false;
        objDst = objSrc;
        ASSERT_TRUE(static_cast<bool>(objDst)) << "Wrong assignment for bool object";

        const int size = 32;
        makeMapObject(objSrc, size);
        objDst["a"]["b"] = 4;
        objDst = objSrc;
        checkMapObject(objDst, size);

        makeArrayObject(objSrc, size, 5);
        makeArrayObject(objDst, 23, 6);
        objDst = objSrc;
        checkArrayObject(objDst, size, 5);
    }

    TEST_F(TestHelper, SizeTest)
    {
        SmartObject obj;

        ASSERT_EQ(0, obj.length()) << "Wrong size for the uninitialized object";

        obj = 1234;
        ASSERT_EQ(0, obj.length()) << "Wrong size for the int object";

        std::string str("Some test very long string");
        obj = str;
        ASSERT_EQ(str.size(), obj.length()) << "The size of the object containing string is not correct";

        obj = true;
        ASSERT_EQ(0, obj.length()) << "Wrong size of the true";

        obj = 0.1234;
        ASSERT_EQ(0, obj.length()) << "Wrong size of the double";

        obj = 'A';
        ASSERT_EQ(0, obj.length()) << "Wrong size of the char";

        makeMapObject(obj, 12);
        ASSERT_EQ(12, obj.length()) << "Wrong size of the object containing map";

        makeArrayObject(obj, 21);
        ASSERT_EQ(21, obj.length()) << "Wrong size of the object containing array";
    }

   TEST(CopyObjectsTest, SmartObjectTest)
   {
       SmartObject obj;

       obj[0] = "test string";

       obj = obj[0];

       ASSERT_EQ("test string", static_cast<std::string>(obj));

       obj["abc"] = "new test string";
       obj = obj["abc"];

       ASSERT_EQ("new test string", static_cast<std::string>(obj));
   }

    TEST(CopyConstructorTest, SmartObjectTest)
    {
        SmartObject srcObj;

        srcObj[0] = "test string";

        SmartObject dstObj = srcObj[0];

        ASSERT_EQ("test string", static_cast<std::string>(dstObj));
    }

    TEST(MapEraseTest, SmartObjectTest)
    {
        SmartObject srcObj;

        srcObj["one"] = 1;
        srcObj["two"] = 2;
        srcObj["three"] = 3;

        ASSERT_EQ(3, srcObj.length());
        ASSERT_EQ(2, srcObj["two"].asInt());

        ASSERT_TRUE(srcObj.erase("two"));
        ASSERT_FALSE(srcObj.erase("two"));

        ASSERT_EQ(2, srcObj.length());
        ASSERT_EQ(-1, srcObj["two"].asInt());
        ASSERT_EQ(3, srcObj.length());      // the element "two" was accessed in the previous line so the element has been created

        srcObj["two"] = 2;

        ASSERT_EQ(1, srcObj["one"].asInt());
        ASSERT_EQ(2, srcObj["two"].asInt());
        ASSERT_EQ(3, srcObj["three"].asInt());

        ASSERT_TRUE(srcObj.erase("one"));

        ASSERT_EQ(2, srcObj.length());

        ASSERT_TRUE(srcObj.erase("two"));

        ASSERT_EQ(1, srcObj.length());

        ASSERT_TRUE(srcObj.erase("three"));

        ASSERT_EQ(0, srcObj.length());

        srcObj["one"]["two"]["three"]["0"] = "1";
        srcObj["one"]["two"]["three"]["1"] = "2";

        ASSERT_EQ(1, srcObj.length());
        ASSERT_EQ(1, srcObj["one"].length());
        ASSERT_EQ(1, srcObj["one"]["two"].length());
        ASSERT_EQ(2, srcObj["one"]["two"]["three"].length());

        ASSERT_TRUE(srcObj["one"]["two"]["three"].erase("0"));
        ASSERT_FALSE(srcObj["one"]["two"]["three"].erase("0"));

        ASSERT_EQ(1, srcObj["one"]["two"]["three"].length());

        ASSERT_TRUE(srcObj["one"].erase("two"));
        ASSERT_EQ(0, srcObj["one"].length());

        srcObj = 1234;       // not a map
        ASSERT_FALSE(srcObj.erase("one"));
    }

    // TODO: Add a test to check accessing an array at strange indexes.

}}}}

#endif  // TEST_COMPONENTS_SMARTOBJECTS_SMARTOBJECTUNITTEST_H_
