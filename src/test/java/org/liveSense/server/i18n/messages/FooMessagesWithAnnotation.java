package org.liveSense.server.i18n.messages;

import com.google.gwt.i18n.client.Messages;

public interface FooMessagesWithAnnotation extends Messages{

    @Key(value="test_none")
    String method_test_None();
    
    @Key(value="test_String")
    String method_test_String(String s);
    
    @Key(value="test_byte")
    String method_test_byte(byte s);

    @Key(value="test_Byte")
    String method_test_Byte(Byte s);
    
    @Key(value="test_short")
    String method_test_short(short s);

    @Key(value="test_Short")
    String method_test_Short(Short s);

    @Key(value="test_int")
    String method_test_int(int i);

    @Key(value="test_Integer")
    String method_test_Integer(Integer i);
    
    @Key(value="test_long")
    String method_test_long(long i);

    @Key(value="test_Long")
    String method_test_Long(Long i);
    
    @Key(value="test_float")
    String method_test_float(float i);

    @Key(value="test_Float")
    String method_test_Float(Float i);
    
    @Key(value="test_double")
    String method_test_double(double i);

    @Key(value="test_Double")
    String method_test_Double(Double i);
    
    @Key(value="test_char")
    String method_test_char(char i);

    @Key(value="test_Character")
    String method_test_Character(Character i);
    
    @Key(value="test_Object")
    String method_test_Object(Object i);
    
    @Key(value="test_2param")
    String method_test_2param(String p1, int p2);

    @Key(value="test_3param")
    String method_test_3param(String p1, int p2, String p3);
    
    @Key(value="test_noprop")
    @DefaultMessage(value="The message is test_noprop({0},{1})")
    String method_test_noprop(String p1, int i);
}
